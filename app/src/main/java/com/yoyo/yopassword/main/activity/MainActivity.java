package com.yoyo.yopassword.main.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.yoyo.yopassword.common.view.RefreshLayout;
import com.yoyo.yopassword.password.entity.PasswordInfo;
import com.yoyo.yopassword.password.view.adapter.PasswordAdapter;

import java.util.ArrayList;
import java.util.Date;
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment implements OnBaseRecyclerViewListener{
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            RefreshLayout refreshLayout= (RefreshLayout) rootView.findViewById(R.id.refresh_layout);
            // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            RecyclerView recyclerViewPassword = (RecyclerView) rootView.findViewById(R.id.recycler_view_password);
            recyclerViewPassword.setHasFixedSize(true);
            //设置布局管理器
            recyclerViewPassword.setLayoutManager(new LinearLayoutManager(this.getContext()));
            PasswordAdapter passwordAdapter=new PasswordAdapter(null);
            passwordAdapter.setOnRecyclerViewListener(this);
            //设置adapter
            recyclerViewPassword.setAdapter(passwordAdapter);
            //设置Item增加、移除动画
            recyclerViewPassword.setItemAnimator(new DefaultItemAnimator());
            //添加分割线
         /*   recyclerViewPassword.addItemDecoration(new DividerItemDecoration(
                    getActivity(), DividerItemDecoration.HORIZONTAL_LIST));*/
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            List<PasswordInfo> passwordInfoList=new ArrayList<>();
            for (int i=0;i<20;i++){
                PasswordInfo passwordInfo=new PasswordInfo();
                passwordInfo.setAccount("yoyo"+i);
                passwordInfo.setPassword("yoyo"+i+i+i);
                passwordInfo.setRemarks("yoyo备注显示信息"+i);
                passwordInfo.setSaveInfoTime(new Date().getTime()-(i*10000));
                passwordInfo.setTitle("yoyoTitle"+i);
                passwordInfo.setTop(i%2==0);
                passwordInfo.setPasswordInfoId(i);
                passwordInfo.setGroupingId(i%2+1);
            }
            passwordAdapter.setmData(passwordInfoList);
            passwordAdapter.notifyDataSetChanged();
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

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return MainActivity.this.getResources().getString(R.string.action_password);
                case 1:
                    return MainActivity.this.getResources().getString(R.string.action_me);
            }
            return null;
        }
    }
}
