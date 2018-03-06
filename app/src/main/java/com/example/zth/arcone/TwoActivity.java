package com.example.zth.arcone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;

/**
 * Created by chris on 2017/6/25.
 * 网上找了一些博客、教程和代码，稍微有点头绪了，现在写自己的Activity代码
 */

public class TwoActivity extends Activity implements SurfaceHolder.Callback, Camera.PictureCallback, Camera.ShutterCallback{
    private static final String TAG = "ChrisAcvitity";
    private SurfaceHolder mHolder;
    private SurfaceView mView;
    public static final int MSG_AUTOFUCS = 1001;
    public static final int MSG_BITMAP = 1002;
    private Bitmap bitmap;
    private Handler handler;
    AutoFocusCallback autoFocusCallback;

    private Button btn;

    private CameraUtil cameraUtil;

    private ImageDialogFragment imageDialogFragment;
    private ProgressDialog mypDialog;
    @Override
    // 创建Activity时执行的动作
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        mView = (SurfaceView) findViewById(R.id.sv);
        mHolder = mView.getHolder();
        mHolder.addCallback(this);


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.v("zzw",""+msg.what);
                switch (msg.what){
                    case MSG_AUTOFUCS:
                        cameraUtil.autoFocus();
                        break;
                    case MSG_BITMAP:


                        mypDialog.dismiss();
                        String strDecode = msg.getData().getString("decode","扫描失败");

                        if(strDecode == null ||strDecode.equals(""))
                            strDecode = "扫描失败";

                        imageDialogFragment.setImage(bitmap);
                        imageDialogFragment.setText(strDecode);
                        imageDialogFragment.show(getFragmentManager(), "ImageDialogFragment");


                        break;
                }

            }
        };
        autoFocusCallback = new AutoFocusCallback();
        autoFocusCallback.setHandler(handler,MSG_AUTOFUCS);

        btn = (Button)findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                cameraUtil.takePicture(TwoActivity.this,TwoActivity.this,TwoActivity.this);

            }
        });

        cameraUtil = new CameraUtil(mHolder,autoFocusCallback);

        mypDialog=new ProgressDialog(TwoActivity.this);
        //实例化
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //设置ProgressDialog 标题
        mypDialog.setMessage("正在处理，请等待");
        mypDialog.setIndeterminate(false);
        //设置ProgressDialog 的进度条是否不明确

        imageDialogFragment = new ImageDialogFragment();
    }

    @Override
    // apk暂停时执行的动作：把相机关闭，避免占用导致其他应用无法使用相机
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
        cameraUtil.onPause();
    }

    @Override
    // 恢复apk时执行的动作
    protected void onResume() {
        super.onResume();
        cameraUtil.onResume();
    }


    // SurfaceHolder.Callback必须实现的方法
    public void surfaceCreated(SurfaceHolder holder){
        cameraUtil.onCreate();
    }

    // SurfaceHolder.Callback必须实现的方法
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        cameraUtil.refreshCamera(); // 这一步是否多余？在以后复杂的使用场景下，此步骤是必须的。

        cameraUtil.setDisplayOrientation(TwoActivity.this);

    }

    // SurfaceHolder.Callback必须实现的方法
    public void surfaceDestroyed(SurfaceHolder holder){

        cameraUtil.onDestroy(this);
        if(bitmap != null){
            bitmap.recycle();
        }
    }






    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        handler.removeCallbacksAndMessages(null);
        cameraUtil.resumeFocus();

        if(!mypDialog.isShowing())
        mypDialog.show();

        if(data != null){
            new Thread(new BitmapThread(bitmap,data,handler,TwoActivity.this)).start();

        }

    }

    @Override
    public void onShutter() {

    }

    public Bitmap getBitmag(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }




}