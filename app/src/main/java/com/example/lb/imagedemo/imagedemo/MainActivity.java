package com.example.lb.imagedemo.imagedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btnCamera;//相机
    private Button btnPhoto;//相册
    private ImageView img; //显示压缩处理过后的图片
    private FileUtil fileUtil;//文件操作类

    private String mCurrentPath="";//当前的未被压缩图片的路径
    private String mPath="";//当前已经被压缩了图片的路径


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        fileUtil = new FileUtil(this);
    }

    private void initUI() {
        btnCamera = (Button) findViewById(R.id.button1);
        btnPhoto = (Button) findViewById(R.id.button2);
        img = (ImageView) findViewById(R.id.img);
        btnCamera.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            //照片的路径
            mCurrentPath=fileUtil.getImgpath(String.valueOf(System.currentTimeMillis()));
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCurrentPath)));
            startActivityForResult(intent,1);
        }
        if (view.getId() == R.id.button2) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent,2);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(data!=null){
                    getPath(1,null);
                }
                break;
            case 2:
                if(data!=null) {
                    getPath(2,data.getData());
                }
                break;
            default:
                break;
        }
    }
    private void getPath(final int type,final Uri uri){
        //因为压缩是一个耗时的过程，所以采用异步的方式
        new Thread((new Runnable() {
            @Override
            public void run() {
                if(type==1){
                    mPath=ImageUtil.getSmallBitmap(mCurrentPath);
                    Log.i("ii","mPath1:"+mPath);
                }else if(type==2){
                    mPath =ImageUtil.getSmallBitmap(ImageUtil.getPhotoPath(MainActivity.this,uri));
                    Log.i("ii","mPath2:"+mPath);
                }
                Log.i("ii","mPath3:"+mPath);
                handler.sendEmptyMessage(0);
            }
        })).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mPath!=null){
               img.setImageBitmap(BitmapFactory.decodeFile(mPath));
            }
        }
    };
}
