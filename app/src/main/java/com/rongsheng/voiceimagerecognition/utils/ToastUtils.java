package com.rongsheng.voiceimagerecognition.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rongsheng.voiceimagerecognition.R;


/**
 * Created by hang on 2017/10/14 0014.
 * toast工具类
 */
public class ToastUtils {

    private static Toast mToast;

    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
            mToast = null;//toast隐藏后，将其置为null
        }
    };

    public static void toast(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_ui, null);//自定义布局
        TextView text = view.findViewById(R.id.tv_toast);//显示的提示文字
        text.setText(message);
        mHandler.removeCallbacks(r);
        if (mToast == null) {//只有mToast==null时才重新创建，否则只需更改提示文字
            mToast = new Toast(context);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.BOTTOM, 0, 200);
            mToast.setView(view);
        }
        mHandler.postDelayed(r, 1000);//延迟1秒隐藏toast
        mToast.show();
    }


    public static void longToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
}
