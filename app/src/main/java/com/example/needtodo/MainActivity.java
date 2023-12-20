package com.example.needtodo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import movableButton.MoveButton;
import tools.BitmapUtils;

public class MainActivity extends BaseActivity  {
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private TextView user_name;
    private TextView user_account;
    private TextView sign;
    private ShapeableImageView headImage;
    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager2;
    private FloatingActionButton add;
    private MoveButton chat;
    private Dialog dialog;
    private View inflate;
    private static final int TAKE_PHOTO_REQUEST = 1;
    public static final int CHOOSE_PHOTO_REQUEST = 2;
    private Uri imageUri;
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true);
    Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendNotification();
        EventBus.getDefault().register(this);
        adapterToolbar();//将顶部导航栏颜色设为和toolbar一致，看起来更加协调
        initNavMenu();//设置侧滑菜单的点击事件
        enterChat();
        viewPaperFragment();
        addThing();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else if (item.getItemId() == R.id.backHome) {
            exit();
        } else if (item.getItemId() == R.id.search) {
            Intent intent = new Intent(this, Search.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.change_view) {
            Intent intent = new Intent(this, SelectionModern.class);
            startActivity(intent);
        }
        return true;
    }


    private static boolean isExit = false;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    private void initNavMenu() {
        navigationView = findViewById(R.id.nav_view);
        User user_information = LitePal.where("online = ?", String.valueOf(1)).findFirst(User.class);
        user_account = navigationView.getHeaderView(0).findViewById(R.id.this_account);
        user_name = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        sign = navigationView.getHeaderView(0).findViewById(R.id.sign_content);
        headImage = navigationView.getHeaderView(0).findViewById(R.id.icon_image);
        headImage.setOnLongClickListener(v -> {
            showDialog();
            return false;
        });
        User header = LitePal.where("online = ?",String.valueOf(1)).findFirst(User.class);
        if(header.getHeadImage_File() == null){}
        else if(header.getHeadImage_File() != null){
            String image = load(header.getHeadImage_File());
            headImage.setImageBitmap(BitmapUtils.base64ToBitmap(image));
        }
        user_name.setText(user_information.getName().toString());
        user_account.setText(user_information.getAccount().toString());
        if (user_information.getSign() != null){
        sign.setText(user_information.getSign().toString());}
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.back_login) {
                User backLogin = LitePal.where("online = ?", String.valueOf(1)).findFirst(User.class);
                backLogin.setToDefault("online");
                backLogin.updateAll("online = ?", String.valueOf(1));
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "成功返回退出账号", Toast.LENGTH_SHORT).show();
                finish();
            } else if (item.getItemId() == R.id.change_pass) {
                Intent intent = new Intent(MainActivity.this, ChangePass.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.change_name) {
                Intent intent = new Intent(MainActivity.this, ChangeName.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.sign) {
                Intent intent = new Intent(MainActivity.this, EditSign.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.destroy_account) {
                Intent intent = new Intent(MainActivity.this, DestroyAccount.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.introduce) {
                Intent intent = new Intent(MainActivity.this, Introduce.class);
                startActivity(intent);
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent message) {
        Toast.makeText(this, message.name, Toast.LENGTH_SHORT).show();
        changeName();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignEvent(MessageSign sign) {
        Toast.makeText(this, sign.name, Toast.LENGTH_SHORT).show();
        changeSign();
    }


    public void changeSign() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        User user_information = LitePal.where("online = ?", String.valueOf(1)).findFirst(User.class);
        TextView sign = navigationView.getHeaderView(0).findViewById(R.id.sign_content);
        if (user_information.getSign() != null) {
            sign.setText(user_information.getSign().toString());
        } else if (user_information.getSign() == null) {
        }
    }

    public void changeName() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        User user_information = LitePal.where("online = ?", String.valueOf(1)).findFirst(User.class);
        TextView user_name = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        user_name.setText(user_information.getName().toString());
    }

    private void adapterToolbar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setFitsSystemWindows(false);
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#2CBAF8"));
        WindowInsetsControllerCompat c = WindowCompat.getInsetsController(window, window.getDecorView());
        c.setAppearanceLightStatusBars(false);
        c.setAppearanceLightNavigationBars(false);
    }

    private void viewPaperFragment() {
        viewPager2 = findViewById(R.id.viewpager2bottom);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        MyViewPaper2BottomAdapter myViewPaper2BottomAdapter = new MyViewPaper2BottomAdapter(this);
        viewPager2.setAdapter(myViewPaper2BottomAdapter);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId()==R.id.todo_view){
                viewPager2.setCurrentItem(0);
            }
            else if (item.getItemId()==R.id.enter_out){
                viewPager2.setCurrentItem(1);
            }
            else if (item.getItemId()==R.id.enter_done){
                viewPager2.setCurrentItem(2);
            }
            return true;
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position==0){
                    bottomNavigationView.setSelectedItemId(R.id.todo_view);
                }
                else if (position==1){
                    bottomNavigationView.setSelectedItemId(R.id.enter_out);
                }
                else if (position==2){
                    bottomNavigationView.setSelectedItemId(R.id.enter_done);
                }
            }
        });
    }
    private void addThing () {
        add = findViewById(R.id.fab);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddThingList.class);
            startActivity(intent);
        });
    }

    private void enterChat() {
        chat = findViewById(R.id.chat);
        chat.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatAi.class);
            startActivity(intent);
        });
    }

    private void setHeaderFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            doTake();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        }
    }
    private void setHeaderFromAlbum() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openAlbum();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doTake();
            } else {
                Toast.makeText(this, "无相机权限", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum();
            } else {
                Toast.makeText(this, "无访问相册权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void doTake() {
        File imageHeader = new File(getExternalCacheDir(),"imageOut.jpeg");
        if (imageHeader.exists()) {
            imageHeader.delete();
        }
        try {
            imageHeader.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT > 24) {
            imageUri = FileProvider.getUriForFile(this,"com.example.needtodo.MainActivity.fileprovider",imageHeader);
        } else {
            imageUri = Uri.fromFile(imageHeader);
        }
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO_REQUEST);
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    headImage.setImageBitmap(bitmap);
                    String imageToBase64 = BitmapUtils.bitmapToBase64(bitmap);
                    save(imageToBase64);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else{};
        } else if (requestCode == CHOOSE_PHOTO_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT < 19) {
                    handleImageBeforeApi19(data);
                } else {handleImageOnApi19(data);}
            }
        }
    }

    private void handleImageBeforeApi19(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private void handleImageOnApi19(@NonNull Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if (TextUtils.equals(uri.getAuthority(),"com.android.providers.media.documents")) {
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            } else if (TextUtils.equals(uri.getAuthority(),"com.android.providers.downloads.documents")) {
                if (documentId != null && documentId.startsWith("msf:")) {
                    resolveMSFContent(uri,documentId);
                    return;
                }
                Uri contenUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(documentId));
                getImagePath(contenUri,null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri,null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        Log.d("file_path",imagePath);
        displayImage(imagePath);
    }

    private void resolveMSFContent(Uri uri, String documentId) {
        File file = new File(getCacheDir(),"temp_file"+getContentResolver().getType(uri).split("/")[1]);
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4*1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer,0,read);
            }
            outputStream.flush();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            headImage.setImageBitmap(bitmap);
            String imageToBase64 = BitmapUtils.bitmapToBase64(bitmap);
            save(imageToBase64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    private String getImagePath (Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
                cursor.close();
            }
            return path;
    }
    private void displayImage(String imagePath) {
        Log.d("below_path",imagePath);
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            headImage.setImageBitmap(bitmap);
            String imageToBase64 = BitmapUtils.bitmapToBase64(bitmap);
            save(imageToBase64);
        }
    }

    public void showDialog(){
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_for_header, null);
        //初始化控件
        TextView choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
        TextView takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
        choosePhoto.setOnClickListener(v -> {setHeaderFromAlbum();
        dialog.cancel();});
        takePhoto.setOnClickListener(v -> {setHeaderFromCamera();
        dialog.cancel();});
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        dialog.show();
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 1350;
        params.height = 400;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    private void save(String inputText) {
        User name_file = LitePal.where("online = ?",String.valueOf(1)).findFirst(User.class);
        if(name_file.getHeadImage_File() == null){
            name_file.setHeadImage_File(String.valueOf(name_file.getId()));
            name_file.save();
        }
        else if(name_file.getHeadImage_File() != null){
            name_file.setHeadImage_File(String.valueOf(name_file.getId()));
            name_file.updateAll("online = ?",String.valueOf(1));
        }
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput(name_file.getHeadImage_File(), Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer !=null) {
                    writer.close();
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String load (String name) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput(name);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine())!=null) {
                content.append(line);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    private void sendNotification() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Log.d("TIMETIME",simpleDateFormat.format(date));
        User this_account = LitePal.where("online = ?", String.valueOf(1)).findFirst(User.class);
        long this_id = this_account.getId();
        List<ThingsList> notification = LitePal.where("deadline = ? and user_id = ? and isDone = ?",simpleDateFormat.format(date),String.valueOf(this_id),String.valueOf(0)).find(ThingsList.class);
        int i = 1, j = 1;
        for (ThingsList onThisDay : notification) {
            DeadSoon(onThisDay, i);
            i++;
        }
        List<ThingsList> closeDead = LitePal.where("isDone = ? and deadline > ? and user_id = ?", String.valueOf(0), simpleDateFormat.format(date), String.valueOf(this_id)).find(ThingsList.class);

        if (closeDead != null) {
            List<String> times = new ArrayList<>();
            for (ThingsList time : closeDead) {
                times.add(time.getDeadline());
            }
            if (!times.isEmpty()) {
                String minDate = Collections.min(times);
                List<ThingsList> soonNotice = LitePal.where("deadline = ?", minDate).find(ThingsList.class);
                Log.d("tag",soonNotice.toString());
                for (ThingsList notifyClose : soonNotice) {
                    Notification(notifyClose, i, j);
                    j++;
                }
            }
        }
    }

    private void DeadSoon(ThingsList thingsList, int i) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }
        Notification notification1 = new NotificationCompat.Builder(com.example.needtodo.MainActivity.this, "default")
                .setContentTitle(thingsList.getHeadline() + " 即将截止")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(thingsList.getThings()))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logincenter)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.nav_logo1))
                .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                .setVibrate(new long[]{0, 1000, 1000, 1000})
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
        manager.notify(i, notification1);
    }
    private void Notification(ThingsList thingsList, int i, int j) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }
        Notification notification1 = new NotificationCompat.Builder(com.example.needtodo.MainActivity.this, "default")
                .setContentTitle(thingsList.getHeadline() + "的截止时间快到了")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(thingsList.getThings()))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logincenter)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.nav_logo1))
                .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                .setVibrate(new long[]{0, 1000, 1000, 1000})
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
        manager.notify(i+j, notification1);
    }

}