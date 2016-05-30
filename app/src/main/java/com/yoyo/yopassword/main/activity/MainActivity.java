package com.yoyo.yopassword.main.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.BaseAppCompatActivity;
import com.yoyo.yopassword.base.OnBaseRecyclerViewListener;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.tool.AppSingletonTools;
import com.yoyo.yopassword.common.tool.StartActivityTools;
import com.yoyo.yopassword.common.util.X3DBUtils;
import com.yoyo.yopassword.common.view.OnToDoItemClickListener;
import com.yoyo.yopassword.common.view.RefreshLayout;
import com.yoyo.yopassword.common.view.SpaceItemDecoration;
import com.yoyo.yopassword.common.view.YoAlertDialog;
import com.yoyo.yopassword.common.view.YoSnackbar;
import com.yoyo.yopassword.grouping.entity.GroupingInfo;
import com.yoyo.yopassword.password.entity.PasswordInfo;
import com.yoyo.yopassword.password.view.adapter.PasswordAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity {
    public SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager container;
    public void init() {
        super.init();
        setContentView(R.layout.activity_main);
        AppSingletonTools.getInstance().initMainActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        container = (ViewPager) findViewById(R.id.container);
        container.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(container);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivityTools.toAddPasswordActivity(MainActivity.this, false, true,0);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_grouping) {
            StartActivityTools.toGroupingActivity(MainActivity.this, false, true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment implements OnBaseRecyclerViewListener {
        private static final String ARG_SECTION_NUMBER = "section_number";
        PasswordAdapter passwordAdapter;
        RefreshLayout refreshLayout;
        OnBaseRecyclerViewListener onBaseRecyclerViewListener = new OnBaseRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public boolean onItemLongClick(final int position) {
                String[] toDo = getContext().getResources().getStringArray(R.array.alert_dialog_list_todo_password_item_long_click);
                YoAlertDialog.showAlertDialogList(getContext(), toDo, new OnToDoItemClickListener() {

                    @Override
                    public void onItemClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ClipboardManager myClipboard = (ClipboardManager)PlaceholderFragment.this.getActivity().getSystemService(CLIPBOARD_SERVICE);
                                String text = passwordAdapter.getItem(position).getPassword();
                                ClipData myClip = ClipData.newPlainText("text", text);
                                myClipboard.setPrimaryClip(myClip);
                                YoSnackbar.showSnackbar(refreshLayout,R.string.copy_success_tip);
                                break;
                            case 1:
                                StartActivityTools.toAddPasswordActivity(PlaceholderFragment.this, true, true,passwordAdapter.getItem(position).getPasswordInfoId());
                                break;
                            case 2:
                                YoAlertDialog.showAlertDialog(getContext(), R.string.password_item_delect_todo, new OnToDoItemClickListener() {
                                    @Override
                                    public void onPositiveClick(DialogInterface dialog, int which) {
                                        super.onPositiveClick(dialog, which);
                                        X3DBUtils.delectById(PasswordInfo.class,passwordAdapter.getItem(position).getPasswordInfoId());
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

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            refreshLayout = (RefreshLayout) rootView.findViewById(R.id.refresh_layout);
            // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
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
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

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

            passwordAdapter.setOnRecyclerViewListener(onBaseRecyclerViewListener);
            refreshPasswordAdapter();
            return rootView;
        }

        private void refreshPasswordAdapter() {
            List<PasswordInfo> passwordInfoList = X3DBUtils.findAll(PasswordInfo.class);
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

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == StartActivityTools.ToAddPasswordActivity_RequestCode && resultCode == StartActivityTools.ToAddPasswordActivity_ResultCode) {
                refreshPasswordAdapter();
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        List<GroupingInfo> pageTitleList;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            pageTitleList = new ArrayList<>();
            refreshData( false);
        }
        public void refreshData(boolean isRefresh){
            List<GroupingInfo> groupingInfoList = X3DBUtils.findAll(GroupingInfo.class);
            pageTitleList.clear();
            pageTitleList.addAll(groupingInfoList);
            if(isRefresh){
                notifyDataSetChanged();
            }
        }

        public void refreshFragmentItem(long groupingId){
            for (int i=0;i<pageTitleList.size();i++) {
                if(pageTitleList.get(i).getGroupingId()==groupingId){
                    ((PlaceholderFragment)getItem(i)).refreshPasswordAdapter();
                }
            }
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
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
        AppSingletonTools.getInstance().destroyMainActivity();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StartActivityTools.ToAddPasswordActivity_RequestCode && resultCode == StartActivityTools.ToAddPasswordActivity_ResultCode) {
            long groupingId=data.getLongExtra(StartActivityTools.ToAddPasswordActivity_GroupingId,0);
            if(groupingId>0){
                mSectionsPagerAdapter.refreshFragmentItem(groupingId);
            }

        }
    }
}
