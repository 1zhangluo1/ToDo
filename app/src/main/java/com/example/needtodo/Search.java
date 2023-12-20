package com.example.needtodo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    private List<ToDoList> searchList = new ArrayList<>();
    private EditText searchView;
    private RecyclerView recyclerView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.search_list_view);
        button = findViewById(R.id.search_yes);
        User user = LitePal.where("online = ?",String.valueOf(1)).findFirst(User.class);
        long user_id = user.getId();
        List<ThingsList> match = LitePal.where("user_id = ?",String.valueOf(user_id)).find(ThingsList.class);
        button.setOnClickListener(v -> {
            for (ThingsList everySearch:match){
                if (everySearch.getHeadline().equals(searchView.getText().toString())) {
                    ToDoList result = new ToDoList(everySearch.getHeadline(),everySearch.getDeadline(),everySearch.getId());
                    searchList.clear();
                    searchList.add(result);
                    loadRecycler();
                }
            }
        });
    }

    private void loadRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        SearchAdapter searchAdapter = new SearchAdapter(searchList);
        recyclerView.setAdapter(searchAdapter);
    }
}