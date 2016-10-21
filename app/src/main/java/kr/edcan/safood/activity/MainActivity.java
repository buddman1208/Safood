package kr.edcan.safood.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.nitrico.lastadapter.BR;
import com.github.nitrico.lastadapter.LastAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

import kr.edcan.safood.R;
import kr.edcan.safood.adapter.MainSafoodAdapter;
import kr.edcan.safood.databinding.ActivityMainBinding;
import kr.edcan.safood.databinding.GroupmemoGridBinding;
import kr.edcan.safood.databinding.MainGroupmemoBinding;
import kr.edcan.safood.databinding.MainSafoodBinding;
import kr.edcan.safood.databinding.MainSearchBinding;
import kr.edcan.safood.models.GroupMemo;
import kr.edcan.safood.models.SafoodContentData;
import kr.edcan.safood.models.SafoodTitleData;
import kr.edcan.safood.utils.DataManager;
import kr.edcan.safood.utils.SafoodHelper;
import kr.edcan.safood.views.CartaTagView;
import kr.edcan.safood.views.SlidingExpandableListView;

public class MainActivity extends AppCompatActivity {

    String[] titleArr = new String[]{"안전한 음식", "그룹 메모", "음식 백과", "내 정보"};

    // Helper, Utils
    SafoodHelper helper;
    DataManager manager;

    // DataBindings
    public ActivityMainBinding binding;
    public MainSearchBinding mainSearchBinding;
    public MainSafoodBinding mainSafoodBinding;

    // Widgets
    SlidingExpandableListView slidingListView;
    ViewPager pager;
    RelativeLayout mainSearchFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainSearchBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.main_search, null, true);
        mainSafoodBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.main_safood, null, true);
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

        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 0:
                    view = inflater.inflate(R.layout.main_safood, container, false);
                    break;
                case 1:
                    view = inflater.inflate(R.layout.main_groupmemo, container, false);
                    break;
                case 2:
                    view = inflater.inflate(R.layout.main_foodencyclopedia, container, false);
                    break;
                case 3:
                    view = inflater.inflate(R.layout.main_info, container, false);
                    break;
            }
            setPage(view, container, getArguments().getInt(ARG_SECTION_NUMBER), inflater);
            return view;
        }

        private void setPage(View view, ViewGroup container, int position, LayoutInflater inflater) {
            switch (position) {
                case 0:
                    final SlidingExpandableListView listview = (SlidingExpandableListView) view.findViewById(R.id.mainExpandableListView);
                    ArrayList<SafoodTitleData> titleArr = new ArrayList<>();
                    ArrayList<ArrayList<SafoodContentData>> contentArr = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        titleArr.add(new SafoodTitleData("m,", new Date()));
                        ArrayList<SafoodContentData> data = new ArrayList<>();
                        data.add(new SafoodContentData("asdf", new Date()));
                        data.add(new SafoodContentData("asdf", new Date()));
                        data.add(new SafoodContentData("asdf", new Date()));
                        contentArr.add(data);
                    }
                    final MainSafoodAdapter adapter = new MainSafoodAdapter(getContext(), titleArr, contentArr);
                    listview.setAdapter(adapter);
                    listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            View view = adapter.getGroupView(groupPosition, true, null, null);
                            RelativeLayout bottomIndicator = (RelativeLayout) view.findViewById(R.id.mainListGroupBottomIndicator);
                            if (listview.isGroupExpanded(groupPosition)) {
                                bottomIndicator.setVisibility(View.INVISIBLE);
                                listview.collapseGroupWithAnimation(groupPosition);
                            } else {
                                bottomIndicator.setVisibility(View.VISIBLE);
                                listview.expandGroupWithAnimation(groupPosition);
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
                    break;
                case 1:
                    CartaTagView newMemo = (CartaTagView) view.findViewById(R.id.newMemo);
                    newMemo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(v.getContext(), NewGroupMemoActivity.class));
                        }
                    });
                    RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.recyclerview);
                    ArrayList<GroupMemo> arrayList = new ArrayList<>();
                    arrayList.add(new GroupMemo("asdf"));
                    arrayList.add(new GroupMemo("asdf"));
                    arrayList.add(new GroupMemo("asdf"));
                    arrayList.add(new GroupMemo("asdf"));
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
                                    binding.title.setText(((GroupMemo) o).getName());
                                }
                            })
                            .onClickListener(new LastAdapter.OnClickListener() {
                                @Override
                                public void onClick(@NotNull Object o, @NotNull View view, int i, int i1) {
                                    Log.e("asdf", i1 + "");
                                }
                            })
                            .into(recyclerView);
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
