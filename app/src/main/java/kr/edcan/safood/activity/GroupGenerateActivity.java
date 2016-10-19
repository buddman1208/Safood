package kr.edcan.safood.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityGroupGenerateBinding;
import kr.edcan.safood.models.Group;
import kr.edcan.safood.utils.NetworkHelper;
import kr.edcan.safood.utils.StringUtils;
import retrofit2.Call;

public class GroupGenerateActivity extends AppCompatActivity {

    ActivityGroupGenerateBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_generate);
        binding.groupGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.fullFilled(binding.groupNameInput)){
//                    Call<Group> generateGroup = NetworkHelper.getNetworkInstance().
                }
            }
        });
    }
}
