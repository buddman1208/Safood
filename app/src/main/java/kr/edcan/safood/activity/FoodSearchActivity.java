package kr.edcan.safood.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.nitrico.lastadapter.BR;
import com.github.nitrico.lastadapter.LastAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityFoodSearchBinding;
import kr.edcan.safood.databinding.FoodSearchContentBinding;
import kr.edcan.safood.models.Food;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.groupSearch:
                search();
        }

    }

    private void search() {
        arrayList.clear();
        arrayList.add(new Food("asdf"));
        arrayList.add(new Food("asdf"));
        arrayList.add(new Food("asdf"));
        arrayList.add(new Food("asdf"));
        LastAdapter.with(arrayList, BR._all)
                .onBindListener(new LastAdapter.OnBindListener() {
                    @Override
                    public void onBind(@NotNull Object o, @NotNull View view, int i, int i1) {
                        Food food = (Food) o;
                        FoodSearchContentBinding binding = DataBindingUtil.getBinding(view);
//                        binding.image.setImageUrl(food);
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
                        if(fromGroupMemo) {
//                            setResult(RESULT_OK, intent.putExtra());
                            finish();
                        }
                    }
                })
                .into(binding.recyclerview);
    }
}
