package kr.edcan.safood.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityGroupSetBinding;

public class GroupSetActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityGroupSetBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_set);
        binding.joinGroup.setOnClickListener(this);
        binding.makeGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.joinGroup:
                startActivity(new Intent(getApplicationContext(), GroupSearchActivity.class));
                finish();
                break;
            case R.id.makeGroup:
                startActivity(new Intent(getApplicationContext(), GroupGenerateActivity.class));
                finish();
                break;
        }
    }
}
