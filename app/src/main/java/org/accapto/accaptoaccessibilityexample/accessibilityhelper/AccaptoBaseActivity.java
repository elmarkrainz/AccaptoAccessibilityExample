package org.accapto.accaptoaccessibilityexample.accessibilityhelper;


import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

/**
 * Created by krajn on 11/09/17.
 */

public class AccaptoBaseActivity extends Activity {//AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        ThemeChanger.getInstance().applyTheme(this);


    }
}
