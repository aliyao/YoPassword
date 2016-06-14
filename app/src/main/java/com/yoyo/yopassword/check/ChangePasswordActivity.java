package com.yoyo.yopassword.check;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.BaseAppCompatActivity;
import com.yoyo.yopassword.common.util.ACacheUtils;
import com.yoyo.yopassword.common.util.EditTextUtils;
import com.yoyo.yopassword.common.view.YoSnackbar;

public class ChangePasswordActivity extends BaseAppCompatActivity {
    EditText etOldPassword,etPassword,etPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();
        etOldPassword=(EditText)findViewById(R.id.et_old_password);
        etPassword=(EditText)findViewById(R.id.et_password);
        etPassword2=(EditText)findViewById(R.id.et_password2);
        etPassword2.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    doConfirm();
                }
                return false;
            }
        });
    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_confirm:
                doConfirm();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * 确定完成
     */
    private void doConfirm() {
        String oldCheckPassword = ACacheUtils.getCheckPassword(ChangePasswordActivity.this);
        String oldPassword = etOldPassword.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String password2 = etPassword2.getText().toString().trim();
        if (TextUtils.isEmpty(oldPassword)) {
            YoSnackbar.showSnackbar(etOldPassword, R.string.change_password_edit_old_password);
            EditTextUtils.requestFocus(etOldPassword);
            return;
        }
        if (!oldPassword.equals(oldCheckPassword)) {
            YoSnackbar.showSnackbar(etOldPassword, R.string.change_password_old_password_error);
            EditTextUtils.requestFocus(etOldPassword);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            YoSnackbar.showSnackbar(etPassword, R.string.change_password_edit_new_password);
            EditTextUtils.requestFocus(etPassword);
            return;
        }
        if (password.length()<6) {
            YoSnackbar.showSnackbar(etPassword, R.string.password_six_tip);
            EditTextUtils.requestFocus(etPassword);
            return;
        }
        if (TextUtils.isEmpty(password2)) {
            YoSnackbar.showSnackbar(etPassword2, R.string.change_password_edit_new_password_too);
            EditTextUtils.requestFocus(etPassword2);
            return;
        }
        if(!password.equals(password2)){
            YoSnackbar.showSnackbar(etPassword2, R.string.edit_password_no_same);
            EditTextUtils.requestFocus(etPassword2);
            return;
        }
        ACacheUtils.setCheckPassword(ChangePasswordActivity.this,password);
        finish();
    }
}
