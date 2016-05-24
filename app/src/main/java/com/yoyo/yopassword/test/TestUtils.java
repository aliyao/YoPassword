package com.yoyo.yopassword.test;

import com.yoyo.yopassword.password.entity.PasswordInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/5/24 15:25
 * 修改人：yoyo
 * 修改时间：2016/5/24 15:25
 * 修改备注：
 */
public class TestUtils {
    public static List<PasswordInfo> getListPasswordInfo(){
        List<PasswordInfo> passwordInfoList=new ArrayList<>();
        for (int i=0;i<20;i++){
            PasswordInfo passwordInfo=new PasswordInfo();
            passwordInfo.setAccount("yoyo"+i);
            passwordInfo.setPassword("yoyo"+i+i+i);
            passwordInfo.setRemarks("yoyo备注显示信息"+i);
            passwordInfo.setSaveInfoTime(new Date().getTime()-(i*10000));
            passwordInfo.setTitle("yoyoTitle"+i);
            passwordInfo.setTop(i%2==0);
            passwordInfo.setPasswordInfoId(i);
            passwordInfo.setGroupingId(i%2+1);
            passwordInfoList.add(passwordInfo);
        }
        return passwordInfoList;
    }
}
