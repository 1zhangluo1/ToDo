package com.example.needtodo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class DoneList extends BaseActivity {

    private ImageView back;
    private List<ToDoList> doneList = new ArrayList<>();
    private DoneAdapter doneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_had_done);
        back = findViewById(R.id.done_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initDoneLists();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.done_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DoneAdapter adapter = new DoneAdapter(doneList);
        recyclerView.setAdapter(adapter);
    }
    private void initDoneLists(){
        User this_account = LitePal.select("id").where("online = ?",String.valueOf(1)).findFirst(User.class);
        long user_id = this_account.getId();
        List<ThingsList> things = LitePal.where("isDone = ? and user_id = ?",String.valueOf(1),String.valueOf(user_id)).find(ThingsList.class);
        for (ThingsList contents: things){
            ToDoList done = new ToDoList(contents.getHeadline(), R.drawable.ic_hadone,contents.getDeadline(),contents.getId());
            doneList.add(done);
        }
    }
}