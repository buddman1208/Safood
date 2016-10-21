package kr.edcan.safood.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Network;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.nitrico.lastadapter.BR;
import com.github.nitrico.lastadapter.LastAdapter;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityFoodSearchBinding;
import kr.edcan.safood.databinding.FoodSearchContentBinding;
import kr.edcan.safood.models.Food;
import kr.edcan.safood.utils.ImageSingleton;
import kr.edcan.safood.utils.NetworkHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodSearchActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityFoodSearchBinding binding;
    boolean fromGroupMemo;
    ArrayList<Food> arrayList = new ArrayList<>();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_food_search);
        setAppbarLayout();
        setDefault();
    }

    private void setAppbarLayout() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("음식 검색");
        binding.groupSearch.setOnClickListener(this);
    }

    private void setDefault() {
        intent = getIntent();
        fromGroupMemo = intent.getBooleanExtra("fromGroupMemo", false);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.groupSearch:
                search();
        }

    }

    private void search() {
        arrayList.clear();
        Call<ArrayList<Food>> getFoodList = NetworkHelper.getNetworkInstance().searchFoodbyKeyword(binding.groupNameInput.getText().toString().trim());
        getFoodList.enqueue(new Callback<ArrayList<Food>>() {
            @Override
            public void onResponse(Call<ArrayList<Food>> call, final Response<ArrayList<Food>> response) {
                switch (response.code()) {
                    case 200:
                        arrayList.addAll(response.body());
                        LastAdapter.with(arrayList, BR._all)
                                .onBindListener(new LastAdapter.OnBindListener() {
                                    @Override
                                    public void onBind(@NotNull Object o, @NotNull View view, int i, int i1) {
                                        Food food = (Food) o;
                                        FoodSearchContentBinding binding = DataBindingUtil.getBinding(view);
                                        binding.image.setImageUrl(food.getThumbnail(), ImageSingleton.getInstance(getApplicationContext()).getImageLoader());
                                        binding.text.setText(food.getName());
                                    }
                                })
                                .layoutHandler(new LastAdapter.LayoutHandler() {
                                    @Override
                                    public int getItemLayout(@NotNull Object o, int i) {
                                        return R.layout.food_search_content;
                                    }
                                })
                                .onClickListener(new LastAdapter.OnClickListener() {
                                    @Override
                                    public void onClick(@NotNull Object o, @NotNull View view, int i, int i1) {
                                        if (fromGroupMemo) {
                                            setResult(RESULT_OK, intent.putExtra("foodInfo",
                                                    new Gson().toJson(response.body().get(i1))));
                                            finish();
                                        } else {
                                            startActivity(new Intent(getApplicationContext(), DetailViewActivity.class)
                                                    .putExtra("json", new Gson().toJson(response.body().get(i1))));
                                        }
                                    }
                                })
                                .into(binding.recyclerview);
                        break;
                    default:
                        Toast.makeText(FoodSearchActivity.this, "검색결과가 없습니다! 다른 검색어로 시도해보세요!", Toast.LENGTH_SHORT).show();
                        Log.e("asdf", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Food>> call, Throwable t) {
                Log.e("asdf", t.getMessage() + " ㅁㄴㅇㄻㄴㅇㄹ");

            }
        });
    }
}
