package com.yoyo.yopassword.main.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.BaseAppCompatActivity;
import com.yoyo.yopassword.base.OnBaseRecyclerViewListener;
import com.yoyo.yopassword.check.CheckPasswordActivity;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.tool.RxBusTools;
import com.yoyo.yopassword.common.tool.YoStartActivityTools;
import com.yoyo.yopassword.common.util.ACacheUtils;
import com.yoyo.yopassword.common.util.ActivityManager;
import com.yoyo.yopassword.common.util.AlertDialogUtils;
import com.yoyo.yopassword.common.util.RxBusUtils;
import com.yoyo.yopassword.common.util.ScreenObserver;
import com.yoyo.yopassword.common.util.X3DBUtils;
import com.yoyo.yopassword.common.view.OnToDoItemClickListener;
import com.yoyo.yopassword.common.view.RefreshLayout;
import com.yoyo.yopassword.common.view.SpaceItemDecoration;
import com.yoyo.yopassword.common.view.YoSnackbar;
import com.yoyo.yopassword.grouping.entity.GroupingInfo;
import com.yoyo.yopassword.hello.activity.HelloLoginActivity;
import com.yoyo.yopassword.main.entity.RxBusAlertdialogItemCopyEntity;
import com.yoyo.yopassword.main.entity.RxBusFragmentItemEntity;
import com.yoyo.yopassword.password.entity.PasswordInfo;
import com.yoyo.yopassword.password.view.adapter.PasswordAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends BaseAppCompatActivity {
    Observable<Object> sectionsPagerAdapterRefreshData;
    Observable<RxBusFragmentItemEntity> placeholderFragmentItemRefreshData;
    Observable<RxBusAlertdialogItemCopyEntity> alertDialogToDoItemCopy;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    ScreenObserver screenObserver;

    public void init() {
        super.init();
        setContentView(R.layout.activity_main);
        // AppSingletonTools.getInstance().initMainActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoStartActivityTools.toAddPasswordActivity_Add(MainActivity.this);
            }
        });
        initRxBusUtils();
        initScreenObserver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_grouping:
                YoStartActivityTools.toGroupingActivity(MainActivity.this);
                return true;
            case R.id.action_sign_out:
                ACacheUtils.signOut(MainActivity.this);
                startActivity(new Intent(MainActivity.this, HelloLoginActivity.class).putExtra(HelloLoginActivity.KEY_TO_LOGIN, true));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment implements OnBaseRecyclerViewListener {
        private static final String ARG_SECTION_GROUPING_ID = "ARG_SECTION_GROUPING_ID";
        PasswordAdapter passwordAdapter;
        RefreshLayout refreshLayout;
        OnBaseRecyclerViewListener onBaseRecyclerViewListener = new OnBaseRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public boolean onItemLongClick(final int position) {
                String[] toDo = getContext().getResources().getStringArray(R.array.alert_dialog_list_todo_password_item_long_click);
                AlertDialogUtils.showAlertDialogList(getContext(), toDo, new OnToDoItemClickListener() {

                    @Override
                    public void onItemClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                startActivity(new Intent(getContext(),CheckPasswordActivity.class).putExtra(CheckPasswordActivity.KEY_TO_CHECK_PASSWORD_COPY_PASSWORD,passwordAdapter.getItem(position).getPassword()).putExtra(CheckPasswordActivity.KEY_TO_CHECK_PASSWORD_COPY,true));
                                break;
                            case 1:
                                YoStartActivityTools.toAddPasswordActivity_Update(PlaceholderFragment.this.getContext(), passwordAdapter.getItem(position).getPasswordInfoId());
                                break;
                            case 2:
                                AlertDialogUtils.showAlertDialog(getContext(), R.string.password_item_delect_todo, new OnToDoItemClickListener() {
                                    @Override
                                    public void onPositiveClick(DialogInterface dialog, int which) {
                                        super.onPositiveClick(dialog, which);
                                        X3DBUtils.delectById(PasswordInfo.class, passwordAdapter.getItem(position).getPasswordInfoId());
                                        refreshPasswordAdapter();
                                    }
                                });
                                break;
                        }

                    }
                });
                return false;
            }
        };

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber, long groupingId) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putLong(ARG_SECTION_GROUPING_ID, groupingId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            refreshLayout = (RefreshLayout) rootView.findViewById(R.id.refresh_layout);
            // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    refreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshPasswordAdapter();
                        }
                    }, AppConfig.RefreshViewTime);
                }
            });
            RecyclerView recyclerViewPassword = (RecyclerView) rootView.findViewById(R.id.recycler_view_password);
            recyclerViewPassword.setHasFixedSize(true);
            //设置布局管理器
            recyclerViewPassword.setLayoutManager(new LinearLayoutManager(this.getContext()));
            passwordAdapter = new PasswordAdapter(null);
            //passwordAdapter.setOnRecyclerViewListener(this);
            //设置adapter
            recyclerViewPassword.setAdapter(passwordAdapter);
            //设置Item增加、移除动画
            recyclerViewPassword.setItemAnimator(new DefaultItemAnimator());
            //添加分割线
         /*   recyclerViewPassword.addItemDecoration(new DividerItemDecoration(
                    getActivity(), DividerItemDecoration.HORIZONTAL_LIST));*/

            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space_item_decoration);
            recyclerViewPassword.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
            passwordAdapter.setOnRecyclerViewListener(onBaseRecyclerViewListener);
            refreshPasswordAdapter();
            return rootView;
        }


        void refreshPasswordAdapter() {
            if (passwordAdapter == null) {
                return;
            }
            long groupingId = getArguments().getLong(ARG_SECTION_GROUPING_ID, 0);

            if (groupingId <= 0) {
                return;
            }
            List<PasswordInfo> passwordInfoList = X3DBUtils.findAll(PasswordInfo.class, "groupingId", "=", groupingId);
            passwordAdapter.setmData(passwordInfoList);
            passwordAdapter.notifyDataSetChanged();
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
        }


        @Override
        public void onItemClick(int position) {

        }

        @Override
        public boolean onItemLongClick(int position) {
            return false;
        }

    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        public List<GroupingInfo> pageTitleList;
        FragmentManager fm;
        private ArrayList<Fragment> mFragmentList;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
            pageTitleList = new ArrayList<>();
            refreshData();
        }

        public void refreshData() {
            List<GroupingInfo> groupingInfoList = X3DBUtils.findAll(GroupingInfo.class);
            pageTitleList.clear();
            pageTitleList.addAll(groupingInfoList);
            updateData();
        }


        public void updateData() {
            ArrayList<Fragment> fragments = new ArrayList<>();
            for (int i = 0, size = pageTitleList.size(); i < size; i++) {
               // Log.e("FPagerAdapter1", pageTitleList.get(i).toString());
                fragments.add(PlaceholderFragment.newInstance(i + 1, pageTitleList.get(i).getGroupingId()));
            }
            setFragmentList(fragments);
        }

        private void setFragmentList(ArrayList<Fragment> fragmentList) {
            if(this.mFragmentList != null){
                mFragmentList.clear();
            }
            this.mFragmentList = fragmentList;
            notifyDataSetChanged();
        }


        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return pageTitleList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < pageTitleList.size()) {
                return pageTitleList.get(position).getGroupingName();
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        // AppSingletonTools.getInstance().destroyMainActivity();
        RxBusUtils.get().unregister(RxBusTools.MainActivity_SectionsPagerAdapter_RefreshData, sectionsPagerAdapterRefreshData);
        RxBusUtils.get().unregister(RxBusTools.MainActivity_PlaceholderFragment_Item_RefreshData, placeholderFragmentItemRefreshData);
        RxBusUtils.get().unregister(RxBusTools.MainActivity_AlertDialog_ToDo_Item_Copy, alertDialogToDoItemCopy);
        if(screenObserver!=null){
            screenObserver.shutdownObserver();
        }
        super.onDestroy();
    }

    //RxJava
    public void initRxBusUtils() {
        //刷新PagerAdapter
        sectionsPagerAdapterRefreshData = RxBusUtils.get()
                .register(RxBusTools.MainActivity_SectionsPagerAdapter_RefreshData);
        sectionsPagerAdapterRefreshData.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object object) {
                        if (object.equals(1) && mSectionsPagerAdapter != null) {
                            mSectionsPagerAdapter.refreshData();
                        }
                    }
                });
        //刷新 placeholderFragment
        placeholderFragmentItemRefreshData = RxBusUtils.get()
                .register(RxBusTools.MainActivity_PlaceholderFragment_Item_RefreshData);
        placeholderFragmentItemRefreshData.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RxBusFragmentItemEntity>() {
                    @Override
                    public void call(RxBusFragmentItemEntity rxBusFragmentItemEntity) {
                        refreshFragmentItem(rxBusFragmentItemEntity);
                    }
                });
        //RxBusUtils.get().post(RxBusTools.MainActivity_SectionsPagerAdapter_RefreshData, 1);

        alertDialogToDoItemCopy = RxBusUtils.get()
                .register(RxBusTools.MainActivity_AlertDialog_ToDo_Item_Copy);
        alertDialogToDoItemCopy.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RxBusAlertdialogItemCopyEntity>() {
                    @Override
                    public void call(RxBusAlertdialogItemCopyEntity rxBusAlertdialogItemCopyEntity) {
                        if (rxBusAlertdialogItemCopyEntity!=null&& !TextUtils.isEmpty(rxBusAlertdialogItemCopyEntity.getPassword())) {
                            ClipboardManager myClipboard = (ClipboardManager) MainActivity.this.getSystemService(CLIPBOARD_SERVICE);
                            String text = rxBusAlertdialogItemCopyEntity.getPassword();
                            ClipData myClip = ClipData.newPlainText("text", text);
                            myClipboard.setPrimaryClip(myClip);
                            YoSnackbar.showSnackbar(mViewPager, R.string.copy_success_tip);
                        }
                    }
                });
    }

    private void refreshFragmentItem(RxBusFragmentItemEntity rxBusFragmentItemEntity) {
        List<GroupingInfo> pageTitleList = mSectionsPagerAdapter.pageTitleList;
        for (int i = 0; i < pageTitleList.size(); i++) {
            if (pageTitleList.get(i).getGroupingId() == rxBusFragmentItemEntity.getNewGroupingId() || pageTitleList.get(i).getGroupingId() == rxBusFragmentItemEntity.getOldGroupingId()) {
                PlaceholderFragment someFragment = (PlaceholderFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, i);
                if (someFragment != null) {
                    someFragment.refreshPasswordAdapter();
                    if (pageTitleList.get(i).getGroupingId() == rxBusFragmentItemEntity.getNewGroupingId()) {
                        mViewPager.setCurrentItem(i);
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           setFinish(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initScreenObserver(){
        screenObserver=new ScreenObserver(MainActivity.this);
        screenObserver.startObserver(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {

            }

            @Override
            public void onScreenOff() {
                startActivity(new Intent(MainActivity.this, CheckPasswordActivity.class));
            }

            @Override
            public void onUserPresent() {

            }
        });
    }
}
