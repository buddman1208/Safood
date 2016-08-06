package kr.edcan.safood.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

import kr.edcan.safood.models.SafoodContentData;
import kr.edcan.safood.models.SafoodTitleData;

/**
 * Created by JunseokOh on 2016. 8. 6..
 */
public class MainSafoodAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<SafoodTitleData> titleArr;
    ArrayList<SafoodContentData> contentArr;

    public MainSafoodAdapter(Context context, ArrayList<SafoodTitleData> titleArr, ArrayList<SafoodContentData> contentArr) {
        this.context = context;
        this.titleArr = titleArr;
        this.contentArr = contentArr;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int i) {
        return 0;
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
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
