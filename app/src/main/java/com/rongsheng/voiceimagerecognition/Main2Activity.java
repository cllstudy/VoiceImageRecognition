package com.rongsheng.voiceimagerecognition;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.idst.nls.NlsClient;
import com.alibaba.idst.nls.NlsListener;
import com.alibaba.idst.nls.internal.protocol.NlsRequest;
import com.alibaba.idst.nls.internal.protocol.NlsRequestProto;
import com.gyf.barlibrary.ImmersionBar;

import static com.rongsheng.voiceimagerecognition.Constant.KID;
import static com.rongsheng.voiceimagerecognition.Constant.KSECRET;

/**
 * @author lei
 * @desc 语音识别
 * @date 2018/3/26 0026 -- 上午 10:17.
 * 个人博客站: http://www.bestlei.top
 */
public class Main2Activity extends AppCompatActivity {

    private EditText mEtShibie;
    private Button mBtStatrtHecheng;
    private NlsClient mNlsClient;
    private NlsRequest mNlsRequest;
    private Context context;
    int iMinBufSize = AudioTrack.getMinBufferSize(16000,
            AudioFormat.CHANNEL_CONFIGURATION_MONO,
            AudioFormat.ENCODING_PCM_16BIT);
    AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 16000,
            AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT,
            iMinBufSize, AudioTrack.MODE_STREAM); //使用audioTrack播放返回的pcm数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImmersionBar.with(this).statusBarColor(R.color.title).init();
        initView();
        context = getApplicationContext();
        mNlsRequest = initNlsRequest();
        String appkey = "nls-service";     //请设置简介页面的Appkey
        mNlsRequest.setApp_key(appkey);    //appkey请从 简介页面的appkey列表中获取
        mNlsRequest.initTts();               //初始化tts请求
        NlsClient.openLog(true);
        NlsClient.configure(getApplicationContext()); //全局配置
        mNlsClient = NlsClient.newInstance(this, mRecognizeListener, null, mNlsRequest);                          //实例化NlsClient
        initTtsContentButton();
    }

    private void initView() {
        mEtShibie = (EditText) findViewById(R.id.et_shibie);
        mBtStatrtHecheng = (Button) findViewById(R.id.bt_statrt_hecheng);
        AppCompatTextView tvTitle = (AppCompatTextView) findViewById(R.id.tv_title);
        AppCompatImageView ivBack = (AppCompatImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(view -> finish());
        tvTitle.setText("语音合成");
    }

    private NlsRequest initNlsRequest() {
        NlsRequestProto proto = new NlsRequestProto(context);
        proto.setApp_user_id("陈"); //设置用户名
        return new NlsRequest(proto);
    }

    private void initTtsContentButton() {
        mBtStatrtHecheng.setOnClickListener(view -> {
            String user_input = mEtShibie.getText().toString();
            if (user_input.equals("")) {
                Toast.makeText(Main2Activity.this, "输入不能为空！", Toast.LENGTH_LONG).show();
            } else {
                mNlsRequest.setTtsEncodeType("pcm"); //返回语音数据格式，支持pcm,wav.alaw
                mNlsRequest.setTtsVolume(50);   //音量大小默认50，阈值0-100
                mNlsRequest.setTtsSpeechRate(0);//语速，阈值-500~500
                mNlsClient.PostTtsRequest(user_input); //用户输入文本
                mNlsRequest.authorize(KID, KSECRET);       //请替换为用户申请到的数加认证key和密钥
                audioTrack.play();
            }
        });
    }

    private NlsListener mRecognizeListener = new NlsListener() {
        @Override
        public void onTtsResult(int status, byte[] ttsResult) {
            switch (status) {
                case NlsClient.ErrorCode.TTS_BEGIN:
                    audioTrack.play();
                    audioTrack.write(ttsResult, 0, ttsResult.length);
                    break;
                case NlsClient.ErrorCode.TTS_TRANSFERRING:
                    audioTrack.write(ttsResult, 0, ttsResult.length);
                    break;
                case NlsClient.ErrorCode.TTS_OVER:
                    audioTrack.stop();
                    break;
                case NlsClient.ErrorCode.CONNECT_ERROR:
                    Toast.makeText(Main2Activity.this, "连接错误", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        audioTrack.release();
        super.onDestroy();
    }


}
