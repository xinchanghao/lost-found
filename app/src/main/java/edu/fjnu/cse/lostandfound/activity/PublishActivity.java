package edu.fjnu.cse.lostandfound.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import edu.fjnu.cse.lostandfound.R;
import edu.fjnu.cse.lostandfound.entities.API_Post;
import edu.fjnu.cse.lostandfound.entities.API_Post_Ret;
import edu.fjnu.cse.lostandfound.entities.API_Return;
import edu.fjnu.cse.lostandfound.net.api;
import edu.fjnu.cse.lostandfound.tools.BitmapUtil;
import edu.fjnu.cse.lostandfound.view.TimePickerDialog;

import static android.util.Base64.NO_WRAP;

public class PublishActivity extends BaseActivity implements TimePickerDialog.TimePickerDialogInterface {
    EditText wpost_etime;
    EditText wpost_etitle, wpost_ePlace, wpost_eDetail;
    ImageView wpost_bq;
    TimePickerDialog timePickerDialog;
    Spinner spinner_nav;
    private ImageView ivPic;
    RelativeLayout relPic;
    Bitmap mBitmap;
    AppContext appContext;
    private int SELECT_PICTURE = 1; // 从图库中选择图片
    private int SELECT_CAMER = 2; // 用相机拍摄照片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        appContext = (AppContext) getApplication();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findView();
    }

    private void findView() {
        wpost_etime = (EditText) findViewById(R.id.wpost_etime);
        wpost_etitle = (EditText) findViewById(R.id.wpost_etitle);
        wpost_ePlace = (EditText) findViewById(R.id.wpost_ePlace);
        wpost_eDetail = (EditText) findViewById(R.id.wpost_eDetail);
        ivPic = (ImageView) findViewById(R.id.wpost_img);
        relPic = (RelativeLayout) findViewById(R.id.wpost_imglayout);
        wpost_bq = (ImageView) findViewById(R.id.wpost_bq);
        wpost_bq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetHeadimg();
            }
        });
        timePickerDialog = new TimePickerDialog(PublishActivity.this);
        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
        wpost_etime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    timePickerDialog.showDatePickerDialog();
                }
            }
        });

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void showSetHeadimg() {
        final PopupWindow popupWindow = new PopupWindow(this);
        View v = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.dialog_changetx, null);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                android.R.color.transparent));
        popupWindow.setContentView(v);
        popupWindow.setAnimationStyle(R.style.AnimationPreview);
        popupWindow.showAtLocation(new View(this), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.35f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        v.findViewById(R.id.tx_camera).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        // 进入相机
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                                getExternalCacheDir(), "edtimg.jpg")));
                        startActivityForResult(intent, SELECT_CAMER);
                    }
                });
        v.findViewById(R.id.tx_photo).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                // 进入图库
                Intent intent = new Intent(
                        Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });
        v.findViewById(R.id.tx_cancle).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            relPic.setVisibility(View.VISIBLE);
            ivPic.setImageBitmap(bitmap);
        }
    };

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    Bitmap bitmap = null;
                    if (requestCode == SELECT_CAMER) {
                        bitmap = BitmapFactory.decodeFile(getExternalCacheDir() + "/edtimg.jpg");
                    } else if (requestCode == SELECT_PICTURE) {
                        Uri uri = data.getData();
                        ContentResolver cr = PublishActivity.this.getContentResolver();
                        try {
                            bitmap = BitmapFactory
                                    .decodeStream(cr.openInputStream(uri));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    Message msg = new Message();
                    msg.obj = ThumbnailUtils.extractThumbnail(bitmap, 300, 300);
                    handler.sendMessage(msg);


                    BitmapUtil.comp(bitmap);
                    mBitmap = bitmap;
//						Message msg2=new Message();
//						msg2.obj=bitmap;
//						handler.sendMessage(msg2);
                }
            });

            thread.start();

        } else {
            Toast.makeText(this, "选择图片失败,请重新选择", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_publish, menu);
        return true;
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.publish_send) {
            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(wpost_etitle.getText().toString())) {
                wpost_etitle.setError("请输入物品标签");
                focusView = wpost_etitle;
                cancel = true;
            } else if (TextUtils.isEmpty(wpost_etime.getText().toString())) {
                wpost_etime.setError("请输入捡到或丢失物品的时间");
                focusView = wpost_etime;
                cancel = true;
            } else if (TextUtils.isEmpty(wpost_ePlace.getText().toString())) {
                wpost_ePlace.setError("请输入捡到或丢失物品的地点");
                focusView = wpost_ePlace;
                cancel = true;
            } else if (TextUtils.isEmpty(wpost_eDetail.getText().toString())) {
                wpost_eDetail.setError("请输入详情，例如物品描述，联系方式等。");
                focusView = wpost_eDetail;
                cancel = true;
            }
            if (cancel) {
                focusView.requestFocus();
                return false;
            }
            String[] img;
            if (mBitmap != null) {
                img = new String[1];
                img[0] = Base64.encodeToString(Bitmap2Bytes(mBitmap), NO_WRAP);
            } else {
                img = new String[0];
            }
            api.Request(new API_Post(appContext.getName(), spinner_nav.getSelectedItemPosition(), wpost_etitle.getText().toString(), wpost_etime.getText().toString(), wpost_ePlace.getText().toString(), wpost_eDetail.getText().toString(), img), new API_Return<API_Post_Ret>() {
                @Override
                public void ret(int Code, API_Post_Ret ret) {
                    if (Code == 0) {
                        finish();
                    } else {
                        System.out.println("error:" + Code);
                    }
                }
            }, PublishActivity.this);
            //System.exit(0);
//            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void positiveListener() {
        wpost_etime.setText(timePickerDialog.getYear() + "-" + timePickerDialog.getMonth() + "-" + timePickerDialog.getDay());
    }

    @Override
    public void negativeListener() {

    }
}
