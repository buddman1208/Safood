package kr.edcan.safood.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    String[] titleArr = new String[]{"검색", "안전한 음식", "내 정보"};
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setDefault();
    }

    private void setDefault() {
//        appBarLayout.setExpanded(true, true);
        binding.mainPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        binding.tablayout.setupWithViewPager(binding.mainPager);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        binding.mainPager.setCurrentItem(1);
        binding.mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("asdf", "position : " + position);
                Log.e("asdf", "positionoffset : " + positionOffset);
                Log.e("asdf", "positionoffsetpixel : " + positionOffsetPixels);
                Log.e("asdf", "page : " + (position + positionOffset));
            }

            @Override
            public void onPageSelected(int position) {
                binding.appbarlayout.setExpanded(true,true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("asdf", "onPageScrollStateChanged : " + state);
            }
        });
    }

    public static class MainFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "pageNumber";

        public static MainFragment newInstance(int pageNum) {
            Bundle args = new Bundle();
            MainFragment fragment = new MainFragment();
            args.putInt(ARG_SECTION_NUMBER, pageNum);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 0:
                    view = inflater.inflate(R.layout.main_search, container, false);
                    break;
                case 1:
                    view = inflater.inflate(R.layout.main_safood, container, false);
                    break;
                case 2:
                    view = inflater.inflate(R.layout.main_info, container, false);
                    break;
            }
            setPage(view);
            return view;
        }

        private void setPage(View view) {

        }
    }

    public class MainPagerAdapter extends FragmentPagerAdapter {


        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MainFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < titleArr.length)
                return titleArr[position];
            return null;
        }
    }
}
