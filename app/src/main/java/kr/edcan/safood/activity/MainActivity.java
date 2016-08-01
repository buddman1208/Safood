package kr.edcan.safood.activity;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kr.edcan.safood.R;

public class MainActivity extends AppCompatActivity {


    TabLayout tabLayout;
    AppBarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefault();
    }

    private void setDefault() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);

    }
}
