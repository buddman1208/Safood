package kr.edcan.safood.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import kr.edcan.safood.R;
import kr.edcan.safood.adapter.MainSafoodAdapter;
import kr.edcan.safood.databinding.ActivityMainBinding;
import kr.edcan.safood.databinding.MainSafoodBinding;
import kr.edcan.safood.databinding.MainSearchBinding;
import kr.edcan.safood.models.SafoodContentData;
import kr.edcan.safood.models.SafoodTitleData;
import kr.edcan.safood.utils.SafoodHelper;
import kr.edcan.safood.views.SlidingExpandableListView;

public class MainActivity extends AppCompatActivity {

    // Variables
    public boolean currentCameraOpen = false;
    String[] titleArr = new String[]{"검색", "안전한 음식", "내 정보"};

    // Helper, Utils
    SafoodHelper helper;

    // DataBindings
    public ActivityMainBinding binding;
    public MainSearchBinding mainSearchBinding;
    public MainSafoodBinding mainSafoodBinding;

    // Widgets
    SlidingExpandableListView slidingListView;
    Camera mainCamera;
    ViewPager pager;
    RelativeLayout mainSearchFrame;
    CameraPreview mainCameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainSearchBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.main_search, null, true);
        mainSafoodBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.main_safood, null, true);
        setDefault();
        setCamera();
    }

    private void setCamera() {
//        mainCameraPreview = new CameraPreview(getApplicationContext(), mainCamera);
        currentCameraOpen = true;
//        binding.mainCameraFrame.addView(mainCameraPreview);
    }

    private void setDefault() {
        helper = new SafoodHelper(getApplicationContext());
        pager = (ViewPager) findViewById(R.id.mainPager);
        binding.mainPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        binding.tablayout.setupWithViewPager(binding.mainPager);
        binding.mainPager.setCurrentItem(1);
        binding.mainPager.setOffscreenPageLimit(4);
        binding.mainAppBarSearch.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        binding.mainAppBarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ));
            }
        });
        binding.mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("asdf", "onPageScrolled");
                float page = position + positionOffset;
                Resources res = getResources();
                if (page <= 1) {
                    int colorPrimary = res.getColor(R.color.colorPrimary);
                    int tabColor = helper.colorCombine(Color.WHITE, colorPrimary, page);
                    int appbarColor = helper.colorCombine(Color.BLACK, Color.WHITE, page);
                    int titleBarColor = helper.colorCombine(Color.BLACK, colorPrimary, page);
                    int titleTextColor = helper.colorCombine(Color.WHITE, colorPrimary, page);
                    int backgroundColor = helper.colorCombine(Color.parseColor("#212121"), Color.WHITE, page);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(titleBarColor);
                    }
                    binding.appbarlayout.setBackgroundColor(appbarColor);
                    binding.mainAppBarTitle.setTextColor(titleTextColor);
                    binding.toolbar.setBackgroundColor(appbarColor);
                    binding.mainNavButton.setColorFilter(tabColor, PorterDuff.Mode.SRC_ATOP);
                    binding.tablayout.setTabTextColors(res.getColor(R.color.commonTextColor), tabColor);
                    binding.tablayout.setSelectedTabIndicatorColor(tabColor);
                    binding.appbarlayout.getBackground().setAlpha(((int) (page * 1000 / 19.6) + 204));
                    binding.mainBackground.setBackgroundColor(backgroundColor);
                }
                if (currentCameraOpen) {
                    if (page >= 1) {
                        stopCamera();
                    }
                } else {
                    if (page < 1)
                        openCamera();
                }
            }


            @Override
            public void onPageSelected(int position) {
                Log.e("asdf", "pageSelected");
                binding.appbarlayout.setExpanded(true, true);
                binding.mainAppBarSearch.setEnabled(position == 0);
                mainSearchFrame = (RelativeLayout) binding.mainPager.findViewById(R.id.mainPhotoLoadingFrame);
                mainSearchFrame.setVisibility((position == 0) ? View.GONE : View.VISIBLE);
                Log.e("asdf_pageseelcted", position + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void openCamera() {
        if (mainCamera == null) {
            currentCameraOpen = true;
            mainCameraPreview.setCameraInstance(mainCameraPreview.mHolder);
        }
    }

    public void stopCamera() {
        if (mainCamera != null) {
            currentCameraOpen = false;
            mainCamera.stopPreview();
            mainCamera.setPreviewCallback(null);
            mainCamera.lock();
            mainCamera.release();
            mainCamera = null;
        }
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
            setPage(view, getArguments().getInt(ARG_SECTION_NUMBER), inflater);
            return view;
        }

        private void setPage(View view, int position, LayoutInflater inflater) {
            switch (position) {
                case 1:
                    final SlidingExpandableListView listview = (SlidingExpandableListView) view.findViewById(R.id.mainExpandableListView);
                    ArrayList<SafoodTitleData> titleArr = new ArrayList<>();
                    ArrayList<ArrayList<SafoodContentData>> contentArr = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        titleArr.add(new SafoodTitleData("m,", new Date()));
                        ArrayList<SafoodContentData> data = new ArrayList<>();
                        data.add(new SafoodContentData("asdf", new Date()));
                        data.add(new SafoodContentData("asdf", new Date()));
                        data.add(new SafoodContentData("asdf", new Date()));
                        contentArr.add(data);
                    }
                    final MainSafoodAdapter adapter = new MainSafoodAdapter(getContext(), titleArr, contentArr);
                    listview.setAdapter(adapter);
                    listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            View view = adapter.getGroupView(groupPosition, true, null, null);
                            RelativeLayout bottomIndicator = (RelativeLayout) view.findViewById(R.id.mainListGroupBottomIndicator);
                            if (listview.isGroupExpanded(groupPosition)) {
                                bottomIndicator.setVisibility(View.INVISIBLE);
                                listview.collapseGroupWithAnimation(groupPosition);
                            } else {
                                bottomIndicator.setVisibility(View.VISIBLE);
                                listview.expandGroupWithAnimation(groupPosition);
                            }
                            return true;
                        }

                    });
            }
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

    class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        public SurfaceHolder mHolder;
        Camera camera;

        public CameraPreview(Context context, Camera cameraInstance) {
            super(context);
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            this.camera = cameraInstance;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.e("asdf", "surfaceCreated");
            setCameraInstance(holder);
        }

        public void setCameraInstance(SurfaceHolder holder) {
            camera = helper.getCameraInstance();
            try {
                camera.reconnect();
                Camera.Parameters parameters = camera.getParameters();
                if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    parameters.set("orientation", "portrait");
                    camera.setDisplayOrientation(90);
                    parameters.setRotation(90);
                } else {
                    parameters.set("orientation", "landscape");
                    camera.setDisplayOrientation(0);
                    parameters.setRotation(0);
                }
                camera.setParameters(parameters);
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            setCameraInstance(holder);
//            Log.e("asdf", "surfaceChanged");
//            if (mHolder.getSurface() == null) {
//                return;
//            }
//            try {
//                camera.stopPreview();
//            } catch (Exception e) {
//            }
//            try {
//                Camera.Parameters parameters = camera.getParameters();
//                Camera.Size size = getBestPreviewSize(w, h);
//                parameters.setPreviewSize(size.width, size.height);
//                camera.setParameters(parameters);
//                camera.setPreviewDisplay(mHolder);
//                camera.startPreview();
//
//            } catch (Exception e) {
//            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.e("asdf", "surfaceDestroyed");

        }

        private Camera.Size getBestPreviewSize(int width, int height) {
            Camera.Size result = null;
            Camera.Parameters p = camera.getParameters();
            for (Camera.Size size : p.getSupportedPreviewSizes()) {
                if (size.width <= width && size.height <= height) {
                    if (result == null) {
                        result = size;
                    } else {
                        int resultArea = result.width * result.height;
                        int newArea = size.width * size.height;

                        if (newArea > resultArea) {
                            result = size;
                        }
                    }
                }
            }
            return result;

        }
    }
}
