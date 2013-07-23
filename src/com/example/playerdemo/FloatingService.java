package com.example.playerdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class FloatingService extends Service {

    WindowManager wm = null;
    WindowManager.LayoutParams wmParams = null;
    View view;
    final static int TIME = 2000;   //2000ms = 2s，快进和快退的步进都是2秒
    ImageButton play = null, pause = null, quit = null;
    ImageButton forward = null, backward = null;
    int position,max;
    SeekBar progress;
    String TAG = "android.intent.action.myvideoplayer";  //自定义的广播标签
    @Override

    public void onCreate() {
        Log.d("FloatingService", "onCreate");
        super.onCreate();
        view = LayoutInflater.from(this).inflate(R.layout.window, null);
        play  = (ImageButton)view.findViewById(R.id.play);
        pause = (ImageButton)view.findViewById(R.id.pause);
        quit  = (ImageButton)view.findViewById(R.id.quitthis);
        progress = (SeekBar)view.findViewById(R.id.progress);
        forward  = (ImageButton)view.findViewById(R.id.forward);
        backward  = (ImageButton)view.findViewById(R.id.backward);

        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setAction(TAG);
                intent.putExtra("flag", "change");    //改变进度条
                intent.putExtra("newpos", position);
                FloatingService.this.sendBroadcast(intent);   //发送广播
                view.setVisibility(View.GONE);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                if(fromUser)
                    position = progress;

            }
        });

        forward.setOnClickListener(new Button.OnClickListener(){



            @Override

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setAction(TAG);
                if(position<=max-TIME)
                    position+= TIME;
                progress.setProgress(position);
                intent.putExtra("flag", "forward");    //快进
                intent.putExtra("newpos", position);
                FloatingService.this.sendBroadcast(intent);   //发送广播
                //view.setVisibility(View.GONE);

            }

        });

        backward.setOnClickListener(new Button.OnClickListener(){



            @Override

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setAction(TAG);
                if(position>=TIME)
                    position-= TIME;
                progress.setProgress(position);
                intent.putExtra("flag", "backward");    //快退
                intent.putExtra("newpos", position);
                FloatingService.this.sendBroadcast(intent);   //发送广播
                //view.setVisibility(View.GONE);

            }

        });

        play.setOnClickListener(new Button.OnClickListener(){



            @Override

            public void onClick(View v) {

                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setAction(TAG);
                intent.putExtra("flag", "play");    //播放
                FloatingService.this.sendBroadcast(intent);   //发送广播
                view.setVisibility(View.GONE);

            }



        });

        pause.setOnClickListener(new Button.OnClickListener(){



            @Override

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setAction(TAG);
                intent.putExtra("flag", "pause");    //暂停
                FloatingService.this.sendBroadcast(intent);   //发送广播
                view.setVisibility(View.GONE);

            }



        });

        quit.setOnClickListener(new Button.OnClickListener(){



            @Override

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setAction(TAG);
                intent.putExtra("flag", "quit");    //退出
                FloatingService.this.sendBroadcast(intent);   //发送广播
                view.setVisibility(View.GONE);

            }



        });

        createView();

    }



    private void createView() {
        // 获取WindowManager
        wm = (WindowManager) getApplicationContext().getSystemService("window");
        // 设置LayoutParams(全局变量）相关参数
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;//2002;
        wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//8;
        wmParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;
        // 设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.format = 1;

        wm.addView(view, wmParams);
        view.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // 获取相对屏幕的坐标，即以屏幕左上角为原点
                view.setVisibility(View.GONE);
                return true;
            }

        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        position = intent.getIntExtra("position", 0);
        max = intent.getIntExtra("max", 0);
        boolean visable = intent.getBooleanExtra("visable", false);
        progress.setMax(max);
        progress.setProgress(position);
        if(visable)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("FloatingService", "onDestroy");
        wm.removeView(view);
        super.onDestroy();

    }

    @Override

    public IBinder onBind(Intent intent) {
        return null;

    }

}