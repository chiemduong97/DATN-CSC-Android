package com.example.client.screens.message.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.client.R;
import com.example.client.models.message.MessengerModel;
import com.example.client.screens.message.items.MessengerItem;
import com.example.client.screens.message.present.MessagePresent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MessageActivity extends AppCompatActivity implements IMessageView, View.OnClickListener{
    private RecyclerView recyclerView;
    private EditText data;
    private MessagePresent mPresent;
    private List<MessengerModel> list;
    private WebSocket socket;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ImageView back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerView);
        data = findViewById(R.id.data);

        mPresent = new MessagePresent(this);

        recyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> recyclerView.scrollToPosition(0));

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://192.168.1.3:8686/socket.php").build();
        socket = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();


        data.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (data.getRight() - data.getCompoundDrawables()[2].getBounds().width())) {
                    String dt = data.getText().toString();
                    if(dt.equals("")) {
                        return false;
                    }
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                    MessengerModel mm = new MessengerModel(getIntent().getIntExtra("user",-1), dt,
                            format.format(new Date()), true);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("id",mm.getId());
                        object.put("data", mm.getData());
                        object.put("createAt",mm.getCreateAt());
                        object.put("fromMe", mm.isFromMe());
                        socket.send(object.toString());
                        List<MessengerModel> _data = new ArrayList<>(list);
                        _data.add(0,mm);
                        list.clear();
                        list.addAll(_data);
                        LinearLayoutManager manager = new LinearLayoutManager(MessageActivity.this, LinearLayoutManager.VERTICAL,false);
                        manager.setReverseLayout(true);
                        manager.setStackFromEnd(true);
                        recyclerView.setLayoutManager(manager);
                        MessengerItem item = new MessengerItem(MessageActivity.this, list);
                        recyclerView.setAdapter(item);
                        recyclerView.scrollToPosition(0);
                        data.setText("");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
            return false;
        });




        back.setOnClickListener(this);
    }
    private final WebSocketListener listener = new WebSocketListener() {
        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);
            text = text.replace("\\\"", "'");
            text = text.substring(1, text.length() - 1);
            try {
                JSONObject object = new JSONObject(text);
                int id = object.getInt("id");
                String data = object.getString("data");
                String createAt = object.getString("createAt");
                MessengerModel mm = new MessengerModel(id, data, createAt,false);
                Log.d("Duong", "onMessage: " + text);
                if (id != getIntent().getIntExtra("user",-1)) {
                    return;
                }
                runOnUiThread(() -> {
                    List<MessengerModel> _data = new ArrayList<>(list);
                    _data.add(0,mm);
                    list.clear();
                    list.addAll(_data);
                    LinearLayoutManager manager = new LinearLayoutManager(MessageActivity.this, LinearLayoutManager.VERTICAL,false);
                    manager.setReverseLayout(true);
                    manager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(manager);
                    MessengerItem item = new MessengerItem(MessageActivity.this, list);
                    recyclerView.setAdapter(item);
                    recyclerView.scrollToPosition(0);

                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);
        }
    };

    @Override
    protected void onResume() {
        mPresent.onShowMessages();
        super.onResume();
    }

    @Override
    public void showMessages(List<MessengerModel> list) {
        this.list = list;
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        MessengerItem item = new MessengerItem(this, list);
        recyclerView.setAdapter(item);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            finish();
            onBackPressed();
        }
    }
}