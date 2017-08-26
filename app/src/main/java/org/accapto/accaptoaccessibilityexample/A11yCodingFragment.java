package org.accapto.accaptoaccessibilityexample;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.graphics.Rect;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.accapto.accaptoaccessibilityexample.accessibilityhelper.SpeechInputHelper;
import org.accapto.accaptoaccessibilityexample.accessibilityhelper.SpeechOutputHelper;
import org.accapto.accaptoaccessibilityexample.accessibilityhelper.ThemeChanger;
import org.accapto.accaptoaccessibilityexample.accessibilityhelper.ThemeSaver;
import org.accapto.accaptoaccessibilityexample.accessibilityhelper.TouchAreaExtender;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Examples of code side support for accessibility
 * <p>
 * - Text to Speech
 * - Speech to text
 * - Touch Area extension
 */


public class A11yCodingFragment extends Fragment {

    private Activity activity;
    private ImageButton btnSpeechInput;
    private EditText edtxtSpeechInput;
    private CheckBox chkSmallTouch;
    private Button btnText2Speech;
    private ToggleButton toggleStyle;

    private SpeechOutputHelper voicer;



    private SpeechInputHelper voiceInputExtension;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a12y_coding, container, false);
        activity = getActivity();


        btnSpeechInput = (ImageButton) view.findViewById(R.id.btnSpeechInput);
        edtxtSpeechInput = (EditText) view.findViewById(R.id.editSpeechInput);
        chkSmallTouch = (CheckBox) view.findViewById(R.id.chkSmallTouch);
        btnText2Speech = (Button) view.findViewById(R.id.btnText2Speech);
        toggleStyle = (ToggleButton) view.findViewById(R.id.btnStyleToogle);


        voiceInputExtension = new SpeechInputHelper(activity);
        btnSpeechInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                voiceInputExtension.startSpeechInput();  /// geht nicht
                promptSpeechInput();                        // geht
            }
        });


        voicer = new SpeechOutputHelper(activity);
        btnText2Speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voicer.speaking("Text wird vorgelesen");
            }
        });




        // toogle style
        toggleStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity a = A11yCodingFragment.this.getActivity();


                ThemeSaver pt = new ThemeSaver(a);

                int styleId;

                if (pt.getProfileTheme() == R.style.AppTheme_HicontrastTheme) {
                    styleId = R.style.AppTheme;
                    toggleStyle.setText("norm");
                }
                else
                {
                    styleId= R.style.AppTheme_HicontrastTheme;
                    toggleStyle.setText("alt");

                }

           /*     if (a instanceof StartActivity) {
                    ((StartActivity) a).changeTheme(styleId);
                }
            */

                ThemeChanger.getInstance().changeTheme(styleId, a);

            }
        });


        TouchAreaExtender tae = new TouchAreaExtender();
        tae.enhanceTouchArea(chkSmallTouch, 100);

        tae.enhanceTouchArea(btnText2Speech, 100);


        //        enhanceTouchArea(chkSmallTouch, 100);
        return view;
    }


    // Speech input

    /*
     Showing google speech input dialog
     */

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, 143);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(activity.getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SpeechInputHelper.SPEECH_INPUT_MODE: {
                // if (resultCode == 1 && null != data) {
                if (null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.i("SPEECH INPUT", result.get(0));
                    edtxtSpeechInput.setText(result.get(0));
                }
                break;
            }
        }
    }

    private void enhanceTouchArea(final View v, final int extraPadding) {
        final View parent = (View) v.getParent();

        parent.post(new Runnable() {
            @Override
            public void run() {

                Rect delegateArea = new Rect();
                View delegate = v;

                delegate.getHitRect(delegateArea);


                delegateArea.top -= extraPadding;
                delegateArea.bottom += extraPadding;
                delegateArea.left -= extraPadding;
                delegateArea.right += extraPadding;

                TouchDelegateComposite tdc = new TouchDelegateComposite(delegateArea, delegate);
                tdc.addDelegate(new TouchDelegate(delegateArea, delegate));

                parent.setTouchDelegate(tdc);


                //parent.setTouchDelegate(new TouchDelegate(delegateArea,delegate));
            }
        });


    }


    /**
     * Helper class to enhance the touch area
     * <p>
     * see also  http://stackoverflow.com/questions/6799066/how-to-use-multiple-touchdelegate
     */

    private class TouchDelegateComposite extends TouchDelegate {

        private final List<TouchDelegate> delegates = new ArrayList<TouchDelegate>();

        public TouchDelegateComposite(Rect rectArea, View view) {
            super(rectArea, view);
        }

        public void addDelegate(TouchDelegate delegate) {
            if (delegate != null) {
                delegates.add(delegate);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            boolean res = false;
            float x = event.getX();
            float y = event.getY();
            for (TouchDelegate delegate : delegates) {
                event.setLocation(x, y);
                res = delegate.onTouchEvent(event) || res;
            }
            return res;
        }
    }

}
