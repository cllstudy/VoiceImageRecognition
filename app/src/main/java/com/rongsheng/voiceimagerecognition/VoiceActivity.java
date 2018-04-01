package com.rongsheng.voiceimagerecognition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.idst.nls.NlsClient;
import com.alibaba.idst.nls.NlsListener;
import com.alibaba.idst.nls.StageListener;
import com.alibaba.idst.nls.internal.protocol.NlsRequest;
import com.alibaba.idst.nls.internal.protocol.NlsRequestProto;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;

import static com.rongsheng.voiceimagerecognition.Constant.KID;
import static com.rongsheng.voiceimagerecognition.Constant.KSECRET;

/**
 * @author lei
 * @desc 一句话语音识别
 * @date 2018/3/26 0026 -- 上午 9:36.
 * 个人博客站: http://www.bestlei.top
 */
public class VoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvShowtext;
    private Button mBtOpen;
    private Button mBtStop;
    private Button mStatrtHecheng;
    private Button mKuaisu;
    private boolean isRecognizing = false;
    private Context context;
    private NlsClient mNlsClient;
    private NlsRequest mNlsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        ImmersionBar.with(this).statusBarColor(R.color.title).init();
        context = getApplicationContext();
        mNlsRequest = initNlsRequest();
        String appkey = "nls-service"; //请设置简介页面的Appkey
        mNlsRequest.setApp_key(appkey);    //appkey列表中获取
        mNlsRequest.setAsr_sc("opu");      //设置语音格式
        // 热词参数
        mNlsRequest.setAsrVocabularyId("vocabid");
        NlsClient.openLog(true);
        NlsClient.configure(getApplicationContext()); //全局配置
        initView();
        initNls();
    }

    private void initView() {
        mTvShowtext = (TextView) findViewById(R.id.tv_showtext);
        mBtOpen = (Button) findViewById(R.id.bt_open);
        mBtStop = (Button) findViewById(R.id.bt_stop);
        mStatrtHecheng = (Button) findViewById(R.id.statrt_hecheng);
        mKuaisu = (Button) findViewById(R.id.kuaisu);
        AppCompatTextView tvTitle = (AppCompatTextView) findViewById(R.id.tv_title);
        AppCompatImageView ivBack = (AppCompatImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(view -> finish());
        tvTitle.setText("语音识别");
        mStatrtHecheng.setOnClickListener(this);
        mKuaisu.setOnClickListener(this);
        ivBack.setOnClickListener(view -> finish());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.statrt_hecheng:
                Intent intent = new Intent(VoiceActivity.this, Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.kuaisu:
                Intent intent2 = new Intent(VoiceActivity.this, Main3Activity.class);
                startActivity(intent2);
                break;
        }
    }

    private void initNls() {
        mNlsClient = NlsClient.newInstance(this, mRecognizeListener, mStageListener, mNlsRequest);                          //实例化NlsClient
        mNlsClient.setMaxRecordTime(60000);  //设置最长语音
        mNlsClient.setMaxStallTime(1000);    //设置最短语音
        mNlsClient.setMinRecordTime(500);    //设置最大录音中断时间
        mNlsClient.setRecordAutoStop(false);  //设置VAD
        mNlsClient.setMinVoiceValueInterval(200); //设置音量回调时长
        initStartRecognizing();
        initStopRecognizing();
    }

    private NlsRequest initNlsRequest() {
        NlsRequestProto proto = new NlsRequestProto(context);
        proto.setApp_user_id("CLL"); //设置在应用中的用户名，可选
        return new NlsRequest(proto);
    }

    private void initStartRecognizing() {
        mBtOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecognizing = true;
                mTvShowtext.setText("正在录音，请稍候！");
                mNlsRequest.authorize(KID, KSECRET); //请替换为用户申请到的Access Key ID和Access Key Secret
                mNlsClient.start();
                mBtOpen.setText("录音中。。。");
            }
        });
    }

    private void initStopRecognizing() {
        mBtStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecognizing = false;
                mTvShowtext.setText("");
                mNlsClient.stop();
                mBtOpen.setText("开始 录音");
            }
        });
    }


    private NlsListener mRecognizeListener = new NlsListener() {
        @Override
        public void onRecognizingResult(int status, RecognizedResult result) {
            switch (status) {
                case NlsClient.ErrorCode.SUCCESS:
                    Gson gson = new Gson();
                    Stu stu = gson.fromJson(result.asr_out, Stu.class);
                    mTvShowtext.setText(stu.getResult());
                    break;
                case NlsClient.ErrorCode.RECOGNIZE_ERROR:
                    Toast.makeText(VoiceActivity.this, "识别错误", Toast.LENGTH_LONG).show();
                    break;
                case NlsClient.ErrorCode.RECORDING_ERROR:
                    Toast.makeText(VoiceActivity.this, "录音错误", Toast.LENGTH_LONG).show();
                    break;
                case NlsClient.ErrorCode.NOTHING:
                    Toast.makeText(VoiceActivity.this, "什么都没说", Toast.LENGTH_LONG).show();
                    break;
            }
            isRecognizing = false;
        }
    };
    private StageListener mStageListener = new StageListener() {
        @Override
        public void onStartRecognizing(NlsClient recognizer) {
            super.onStartRecognizing(recognizer);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onStopRecognizing(NlsClient recognizer) {
            super.onStopRecognizing(recognizer);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onStartRecording(NlsClient recognizer) {
            super.onStartRecording(recognizer);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onStopRecording(NlsClient recognizer) {
            super.onStopRecording(recognizer);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onVoiceVolume(int volume) {
            super.onVoiceVolume(volume);
        }
    };
}
