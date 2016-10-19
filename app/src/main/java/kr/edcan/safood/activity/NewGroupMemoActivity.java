package kr.edcan.safood.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityNewGroupMemoBinding;

public class NewGroupMemoActivity extends AppCompatActivity implements View.OnClickListener {

    final int RESULT_CODE = 6974;
    int currentColor = 0;
    View colorSelView[];
    ActivityNewGroupMemoBinding binding;
    String colors[] = new String[]{"#ef5350",
            "#29b6f6",
            "#4db6ac",
            "#a1887f",
            "#ff9100"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_group_memo);
        setAppbarLayout();
        setDefault();
    }


    private void setAppbarLayout() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("새로운 메모 작성하기");
    }

    private void setDefault() {
        colorSelView = new View[]{binding.color1, binding.color2, binding.color3, binding.color4, binding.color5};
        for (View v : colorSelView) v.setOnClickListener(this);
        setMemoColor(0);
        binding.searchFood.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.color1:
                setMemoColor(0);
                break;
            case R.id.color2:
                setMemoColor(1);
                break;
            case R.id.color3:
                setMemoColor(2);
                break;
            case R.id.color4:
                setMemoColor(3);
                break;
            case R.id.color5:
                setMemoColor(4);
                break;
            case R.id.searchFood:
                startActivityForResult(new Intent(getApplicationContext(), FoodSearchActivity.class).putExtra("fromGroupMemo", true), RESULT_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setMemoColor(int position) {
        currentColor = position;
        for (int i = 0; i < colorSelView.length; i++) {
            colorSelView[i].setBackgroundColor(Color.parseColor(colors[i]));
            colorSelView[i].setAlpha(((float)((i == position) ? 1 : 0.3)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
