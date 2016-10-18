package kr.edcan.safood.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityLoginBinding;
import kr.edcan.safood.models.User;
import kr.edcan.safood.utils.DataManager;
import kr.edcan.safood.utils.NetworkHelper;
import kr.edcan.safood.utils.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Call<User> nativeLogin;
    DataManager manager;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new DataManager(getApplicationContext());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.login.setOnClickListener(this);
        binding.cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                checkLogin();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    private void checkLogin() {
        if (StringUtils.fullFilled(binding.loginEmail, binding.loginPassword)) {
            nativeLogin = NetworkHelper.getNetworkInstance().nativeLogin(
                    binding.loginEmail.getText().toString().trim(),
                    binding.loginPassword.getText().toString().trim()
            );
            nativeLogin.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    switch (response.code()) {
                        case 200:
                            manager.saveUserInfo(response.body());
                            Toast.makeText(LoginActivity.this, response.body().getUsername() + " 님 환영합니다!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            AuthActivity.finishThis();
                            finish();
                            break;
                        case 400:
                            Toast.makeText(LoginActivity.this, "아이디 혹은 비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "서버와의 연동에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                            Log.e("asdf", response.code() + "");
                            break;
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "서버와의 연동에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("asdf", t.getMessage());

                }
            });
        } else Toast.makeText(this, "빈칸 없이 입력해주세요!", Toast.LENGTH_SHORT).show();
    }
}
