package kr.edcan.safood.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    static Activity activity;

    public static void finishThis() {
        if (activity != null) activity.finish();
    }

    ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        binding.login.setOnClickListener(this);
        binding.register.setOnClickListener(this);

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
