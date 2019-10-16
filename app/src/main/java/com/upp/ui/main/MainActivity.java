package com.upp.ui.main;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;

import com.upp.R;
import com.upp.adapter.MyPagerAdapter;
import com.upp.ui.BaseAppCompatActivity;
import com.upp.ui.common.CommonFragment;
import com.upp.ui.index.IndexFragment;
import com.upp.utils.Constants;
import com.upp.utils.Keys;
import com.upp.utils.SPUtils;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.ArrayList;

import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

/**
 * @author flymegoc
 */
public class MainActivity extends BaseAppCompatActivity {
    private CoordinatorTabLayout mCoordinatorTabLayout;
    private int[] mImageArray, mColorArray;
    private ArrayList<Fragment> mFragments;
    private final String[] mTitles = {"主页", "当前最热", "最近得分", "10分钟以上", "本月讨论", "本月收藏", "收藏最多", "最近加精", "本月最热", "上月最热", "高清(会员)"};
    private ViewPager mViewPager;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.drawer_layout)
//    DrawerLayout drawerLayout;
//    @BindView(R.id.nav_view)
//    NavigationView navView;

    private Fragment mCurrentFragment;
    private IndexFragment indexFragment;
    private CommonFragment commonFragment;
    private CommonFragment rpFragment;
    private CommonFragment tenMinutesFragment;
    private CommonFragment thisMonthFragment;
    private CommonFragment thisMonthCollectFragment;
    private CommonFragment mostCollectFragment;
    private CommonFragment nearScoreFragment;
    private CommonFragment thisMonthHotFragment;
    private CommonFragment lastMonthHotFragment;
    private CommonFragment hdVideoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
//        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//        navView.setNavigationItemSelectedListener(this);
//
//        mCurrentFragment = new Fragment();
//        indexFragment = IndexFragment.getInstance();
//        getSupportFragmentManager().beginTransaction().add(R.id.content, indexFragment).commit();
//        mCurrentFragment = indexFragment;
        initFragments();
        initViewPager();

        mCoordinatorTabLayout = (CoordinatorTabLayout) findViewById(R.id.coordinatortablayout);
        mCoordinatorTabLayout.setTranslucentStatusBar(this)
                .setTitle("")
                .setBackEnable(false)
                .setupWithViewPager(mViewPager);

    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        if (indexFragment == null) {
            indexFragment = IndexFragment.getInstance();
        }
        mFragments.add(indexFragment);

        if (commonFragment == null) {
            commonFragment = CommonFragment.getInstance("hot", null);
        }
        mFragments.add(commonFragment);
        if (rpFragment == null) {
            rpFragment = CommonFragment.getInstance("rp", null);
        }
        mFragments.add(rpFragment);

        if (tenMinutesFragment == null) {
            tenMinutesFragment = CommonFragment.getInstance("long", null);
        }
        mFragments.add(tenMinutesFragment);

        if (thisMonthFragment == null) {
            thisMonthFragment = CommonFragment.getInstance("md", null);
        }
        mFragments.add(thisMonthFragment);

        if (thisMonthCollectFragment == null) {
            thisMonthCollectFragment = CommonFragment.getInstance("tf", null);
        }
        mFragments.add(thisMonthCollectFragment);

        if (mostCollectFragment == null) {
            mostCollectFragment = CommonFragment.getInstance("mf", null);
        }
        mFragments.add(mostCollectFragment);

        if (nearScoreFragment == null) {
            nearScoreFragment = CommonFragment.getInstance("rf", null);
        }
        mFragments.add(nearScoreFragment);

        if (thisMonthHotFragment == null) {
            thisMonthHotFragment = CommonFragment.getInstance("top", null);
        }
        mFragments.add(thisMonthHotFragment);

        if (lastMonthHotFragment == null) {
            lastMonthHotFragment = CommonFragment.getInstance("top", "-1");
        }
        mFragments.add(lastMonthHotFragment);

        if (hdVideoFragment == null) {
            hdVideoFragment = CommonFragment.getInstance("hd", null);
        }
        mFragments.add(hdVideoFragment);
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
    }


    private void init() {
        File file = new File(Constants.DOWNLOAD_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        AndPermission.with(this)
                .requestCode(300)
                .permission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .start();
    }

    private void showAboutMeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about_me);
        builder.setView(R.layout.about_me);
        builder.show();
    }

    //切换类型
    public void switchContent(Fragment toHide, Fragment toShow) {
        if (mCurrentFragment != toShow) {
            mCurrentFragment = toShow;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out);
            // 先判断是否被add过
            if (!toShow.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.hide(toHide).add(R.id.content, toShow).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                transaction.hide(toHide).show(toShow).commit();
            }
        }
    }
}
