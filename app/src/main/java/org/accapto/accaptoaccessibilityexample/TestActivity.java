package org.accapto.accaptoaccessibilityexample;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeechService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * just ome tests
 */
public class TestActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 1;
    private ImageButton btnSpeak;
    private EditText edtxtSpeechInput;


    private SelfVoicer sv;
    private boolean isSelfVoicing = true;

    private TextToSpeech t1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        edtxtSpeechInput = (EditText) findViewById(R.id.editText);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeechInput);


        View v = findViewById(android.R.id.content);

        v.setBackgroundColor(Color.rgb(100,100,0));


        edtxtSpeechInput.setBackgroundColor(Color.rgb(10,1,100));
        edtxtSpeechInput.setTextColor(Color.rgb(100,100,0));


        enhanceTouchArea(btnSpeak,50);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        sv = new SelfVoicer(this);


        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.GERMAN);
                }
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (isSelfVoicing) {
            sv.speaking("willkommen im Demo");
        }


        t1.speak("Startseite", TextToSpeech.QUEUE_FLUSH, null, null);

    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edtxtSpeechInput.setText(result.get(0));
                }
                break;
            }

        }
    }




    private void enhanceTouchArea(final View v, final int extraPadding){
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

                TouchDelegateComposite tdc = new   TouchDelegateComposite(delegateArea,delegate);
                tdc.addDelegate(new TouchDelegate(delegateArea,delegate));

                parent.setTouchDelegate(tdc);

                //parent.setTouchDelegate(new TouchDelegate(delegateArea,delegate));
            }
        });


    }



    // http://stackoverflow.com/questions/6799066/how-to-use-multiple-touchdelegate

    class TouchDelegateComposite extends TouchDelegate {

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





    private class SelfVoicer {

        private TextToSpeech t2s;

        public SelfVoicer(Context c) {
            t2s = new TextToSpeech(c, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        t2s.setLanguage(Locale.UK);
                    }
                }
            });
        }


        public void speaking(String text) {
            t2s.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

        }
    }




}
