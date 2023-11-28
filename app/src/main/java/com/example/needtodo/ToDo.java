package com.example.needtodo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
public class ToDo extends Fragment {
    private List<ToDoList> toDoListList = new ArrayList<>();
    private ToDoListAdapter toDoListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private View view;
    private LinearLayout linearLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_to_do, container, false);
        EventBus.getDefault().register(this);
        initToDoLists();
        initRefresh();
        loadList();
        return this.view;
    }

    private void initRefresh() {
        this.swipeRefreshLayout = (SwipeRefreshLayout) this.view.findViewById(R.id.swipe_refresh);
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
                        initToDoLists();
                        loadList();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initToDoLists() {
        toDoListList.clear();
        User this_account = LitePal.select("id").where("online = ?", String.valueOf(1)).findFirst(User.class);
        long user_id = this_account.getId();
        List<ThingsList> topThings = LitePal.where("user_id = ? and isDone = ? and isOutDate = ? and setTop =?",String.valueOf(user_id), String.valueOf(0), String.valueOf(0),String.valueOf(1)).find(ThingsList.class);
        for (ThingsList tops : topThings) {
            ToDoList top = new ToDoList(tops.getHeadline(), R.drawable.ic_todo, tops.getDeadline(), tops.getId());
            toDoListList.add(top);
        }
        List<ThingsList> things = LitePal.where("user_id = ? and isDone = ? and isOutDate = ? and setTop =?", String.valueOf(user_id), String.valueOf(0), String.valueOf(0),String.valueOf(0)).find(ThingsList.class);
        for (ThingsList contents : things) {
            ToDoList add = new ToDoList(contents.getHeadline(), R.drawable.ic_todo, contents.getDeadline(), contents.getId());
            toDoListList.add(add);
        }
    }
    private void loadList() {
        recyclerView = (RecyclerView) this.view.findViewById(R.id.todo_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ToDoListAdapter adapter = new ToDoListAdapter(toDoListList);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateList messageEvent) {
        initToDoLists();
        loadList();
    }
}