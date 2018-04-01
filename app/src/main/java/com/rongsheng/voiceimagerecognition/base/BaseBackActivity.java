package com.rongsheng.voiceimagerecognition.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.TextView;

import com.rongsheng.voiceimagerecognition.R;


/**
 * @author hang
 * @date 2017/10/18 0018
 * 自定义title
 */

public abstract class BaseBackActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.iv_back).setOnClickListener(view -> activityFinish());
    }

    //设置title
    @Override
    public void setTitle(@StringRes int titleID) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(titleID);
    }

    public void setTitle(String title) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(title);
    }

}