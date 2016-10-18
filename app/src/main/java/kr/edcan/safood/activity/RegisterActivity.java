package kr.edcan.safood.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityRegisterBinding;
import kr.edcan.safood.models.User;
import kr.edcan.safood.utils.NetworkHelper;
import kr.edcan.safood.utils.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Call<User> registerUser;
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.register.setOnClickListener(this);
        binding.cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                checkRegister();
                break;
            case R.id.cancel:
                finish();
        }
    }

    private void checkRegister() {
        if (StringUtils.fullFilled(binding.registerEmail, binding.registerName, binding.registerPassword, binding.registerPasswordRe)) {
            if (!StringUtils.validEmail(binding.registerEmail)) {
                Toast.makeText(this, "올바른 이메일 형식을 입력해주세요!", Toast.LENGTH_SHORT).show();
            } else if (!StringUtils.checkPassword(binding.registerPassword, binding.registerPasswordRe)) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
            } else {
                registerUser = NetworkHelper.getNetworkInstance().registerUser(
                        binding.registerName.getText().toString().trim(),
                        binding.registerPassword.getText().toString(),
                        binding.registerEmail.getText().toString().trim()
                );
                registerUser.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        switch (response.code()) {
                            case 200:
                                Toast.makeText(RegisterActivity.this, "입력하신 정보로 가입되었습니다.\n로그인해주세요!.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                                break;
                            case 409:
                                Toast.makeText(RegisterActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(RegisterActivity.this, "서버와의 연동에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                Log.e("asdf", response.code() + "");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "서버와의 연동에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("asdf", t.getMessage());

                    }
                });
            }
        } else Toast.makeText(RegisterActivity.this, "빈칸 없이 입력해주세요!", Toast.LENGTH_SHORT).show();
    }
}
