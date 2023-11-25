package com.example.needtodo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class OutDateList extends BaseActivity {

    private ImageView back;
    private List<ToDoList> outList = new ArrayList<>();
    private OutDateAdapter outDateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_date_list);
        back = findViewById(R.id.out_back);
         back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initOutLists();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.out_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DoneAdapter adapter = new DoneAdapter(outList);
        recyclerView.setAdapter(adapter);
    }
    private void initOutLists(){
        User this_account = LitePal.select("id").where("online = ?",String.valueOf(1)).findFirst(User.class);
        long user_id = this_account.getId();
        List<ThingsList> things = LitePal.where("isOutDate = ? and user_id = ?",String.valueOf(1),String.valueOf(user_id)).find(ThingsList.class);
        if(things!=null) {
            for (ThingsList contents : things) {
                ToDoList out = new ToDoList(contents.getHeadline(), R.drawable.icon_outdate, contents.getDeadline(), contents.getId());
                outList.add(out);
            }
        }
    }
}