package com.agenthun.eseal_v2;

import com.agenthun.material.animation.MenuAndBackButton;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.mints.base.MintsBaseActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends MintsBaseActivity implements OnClickListener {
    private MenuAndBackButton menuAndBackButton;
    private ResideMenu resideMenu;
    private ResideMenuItem itemSearch;
    private ResideMenuItem itemHistory;
    private ResideMenuItem itemSetting;
    private ResideMenuItem itemAbout;
/*    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.inject(this);

        setUpMenu();
        menuAndBackButton = (MenuAndBackButton) findViewById(R.id.menu_btn);
        menuAndBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (menuAndBackButton.isInBackState()) {
                    menuAndBackButton.goToMenu();
                    resideMenu.closeMenu();
                } else {
                    menuAndBackButton.goToArrow();
                    resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                }
            }
        });
        pushFragmentToBackStack(MainFragment.class, null);
    }

    private void setUpMenu() {
        // TODO Auto-generated method stub
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        resideMenu.setScaleValue(0.618f);

        itemSearch = new ResideMenuItem(this, R.drawable.icon_search, "搜索设备");
        itemHistory = new ResideMenuItem(this, R.drawable.icon_history, "历史记录");
        itemSetting = new ResideMenuItem(this, R.drawable.icon_settings, "设置");
        itemAbout = new ResideMenuItem(this, R.drawable.icon_about, "关于");

        itemSearch.setOnClickListener(this);
        itemHistory.setOnClickListener(this);
        itemSetting.setOnClickListener(this);
        itemAbout.setOnClickListener(this);

        resideMenu.addMenuItem(itemSearch, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemHistory, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSetting, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_LEFT);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return resideMenu.dispatchTouchEvent(ev);
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {

        @Override
        public void openMenu() {
            // TODO Auto-generated method stub
            menuAndBackButton.goToArrow();
        }

        @Override
        public void closeMenu() {
            // TODO Auto-generated method stub
            menuAndBackButton.goToMenu();
        }

    };

    private void changeFragment(Fragment fragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager().beginTransaction()
                .replace(getFragmentContainerId(), fragment)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == itemSearch) {
            Toast.makeText(this, "搜索设备", Toast.LENGTH_SHORT).show();
            // pushFragmentToBackStack(MainFragment.class, null);
            changeFragment(new MainFragment());
        } else if (v == itemHistory) {
            Toast.makeText(this, "历史记录", Toast.LENGTH_SHORT).show();
            changeFragment(new DeviceItemFragment());
        } else if (v == itemSetting) {
            Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
        } else if (v == itemAbout) {
            Toast.makeText(this, "关于", Toast.LENGTH_SHORT).show();
        }
        resideMenu.closeMenu();
    }

    @Override
    protected int getFragmentContainerId() {
        // TODO Auto-generated method stub
        return R.id.id_home_fragment;
    }
}
