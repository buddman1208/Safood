package kr.edcan.safood.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.nitrico.lastadapter.LastAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityGroupSearchBinding;
import kr.edcan.safood.databinding.ActivityGroupSetBinding;
import kr.edcan.safood.databinding.CommonListviewContentBinding;
import kr.edcan.safood.models.Group;
import kr.edcan.safood.utils.NetworkHelper;
import kr.edcan.safood.utils.NetworkInterface;
import kr.edcan.safood.utils.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupSearchActivity extends AppCompatActivity implements LastAdapter.LayoutHandler, LastAdapter.OnClickListener, LastAdapter.OnBindListener {

    ActivityGroupSearchBinding binding;
    ArrayList<Group> result = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_search);
        initAppbarLayout();
        setDefault();
    }

    private void setDefault() {
        binding.groupSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.fullFilled(binding.groupNameInput)) {
                    Call<ArrayList<Group>> searchGroup = NetworkHelper.getNetworkInstance().searchGroup(binding.groupNameInput.getText().toString().trim());
                    searchGroup.enqueue(new Callback<ArrayList<Group>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                            Log.e("asdf", "asdf");
//                                result = response.body();
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Group>> call, Throwable t) {

                        }
                    });
                    LastAdapter.with(result, 0)
                            .layoutHandler(GroupSearchActivity.this)
                            .onBindListener(GroupSearchActivity.this)
                            .onClickListener(GroupSearchActivity.this)
                            .into(binding.recyclerView);
                } else
                    Toast.makeText(GroupSearchActivity.this, "검색어를 입력해주세요!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initAppbarLayout() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.result.setVisibility(View.VISIBLE);
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

    @Override
    public int getItemLayout(@NotNull Object o, int i) {
        return R.layout.common_listview_content;
    }

    @Override
    public void onClick(@NotNull Object o, @NotNull View view, int i, int i1) {
        Group group = (Group) o;
        new MaterialDialog.Builder(this)
                .title("확인해주세요!")
                .content(group.getGroupname() + " 그룹에 가입합니다.")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        Call<S>
//                        Toast.makeText(GroupSearchActivity.this, "그룹 생성에 성공했습니다!", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        GroupSetActivity.finishThis();
//                        finish();

                    }
                })
                .show();

    }

    @Override
    public void onBind(@NotNull Object o, @NotNull View view, int i, int i1) {
        switch (i) {
            case R.layout.common_listview_content:
                Group group = (Group) o;
                CommonListviewContentBinding binding = DataBindingUtil.getBinding(view);
                binding.commonListViewTitle.setText(group.getGroupname());
                binding.commonListViewContent.setText(group.getAdmin());

        }
    }
}
