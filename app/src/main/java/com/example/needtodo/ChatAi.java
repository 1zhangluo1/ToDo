package com.example.needtodo;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatAi extends AppCompatActivity {

    private ImageButton back;
    private ImageButton send;
    private EditText send_question;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Messages> messagesList = new ArrayList<>();
    private OkHttpClient okHttpClient;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_ai);
        back = findViewById(R.id.back_gpt);
        back.setOnClickListener(v -> finish());
        send = findViewById(R.id.send);
        send_question = findViewById(R.id.send_message);
        send.setOnClickListener(v -> {
            if (!send_question.getText().toString().isEmpty()){
                String question = send_question.getText().toString();
                addMessage(question, Messages.SEND_BY_ME);
                try {
                    callAPI(question);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(ChatAi.this, "已发送", Toast.LENGTH_SHORT).show();
                send_question.setText("");}else if (send_question.getText().toString().isEmpty()){
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();}
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.dialog_show);
        adapter = new MessageAdapter(messagesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void addMessage(String question,String send_by) {
        runOnUiThread(() -> {
            messagesList.add(new Messages(question,send_by));
            adapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(adapter.getItemCount());
        });
    }

    private void  addAnswer(String response) {
        addMessage(response,Messages.SEND_BY_AI);
    }

    private void callAPI(String question) throws JSONException{
        okHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        ArrayList<ResponseMessage> messages = new ArrayList<>();
        messages.add(new ResponseMessage(
                question,"user"
        ));
        OpenAiRequest openAiRequest = new OpenAiRequest(
                messages,"gpt-3.5-turbo-1106",false
        );
        String json = gson.toJson(openAiRequest);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url("https://api.nextapi.fun/openai/v1/chat/completions").header("Authorization", "Bearer ak-02cEHw1NK2suAumnWCc7bu3V4hrCJTiz0S5mbrqsyXSVhiRX")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addAnswer("请求失败,请检查网络连接"+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String resp = response.body().string();
                    ChatResponse chatResponse = gson.fromJson(resp, ChatResponse.class);
                    ResponseMessage msg = chatResponse.getChoices().get(0).getMessage();
                    addAnswer(msg.getContent());
                } else {
                    addAnswer("系统异常,请检查余额");
                }
            }
        });
    }
}