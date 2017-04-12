package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.AuthenticationData;
import com.gs.buluo.store.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.store.camera.CameraActivity;
import com.gs.buluo.store.network.TribeUploader;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2017/1/12.
 */
public class Authentication2Activity extends BaseActivity {
    private String front;
    private String back;
    @Bind(R.id.identify_back_image)
    ImageView backImg;
    @Bind(R.id.identify_front_image)
    ImageView frontImg;
    private AuthenticationData data;
    private String mPhotoPath;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        data = getIntent().getParcelableExtra(Constant.AUTH);

        findViewById(R.id.ll_identify_front).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePhoto(true);
            }
        });
        findViewById(R.id.ll_identify_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePhoto(false);
            }
        });
        findViewById(R.id.qualification_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (front==null||back==null)return;
                List<String> list=new ArrayList<>();
                list.add(front);
                list.add(back);
                data.idCardPicture = list;
                Intent intent = new Intent(getCtx(),Authentication3Activity.class);
                intent.putExtra(Constant.AUTH,data);
                startActivity(intent);
            }
        });
        findView(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showChoosePhoto(final boolean isFront) {
        Intent intent = new Intent(Authentication2Activity.this, CameraActivity.class);
        if (isFront){
            startActivityForResult(intent, 100);
        }else{
            startActivityForResult(intent, 101);
        }
//        FunctionConfig functionConfig = new FunctionConfig.Builder()
//                .setEnableEdit(true)
//                .setEnableCamera(true)
//                .setEnableRotate(true)
//                .setEnablePreview(true)
//                .build();
//        ChoosePhotoPanel panel=new ChoosePhotoPanel(this, functionConfig, new ChoosePhotoPanel.OnSelectedFinished() {
//            @Override
//            public void onSelected(String string) {
//                showLoadingDialog();
//                uploadPic(string,isFront);
//            }
//        });
//        panel.show();
    }

    private void uploadPic(String pic, final boolean isFront) {
        TribeUploader.getInstance().uploadFile("id", "", pic, new TribeUploader.UploadCallback() {
            @Override
            public void uploadSuccess(UploadAccessResponse.UploadResponseBody data) {
                dismissDialog();
                if (isFront){
                    frontImg.setVisibility(View.VISIBLE);
                    front = data.objectKey;
                    Glide.with(getCtx()).load(GlideUtils.formatImageUrl(data.objectKey)).centerCrop().into(frontImg);
                    findViewById(R.id.identify_front).setVisibility(View.GONE);
                }else {
                    backImg.setVisibility(View.VISIBLE);
                    back=data.objectKey;
                    findViewById(R.id.identify_back).setVisibility(View.GONE);
                    Glide.with(getCtx()).load(GlideUtils.formatImageUrl(data.objectKey)).centerCrop().into(backImg);
                }
            }

            @Override
            public void uploadFail() {
                dismissDialog();
                ToastUtils.ToastMessage(getCtx(),R.string.upload_fail);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("TAG","onActivityResult");
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String path=extras.getString("path");
                String type=extras.getString("type");
                Toast.makeText(getApplicationContext(),"path:"+ path + " type:" + type, Toast.LENGTH_LONG).show();
                front =path;
                File file = new File(path);
                FileInputStream inStream = null;
                try {
                    inStream = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                    frontImg.setVisibility(View.VISIBLE);
                    frontImg.setImageBitmap(bitmap);
                    inStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String path=extras.getString("path");
                String type=extras.getString("type");
                Toast.makeText(getApplicationContext(),"path:"+ path + " type:" + type, Toast.LENGTH_LONG).show();
                back =path;
                File file = new File(path);
                FileInputStream inStream = null;
                try {
                    inStream = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                    backImg.setVisibility(View.VISIBLE);
                    backImg.setImageBitmap(bitmap);
                    inStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_qualification;
    }
}
