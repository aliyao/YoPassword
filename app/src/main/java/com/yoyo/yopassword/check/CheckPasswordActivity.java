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
import android.widget.EditText;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.common.util.ACacheUtils;
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
        etPassword2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:             // 键盘松开
                            doConfirm();
                            break;
                        case KeyEvent.ACTION_DOWN:          // 键盘按下
                            break;
                    }
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
        String password = etPassword.getText().toString();
        String password2 = etPassword2.getText().toString();

        if (TextUtils.isEmpty(password)) {
            YoSnackbar.showSnackbar(etPassword, R.string.edit_password);
            return;
        }
        if (password.length()<6) {
            YoSnackbar.showSnackbar(etPassword, R.string.password_six_tip);
            return;
        }
        if (TextUtils.isEmpty(password2)) {
            YoSnackbar.showSnackbar(etPassword, R.string.edit_password_too);
            return;
        }
        if(!password.equals(password2)){
            YoSnackbar.showSnackbar(etPassword, R.string.edit_password_no_same);
            return;
        }
        ACacheUtils.setCheckPassword(CheckPasswordActivity.this,password);
        startActivity(new Intent(CheckPasswordActivity.this, MainActivity.class));
        isSuccess=true;
        finish();
    }
}
