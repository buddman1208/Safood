package kr.edcan.safood.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityGroupGenerateBinding;
import kr.edcan.safood.models.Group;
import kr.edcan.safood.utils.DataManager;
import kr.edcan.safood.utils.NetworkHelper;
import kr.edcan.safood.utils.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupGenerateActivity extends AppCompatActivity {

    DataManager manager;
    ActivityGroupGenerateBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new DataManager(getApplicationContext());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_generate);
        binding.groupGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.fullFilled(binding.groupNameInput)){
                    Call<Group> generateGroup = NetworkHelper.getNetworkInstance()
                            .createGroup(binding.groupNameInput.getText().toString().trim(), manager.getActiveUser().second.getApikey());
                    generateGroup.enqueue(new Callback<Group>() {
                        @Override
                        public void onResponse(Call<Group> call, Response<Group> response) {
                            switch (response.code()){
                                case 200:
                                    Toast.makeText(GroupGenerateActivity.this, "그룹 생성에 성공했습니다!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    GroupSetActivity.finishThis();
                                    finish();
                                    break;
                                case 409:
                                    Toast.makeText(GroupGenerateActivity.this, "이미 해당 이름을 가진 그룹이 있습니다", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<Group> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}
