package kr.edcan.safood.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import kr.edcan.safood.R;
import kr.edcan.safood.camera.CameraManager;
import kr.edcan.safood.utils.AmbientLightManager;
import kr.edcan.safood.utils.CameraActivityHandler;
import kr.edcan.safood.utils.InactivityTimer;
import kr.edcan.safood.utils.IntentSource;
import kr.edcan.safood.utils.ResultHandler;
import kr.edcan.safood.utils.ResultHandlerFactory;
import kr.edcan.safood.views.ViewfinderView;

public class CameraActivity extends AppCompatActivity  implements SurfaceHolder.Callback  {

    private final String TAG = "Safood";
    public static CameraManager cameraManager;
    public static void performClick() {
        cameraManager.autoFocus();
    }
    public CameraActivityHandler handler;
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


    @Override
    protected void onResume() {
        super.onResume();

        cameraManager = new CameraManager(getApplication());
        handler = null;
        lastResult = null;
        resetStatusView();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
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

    private void setCameraDefault() {
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        ambientLightManager = new AmbientLightManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setCameraDefault();
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        viewfinderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraManager.autoFocus();
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
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
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
            inactivityTimer.onActivity();
            lastResult = rawResult;
            ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
            handleDecodeInternally(rawResult, resultHandler, barcode);
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
                handler = new CameraActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
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
}
