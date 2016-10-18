package kr.edcan.safood.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import kotlin.internal.InlineOnly;
import kr.edcan.safood.R;
import kr.edcan.safood.adapter.MainSafoodAdapter;
import kr.edcan.safood.camera.CameraManager;
import kr.edcan.safood.databinding.ActivityMainBinding;
import kr.edcan.safood.databinding.MainSafoodBinding;
import kr.edcan.safood.databinding.MainSearchBinding;
import kr.edcan.safood.models.SafoodContentData;
import kr.edcan.safood.models.SafoodTitleData;
import kr.edcan.safood.utils.AmbientLightManager;
import kr.edcan.safood.utils.InactivityTimer;
import kr.edcan.safood.utils.IntentSource;
import kr.edcan.safood.utils.MainActivityHandler;
import kr.edcan.safood.utils.ResultHandler;
import kr.edcan.safood.utils.ResultHandlerFactory;
import kr.edcan.safood.utils.SafoodHelper;
import kr.edcan.safood.views.SlidingExpandableListView;
import kr.edcan.safood.views.ViewfinderView;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private final String TAG = "TAG";

    public static CameraManager cameraManager;

    public static void performClick() {
        cameraManager.autoFocus();
    }

    public MainActivityHandler handler;
    public Result savedResultToShow;
    public static ViewfinderView viewfinderView;
    public Result lastResult;
    public boolean hasSurface;
    public IntentSource source;
    public Collection<BarcodeFormat> decodeFormats;
    public Map<DecodeHintType, ?> decodeHints;
    public String characterSet;
    public InactivityTimer inactivityTimer;
    public AmbientLightManager ambientLightManager;

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    String[] titleArr = new String[]{"검색", "안전한 음식", "그룹 메모", "음식 백과", "내 정보"};

    // Helper, Utils
    SafoodHelper helper;

    // DataBindings
    public ActivityMainBinding binding;
    public MainSearchBinding mainSearchBinding;
    public MainSafoodBinding mainSafoodBinding;

    // Widgets
    SlidingExpandableListView slidingListView;
    ViewPager pager;
    RelativeLayout mainSearchFrame;

    @Override
    protected void onResume() {
        super.onResume();

        cameraManager = new CameraManager(getApplication());
        handler = null;
        lastResult = null;
        resetStatusView();
        SurfaceView surfaceView = binding.previewView;
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }
        ambientLightManager.start(cameraManager);
        inactivityTimer.onResume();
        source = IntentSource.NONE;
        decodeFormats = null;
        characterSet = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainSearchBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.main_search, null, true);
        mainSafoodBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.main_safood, null, true);
        setCameraDefault();
        setDefault();
    }



    private void setCameraDefault() {
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        ambientLightManager = new AmbientLightManager(this);
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
                } else {
                    binding.appbarlayout.setBackgroundColor(Color.WHITE);
                    binding.toolbar.setBackgroundColor(Color.WHITE);
                    binding.mainBackground.setBackgroundColor(Color.WHITE);
                }
            }


            @Override
            public void onPageSelected(int position) {
                binding.appbarlayout.setExpanded(true, true);
                binding.mainAppBarSearch.setEnabled(position == 0);
                if (position == 0) restartPreviewAfterDelay(0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = binding.previewView;
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (source == IntentSource.NATIVE_APP_INTENT) {
                    Log.e("asdf", "asdf");
                    finish();
                    return true;
                }
                if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                cameraManager.setTorch(false);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraManager.setTorch(true);
                return true;
        }
        return super.onKeyDown(keyCode, event);
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
        public void onResume() {
            super.onResume();
            if (viewfinderView != null) {
                viewfinderView.setCameraManager(cameraManager);
                viewfinderView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraManager.autoFocus();
                    }
                });
            }
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
                    view = inflater.inflate(R.layout.main_groupmemo, container, false);
                    break;
                case 3:
                    view = inflater.inflate(R.layout.main_foodencyclopedia, container, false);
                    break;
                case 4:
                    view = inflater.inflate(R.layout.main_info, container, false);
                    break;
            }
            setPage(view, getArguments().getInt(ARG_SECTION_NUMBER), inflater);
            return view;
        }

        private void setPage(View view, int position, LayoutInflater inflater) {
            switch (position) {
                case 0:
                    RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.touch);
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainActivity.performClick();
                        }
                    });
                    viewfinderView = (ViewfinderView) view.findViewById(R.id.viewfinder_view);
                    viewfinderView.setCameraManager(cameraManager);
                    viewfinderView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cameraManager.autoFocus();
                        }
                    });

                    break;
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
                    listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                            Toast.makeText(getContext(), "asdf", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                    break;
            }
        }
    }


    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        Log.e("asdf", "Catch");
        if (binding.mainPager.getCurrentItem() == 0) {
            Log.e("asdf", "on 0");
            inactivityTimer.onActivity();
            lastResult = rawResult;
            ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
            handleDecodeInternally(rawResult, resultHandler, barcode);
        }
    }

    // Put up our own UI for how to handle the decoded contents.
    private void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
        CharSequence displayContents = resultHandler.getDisplayContents();
        viewfinderView.setVisibility(View.GONE);
        showResultDialog(displayContents.toString());
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                handler = new MainActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        MaterialDialog builder = new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content("기기의 카메라에 접근하는 중에 문제가 발생했습니다.")
                .positiveText("확인")
                .show();
    }

    private void showResultDialog(String resultData) {
        MaterialDialog builder = new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(resultData)
                .positiveText("확인")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        restartPreviewAfterDelay(0);
                    }
                })
                .show();
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                restartPreviewAfterDelay(0);
            }
        });
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView() {
        if (viewfinderView != null) viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;
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
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < titleArr.length)
                return titleArr[position];
            return null;
        }
    }

}
