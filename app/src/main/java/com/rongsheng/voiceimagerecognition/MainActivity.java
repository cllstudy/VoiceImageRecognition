package com.rongsheng.voiceimagerecognition;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.rongsheng.voiceimagerecognition.activity.ImageActivity2;
import com.rongsheng.voiceimagerecognition.base.BaseActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_voice)
    Button mainVoice;
    @BindView(R.id.main_image)
    Button mainImage;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        tvTitle.setText(R.string.app_name);
        initPermission();
    }

    @OnClick({R.id.main_voice, R.id.main_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_voice:
                toActivity(VoiceActivity.class);
                break;
            case R.id.main_image:
                toActivity(ImageActivity2.class);
                break;
        }
    }

    private void initPermission() {
        AndPermission.with(this)
                .requestCode(300)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .rationale((requestCode, rationale) -> AndPermission.rationaleDialog(MainActivity.this, rationale)
                        .show())
                .callback(this)
                .start();
    }

    @PermissionYes(300)
    private void getPermissionYes(List<String> grantedPermissions) {
    }

    @PermissionNo(300)
    private void getPermissionNo(List<String> deniedPermissions) {
        AndPermission.defaultSettingDialog(MainActivity.this, 300).show();
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                // System.exit(0);
                moveTaskToBack(false);
            } else {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
        super.onBackPressed();
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).statusBarColor(R.color.title).init();
    }
}
