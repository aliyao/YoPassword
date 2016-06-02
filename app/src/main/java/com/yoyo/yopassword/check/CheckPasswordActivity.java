package com.yoyo.yopassword.check;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.common.util.ACacheUtils;
import com.yoyo.yopassword.common.util.EditTextUtils;
import com.yoyo.yopassword.common.view.YoSnackbar;
import com.yoyo.yopassword.hello.activity.HelloLoginActivity;
import com.yoyo.yopassword.main.activity.MainActivity;

public class CheckPasswordActivity extends AppCompatActivity {
    boolean isSuccess;
    EditText etPassword,etPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        setupActionBar();
        isSuccess=false;
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
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void finish() {
        if (!isSuccess){
            ACacheUtils.signOut(CheckPasswordActivity.this);
        }
        super.finish();
    }

    public void onYoClick(View view){
        switch (view.getId()){
            case R.id.btn_sign_out:
                startActivity(new Intent(CheckPasswordActivity.this, HelloLoginActivity.class).putExtra(HelloLoginActivity.KEY_TO_LOGIN,true));
                finish();
                break;
        }
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
            case android.R.id.home:
                startActivity(new Intent(CheckPasswordActivity.this, HelloLoginActivity.class).putExtra(HelloLoginActivity.KEY_TO_LOGIN,true));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * 确定完成
     */
    private void doConfirm() {
        String password = etPassword.getText().toString().trim();
        String password2 = etPassword2.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            YoSnackbar.showSnackbar(etPassword, R.string.edit_password);
            EditTextUtils.requestFocus(etPassword);
            return;
        }
        if (password.length()<6) {
            YoSnackbar.showSnackbar(etPassword, R.string.password_six_tip);
            EditTextUtils.requestFocus(etPassword);
            return;
        }
        if (TextUtils.isEmpty(password2)) {
            YoSnackbar.showSnackbar(etPassword2, R.string.edit_password_too);
            EditTextUtils.requestFocus(etPassword2);
            return;
        }
        if(!password.equals(password2)){
            YoSnackbar.showSnackbar(etPassword2, R.string.edit_password_no_same);
            EditTextUtils.requestFocus(etPassword2);
            return;
        }
        ACacheUtils.setCheckPassword(CheckPasswordActivity.this,password);
        startActivity(new Intent(CheckPasswordActivity.this, MainActivity.class));
        isSuccess=true;
        finish();
    }
}
