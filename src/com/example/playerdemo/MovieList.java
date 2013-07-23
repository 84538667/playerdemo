package com.example.playerdemo;

import java.io.File;

import java.util.ArrayList;

import java.util.HashMap;



import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import android.os.Environment;

import android.util.Log;

import android.view.Menu;

import android.view.MenuItem;

import android.view.View;

import android.widget.AdapterView;

import android.widget.Button;

import android.widget.ListView;

import android.widget.SimpleAdapter;



public class MovieList extends Activity{

    final static int UPDATE = Menu.FIRST;
    final static int QUIT   = Menu.FIRST+1;
    private ListView lv;
    ArrayList name;
    String path = Environment.getExternalStorageDirectory().getPath();//+"/movie.mp4";
    String file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        setTitle(path);

        lv = (ListView) findViewById(R.id.lv);

        name = new ArrayList();

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();// 获得SD卡路径
            // File path = new File("/mnt/sdcard/");
            File[] files = path.listFiles();// 读取
            getFileName(files);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, name, R.layout.sd_list,
                new String[] { "视频名称" }, new int[] { R.id.mp4 });
        lv.setAdapter(adapter);

        for (int i = 0; i < name.size(); i++) {
            Log.i("zeng", "list.  name:  " + name.get(i));
        }

        lv.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                //setTitle(lv.getItemAtPosition(position).toString()+":"+String.valueOf(lv.getItemIdAtPosition(position)));
                String str = lv.getItemAtPosition(position).toString();
                file = str.substring("{视频名称=".length(), str.lastIndexOf("}"));
                String moviename = path+"/"+file;
                Log.i("moviename",moviename);
                //setTitle(moviename+":"+lv.getItemAtPosition(position).toString());
                Intent intent = new Intent(MovieList.this,Myvideoplayer.class);
                intent.putExtra("moviename", moviename);
                startActivity(intent);

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        int i = Menu.FIRST;
        menu.add(i, UPDATE, i, "更新列表");
        menu.add(i, QUIT, i, "退出");
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case UPDATE:
                File path = Environment.getExternalStorageDirectory();// 获得SD卡路径
                File[] files = path.listFiles();// 读取
                getFileName(files);                   //更新列表
                break;

            case QUIT:            //退出
                this.finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void getFileName(File[] files) {   //仅搜索当前目录下的文件
        if (files != null) {// 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                /* if (file.isDirectory()) {
                    Log.i("zeng", "若是文件目录。继续读1" + file.getName().toString()
                            + file.getPath().toString());
                    getFileName(file.listFiles());
                    Log.i("zeng", "若是文件目录。继续读2" + file.getName().toString()
                            + file.getPath().toString());
                } else {
                   */
                String fileName = file.getName();

                if (fileName.endsWith(".mp4")||fileName.endsWith(".3gp")) {

                    HashMap map = new HashMap();

                    String s = fileName.substring(0,
                            fileName.lastIndexOf(".")).toString();

                    Log.i("zeng", "文件名mp4或3gp：：   " + s);

                    map.put("视频名称", fileName);

                    name.add(map);

                }

            }

        }

        //}

    }



}