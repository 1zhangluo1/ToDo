package com.example.needtodo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class OutList extends Fragment {
    private List<ToDoList> OutList = new ArrayList<>();
    private OutDateAdapter outDateAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private View view;
    private ImageView imageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_out_list, container, false);
        imageView = this.view.findViewById(R.id.out_empty_background);
        EventBus.getDefault().register(this);
        initOutLists();
        createList();
        initRefresh();
        return this.view;
    }

    private void createList() {
        RecyclerView recyclerView = (RecyclerView) this.view.findViewById(R.id.out_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DoneAdapter adapter = new DoneAdapter(OutList);
        recyclerView.setAdapter(adapter);
        User this_account = LitePal.select("id").where("online = ?", String.valueOf(1)).findFirst(User.class);
        long user_id = this_account.getId();
        ThingsList thingsList = LitePal.where("user_id = ? and isDone = ? and isOutDate = ?", String.valueOf(user_id), String.valueOf(0), String.valueOf(1)).findFirst(ThingsList.class);
        if (thingsList == null) {
            imageView.setImageResource(R.drawable.empty_data);
        }
    }

    private void initOutLists() {
        OutList.clear();
        User this_account = LitePal.select("id").where("online = ?", String.valueOf(1)).findFirst(User.class);
        long user_id = this_account.getId();
        List<ThingsList> things = LitePal.where("isOutDate = ? and user_id = ? and isDone = ? ", String.valueOf(1), String.valueOf(user_id),String.valueOf(0)).find(ThingsList.class);
        if (things != null) {
            for (ThingsList contents : things) {
                ToDoList out = new ToDoList(contents.getHeadline(), R.drawable.icon_outdate, contents.getDeadline(), contents.getId());
                OutList.add(out);
            }
        }
    }
    private void initRefresh() {
        this.swipeRefreshLayout = (SwipeRefreshLayout) this.view.findViewById(R.id.swipe_refresh2);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initOutLists();
                        createList();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateList messageEvent) {
        initOutLists();
        createList();
    }
}