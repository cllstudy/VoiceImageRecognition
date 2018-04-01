package com.rongsheng.voiceimagerecognition.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by hang on 2018/3/27 0027.
 */

public class ImageBean implements Serializable {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(content)) {
            stringBuffer.append(content);
        }
        return stringBuffer.toString();
    }
}
