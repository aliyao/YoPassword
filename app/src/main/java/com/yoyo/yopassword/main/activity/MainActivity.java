package com.yoyo.yopassword.main.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.yoyo.yopassword.common.view.RefreshLayout;
import com.yoyo.yopassword.common.view.SpaceItemDecoration;
import com.yoyo.yopassword.password.entity.GroupingInfo;
import com.yoyo.yopassword.password.view.adapter.PasswordAdapter;
import com.yoyo.yopassword.test.TestUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.main_snackbar_tip, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        if (id == R.id.action_back) {
            finish();
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment implements OnBaseRecyclerViewListener{
        private static final String ARG_SECTION_NUMBER = "section_number";
        PasswordAdapter passwordAdapter;
        RefreshLayout refreshLayout;

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
            refreshLayout= (RefreshLayout) rootView.findViewById(R.id.refresh_layout);
            // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            RecyclerView recyclerViewPassword = (RecyclerView) rootView.findViewById(R.id.recycler_view_password);
            recyclerViewPassword.setHasFixedSize(true);
            //设置布局管理器
            recyclerViewPassword.setLayoutManager(new LinearLayoutManager(this.getContext()));
            passwordAdapter=new PasswordAdapter(TestUtils.getListPasswordInfo());
            passwordAdapter.setOnRecyclerViewListener(this);
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
                            passwordAdapter.setmData(TestUtils.getListPasswordInfo());
                            passwordAdapter.notifyDataSetChanged();
                            refreshLayout.setRefreshing(false);
                        }
                    }, AppConfig.RefreshViewTime);
                }
            });
            return rootView;
        }

        @Override
        public void onItemClick(int position) {

        }

        @Override
        public boolean onItemLongClick(int position) {
            return false;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        List<GroupingInfo> pageTitleList;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            pageTitleList=new ArrayList<>();
            GroupingInfo groupingInfo=new GroupingInfo();
            groupingInfo.setGroupingId(0);
            groupingInfo.setGroupingName(MainActivity.this.getResources().getString(R.string.action_password));
            pageTitleList.add(groupingInfo);
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
            if(position<pageTitleList.size()){
                return pageTitleList.get(position).getGroupingName();
            }
            return null;
        }
    }


}
