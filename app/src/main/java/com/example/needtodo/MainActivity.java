package com.example.needtodo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements LifecycleObserver {

    private DrawerLayout mDrawerLayout;
    private List<ToDoList> toDoListList = new ArrayList<>();
    private ToDoListAdapter toDoListAdapter;
    private ImageView setTime;
    private ImageView outDate;
    private NavigationView navigationView;
    private ImageView refresh;
    private ImageView doneList;
    private TextView user_name;
    private TextView user_account;
    private TextView sign;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initNavMenu();
        initSetTime();
        initOutDate();
        initDoneList();
        initToDoLists();
        initRefresh();
        recyclerView = (RecyclerView) findViewById(R.id.todo_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ToDoListAdapter adapter = new ToDoListAdapter(toDoListList);
        recyclerView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    private void initToDoLists() {
        toDoListList.clear();
        User this_account = LitePal.select("id").where("online = ?", String.valueOf(1)).findFirst(User.class);
        long user_id = this_account.getId();
        List<ThingsList> things = LitePal.where("user_id = ? and isDone = ? and isOutDate = ?",String.valueOf(user_id),String.valueOf(0),String.valueOf(0)).find(ThingsList.class);
        for (ThingsList contents : things) {
            ToDoList add = new ToDoList(contents.getHeadline(), R.drawable.ic_todo, contents.getDeadline(), contents.getId());
            toDoListList.add(add);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = getIntent();
        String thisAccount = intent.getStringExtra("this_account");
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else if (item.getItemId() == R.id.backHome) {
            exit();
        } else if (item.getItemId() == R.id.add) {
            intent = new Intent(MainActivity.this, AddThingList.class);
            intent.putExtra("this_account", thisAccount);
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
                User backLogin= LitePal.where("online = ?",String.valueOf(1)).findFirst(User.class);
                backLogin.setToDefault("online");
                backLogin.updateAll("online = ?",String.valueOf(1));
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
                Intent intent = new Intent(MainActivity.this,Introduce.class);
                startActivity(intent);
            }
            return false;
        });
    }

    private void initSetTime() {
        setTime = (ImageView) findViewById(R.id.set_time);
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetTime.class);
                startActivity(intent);
            }
        });
    }

    private void initOutDate() {
        outDate = findViewById(R.id.enter_outDate);
        outDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OutDateList.class);
                startActivity(intent);
            }
        });
    }

    private void initDoneList() {
        doneList = findViewById(R.id.had_done);
        doneList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DoneList.class);
                startActivity(intent);
            }
        });
    }

    private void initRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshThings();
            }
        });
    }
    private void refreshThings() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initToDoLists();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
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
        }
    }
    public void changeName() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        User user_information = LitePal.where("online = ?", String.valueOf(1)).findFirst(User.class);
        TextView user_name = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        user_name.setText(user_information.getName().toString());
    }
}