package xyz.bmi;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import xyz.fragment.BmiFragment;


public class MainActivity extends AppCompatActivity {


    private int mSystem;
    private int mSystemTemp;
    private Fragment[] mFragments;
    public static final String PREFERENCE_NAME = "Setting";
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    //private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        mSystem = sp.getInt("system", 0);
        mFragments = new Fragment[3];
        for(int i=0;i<3;i++) {
            mFragments[i]=BmiFragment.NewInstance(mSystem);
        }
        MySimpleFragmentPagerAdapter mySimpleFragmentPagerAdapter = new MySimpleFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mySimpleFragmentPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }



    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sp = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("system", mSystem);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mSystemTemp = mSystem;
        if (item.getItemId() == R.id.menu_metric) {
            new AlertDialog.Builder(MainActivity.this).setTitle(R.string.select_unit)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setSingleChoiceItems(new String[]{getString(R.string.metric_system), getString(R.string.imperial_system)}, mSystem,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSystemTemp = which;
                                }
                            })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mSystemTemp == mSystem)
                                return;
                            mSystem = mSystemTemp;
                            //((BmiFragment) fragment).setSystem(mSystem);


                        }
                    })
                    .show();


        }
        if (item.getItemId() == R.id.menu_about) {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(R.string.title_about)
                    .setMessage(R.string.alert_dialog__msg)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage(R.string.confirm_exit)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                }).show();
    }

    private class  MySimpleFragmentPagerAdapter extends FragmentPagerAdapter{
        private CharSequence tabTitles[] = new CharSequence[]{getText(R.string.bmi), getText(R.string.waist_height_ratio), getText(R.string.fat_percentage)};
        MySimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
