package com.rongsheng.voiceimagerecognition.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.rongsheng.voiceimagerecognition.R;
import com.rongsheng.voiceimagerecognition.utils.ToastUtils;


/**
 * Created by hang on 2017/10/14 0014.
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    private InputMethodManager imm;
    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue_26));
        }
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            //初始化沉浸式
            if (isImmersionBarEnabled())
                initImmersionBar();
            initView(savedInstanceState);
            initData();
        }
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    private Dialog dialog;

    private void freshDialog(Context context, String msg) {
        if (dialog != null) {
            dialog = null;
        }
        dialog = new Dialog(context, R.style.FreshWaitDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_fresh, null, false);
        if (msg != null) {
            TextView tvMessage = view.findViewById(R.id.tv_fresh_dialog_message);
            tvMessage.setText(msg);
        }
        ImageView iv = view.findViewById(R.id.iv_dialog_fresh);
        Animation animation = new RotateAnimation(0, 1080, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(Animation.INFINITE);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);
        iv.startAnimation(animation);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
    }


    public abstract
    @LayoutRes
    int getLayoutId();

    public abstract void initView(@Nullable Bundle savedInstanceState);

    /**
     * 数据处理
     */
    public abstract void initData();

    @Override
    public void toast(String msg) {
        ToastUtils.toast(this, msg);
    }

    @Override
    public void toast(@StringRes int stringId) {
        ToastUtils.toast(this, getString(stringId));
    }

    @Override
    public void showWaitDialog(String msg) {
        freshDialog(this, msg);
    }

    @Override
    public void dismissWaitDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void toActivity(Class className) {
        Intent toItt = new Intent(this, className);
        startActivity(toItt);
    }

    @Override
    public void toActivity(Class className, String key, Object value) {
        Intent toItt = new Intent(this, className);
        toItt.putExtra(key, (Parcelable) value);
        startActivity(toItt);
    }

    @Override
    public void activityFinish() {
        this.finish();
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.imm = null;
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
    }

    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

}
