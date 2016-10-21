package kr.edcan.safood.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityDetailViewBinding;
import kr.edcan.safood.models.Food;
import kr.edcan.safood.models.SafoodGroup;
import kr.edcan.safood.models.User;
import kr.edcan.safood.utils.DataManager;
import kr.edcan.safood.utils.ImageSingleton;
import kr.edcan.safood.utils.NetworkHelper;
import kr.edcan.safood.views.CartaTagView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewActivity extends AppCompatActivity {

    boolean isSafe = false;
    ActivityDetailViewBinding binding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_view);
        setDefault();
        setAppbarLayout();
    }

    private void setAppbarLayout() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        binding.toolbar.setBackgroundColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("식품 자세히 보기");
    }

    private void setDefault() {
        intent = getIntent();
        final Food food = new Gson().fromJson(intent.getStringExtra("json"), Food.class);
        binding.title.setText(food.getName());
        binding.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        binding.image.setImageUrl(food.getThumbnail(), ImageSingleton.getInstance(this).getImageLoader());
        if (food.getAllergy().size() == 0) {
            isSafe = true;
            binding.addToSafood.setText("안전한 음식에 추가");
            binding.alertParent.setVisibility(View.GONE);
        } else {
            binding.alertParent.setVisibility(View.VISIBLE);
            binding.addToSafood.setText("닫기");
            for (String s : food.getAllergy()) {
                LinearLayout parent = binding.alertCartaViewParent;
                CartaTagView view = (CartaTagView) getLayoutInflater().inflate(R.layout.carta_tag_view, null, false);
                view.setText(s);
                parent.addView(view);
            }
        }
        binding.ingredientAll.setText(food.getFoodIngredient());
        binding.addToSafood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSafe) {
                    Call<ArrayList<SafoodGroup>> getSafoodGroupList = NetworkHelper.getNetworkInstance().getSafoodGroupList(new DataManager(getApplicationContext()).getActiveUser().second.getApikey());
                    getSafoodGroupList.enqueue(new Callback<ArrayList<SafoodGroup>>() {
                        @Override
                        public void onResponse(Call<ArrayList<SafoodGroup>> call, final Response<ArrayList<SafoodGroup>> response) {
                            switch (response.code()) {
                                case 200:
                                   showAddDialog(response, food);
                                    break;
                                default:
                                    break;

                            }
                        }


                        @Override
                        public void onFailure(Call<ArrayList<SafoodGroup>> call, Throwable t) {

                        }
                    });
                } else finish();
            }
        });
    }

    private void showAddDialog(final Response<ArrayList<SafoodGroup>> response, final Food food) {
        final ArrayList<String> groupList = new ArrayList<String>();
        for (int i = 0; i < response.body().size(); i++) {
            groupList.add(response.body().get(i).getName());
        }
        new MaterialDialog.Builder(DetailViewActivity.this)
                .title("추가할 그룹 선택")
                .items(groupList)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Call<ArrayList<SafoodGroup>> addToGroup =
                                NetworkHelper.getNetworkInstance().addToSafoodGroup(
                                        response.body().get(which).getId(),
                                        new DataManager(getApplicationContext()).getActiveUser().second.getApikey(),
                                        food.getFoodid());
                        Log.e("asdf", food.getFoodid());
                        addToGroup.enqueue(new Callback<ArrayList<SafoodGroup>>() {
                            @Override
                            public void onResponse(Call<ArrayList<SafoodGroup>> call, Response<ArrayList<SafoodGroup>> response) {
                                switch (response.code()){
                                    case 200:
                                        Toast.makeText(DetailViewActivity.this, "안전한 음식에 추가되었습니다!", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(DetailViewActivity.this, response.code()+"", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<SafoodGroup>> call, Throwable t) {
                                Log.e("asdf", t.getMessage());
                            }
                        });
                    }
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

