package com.yoyo.yopassword.password.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.BaseAppCompatActivity;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.tool.RxBusTools;
import com.yoyo.yopassword.common.tool.YoStartActivityTools;
import com.yoyo.yopassword.common.util.EditTextUtils;
import com.yoyo.yopassword.common.util.RxBusUtils;
import com.yoyo.yopassword.common.util.X3DBUtils;
import com.yoyo.yopassword.common.util.safe.AESUtils;
import com.yoyo.yopassword.common.view.YoSnackbar;
import com.yoyo.yopassword.grouping.entity.GroupingInfo;
import com.yoyo.yopassword.main.entity.RxBusFragmentItemEntity;
import com.yoyo.yopassword.password.entity.PasswordInfo;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class AddPasswordActivity extends BaseAppCompatActivity {
    Observable<String> adapterRefreshData;
    Button groupingBtn;
    EditText et_title, et_account, et_password, et_remarks;
    CheckBox cb_is_top, cb_is_hide_account;

    GroupingInfo groupingInfo;
    boolean isUpdate;
    long updatePasswordInfoId;
    PasswordInfo passwordInfo;
    long oldGroupingId;

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

        cb_is_hide_account.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showHideAccount();
            }
        });

        setupActionBar();
        isUpdate = getIntent().getBooleanExtra(YoStartActivityTools.ToAddPasswordActivity_IsUpdate, false);
        updatePasswordInfoId = getIntent().getLongExtra(YoStartActivityTools.ToAddPasswordActivity_PasswordInfoId, 0);
        if (isUpdate && updatePasswordInfoId > 0) {
            passwordInfo = X3DBUtils.findItem(PasswordInfo.class, updatePasswordInfoId);
            groupingInfo = X3DBUtils.findItem(GroupingInfo.class, passwordInfo.getGroupingId());
            oldGroupingId=groupingInfo.getGroupingId();
            updatePasswordInfo();
        } else {
            groupingInfo = X3DBUtils.findItem(GroupingInfo.class, AppConfig.DefaultGroupingId);
            refreshGroupingInfo();
        }
        initRxBusUtils();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void updatePasswordInfo() {
        try {
            String accountDes= AESUtils.decrypt(passwordInfo.getAccount(),AppConfig.APP_AES_KEY);
            et_account.setText(accountDes);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            String passwordDes= AESUtils.decrypt(passwordInfo.getPassword(),AppConfig.APP_AES_KEY);
            et_password.setText(passwordDes);
        }catch (Exception e){
            e.printStackTrace();
        }

        et_title.setText(passwordInfo.getTitle());
        et_remarks.setText(passwordInfo.getRemarks());
        cb_is_top.setChecked(passwordInfo.isTop());
        cb_is_hide_account.setChecked(passwordInfo.isHideAccount());
        refreshGroupingInfo();
    }

    private void refreshGroupingInfo() {
        if (groupingBtn != null && groupingInfo != null) {
            groupingBtn.setText(groupingInfo.getGroupingName());
        }
        showHideAccount();
    }

    private void showHideAccount(){
        if(cb_is_hide_account.isChecked()){
            et_account.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }else {
            et_account.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    public void onYoClick(View v) {
        switch (v.getId()) {
            case R.id.btn_grouping:
                YoStartActivityTools.toGroupingActivity_Select(AddPasswordActivity.this);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
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
        String title = et_title.getText().toString().trim();
        String account = et_account.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String remarks = et_remarks.getText().toString().trim();
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
        if (password.length() < 6) {
            YoSnackbar.showSnackbar(et_password, R.string.password_six_tip);
            EditTextUtils.requestFocus(et_password);
            return;
        }

        try {
            String accountEncrypt= AESUtils.encrypt(account,AppConfig.APP_AES_KEY);
            String passwordEncrypt= AESUtils.encrypt(password,AppConfig.APP_AES_KEY);
            PasswordInfo passwordInfoEdit = new PasswordInfo();
            passwordInfoEdit.setTitle(title);
            passwordInfoEdit.setAccount(accountEncrypt);
            passwordInfoEdit.setPassword(passwordEncrypt);
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
            RxBusUtils.get().post(RxBusTools.MainActivity_PlaceholderFragment_Item_RefreshData, new RxBusFragmentItemEntity(oldGroupingId,passwordInfoEdit.getGroupingId()));
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initRxBusUtils() {
        adapterRefreshData = RxBusUtils.get()
                .register(RxBusTools.AddPasswordActivity_Adapter_RefreshData);

        adapterRefreshData.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object gId) {
                        long groupingId = (long) gId;
                        if (groupingId <= 0) return;
                        GroupingInfo groupingInfoData = X3DBUtils.findItem(GroupingInfo.class, groupingId);
                        if (groupingInfoData != null) {
                            groupingInfo = groupingInfoData;
                            refreshGroupingInfo();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        RxBusUtils.get().unregister(RxBusTools.AddPasswordActivity_Adapter_RefreshData, adapterRefreshData);
        super.onDestroy();
    }
}
