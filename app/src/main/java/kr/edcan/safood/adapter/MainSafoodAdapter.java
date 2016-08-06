package kr.edcan.safood.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kr.edcan.safood.R;
import kr.edcan.safood.databinding.MainSafoodListviewGroupChildBinding;
import kr.edcan.safood.databinding.MainSafoodListviewGroupContentBinding;
import kr.edcan.safood.models.SafoodContentData;
import kr.edcan.safood.models.SafoodTitleData;
import kr.edcan.safood.views.SlidingExpandableListView;

/**
 * Created by JunseokOh on 2016. 8. 6..
 */
public class MainSafoodAdapter extends SlidingExpandableListView.AnimatedExpandableListAdapter {
    Context context;
    ArrayList<SafoodTitleData> titleArr;
    ArrayList<ArrayList<SafoodContentData>> contentArr;
    LayoutInflater inflater;

    public MainSafoodAdapter(Context context, ArrayList<SafoodTitleData> titleArr, ArrayList<ArrayList<SafoodContentData>> contentArr) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.titleArr = titleArr;
        this.contentArr = contentArr;
    }

    @Override
    public int getGroupCount() {
        return titleArr.size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean b, View view, ViewGroup viewGroup) {
        MainSafoodListviewGroupContentBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_safood_listview_group_content, null, true);
        SafoodTitleData data = titleArr.get(position);

        // Set Data
        binding.mainListGroupTitle.setText(data.getTitle());
        binding.mainListGroupContent.setText(data.getLastModifiedDate().toLocaleString());
        binding.mainListGroupBottomIndicator.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        return binding.getRoot();
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MainSafoodListviewGroupChildBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_safood_listview_group_child, null, true);
        SafoodContentData data = contentArr.get(groupPosition).get(childPosition);
        binding.mainListChildTitle.setText(data.getTitle());
        binding.mainListChildContent.setText(data.getAddedDate().toLocaleString() + "에 추가됨");
        if (childPosition == contentArr.get(groupPosition).size() - 1) {
            binding.mainListChildBottomIndicator.setVisibility(View.INVISIBLE);
        }
        return binding.getRoot();
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return contentArr.get(groupPosition).size();
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
