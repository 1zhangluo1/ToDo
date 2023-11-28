package com.example.needtodo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class HadDoneList extends Fragment{
    private View view;
    private DoneAdapter doneAdapter;
    private List<ToDoList>doneList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_had_done_list, container, false);
        EventBus.getDefault().register(this);
        initRefresh();
        initDoneList();
        createDoneList();
        return this.view;
    }
    private void createDoneList() {
        RecyclerView recyclerView = (RecyclerView) this.view.findViewById(R.id.done_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DoneAdapter adapter = new DoneAdapter(doneList);
        recyclerView.setAdapter(adapter);
    }
    private void initDoneList() {
        doneList.clear();
        User this_account = LitePal.select("id").where("online = ?",String.valueOf(1)).findFirst(User.class);
        long user_id = this_account.getId();
        List<ThingsList> things = LitePal.where("isDone = ? and user_id = ?",String.valueOf(1),String.valueOf(user_id)).find(ThingsList.class);
        for (ThingsList contents: things){
            ToDoList done = new ToDoList(contents.getHeadline(), R.drawable.ic_hadone,contents.getDeadline(),contents.getId());
            doneList.add(done);
        }
    }
    private void initRefresh() {
        this.swipeRefreshLayout = (SwipeRefreshLayout) this.view.findViewById(R.id.swipe_refresh3);
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
                        initDoneList();
                        createDoneList();
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
        initDoneList();
        createDoneList();
    }
}