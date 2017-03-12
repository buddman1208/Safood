package kr.edcan.safood.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.nitrico.lastadapter.BR;
import com.github.nitrico.lastadapter.LastAdapter;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

import kr.edcan.safood.R;
import kr.edcan.safood.adapter.MainSafoodAdapter;
import kr.edcan.safood.databinding.ActivityMainBinding;
import kr.edcan.safood.databinding.GroupmemoGridBinding;
import kr.edcan.safood.databinding.MainGroupmemoBinding;
import kr.edcan.safood.databinding.MainInfoBinding;
import kr.edcan.safood.databinding.MainSafoodBinding;
import kr.edcan.safood.databinding.MainSearchBinding;
import kr.edcan.safood.databinding.NewfolderViewBinding;
import kr.edcan.safood.models.Food;
import kr.edcan.safood.models.GroupMemo;
import kr.edcan.safood.models.SafoodContentData;
import kr.edcan.safood.models.SafoodGroup;
import kr.edcan.safood.models.SafoodTitleData;
import kr.edcan.safood.models.User;
import kr.edcan.safood.utils.DataManager;
import kr.edcan.safood.utils.NetworkHelper;
import kr.edcan.safood.utils.SafoodHelper;
import kr.edcan.safood.utils.StringUtils;
import kr.edcan.safood.views.CartaTagView;
import kr.edcan.safood.views.SlidingExpandableListView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String[] titleArr = new String[]{"안전한 음식", "그룹 메모", "음식 백과", "내 정보"};

    // Helper, Utils
    SafoodHelper helper;
    DataManager manager;

    // DataBindings
    public ActivityMainBinding binding;
    // Widgets
    SlidingExpandableListView slidingListView;
    ViewPager pager;
    RelativeLayout mainSearchFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setDefault();
    }


    private void setDefault() {
        manager = new DataManager(getApplicationContext());
        if (manager.getActiveUser().second.getGroupid().equals("")) {
            startActivity(new Intent(getApplicationContext(), GroupSetActivity.class));
            finish();
        }
        helper = new SafoodHelper(getApplicationContext());
        pager = (ViewPager) findViewById(R.id.mainPager);
        binding.mainPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        binding.tablayout.setupWithViewPager(binding.mainPager);
        binding.mainPager.setCurrentItem(0);
        binding.mainPager.setOffscreenPageLimit(4);
        binding.mainAppbarLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CameraActivity.class));
            }
        });
        binding.mainNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.mainDrawer.openDrawer(GravityCompat.START);
            }
        });
    }


    public static class MainFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "pageNumber";
        boolean hasLaunched = false;
        ArrayList<SafoodTitleData> titleArr = new ArrayList<>();
        ArrayList<ArrayList<SafoodContentData>> contentArr = new ArrayList<>();
        MainSafoodAdapter adapter;
        MaterialDialog loading;
        MaterialDialog.Builder newFolder;
        CartaTagView[] arr;
        SlidingExpandableListView listview;

        public static MainFragment newInstance(int pageNum) {
            Bundle args = new Bundle();
            MainFragment fragment = new MainFragment();
            args.putInt(ARG_SECTION_NUMBER, pageNum);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onResume() {
            super.onResume();
            if (hasLaunched) {
                Call<ArrayList<SafoodGroup>> getSafoodGroupList = NetworkHelper.getNetworkInstance().getSafoodGroupList(new DataManager(getContext()).getActiveUser().second.getApikey());
                getSafoodGroupList.enqueue(new Callback<ArrayList<SafoodGroup>>() {
                    @Override
                    public void onResponse(Call<ArrayList<SafoodGroup>> call, Response<ArrayList<SafoodGroup>> response) {
                        switch (response.code()) {
                            case 200:
                                setSafoodList(response.body(), listview, false);
                                break;
                            default:
                                break;

                        }
                    }


                    @Override
                    public void onFailure(Call<ArrayList<SafoodGroup>> call, Throwable t) {

                    }
                });
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            setDefault(inflater);
            ViewDataBinding binding = null;
            Log.e("asdf", getArguments().getInt(ARG_SECTION_NUMBER) + " Page");
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 0:
                    binding = DataBindingUtil.inflate(inflater, R.layout.main_safood, container, false);
                    binding.setVariable(BR._all, "asdf");
                    break;
                case 1:
                    binding = DataBindingUtil.inflate(inflater, R.layout.main_groupmemo, container, false);
                    break;
                case 2:
                    binding = DataBindingUtil.inflate(inflater, R.layout.main_foodencyclopedia, container, false);
                    break;
                case 3:
                    binding = DataBindingUtil.inflate(inflater, R.layout.main_info, container, false);
                    break;
            }
            setPage(binding, container, getArguments().getInt(ARG_SECTION_NUMBER), inflater);
            return binding.getRoot();
        }

        public void setDefault(LayoutInflater inflater) {
            loading = new MaterialDialog.Builder(inflater.getContext())
                    .title("로딩중입니다.")
                    .cancelable(false)
                    .progress(true, 0)
                    .build();
            final NewfolderViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.newfolder_view, null, false);
            newFolder = new MaterialDialog.Builder(getContext())
                    .title("새로운 안전한 음식 카테고리 추가")
                    .customView(binding.getRoot(), false).positiveText("생성").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String groupName = binding.editText.getText().toString().trim();
                            if (!groupName.equals("")) {
                                NetworkHelper.getNetworkInstance().newSafoodGroup(
                                        groupName,
                                        new DataManager(getContext()).getActiveUser().second.getApikey(),
                                        0
                                ).enqueue(new Callback<ArrayList<SafoodGroup>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<SafoodGroup>> call, Response<ArrayList<SafoodGroup>> response) {
                                        switch (response.code()) {
                                            case 200:
                                                Toast.makeText(getContext(), "폴더가 생성되었습니다!", Toast.LENGTH_SHORT).show();
                                                setSafoodList(response.body(), listview, true);
                                                binding.errorMessage.setVisibility(View.GONE);
                                                break;
                                            case 409:
                                                binding.errorMessage.setText("이미 해당 이름을 가진 폴더가 있습니다!");
                                                binding.errorMessage.setVisibility(View.VISIBLE);
                                                newFolder.show();
                                        }
                                        binding.editText.setText("");
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<SafoodGroup>> call, Throwable t) {
                                        Log.e("asdf", t.getMessage());
                                    }
                                });
                            } else {
                                binding.errorMessage.setVisibility(View.VISIBLE);
                                binding.errorMessage.setText("공백 없이 입력해주세요!");
                                newFolder.show();
                                binding.editText.setText("");
                            }
                        }
                    }).cancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            binding.errorMessage.setVisibility(View.GONE);
                        }
                    });
        }

        private void setSafoodList(ArrayList<SafoodGroup> arrayList, final SlidingExpandableListView listview, boolean append) {
            hasLaunched = true;
            titleArr.clear();
            contentArr.clear();
            for (int i = 0; i < arrayList.size(); i++) {
                titleArr.add(new SafoodTitleData(arrayList.get(i).getName(), arrayList.get(i).getFoodList().size()));
                ArrayList<SafoodContentData> data = new ArrayList<>();
                for (Food f : arrayList.get(i).getFoodList()) {
                    data.add(new SafoodContentData(f.getFoodName()));
                }
                contentArr.add(data);
            }
            if (!append)
                adapter = new MainSafoodAdapter(getContext(), titleArr, contentArr);
            else adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);
            listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    if (titleArr.get(groupPosition).getContentSize() != 0) {
                        View view = adapter.getGroupView(groupPosition, true, null, null);
                        RelativeLayout bottomIndicator = (RelativeLayout) view.findViewById(R.id.mainListGroupBottomIndicator);
                        if (listview.isGroupExpanded(groupPosition)) {
                            bottomIndicator.setVisibility(View.INVISIBLE);
                            listview.collapseGroupWithAnimation(groupPosition);
                        } else {
                            bottomIndicator.setVisibility(View.VISIBLE);
                            listview.expandGroupWithAnimation(groupPosition);
                        }
                    }
                    return true;
                }

            });
            listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                    Toast.makeText(getContext(), "asdf", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        private void setPage(ViewDataBinding binding, final ViewGroup container, int position, final LayoutInflater inflater) {
            switch (position) {
                case 0:
                    MainSafoodBinding safoodBinding = (MainSafoodBinding) binding;
                    listview = safoodBinding.mainExpandableListView;
                    safoodBinding.editSafoodGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(), "항목을 길게 눌러 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    NetworkHelper.getNetworkInstance().getSafoodGroupList(new DataManager(getContext()).getActiveUser().second.getApikey()).enqueue(new Callback<ArrayList<SafoodGroup>>() {
                        @Override
                        public void onResponse(Call<ArrayList<SafoodGroup>> call, Response<ArrayList<SafoodGroup>> response) {
                            switch (response.code()) {
                                case 200:
                                    setSafoodList(response.body(), listview, false);
                                    break;
                                default:
                                    break;

                            }
                        }


                        @Override
                        public void onFailure(Call<ArrayList<SafoodGroup>> call, Throwable t) {

                        }
                    });
                    safoodBinding.newSafoodGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newFolder.show();
                        }
                    });
                    break;
                case 1:
                    final MainGroupmemoBinding groupmemoBinding = (MainGroupmemoBinding) binding;
                    groupmemoBinding.newMemo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(v.getContext(), NewGroupMemoActivity.class));
                        }
                    });
                    groupmemoBinding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    final ArrayList<GroupMemo> arrayList = new ArrayList<>();
                    NetworkHelper.getNetworkInstance().getGroupMemo(
                            new DataManager(getContext()).getActiveUser().second.getGroupid()
                    ).enqueue(new Callback<ArrayList<GroupMemo>>() {
                        @Override
                        public void onResponse(Call<ArrayList<GroupMemo>> call, Response<ArrayList<GroupMemo>> response) {
                            switch (response.code()) {
                                case 200:
                                    arrayList.addAll(response.body());
                                    LastAdapter.with(arrayList, BR._all)
                                            .layoutHandler(new LastAdapter.LayoutHandler() {
                                                @Override
                                                public int getItemLayout(@NotNull Object o, int i) {
                                                    return R.layout.groupmemo_grid;
                                                }
                                            })
                                            .onBindListener(new LastAdapter.OnBindListener() {
                                                @Override
                                                public void onBind(@NotNull Object o, @NotNull View view, int i, int i1) {
                                                    GroupmemoGridBinding binding = DataBindingUtil.getBinding(view);
                                                    GroupMemo data = (GroupMemo) o;
                                                    binding.title.setText(data.getTitle());
                                                    binding.content.setText(data.getContent());
                                                    binding.foods.setText(data.getFoods().size() + " 개의 음식");
                                                }
                                            })
                                            .onClickListener(new LastAdapter.OnClickListener() {
                                                @Override
                                                public void onClick(@NotNull Object o, @NotNull View view, int i, int i1) {
                                                    startActivity(new Intent(getContext(), GroupMemoActivity.class)
                                                            .putExtra("json", new Gson().toJson(o)));
                                                }
                                            })
                                            .into(groupmemoBinding.recyclerview);
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<GroupMemo>> call, Throwable t) {
                            Log.e("asdf", t.getMessage());
                        }
                    });

                    break;
                case 3:
                    MainInfoBinding infoBinding = (MainInfoBinding) binding;
                    arr = new CartaTagView[]{
                            infoBinding.c6, infoBinding.c7, infoBinding.c8, infoBinding.c9, infoBinding.c10, infoBinding.c11, infoBinding.c12, infoBinding.c13, infoBinding.c14, infoBinding.c15, infoBinding.c1, infoBinding.c2, infoBinding.c3, infoBinding.c4, infoBinding.c5
                    };
                    User.Exception exception = new DataManager(getContext()).getActiveUser().second.getException();
                    for (int i = 0; i < 10; i++) {
                        arr[i].setFullMode(exception.getAllergy().get(i).equals("true"));
                    }
                    for (int i = 10; i < arr.length; i++) {
                        arr[i].setFullMode(exception.getReligion().get(i - 10).equals("true"));
                    }
                    for (CartaTagView anArr : arr) {
                        anArr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((CartaTagView) v).setFullMode(!((CartaTagView) v).getFullMode());
                            }
                        });
                    }
                    infoBinding.saveFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final ArrayList<Boolean> allergic = new ArrayList<Boolean>(), religious = new ArrayList<Boolean>();
                            for (int i = 0; i < 10; i++) {
                                allergic.add(arr[i].getFullMode());
                            }
                            for (int i = 10; i < arr.length; i++) {
                                religious.add(arr[i].getFullMode());
                            }
                            final String query = StringUtils.convertExceptionArray(allergic, religious);
                            new MaterialDialog.Builder(v.getContext())
                                    .title("정보를 저장하시겠습니까?")
                                    .positiveText("확인")
                                    .positiveColor(getResources().getColor(R.color.colorPrimary))
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            loading.show();
                                            NetworkHelper.getNetworkInstance().updateException(new DataManager(getContext()).getActiveUser().second.getApikey(), query)
                                                    .enqueue(new Callback<ResponseBody>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            loading.dismiss();
                                                            switch (response.code()) {
                                                                case 200:
                                                                    Toast.makeText(getContext(), "데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                                                                    break;
                                                                default:
                                                                    Toast.makeText(getContext(), "서버와의 연동에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                                            }
                                                            Log.e("asdf", response.code() + "");
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                            Log.e("asdf", t.getMessage());
                                                        }
                                                    });
                                        }
                                    }).show();
                        }
                    });
                    break;
            }
        }
    }


    public class MainPagerAdapter extends FragmentPagerAdapter {


        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MainFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < titleArr.length)
                return titleArr[position];
            return null;
        }
    }

}
