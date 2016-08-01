package kr.edcan.safood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vkondrav.swiftadapter.SwiftAdapter;

import kr.edcan.safood.R;

/**
 * Created by KOHA_DESKTOP on 2016. 8. 1..
 */
public class MainExpandableAdapter extends SwiftAdapter<MainExpandableAdapter.ViewHolder> {

    LayoutInflater inflater;
    Context c;

    public MainExpandableAdapter(Context c) {
        this.c = c;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getNumberOfLvl0Items() {
        return 10;
    }

    @Override
    public ViewHolder onCreateLvl0ItemViewHolder(ViewGroup parent) {
        ViewHolder lv0holder = new ViewHolder(inflater.inflate(R.layout.main_expandable_lv0_content, parent, false));
        return super.onCreateLvl0ItemViewHolder(parent);
    }

    @Override
    public void onBindLvl0Item(ViewHolder holder, ItemIndex index) {

        super.onBindLvl0Item(holder, index);
    }
}
