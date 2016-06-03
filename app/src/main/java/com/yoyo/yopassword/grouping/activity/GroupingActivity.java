package com.yoyo.yopassword.grouping.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.BaseAppCompatActivity;
import com.yoyo.yopassword.base.OnBaseRecyclerViewListener;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.tool.RxBusTools;
import com.yoyo.yopassword.common.tool.YoStartActivityTools;
import com.yoyo.yopassword.common.util.AlertDialogUtils;
import com.yoyo.yopassword.common.util.RxBusUtils;
import com.yoyo.yopassword.common.util.X3DBUtils;
import com.yoyo.yopassword.common.view.OnToDoItemClickListener;
import com.yoyo.yopassword.common.view.RefreshLayout;
import com.yoyo.yopassword.common.view.SpaceItemDecoration;
import com.yoyo.yopassword.common.view.YoSnackbar;
import com.yoyo.yopassword.grouping.entity.GroupingInfo;
import com.yoyo.yopassword.grouping.view.adapter.GroupingAdapter;
import com.yoyo.yopassword.password.entity.PasswordInfo;

import java.util.Date;
import java.util.List;

public class GroupingActivity extends BaseAppCompatActivity {
    GroupingAdapter groupingAdapter;
    RefreshLayout refreshLayout;
    boolean isSelect;
    OnBaseRecyclerViewListener onBaseRecyclerViewListener = new OnBaseRecyclerViewListener() {
        @Override
        public void onItemClick(int position) {
            if (isSelect) {
                RxBusUtils.get().post(RxBusTools.AddPasswordActivity_Adapter_RefreshData, groupingAdapter.getItem(position).getGroupingId());
                finish();
            }
        }

        @Override
        public boolean onItemLongClick(final int position) {
            String[] toDo = getResources().getStringArray(R.array.alert_dialog_list_todo_grouping_item_long_click);
            GroupingInfo groupingInfoTo = groupingAdapter.getItem(position);
            if (groupingInfoTo.getGroupingId() == AppConfig.DefaultGroupingId) {
                String[] mToDo = toDo;
                toDo = new String[1];
                toDo[0] = mToDo[0];
            }
            AlertDialogUtils.showAlertDialogList(GroupingActivity.this, toDo, new OnToDoItemClickListener() {

                @Override
                public void onItemClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            doGroupingNameEdittext(groupingAdapter.getItem(position));
                            break;
                        case 1:
                            AlertDialogUtils.showAlertDialog(GroupingActivity.this, R.string.grouping_item_delect_todo, new OnToDoItemClickListener() {
                                @Override
                                public void onPositiveClick(DialogInterface dialog, int which) {
                                    super.onPositiveClick(dialog, which);
                                    try {
                                        List<PasswordInfo> passwordInfoList=X3DBUtils.findAll(PasswordInfo.class,"groupingId","=",groupingAdapter.getItem(position).getGroupingId());
                                        if(passwordInfoList!=null&&passwordInfoList.size()>0){
                                            for (int i=0;i<passwordInfoList.size();i++){
                                                passwordInfoList.get(i).setGroupingId(AppConfig.DefaultGroupingId);
                                            }
                                            X3DBUtils.save(passwordInfoList);
                                        }
                                        X3DBUtils.delectById(GroupingInfo.class, groupingAdapter.getItem(position).getGroupingId());
                                        refreshGrouping();
                                        refreshMainActivityGrouping();
                                        refreshMainActivityGroupingDel();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                            break;
                    }

                }
            });
            return false;
        }
    };

    private void refreshMainActivityGrouping() {
        RxBusUtils.get().post(RxBusTools.MainActivity_SectionsPagerAdapter_RefreshData, 1);
    }
    private void refreshMainActivityGroupingDel() {
        RxBusUtils.get().post(RxBusTools.MainActivity_PlaceholderFragment_Del_RefreshData, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouping);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doGroupingNameEdittext(null);
            }
        });
        setupActionBar();
        refreshLayout = (RefreshLayout) findViewById(R.id.refresh_layout);
        RecyclerView recyclerViewGrouping = (RecyclerView) findViewById(R.id.recycler_view_grouping);
        recyclerViewGrouping.setHasFixedSize(true);
        //设置布局管理器
        recyclerViewGrouping.setLayoutManager(new LinearLayoutManager(this));
        groupingAdapter = new GroupingAdapter(null);
        //设置adapter
        recyclerViewGrouping.setAdapter(groupingAdapter);
        //设置Item增加、移除动画
        recyclerViewGrouping.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
         /*   recyclerViewGrouping.addItemDecoration(new DividerItemDecoration(
                    getActivity(), DividerItemDecoration.HORIZONTAL_LIST));*/

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space_item_decoration);
        recyclerViewGrouping.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshGrouping();
                    }
                }, AppConfig.RefreshViewTime);
            }
        });
        groupingAdapter.setOnRecyclerViewListener(onBaseRecyclerViewListener);
        refreshLayout.setRefreshing(true);
        refreshGrouping();
        isSelect = getIntent().getBooleanExtra(YoStartActivityTools.ToGroupingActivity_IsSelect, false);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshGrouping() {
        List<GroupingInfo> groupingInfoList = X3DBUtils.findAll(GroupingInfo.class);
        groupingAdapter.setmData(groupingInfoList);
        groupingAdapter.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    /**
     * @param groupingInfo
     */
    private void doGroupingNameEdittext(final GroupingInfo groupingInfo) {
        View viewlayout = LayoutInflater.from(GroupingActivity.this).inflate(R.layout.view_add_grouping_edittext, null);
        final EditText groupingNameEditText = (EditText) viewlayout.findViewById(R.id.et_add_grouping);
        if (groupingInfo != null && !TextUtils.isEmpty(groupingInfo.getGroupingName())) {
            groupingNameEditText.setText(groupingInfo.getGroupingName());
            groupingNameEditText.setSelection(groupingInfo.getGroupingName().length());
            groupingNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    //当actionId == XX_SEND 或者 XX_DONE时都触发
                    //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                    //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                    if (actionId == EditorInfo.IME_ACTION_SEND
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                        //处理事件
                        doPositiveClick(groupingNameEditText, groupingInfo);
                    }
                    return false;
                }
            });
        }
        AlertDialogUtils.showAlertDialogEditText(GroupingActivity.this, R.string.action_add_grouping, viewlayout, new OnToDoItemClickListener() {
            @Override
            public void onPositiveClick(DialogInterface dialog, int which) {
                super.onPositiveClick(dialog, which);
                doPositiveClick(groupingNameEditText, groupingInfo);
            }
        });
    }

    private void doPositiveClick(EditText groupingNameEditText, GroupingInfo groupingInfo) {
        String groupingName = groupingNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(groupingName)) {
            YoSnackbar.showSnackbar(refreshLayout, R.string.edit_grouping_name);
            return;
        }
        GroupingInfo groupingInfoNew = groupingInfo;
        if (groupingInfoNew == null) {
            groupingInfoNew = new GroupingInfo();
        }
        groupingInfoNew.setGroupingName(groupingName);
        if (groupingInfoNew.getSaveInfoTime() <= 0) {
            groupingInfoNew.setSaveInfoTime(new Date().getTime());
        }
        X3DBUtils.save(groupingInfoNew);
        refreshGrouping();
        refreshMainActivityGrouping();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
