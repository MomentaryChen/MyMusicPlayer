package com.example.momentary.mymusicplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private Button bplay,bpause,bstop;
    private SeekBar seekBar ;
    private TextView song_time;
    private MediaPlayer mp=new MediaPlayer();
    int now_sencond = 0;
    private Timer mTimer = new Timer();
    private String now_time,end_time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bplay = (Button) findViewById(R.id.play);
        bpause = (Button) findViewById(R.id.pause);
        bstop = (Button) findViewById(R.id.stop);
        seekBar = (SeekBar) findViewById(R.id.player_seek);
        song_time = (TextView) findViewById(R.id.song_time);
        mp=MediaPlayer.create(this, R.raw.raw);
        //歌曲時間長度顯示
        int t = mp.getDuration()/1000;
        end_time= String.format("%02d:%02d",(t/ 60),t % 60);
//        mp.prepareAsync();
        seekBar.setOnSeekBarChangeListener(new MySeekbar());
        seekBar.setMax( mp.getDuration());
//        mp=MediaPlayer.create(this, Uri.parse("http://www.youtube.com/watch?v=SgGhtjKWLOE&feature=feedrec"));
        bplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mp!= null) {
                        mp.stop();
                    }
                    mp.prepare();
                    mp.start();


                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        bpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    bpause.setText("PAUSE");
                    now_sencond = mp.getCurrentPosition();
                    mp.pause();
                }else{
                    bpause.setText("RESUME");
                    mp.seekTo(now_sencond);
                    mp.start();
                }
            }
        });
        bstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    mp.stop();
                }
            }
        });
        final TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (mp!= null && mp.isPlaying() == true) {
                        Message message = new Message();
                        message.what = 0;
                        mHandler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(){
            @Override
            public void run(){
                mTimer.schedule(timertask,100,100);
            }
        }.run();
    }
    @Override
    protected void onDestroy() {
        if(mp != null)
            mp.stop();
            mp.release();
            mp=null;
        super.onDestroy();
    }

    class MySeekbar implements SeekBar.OnSeekBarChangeListener {
        //当进度条变化时触发
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }
        //开始拖拽进度条
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        //停止拖拽进度条
        public void onStopTrackingTouch(SeekBar seekBar) {
            if(mp!= null && mp.isPlaying()){
                mp.seekTo(seekBar.getProgress());
                mp.start();
            }

        }
        private void updateTime(TextView textView,int millisecond){
            int second = millisecond/1000;
            int mm = second % 3600 / 60;
            int ss = second % 60;
            String str = null;
            str = String.format("%02d:%02d",mm,ss);
            textView.setText(str);
        }
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    seekBar.setProgress(mp.getCurrentPosition());
                    int t = mp.getCurrentPosition() /1000 ;
                    now_time = String.format("%02d:%02d",(t/ 60),t % 60);
                    song_time.setText(now_time+"/"+end_time);
                    break;
                default:
                    break;
            }
        }
    };
}
