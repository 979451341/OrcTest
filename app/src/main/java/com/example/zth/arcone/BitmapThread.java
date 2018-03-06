package com.example.zth.arcone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.zth.arcone.tess.TessEngine;


/**
 * Created by ZTH on 2018/3/6.
 */

public class BitmapThread  implements Runnable {

    private Bitmap bitmap;
    private byte[] data;
    private Handler handler;
    private TwoActivity activity;

    public BitmapThread(Bitmap mBitmap,byte[] data,Handler handler,TwoActivity activity) {
        this.bitmap = mBitmap;
        this.data = data;
        this.handler = handler;
        this.activity = activity;
    }

    @Override
    public void run() {
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        bitmap = rotateBitmap(bitmap,90);

        int PMwidth = bitmap.getWidth(); // 得到图片的宽，高
        int PMheight = bitmap.getHeight();

        int left = PMwidth/10;
        int top = PMheight/3;
        int right = PMwidth*9/10;
        int bottom = PMheight*4/9;
        int width = right - left;
        int height = bottom - top;

        Log.v("zzw",PMheight+" "+PMwidth);


        bitmap = Bitmap.createBitmap(bitmap, left, top, width, height, null,
                false);

        activity.setBitmap(bitmap);
        String strDecode =  TessEngine.Generate(MyApplication.sAppContext).detectText(bitmap);

        Message message = Message.obtain();
        message.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString("decode",strDecode);
        message.setData(bundle);
        message.what = TwoActivity.MSG_BITMAP;
        handler.sendMessage(message);

    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
