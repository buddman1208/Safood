package kr.edcan.safood.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Network;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityAuthBinding;
import kr.edcan.safood.models.User;
import kr.edcan.safood.utils.DataManager;
import kr.edcan.safood.utils.NetworkHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    static Activity activity;

    public static void finishThis() {
        if (activity != null) activity.finish();
    }

    ActivityAuthBinding binding;
    DataManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        setPermission();
    }

    private void setPermission() {
        new TedPermission(this)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .setRationaleMessage("Safood를 사용하기 위해 권한을 요청합니다.")
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        checkAuth();

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(AuthActivity.this, "권한을 허용하지 않아 Safood를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .check();
    }

    private void checkAuth() {
        manager = new DataManager(getApplicationContext());
        if (manager.getActiveUser().first) {
            Call<User> authenticateUser = NetworkHelper.getNetworkInstance().authenticateUser(
                    manager.getActiveUser().second.getUserid(),
                    manager.getActiveUser().second.getApikey()
            );
            authenticateUser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    switch (response.code()) {
                        case 200:
                            manager.saveUserInfo(response.body());
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Toast.makeText(AuthActivity.this, response.body().getUsername() + " 님 환영합니다!", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        default:
                            Log.e("asdf", response.code() + "");
                            initUI();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    initUI();
                    Log.e("asdf", t.getMessage());
                }
            });
        } else initUI();
    }

    private void initUI() {
        binding.login.setOnClickListener(this);
        binding.register.setOnClickListener(this);
        binding.bottomBarParent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.register:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
        }
    }
}
