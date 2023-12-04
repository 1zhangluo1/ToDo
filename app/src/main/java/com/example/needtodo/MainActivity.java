package com.example.needtodo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

public class MainActivity extends BaseActivity implements LifecycleObserver {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private TextView user_name;
    private TextView user_account;
    private TextView sign;
    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager2;
    private FloatingActionButton add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        adapterToolbar();//将顶部导航栏颜色设为和toolbar一致，看起来更加协调
        initNavMenu();//设置侧滑菜单的点击事件
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
        if (user_information.getSign() != null) {
            sign.setText(user_information.getSign().toString());
        }
        user_name.setText(user_information.getName().toString());
        user_account.setText(user_information.getAccount().toString());
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
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
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
}