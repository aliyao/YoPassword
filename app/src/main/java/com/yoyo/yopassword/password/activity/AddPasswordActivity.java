package com.yoyo.yopassword.password.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.BaseAppCompatActivity;
import com.yoyo.yopassword.common.tool.StartActivityTools;
import com.yoyo.yopassword.common.util.X3DBUtils;
import com.yoyo.yopassword.grouping.entity.GroupingInfo;
import com.yoyo.yopassword.password.entity.PasswordInfo;

public class AddPasswordActivity extends BaseAppCompatActivity {
    Button groupingBtn;
    EditText et_title,et_account,et_password,et_remarks;
    CheckBox cb_is_top;
    GroupingInfo groupingInfo;
    boolean isUpdate;
    long updatePasswordInfoId;
    PasswordInfo passwordInfo;

     public void init(){
         setContentView(R.layout.activity_add_password);
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

        groupingBtn=(Button)findViewById(R.id.btn_grouping);
        et_title=(EditText)findViewById(R.id.et_title);
        et_account=(EditText)findViewById(R.id.et_account);
        et_password=(EditText)findViewById(R.id.et_password);
        et_remarks=(EditText)findViewById(R.id.et_remarks);
        cb_is_top=(CheckBox)findViewById(R.id.cb_is_top);

         isUpdate=getIntent().getBooleanExtra(StartActivityTools.ToAddPasswordActivity_IsUpdate ,false);
         updatePasswordInfoId=getIntent().getLongExtra(StartActivityTools.ToAddPasswordActivity_PasswordInfoId ,0);
         if(isUpdate&&updatePasswordInfoId>0){
             passwordInfo=X3DBUtils.findItem(PasswordInfo.class,updatePasswordInfoId);
             groupingInfo=X3DBUtils.findItem(GroupingInfo.class,passwordInfo.getGroupingId());
             updatePasswordInfo();
         }else{
             groupingInfo=new GroupingInfo(AddPasswordActivity.this.getResources().getString(R.string.action_password),0);
             groupingInfo.setGroupingId(1);
             refreshGroupingInfo();
         }

    }
    private void updatePasswordInfo(){
        et_title.setText(passwordInfo.getTitle());
        et_account.setText(passwordInfo.getAccount());
        et_remarks.setText(passwordInfo.getRemarks());
        cb_is_top.setChecked(passwordInfo.isTop());
        refreshGroupingInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== StartActivityTools.ToGroupingActivity_RequestCode&&resultCode==StartActivityTools.ToGroupingActivity_ResultCode&&data!=null){
            long groupingId=data.getLongExtra(StartActivityTools.ToGroupingActivity_GroupingId,0);
            if(groupingId<=0) return;
            GroupingInfo groupingInfoData= X3DBUtils.findItem(GroupingInfo.class,groupingId);
            if(groupingInfoData!=null){
                groupingInfo=groupingInfoData;
                refreshGroupingInfo();
            }
        }
    }

    private void refreshGroupingInfo(){
        groupingBtn.setText(groupingInfo.getGroupingName());
    }

    public void onYoClick(View v) {
        switch (v.getId()){
            case R.id.btn_grouping:
                StartActivityTools.toGroupingActivity(AddPasswordActivity.this,true,true);
                break;
        }

    }

    @Override
    public void finish() {
        StartActivityTools.doAddPasswordActivitySetResult(AddPasswordActivity.this,passwordInfo.getPasswordInfoId());
        super.finish();

    }
}
