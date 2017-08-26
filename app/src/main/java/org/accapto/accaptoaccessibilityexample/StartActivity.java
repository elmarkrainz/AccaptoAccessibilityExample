package org.accapto.accaptoaccessibilityexample;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;


import org.accapto.accaptoaccessibilityexample.accessibilityhelper.ThemeChanger;
import org.accapto.accaptoaccessibilityexample.accessibilityhelper.ThemeSaver;

import java.util.ArrayList;
import java.util.List;


/**
 * activity with tabbed navigation to hold examples for Android A11y
 */


public class StartActivity extends AppCompatActivity {

    private CustomPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

//       applyTheme();

        ThemeChanger.getInstance().applyTheme(this);

       setContentView(R.layout.activity_start);

      //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //   setSupportActionBar(toolbar);


        createTabs();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    private void createTabs() {
        mSectionsPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(new A11yExamplesFragment(), "Widgets");
        mSectionsPagerAdapter.addFragment(new A11yCodingFragment(), "Coding");
        mSectionsPagerAdapter.addFragment(new TalkBackFragment(), "TalkBack");


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    //  actions form fragments

    public void openA11ySettings(View v) {

        Intent dialogIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }


    public void openTalkbackUrl(View v) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://support.google.com/accessibility/android/answer/6283677?hl=en&ref_topic=3529932"));
        startActivity(intent);

    }







    private void applyThemeXX() {
        ThemeSaver pt = new ThemeSaver(this);
        setTheme(pt.getProfileTheme());
    }


    public void changeThemeXX(int styleId) {
        // profileTheme Helper
        ThemeSaver pt = new ThemeSaver(this);
        pt.setProfileTheme(styleId);

        // restart app
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }







    /**
     * Adapter for Fragmenst, based on default Android Studio Activity and
     * <p>
     * http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/
     */

    public class CustomPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
