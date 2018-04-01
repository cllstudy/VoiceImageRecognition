package com.rongsheng.voiceimagerecognition.base;

import android.support.annotation.StringRes;

/**
 * Created by hang on 2017/10/14 0014.
 */
public interface IBaseView {

    void toast(String msg);

    void toast(@StringRes int strignId);

    void showWaitDialog(String msg);

    void dismissWaitDialog();

    void toActivity(Class className);

    void toActivity(Class className, String key, Object value);

    void activityFinish();
}
