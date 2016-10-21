package kr.edcan.safood.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityGroupMemoBinding;
import kr.edcan.safood.models.GroupMemo;
import kr.edcan.safood.models.User;
import kr.edcan.safood.utils.ImageSingleton;
import kr.edcan.safood.views.RoundImageView;

public class GroupMemoActivity extends AppCompatActivity {

    Intent intent;
    GroupMemo memo;
    ActivityGroupMemoBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_memo);
        setDefault();
        setAppbarLayout();
    }

    private void setAppbarLayout() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("메모 보기");
    }

    private void setDefault() {
        intent = getIntent();
        memo = new Gson().fromJson(intent.getStringExtra("json"), GroupMemo.class);
        binding.title.setText(memo.getTitle());
        binding.content.setText(memo.getContent());
        for(String s : memo.getFoods()){
            LinearLayout parent = binding.foodContainer;
            View addtarget = getLayoutInflater().inflate(R.layout.food_memo_foodadd, null, false);
            RoundImageView imageView = (RoundImageView) addtarget.findViewById(R.id.image);
            TextView title = (TextView) addtarget.findViewById(R.id.text);
            title.setText(s);
            imageView.setVisibility(View.GONE);
            parent.addView(addtarget);
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
