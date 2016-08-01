package kr.edcan.safood.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vkondrav.swiftadapter.SwiftAdapter;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import kr.edcan.safood.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final Adapter adapter = new Adapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setFoodItem(createFoodItem(true, ));

    }

    private static FoodItem createFoodItem(boolean isHeadNode, ArrayList<String> titleArr, ArrayList<Pair<String, String>> contentArr) {
        FoodItem FoodItem = new FoodItem("Test");
        for (String title : titleArr) {
            FoodItem.itemList.add(title);
        }
        if (isHeadNode) {
            for (Pair<String, String> pair : contentArr) {
                FoodItem.FoodItemList.add(createFoodItem(false, pair.first, pair.second, null));
            }
        }
        return FoodItem;
    }

    private static class FoodItem {
        String name;
        ArrayList<String> itemList;
        ArrayList<FoodItem> FoodItemList;

        public FoodItem(String name) {
            this.name = name;
            itemList = new ArrayList<>();
            FoodItemList = new ArrayList<>();
        }
    }

    private static class Adapter extends SwiftAdapter<Adapter.ViewHolder> {

        private FoodItem FoodItem;

        private Context context;

        private LayoutInflater layoutInflater;

        public Adapter(Context context) {
            this.context = context;
            FoodItem = new FoodItem("");
            layoutInflater = LayoutInflater.from(context);
        }

        public void setFoodItem(FoodItem FoodItem) {
            closeAll();
            this.FoodItem = FoodItem;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView title;
            public View icon1;
            public View icon2;
            public View icon3;
            public ImageView expand;

            public ViewHolder(View itemView) {
                super(itemView);

                title = (TextView) itemView.findViewById(R.id.title);
                icon1 = itemView.findViewById(R.id.icon1);
                icon2 = itemView.findViewById(R.id.icon2);
                icon3 = itemView.findViewById(R.id.icon3);
                expand = (ImageView) itemView.findViewById(R.id.expand);
            }
        }

        /**
         * NO SECTION ROWS
         **/

        @Override
        public int getNumberOfLvl0Items() {
            return FoodItem.itemList.size();
        }

        public class Lvl0ItemViewHolder extends ViewHolder {

            public Lvl0ItemViewHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public ViewHolder onCreateLvl0ItemViewHolder(ViewGroup parent) {
            return new Lvl0ItemViewHolder(layoutInflater.inflate(R.layout.custom_row, parent, false));
        }

        @Override
        public void onBindLvl0Item(ViewHolder holder, ItemIndex index) {
            if (holder instanceof Lvl0ItemViewHolder) {

                Lvl0ItemViewHolder lvl0ItemViewHolder = (Lvl0ItemViewHolder) holder;

                String title = FoodItem
                        .itemList
                        .get(index.item);

                lvl0ItemViewHolder.title.setText(title);
            }
        }

        /**
         * SECTIONS
         **/

        @Override
        public int getNumberOfLvl1Sections() {
            return FoodItem.FoodItemList.size();
        }

        public class Lv1SectionViewHolder extends ViewHolder {

            public Lv1SectionViewHolder(View itemView) {
                super(itemView);

                itemView.setBackgroundResource(R.color.section);
            }

            public void setOpen(ItemIndex index) {
                if (isLvl1SectionOpened(index.lvl1Section)) {
                    expand.setRotation(180);
                } else {
                    expand.setRotation(0);
                }
            }
        }

        @Override
        public ViewHolder onCreateLvl1SectionViewHolder(ViewGroup parent) {
            return new Lv1SectionViewHolder(layoutInflater.inflate(R.layout.custom_row, parent, false));
        }

        @Override
        public void onBindLvl1Section(ViewHolder holder, final ItemIndex index) {
            if (holder instanceof Lv1SectionViewHolder) {

                final Lv1SectionViewHolder lv1SectionViewHolder = (Lv1SectionViewHolder) holder;

                String title = FoodItem
                        .FoodItemList
                        .get(index.lvl1Section)
                        .name;

                lv1SectionViewHolder.title.setText(title);

                lv1SectionViewHolder.icon1.setVisibility(View.VISIBLE);
                lv1SectionViewHolder.expand.setVisibility(View.VISIBLE);

                lv1SectionViewHolder.setOpen(index);

                lv1SectionViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCloseLvl1Section(index);
                        lv1SectionViewHolder.setOpen(index);
                    }
                });
            }
        }

        @Override
        public int getNumberOfLvl1ItemsForSection(int lvl1Section) {
            return FoodItem.FoodItemList.get(lvl1Section)
                    .itemList.size();
        }

        public class Lvl1ItemViewHolder extends ViewHolder {

            public Lvl1ItemViewHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public ViewHolder onCreateLvl1ItemViewHolder(ViewGroup parent) {
            return new Lvl1ItemViewHolder(layoutInflater.inflate(R.layout.custom_row, parent, false));
        }

        @Override
        public void onBindLvl1Item(ViewHolder holder, ItemIndex index) {
            if (holder instanceof Lvl1ItemViewHolder) {

                Lvl1ItemViewHolder lvl1ItemViewHolder = (Lvl1ItemViewHolder) holder;

                String title = FoodItem
                        .FoodItemList
                        .get(index.lvl1Section)
                        .itemList
                        .get(index.item);

                lvl1ItemViewHolder.icon1.setVisibility(View.VISIBLE);

                lvl1ItemViewHolder.title.setText(title);
            }
        }

    }
}