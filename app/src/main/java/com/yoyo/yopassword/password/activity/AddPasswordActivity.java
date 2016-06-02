package com.yoyo.yopassword.password.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.BaseAppCompatActivity;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.tool.StartActivityTools;
import com.yoyo.yopassword.common.util.EditTextUtils;
import com.yoyo.yopassword.common.util.X3DBUtils;
import com.yoyo.yopassword.common.view.YoSnackbar;
import com.yoyo.yopassword.grouping.entity.GroupingInfo;
import com.yoyo.yopassword.password.entity.PasswordInfo;

public class AddPasswordActivity extends BaseAppCompatActivity {
    Button groupingBtn;
    EditText et_title, et_account, et_password, et_remarks;
    CheckBox cb_is_top, cb_is_hide_account;

    GroupingInfo groupingInfo;
    boolean isUpdate;
    long updatePasswordInfoId;
    PasswordInfo passwordInfo;
    boolean isEdit;

    public void init() {
        setContentView(R.layout.activity_add_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        groupingBtn = (Button) findViewById(R.id.btn_grouping);
        et_title = (EditText) findViewById(R.id.et_title);
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        et_remarks = (EditText) findViewById(R.id.et_remarks);
        cb_is_top = (CheckBox) findViewById(R.id.cb_is_top);
        cb_is_hide_account = (CheckBox) findViewById(R.id.cb_is_hide_account);
        setupActionBar();
        isEdit = false;
        isUpdate = getIntent().getBooleanExtra(StartActivityTools.ToAddPasswordActivity_IsUpdate, false);
        updatePasswordInfoId = getIntent().getLongExtra(StartActivityTools.ToAddPasswordActivity_PasswordInfoId, 0);
        if (isUpdate && updatePasswordInfoId > 0) {
            passwordInfo = X3DBUtils.findItem(PasswordInfo.class, updatePasswordInfoId);
            groupingInfo = X3DBUtils.findItem(GroupingInfo.class, passwordInfo.getGroupingId());
            updatePasswordInfo();
        } else {
            groupingInfo = X3DBUtils.findItem(GroupingInfo.class, AppConfig.DefaultGroupingId);
            refreshGroupingInfo();
        }

    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void updatePasswordInfo() {
        et_title.setText(passwordInfo.getTitle());
        et_account.setText(passwordInfo.getAccount());
        et_remarks.setText(passwordInfo.getRemarks());
        et_password.setText(passwordInfo.getPassword());
        cb_is_top.setChecked(passwordInfo.isTop());
        cb_is_hide_account.setChecked(passwordInfo.isHideAccount());
        refreshGroupingInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StartActivityTools.ToGroupingActivity_RequestCode && resultCode == StartActivityTools.ToGroupingActivity_ResultCode && data != null) {
            long groupingId = data.getLongExtra(StartActivityTools.ToGroupingActivity_GroupingId, 0);
            if (groupingId <= 0) return;
            GroupingInfo groupingInfoData = X3DBUtils.findItem(GroupingInfo.class, groupingId);
            if (groupingInfoData != null) {
                groupingInfo = groupingInfoData;
                refreshGroupingInfo();
            }
        }
    }

    private void refreshGroupingInfo() {
        groupingBtn.setText(groupingInfo.getGroupingName());
    }

    public void onYoClick(View v) {
        switch (v.getId()) {
            case R.id.btn_grouping:
                StartActivityTools.toGroupingActivity(AddPasswordActivity.this, true, true);
                break;
        }
    }

    @Override
    public void finish() {
        if (isEdit) {
            StartActivityTools.doAddPasswordActivitySetResult(AddPasswordActivity.this, groupingInfo.getGroupingId());
        }
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_confirm:
                doConfirm();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 确定完成
     */
    private void doConfirm() {
        String title = et_title.getText().toString();
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        String remarks = et_remarks.getText().toString();
        long groupingId = groupingInfo.getGroupingId();
        boolean isTop = cb_is_top.isChecked();
        boolean isHideAccount = cb_is_hide_account.isChecked();
        if (TextUtils.isEmpty(title)) {
            YoSnackbar.showSnackbar(et_title, R.string.edit_title);
            EditTextUtils.requestFocus(et_title);
            return;
        }
        if (TextUtils.isEmpty(account)) {
            YoSnackbar.showSnackbar(et_account, R.string.edit_account);
            EditTextUtils.requestFocus(et_account);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            YoSnackbar.showSnackbar(et_password, R.string.edit_password);
            EditTextUtils.requestFocus(et_password);
            return;
        }
        if (password.length()<6) {
            YoSnackbar.showSnackbar(et_password, R.string.password_six_tip);
            EditTextUtils.requestFocus(et_password);
            return;
        }

        PasswordInfo passwordInfoEdit = new PasswordInfo();
        passwordInfoEdit.setTitle(title);
        passwordInfoEdit.setAccount(account);
        passwordInfoEdit.setPassword(password);
        passwordInfoEdit.setRemarks(remarks);
        passwordInfoEdit.setGroupingId(groupingId);
        passwordInfoEdit.setTop(isTop);
        passwordInfoEdit.setHideAccount(isHideAccount);
        if (passwordInfo != null) {
            passwordInfoEdit.setSaveInfoTime(passwordInfo.getSaveInfoTime());
        }

        if (updatePasswordInfoId > 0) {
            passwordInfoEdit.setPasswordInfoId(updatePasswordInfoId);
        }
        X3DBUtils.save(passwordInfoEdit);
        isEdit = true;
        finish();
    }
}
