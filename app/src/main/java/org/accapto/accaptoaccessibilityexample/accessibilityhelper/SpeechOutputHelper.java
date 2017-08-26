package org.accapto.accaptoaccessibilityexample.accessibilityhelper;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Helperclass for easy Text-to-speech output
 *
 * @author EKrainz
 */

public class SpeechOutputHelper {

    private final Context context;
    private Locale locale;
    private TextToSpeech t2s;


    public SpeechOutputHelper(Context c) {

        this.context =c;
        this.locale = getDeviceLocale();

        init(c);
    }


    public SpeechOutputHelper(Context c, Locale l) {
        this.context =c;
        this.locale = l;

        init(c);
    }



    private void init(Context c) {
        t2s = new TextToSpeech(c, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t2s.setLanguage(SpeechOutputHelper.this.locale);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    private Locale getDeviceLocale(){
       return context.getResources().getConfiguration().locale;
    }

    /**
     * easy methiod for speech output
     * @param text text for speech
     */
    public void speaking(String text) {
        t2s.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

    }

    public void setLocale(Locale locale){
        this.locale = locale;
    }


}
