package com.example.zth.arcone;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Created by ZTH on 2018/3/5.
 */

public class CameraUtil {

    private Camera mCamera;

    private static final String TAG = "CameraUtil";
    private SurfaceHolder surfaceHolder;
    private AutoFocusCallback autoFocusCallback;
    private int width = 0, height = 0;
    private boolean getSize = false;
    public CameraUtil(SurfaceHolder surfaceHolder,AutoFocusCallback autoFocusCallback){
        this.surfaceHolder = surfaceHolder;
        this.autoFocusCallback = autoFocusCallback;

    };

    public void onResume(){
        if (null!=mCamera){
            mCamera = getCameraInstance();
            setParameters();
            try {
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();
            } catch(IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }
    }

    public void onCreate(){
        mCamera = getCameraInstance();
        setParameters();
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch(IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void onPause(){
        if(mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void onDestroy(SurfaceHolder.Callback callback){
        surfaceHolder.removeCallback(callback);
        if(mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

    }

    public android.hardware.Camera getCameraInstance(){
        android.hardware.Camera c = null;
        try {
            c = android.hardware.Camera.open();
        } catch(Exception e){
            Log.d("TAG", "camera is not available");
        }
        return c;
    }

    // 获取当前窗口管理器显示方向
    public int getDisplayOrientation(Activity activity){
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation){
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        android.hardware.Camera.CameraInfo camInfo =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);

        // 这里其实还是不太懂：为什么要获取camInfo的方向呢？相当于相机标定？？
        int result = (camInfo.orientation - degrees + 360) % 360;

        return result;
    }


    // 刷新相机
    public void refreshCamera(){
        if (surfaceHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch(Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings





        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCallback);
        } catch (Exception e) {

        }
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setDisplayOrientation(Activity activity){
        int rotation = getDisplayOrientation(activity); //获取当前窗口方向
        mCamera.setDisplayOrientation(rotation); //设定相机显示方向
    }



    public void autoFocus(){
        mCamera.autoFocus(autoFocusCallback);
    }

    public void takePicture(Camera.ShutterCallback shutter, Camera.PictureCallback raw,
                            Camera.PictureCallback postview){
        mCamera.takePicture(shutter,raw,postview);
    }

    public void setParameters(){
        if(!getSize ){
            getSize = true;
            for(Camera.Size size :mCamera.getParameters().getSupportedPictureSizes()){

                if(width < size.width){
                    width = size.width;
                    height = size.height;
                }
            }
            Log.v("zzwzzw",width+" "+height);
            Camera.Parameters mParameters = mCamera.getParameters();
            mParameters.setPictureSize(width,height);
            mCamera.setParameters(mParameters);
        }
    }

    public void resumeFocus( ){

        //mCamera = camera;
        if(mCamera != null){
       //     mCamera.cancelAutoFocus(); //这一句很关键
            refreshCamera();
        }

    }

}
