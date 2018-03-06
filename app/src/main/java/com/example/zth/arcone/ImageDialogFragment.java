package com.example.zth.arcone;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ZTH on 2018/3/5.
 */

public class ImageDialogFragment extends DialogFragment
{

    private ImageView iv;
    private TextView tv;

    private Bitmap bitmap;
    private String s;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_image_dialog, container,
                false);

        iv = (ImageView)view.findViewById(R.id.iv);
        tv = (TextView)view.findViewById(R.id.tv);

        iv.setImageBitmap(bitmap);
        tv.setText(s);

        return view;


    }

    public void setImage(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public void setText(String s){
        this.s = s;
    }




}