package kr.edcan.safood.activity;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.ActivityGroupGenerateBinding;
import kr.edcan.safood.models.Group;
import kr.edcan.safood.utils.DataManager;
import kr.edcan.safood.utils.NetworkHelper;
import kr.edcan.safood.utils.StringUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupGenerateActivity extends AppCompatActivity {

    private boolean isFileSelected = false;
    private String picturePath = "";
    DataManager manager;
    ActivityGroupGenerateBinding binding;
    private static int RESULT_LOAD_IMAGE = 6973;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new DataManager(getApplicationContext());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_generate);
        setAppbarLayout();
        binding.groupImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        binding.groupGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.fullFilled(binding.groupNameInput)) {
                    if (isFileSelected) {
                        RequestBody groupname = RequestBody.create(MediaType.parse("text/plain"), binding.groupNameInput.getText().toString().trim());
                        RequestBody apikey = RequestBody.create(MediaType.parse("text/plain"), manager.getActiveUser().second.getApikey());
                        RequestBody imageBody = RequestBody.create(MediaType.parse("image/png"), new File(picturePath));
                        Call<Group> generateGroup = NetworkHelper.getNetworkInstance()
                                .createGroup(imageBody, groupname, apikey);
                        generateGroup.enqueue(new Callback<Group>() {
                            @Override
                            public void onResponse(Call<Group> call, Response<Group> response) {
                                switch (response.code()) {
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
                    } else
                        Toast.makeText(GroupGenerateActivity.this, "이미지를 선택해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAppbarLayout() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("그룹 생성하기");
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            binding.groupImageCover.setVisibility(View.GONE);
            isFileSelected = true;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            binding.groupImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        } else isFileSelected = false;
    }
}
