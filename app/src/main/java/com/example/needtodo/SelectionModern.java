package com.example.needtodo;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import overwrite.OutRecyclerView;

public class SelectionModern extends AppCompatActivity {
    private OutRecyclerView outRecyclerView;
    private List<Selection> outside = new ArrayList<>();
    private List<Selection.SingleSelection> inside1 = new ArrayList<>();
    private List<Selection.SingleSelection> inside2 = new ArrayList<>();
    private List<Selection.SingleSelection> inside3 = new ArrayList<>();
    private List<Selection.SingleSelection> inside4 = new ArrayList<>();
    private List<Selection.SingleSelection> inside5 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_modern);
        ImageView imageView = findViewById(R.id.back_selection);
        imageView.setOnClickListener(v -> finish());
        outRecyclerView = findViewById(R.id.out_recyclerview);
        initData();
        initView();
    }

    private void initView() {
        outRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        outRecyclerView.setAdapter(new OutAdapter(this, outside));
    }

    private void initData() {
        User user = LitePal.where("online = ?", String.valueOf(1)).findFirst(User.class);
        long id = user.getId();
        List<ThingsList> selection = LitePal.where("user_id = ?", String.valueOf(id)).find(ThingsList.class);
        for (ThingsList singleSelection : selection) {
            if ("其它".equals(singleSelection.getType())) {
                inside1.add(new Selection.SingleSelection(singleSelection.getHeadline(), singleSelection.getId()));
            } else if ("学习".equals(singleSelection.getType())) {
                inside2.add(new Selection.SingleSelection(singleSelection.getHeadline(), singleSelection.getId()));
            } else if ("工作".equals(singleSelection.getType())) {
                inside3.add(new Selection.SingleSelection(singleSelection.getHeadline(), singleSelection.getId()));
            } else if ("生活".equals(singleSelection.getType())) {
                inside4.add(new Selection.SingleSelection(singleSelection.getHeadline(), singleSelection.getId()));
            } else if ("家庭".equals(singleSelection.getType())) {
                inside5.add(new Selection.SingleSelection(singleSelection.getHeadline(), singleSelection.getId()));
            }
        }
        outside.add(new Selection("其它:", inside1));
        outside.add(new Selection("学习:", inside2));
        outside.add(new Selection("工作:", inside3));
        outside.add(new Selection("生活:", inside4));
        outside.add(new Selection("家庭:", inside5));
    }
}