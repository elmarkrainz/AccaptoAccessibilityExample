package org.accapto.accaptoaccessibilityexample.accessibilityhelper;

import android.content.Context;
import android.content.SharedPreferences;

import org.accapto.accaptoaccessibilityexample.R;

/**
 * Created by krajn on 19/08/17.
 */
public class ThemeSaver {

    public final String PROFILE_NORMAL = "normal";
    public final String PROFILE_ALTERNATIVE = "alternaive";

    private final String PROFILE_THEME_KEY = "profile";
    private static final String SHARED_SETTINGS = "Profile_Settings";


    private SharedPreferences profileThemePrefs;
    private int profileTheme;


    public ThemeSaver(Context c) {
        profileThemePrefs = c.getSharedPreferences(SHARED_SETTINGS, 0);
        loadSettings();
    }


    private void loadSettings() {

        this.profileTheme = (profileThemePrefs.getInt(PROFILE_THEME_KEY, R.style.AppTheme));
    }

    private void saveSettings() {

        // save to shared Prefs
        SharedPreferences.Editor edPrfs = profileThemePrefs.edit(); // Prefs Editor
        // values
        edPrfs.putInt(PROFILE_THEME_KEY, this.profileTheme);
        edPrfs.commit();
    }


    public int getProfileTheme() {
        return profileTheme;
    }

    public void setProfileTheme(int profileTheme) {
        this.profileTheme = profileTheme;
        saveSettings();
    }

}
