package com.yoyo.yopassword.grouping.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.OnBaseRecyclerViewListener;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.util.X3DBUtils;
import com.yoyo.yopassword.common.view.OnToDoItemClickListener;
import com.yoyo.yopassword.common.view.RefreshLayout;
import com.yoyo.yopassword.common.view.SpaceItemDecoration;
import com.yoyo.yopassword.common.view.YoAlertDialog;
import com.yoyo.yopassword.common.view.YoSnackbar;
import com.yoyo.yopassword.grouping.entity.GroupingInfo;
import com.yoyo.yopassword.grouping.view.adapter.GroupingAdapter;

import java.util.Date;
import java.util.List;

public class GroupingActivity extends AppCompatActivity {
    GroupingAdapter groupingAdapter;
    RefreshLayout refreshLayout;
    OnBaseRecyclerViewListener onBaseRecyclerViewListener=new  OnBaseRecyclerViewListener() {
        @Override
        public void onItemClick(int position) {

        }

        @Override
        public boolean onItemLongClick(final int position) {
            String[] toDo = getResources().getStringArray(R.array.alert_dialog_list_todo_grouping_item_long_click);
            YoAlertDialog.showAlertDialogList(GroupingActivity.this,toDo,new OnToDoItemClickListener(){

                @Override
                public void onItemClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            doGroupingNameEdittext(groupingAdapter.getItem(position));
                            break;
                        case 1:
                            YoAlertDialog.showAlertDialog(GroupingActivity.this, R.string.grouping_item_delect_todo,new OnToDoItemClickListener(){
                                @Override
                                public void onPositiveClick(DialogInterface dialog, int which) {
                                    super.onPositiveClick(dialog, which);
                                    X3DBUtils.delectById(GroupingInfo.class,groupingAdapter.getItem(position).getGroupingId());
                                    refreshGrouping();
                                }
                            });
                            break;
                    }

                }
            });
            return false;
        }
    };

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

        refreshLayout= (RefreshLayout) findViewById(R.id.refresh_layout);
        RecyclerView recyclerViewGrouping = (RecyclerView) findViewById(R.id.recycler_view_grouping);
        recyclerViewGrouping.setHasFixedSize(true);
        //设置布局管理器
        recyclerViewGrouping.setLayoutManager(new LinearLayoutManager(this));
        groupingAdapter=new GroupingAdapter(null);
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
    }

    public void refreshGrouping(){
        List<GroupingInfo> groupingInfoList= X3DBUtils.findAll(GroupingInfo.class);
        groupingAdapter.setmData(groupingInfoList);
        groupingAdapter.notifyDataSetChanged();
        if(refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }

    /**
     *
     * @param groupingInfo
     */
    private void doGroupingNameEdittext(final GroupingInfo groupingInfo){
        View viewlayout= LayoutInflater.from(GroupingActivity.this).inflate(R.layout.view_add_grouping_edittext, null);
        final EditText groupingNameEditText=(EditText)viewlayout.findViewById(R.id.et_add_grouping);
        if(groupingInfo!=null&&!TextUtils.isEmpty(groupingInfo.getGroupingName())){
            groupingNameEditText.setText(groupingInfo.getGroupingName());
        }
        YoAlertDialog.showAlertDialogEditText(GroupingActivity.this,R.string.action_add_grouping,viewlayout,new OnToDoItemClickListener(){
            @Override
            public void onPositiveClick(DialogInterface dialog, int which) {
                super.onPositiveClick(dialog, which);
                String groupingName=groupingNameEditText.getText().toString();
                if(TextUtils.isEmpty(groupingName)){
                    YoSnackbar.showSnackbar(refreshLayout,R.string.edit_grouping_name);
                    return;
                }
                GroupingInfo groupingInfoNew=groupingInfo;
                if(groupingInfoNew==null){
                    groupingInfoNew= new GroupingInfo();
                }
                groupingInfoNew.setGroupingName(groupingName);
                if(groupingInfoNew.getSaveInfoTime()<=0){
                    groupingInfoNew.setSaveInfoTime(new Date().getTime());
                }
                X3DBUtils.save(groupingInfoNew);
                refreshGrouping();
            }
        });
    }

}
