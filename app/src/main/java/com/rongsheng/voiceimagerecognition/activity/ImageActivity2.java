package com.rongsheng.voiceimagerecognition.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.rongsheng.voiceimagerecognition.ImageBean;
import com.rongsheng.voiceimagerecognition.R;
import com.rongsheng.voiceimagerecognition.base.BaseActivity;
import com.rongsheng.voiceimagerecognition.utils.FileUtil;
import com.rongsheng.voiceimagerecognition.utils.RecognizeService;

import java.util.List;

public class ImageActivity2 extends BaseActivity implements View.OnClickListener {

    private Button mBtClick;
    private TextView mTvShow;
    private boolean hasGotToken = false;
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;

    @Override
    public int getLayoutId() {
        return R.layout.activity_image2;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).statusBarColor(R.color.title).init();
        mBtClick = (Button) findViewById(R.id.bt_click);
        mTvShow = (TextView) findViewById(R.id.tv_show);
        AppCompatImageView ivBack = (AppCompatImageView) findViewById(R.id.iv_back);
        AppCompatTextView tvTitle = (AppCompatTextView) findViewById(R.id.tv_title);
        tvTitle.setText("文字识别");
        ivBack.setOnClickListener(view -> finish());
        mBtClick.setOnClickListener(this);
        initAccessToken();
    }


    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        if (!checkTokenStatus()) {
            return;
        }
        Intent intent = new Intent(ImageActivity2.this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);

    }

    private void initAccessToken() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                toast("licence方式获取token失败" + error.getMessage());
            }
        }, getApplicationContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 识别成功回调，通用文字识别
        if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneralBasic(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            Gson gson = new Gson();
                            ImageBean imageBean = gson.fromJson(result, ImageBean.class);
                            List<ImageBean.WordsResult> result1 = imageBean.getWords_result();
                            String res = " ";
                            for (ImageBean.WordsResult result2 : result1) {
                                res += result2.getWords() + "\n";
                            }
                            mTvShow.setText(res);
                        }
                    });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OCR.getInstance().release();
    }
}
