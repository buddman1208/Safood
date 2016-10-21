package kr.edcan.safood.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityNewGroupMemoBinding;
import kr.edcan.safood.models.Food;
import kr.edcan.safood.models.GroupMemo;
import kr.edcan.safood.models.User;
import kr.edcan.safood.utils.DataManager;
import kr.edcan.safood.utils.ImageSingleton;
import kr.edcan.safood.utils.NetworkHelper;
import kr.edcan.safood.utils.StringUtils;
import kr.edcan.safood.views.RoundImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    ArrayList<Food> memolist = new ArrayList<>();

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
        binding.memoComplete.setOnClickListener(this);
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
                break;
            case R.id.memoComplete:
                postArticle();
                break;

        }
    }

    private void postArticle() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Food f : memolist) {
            arrayList.add(f.getName());
        }
        Call<ArrayList<GroupMemo>> postMemo = NetworkHelper.getNetworkInstance().addMemo(
                binding.title.getText().toString().trim(),
                binding.content.getText().toString().trim(),
                currentColor,
                StringUtils.convertArraytoString(arrayList),
                new DataManager(getApplicationContext()).getActiveUser().second.getGroupid()
                );
        postMemo.enqueue(new Callback<ArrayList<GroupMemo>>() {
            @Override
            public void onResponse(Call<ArrayList<GroupMemo>> call, Response<ArrayList<GroupMemo>> response) {
                switch (response.code()){
                    case 200:
                        Toast.makeText(NewGroupMemoActivity.this, "정상적으로 메모가 작성되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GroupMemo>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE && resultCode == RESULT_OK) {
            Food food = new Gson().fromJson(data.getStringExtra("foodInfo"), Food.class);
            memolist.add(food);
            LinearLayout parent = binding.foodContainer;
            View addtarget = getLayoutInflater().inflate(R.layout.food_memo_foodadd, null, false);
            RoundImageView imageView = (RoundImageView) addtarget.findViewById(R.id.image);
            TextView title = (TextView) addtarget.findViewById(R.id.text);
            imageView.setImageUrl(food.getThumbnail(), ImageSingleton.getInstance(this).getImageLoader());
            title.setText(food.getName());
            parent.addView(addtarget);
        }
    }

    private void setMemoColor(int position) {
        currentColor = position;
        for (int i = 0; i < colorSelView.length; i++) {
            colorSelView[i].setBackgroundColor(Color.parseColor(colors[i]));
            colorSelView[i].setAlpha(((float) ((i == position) ? 1 : 0.3)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
