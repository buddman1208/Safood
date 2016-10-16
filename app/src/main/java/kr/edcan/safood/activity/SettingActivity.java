package kr.edcan.safood.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.github.nitrico.lastadapter.LastAdapter;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivitySettingBinding;
import kr.edcan.safood.databinding.CommonListviewContentBinding;

public class SettingActivity extends AppCompatActivity implements LastAdapter.OnBindListener, LastAdapter.OnClickListener, LastAdapter.LayoutHandler {

    ActivitySettingBinding binding;
    ArrayList<CommonListViewData> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        initAppbarLayout();
        setData();
    }


    private void initAppbarLayout() {
        binding.toolbar.setBackgroundColor(Color.WHITE);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("계정 설정");
    }

    private void setData() {
        arrayList.add(new CommonListViewData("언어 설정", "Set language for using app"));
        arrayList.add(new CommonListViewData("회원정보 수정", "사용자의 기본적인 정보를 수정합니다."));
        arrayList.add(new CommonListViewData("로그아웃", "Safood에서 로그아웃합니다."));
        binding.settingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LastAdapter.with(arrayList, 0)
                .map(CommonListViewData.class, R.layout.common_listview_content)
                .layoutHandler(this)
                .onBindListener(this)
                .onClickListener(this)
                .into(binding.settingsRecyclerView);
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

    @Override
    public void onBind(@NotNull Object o, @NotNull View view, int type, int position) {
        CommonListViewData data = (CommonListViewData) o;
        CommonListviewContentBinding binding = DataBindingUtil.getBinding(view);
        binding.commonListViewTitle.setText(data.getTitle());
        binding.commonListViewContent.setText(data.getContent());
        binding.commonListViewIcon.setVisibility(View.GONE);
    }

    @Override
    public void onClick(@NotNull Object o, @NotNull View view, int i, int i1) {
        switch (i1){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    @Override
    public int getItemLayout(@NotNull Object o, int i) {
        return R.layout.common_listview_content;
    }
}
