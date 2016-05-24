package com.yoyo.yopassword.common.util;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DensityUtils {
  
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int pxTodip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
   /** 
    * 将sp值转换为px值，保证文字大小不变 
    *  
    * @param spValue 
    *            （DisplayMetrics类中属性scaledDensity）
    * @return 
    */  
   public static int spTopx(Context context, float spValue) {
       final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
       return (int) (spValue * fontScale + 0.5f);  
   }  
  /* public static int pxTosp(Context context, float pxValue) {
       final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
       return (int) (pxValue / fontScale + 0.5f);  
   }*/
   public static int pxTosp(Context context, float pxValue) {
       final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
       return (int) (pxValue / fontScale );  
   }

    public static final Point getScreenSize(Context context){
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        return screenSize;
    }
}  
