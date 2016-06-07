package com.yoyo.yopassword.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/5/23 16:16
 * 修改人：yoyo
 * 修改时间：2016/5/23 16:16
 * 修改备注：
 */
public class BaseAppCompatActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init();
  }

  public void init(){}

  @Override
  protected void onResume() {
    super.onResume();
  }
}
