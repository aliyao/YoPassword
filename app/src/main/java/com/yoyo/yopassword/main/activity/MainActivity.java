package com.yoyo.yopassword.main.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.BaseAppCompatActivity;
import com.yoyo.yopassword.base.OnBaseRecyclerViewListener;
import com.yoyo.yopassword.check.ChangePasswordActivity;
import com.yoyo.yopassword.check.CheckPasswordActivity;
import com.yoyo.yopassword.check.SetPasswordActivity;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.tool.AppSingletonTools;
import com.yoyo.yopassword.common.tool.RxBusTools;
import com.yoyo.yopassword.common.tool.YoStartActivityTools;
import com.yoyo.yopassword.common.util.ACacheUtils;
import com.yoyo.yopassword.common.util.AlertDialogUtils;
import com.yoyo.yopassword.common.util.AppInfoUtil;
import com.yoyo.yopassword.common.util.BmobUtils;
import com.yoyo.yopassword.common.util.DateUtils;
import com.yoyo.yopassword.common.util.MyCloudCodeListener;
import com.yoyo.yopassword.common.util.RxBusUtils;
import com.yoyo.yopassword.common.util.ScreenObserver;
import com.yoyo.yopassword.common.util.X3DBUtils;
import com.yoyo.yopassword.common.util.safe.AESUtils;
import com.yoyo.yopassword.common.view.OnToDoItemClickListener;
import com.yoyo.yopassword.common.view.RefreshLayout;
import com.yoyo.yopassword.common.view.SpaceItemDecoration;
import com.yoyo.yopassword.common.view.YoSnackbar;
import com.yoyo.yopassword.grouping.entity.GroupingInfo;
import com.yoyo.yopassword.hello.activity.HelloLoginActivity;
import com.yoyo.yopassword.main.entity.AppUpdateEntity;
import com.yoyo.yopassword.main.entity.JsonEntity;
import com.yoyo.yopassword.main.entity.RxBusFragmentItemEntity;
import com.yoyo.yopassword.password.entity.PasswordInfo;
import com.yoyo.yopassword.password.view.adapter.PasswordAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends BaseAppCompatActivity {
    Observable<Object> sectionsPagerAdapterRefreshData;
    Observable<RxBusFragmentItemEntity> placeholderFragmentItemRefreshData;
   // Observable<RxBusAlertdialogItemCopyEntity> alertDialogToDoItemCopy;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    ScreenObserver screenObserver;

    public void init() {
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
        initBmob();
    }
    private void initBmob() {
        //提供以下两种方式进行初始化操作：
        //第一：默认初始化
        Bmob.initialize(this, AppConfig.MY_BMOB_APPLICATION_ID);

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

        //查找Person表里面id为6b6c11c537的数据
        /*BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
        bmobQuery.getObject(this, "6b6c11c537", new GetListener<Person>() {
            @Override
            public void onSuccess(Person object) {
                toast("查询成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                toast("查询失败：" + msg);
            }
        });*/

        updateAppCheck();
    }

    /**
     * app检查更新
     */
    private void updateAppCheck(){
        try {
            JSONObject params = new JSONObject();
            int versionCode = AppInfoUtil.getVersionCode(this);
            if(versionCode<=0){
                return;
            }
            params.put("versionCode", versionCode);
            BmobUtils.callEndpoint(this, AppConfig.MY_BMOB_UPDATE_APP_CLOUDCODENAME, params, new MyCloudCodeListener<JsonEntity<AppUpdateEntity>>() {
                @Override
                public void onMySuccess(boolean isSuccess,final JsonEntity<AppUpdateEntity> result) {
                    super.onMySuccess(isSuccess, result);
                    if(isSuccess){
                        AlertDialogUtils.showAlertDialog( MainActivity.this,0,R.string.update_text, R.string.btn_ok_web_update, R.string.btn_cancle, 0,null,false,false,new OnToDoItemClickListener(){
                            @Override
                            public void onPositiveClick(DialogInterface dialog, int which) {
                                super.onPositiveClick(dialog, which);
                                if(!TextUtils.isEmpty(result.getResult().getUpdateUrl())){
                                    Uri uri = Uri.parse(result.getResult().getUpdateUrl());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onMyFailure(int i, String s) {
                    super.onMyFailure(i, s);
                    YoSnackbar.showSnackbar(mViewPager,R.string.net_error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            case R.id.action_change_password:
                startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
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
                doShowItemPasswordInfo(getContext(), passwordAdapter.getItem(position));
            }

            @Override
            public boolean onItemLongClick(final int position) {
                String[] toDo = getContext().getResources().getStringArray(R.array.alert_dialog_list_todo_password_item_long_click);
                AlertDialogUtils.showAlertDialogList(getContext(), toDo, new OnToDoItemClickListener() {

                    @Override
                    public void onItemClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ClipboardManager myClipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                                String text = passwordAdapter.getItem(position).getPassword();
                                ClipData myClip = ClipData.newPlainText("text", text);
                                myClipboard.setPrimaryClip(myClip);
                                YoSnackbar.showSnackbar(refreshLayout, R.string.copy_success_tip);
                               // startActivity(new Intent(getContext(), CheckPasswordActivity.class).putExtra(CheckPasswordActivity.KEY_TO_CHECK_PASSWORD_COPY_PASSWORD, passwordAdapter.getItem(position).getPassword()).putExtra(CheckPasswordActivity.KEY_TO_CHECK_PASSWORD_COPY, true));
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
            List<PasswordInfo> passwordInfoList = X3DBUtils.findAll(PasswordInfo.class, "groupingId", "=", groupingId,"saveInfoTime",true);
            List<PasswordInfo> passwordList =new ArrayList<>();
            if(passwordInfoList!=null){
                int topNum=0;
                for(int i=0;i<passwordInfoList.size();i++){
                    try {
                        String account=passwordInfoList.get(i).getAccount();
                        String accountDes= AESUtils.decrypt(account,AppConfig.APP_AES_KEY);
                        passwordInfoList.get(i).setAccount(accountDes);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        String password=passwordInfoList.get(i).getPassword();
                        String passwordDes= AESUtils.decrypt(password,AppConfig.APP_AES_KEY);
                        passwordInfoList.get(i).setPassword(passwordDes);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if( passwordInfoList.get(i).isTop()){
                        passwordList.add( topNum,passwordInfoList.get(i));
                        topNum++;
                    }else{
                        passwordList.add( passwordInfoList.get(i));
                    }
                }
            }
            passwordAdapter.setmData(passwordList);
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
            if (this.mFragmentList != null) {
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
        //RxBusUtils.get().unregister(RxBusTools.MainActivity_AlertDialog_ToDo_Item_Copy, alertDialogToDoItemCopy);
        if (screenObserver != null) {
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

      /*  alertDialogToDoItemCopy = RxBusUtils.get()
                .register(RxBusTools.MainActivity_AlertDialog_ToDo_Item_Copy);
        alertDialogToDoItemCopy.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RxBusAlertdialogItemCopyEntity>() {
                    @Override
                    public void call(RxBusAlertdialogItemCopyEntity rxBusAlertdialogItemCopyEntity) {
                        if (rxBusAlertdialogItemCopyEntity != null && !TextUtils.isEmpty(rxBusAlertdialogItemCopyEntity.getPassword())) {
                            ClipboardManager myClipboard = (ClipboardManager) MainActivity.this.getSystemService(CLIPBOARD_SERVICE);
                            String text = rxBusAlertdialogItemCopyEntity.getPassword();
                            ClipData myClip = ClipData.newPlainText("text", text);
                            myClipboard.setPrimaryClip(myClip);
                            YoSnackbar.showSnackbar(mViewPager, R.string.copy_success_tip);
                        }
                    }
                });*/
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

    private void initScreenObserver() {
        screenObserver = new ScreenObserver(MainActivity.this);
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

    /**
     * 展示用户信息
     *
     * @param passwordInfo
     */
    private static void doShowItemPasswordInfo(Context context, PasswordInfo passwordInfo) {
        View vlayout = LayoutInflater.from(context).inflate(R.layout.view_alert_dialog_password_info, null);
        TextView password_item_title = (TextView) vlayout.findViewById(R.id.password_item_title);
        TextView password_item_save_info_time = (TextView) vlayout.findViewById(R.id.password_item_save_info_time);
        TextView password_item_account = (TextView) vlayout.findViewById(R.id.password_item_account);
        TextView password_item_remarks = (TextView) vlayout.findViewById(R.id.password_item_remarks);
        TextView password_item_password = (TextView) vlayout.findViewById(R.id.password_item_password);
        View password_item_top = vlayout.findViewById(R.id.password_item_top);
        password_item_account.setText(passwordInfo.getAccount());
        password_item_password.setText(passwordInfo.getPassword());
        password_item_remarks.setText(AppSingletonTools.getRemarksText(passwordInfo.getRemarks()));
        password_item_title.setText(passwordInfo.getTitle());
        password_item_save_info_time.setText(DateUtils.getTimestampString(passwordInfo.getSaveInfoTime()));
        password_item_top.setVisibility(passwordInfo.isTop() ? View.VISIBLE : View.INVISIBLE);

        AlertDialogUtils.showAlertDialogInfo(context, R.string.action_password_info_item, vlayout);
    }

}
