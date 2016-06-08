package com.yoyo.yopassword.check;

import android.content.Intent;
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
import com.yoyo.yopassword.common.util.ActivityManager;
import com.yoyo.yopassword.common.util.EditTextUtils;
import com.yoyo.yopassword.common.view.YoSnackbar;
import com.yoyo.yopassword.hello.activity.HelloLoginActivity;
import com.yoyo.yopassword.main.activity.MainActivity;

public class CheckPasswordActivity extends BaseAppCompatActivity {
    EditText etPassword;
    boolean isSuccess;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_check_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNoCheck(true);
        isSuccess = false;
        etPassword = (EditText) findViewById(R.id.et_password);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

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

    /**
     * 确定完成
     */
    private void doConfirm() {
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            YoSnackbar.showSnackbar(etPassword, R.string.edit_password);
            EditTextUtils.requestFocus(etPassword);
            return;
        }
        if (password.length() < 6) {
            YoSnackbar.showSnackbar(etPassword, R.string.password_six_tip);
            EditTextUtils.requestFocus(etPassword);
            return;
        }
        String rightPassword = ACacheUtils.getCheckPassword(CheckPasswordActivity.this);
        if (TextUtils.isEmpty(rightPassword) || !password.equals(rightPassword)) {
            YoSnackbar.showSnackbar(etPassword, R.string.edit_password_error);
            return;
        }
        if(ActivityManager.getInstance().isToMainAcrivity()){
            startActivity(new Intent(CheckPasswordActivity.this, MainActivity.class));
        }
        isSuccess = true;
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_check_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_confirm:
                doConfirm();
                return true;
            case R.id.action_sign_out:
                ACacheUtils.signOut(CheckPasswordActivity.this);
                ActivityManager.getInstance().finishOthers();
                startActivity(new Intent(CheckPasswordActivity.this, HelloLoginActivity.class).putExtra(HelloLoginActivity.KEY_TO_LOGIN, true));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityManager.getInstance().exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
