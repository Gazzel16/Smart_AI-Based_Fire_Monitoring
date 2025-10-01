package com.example.smartai_basedfiremonitoring.ChatBot;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.Adapter.ChatAdapter;
import com.example.smartai_basedfiremonitoring.Model.ChatBotModel;
import com.example.smartai_basedfiremonitoring.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

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

public class ChatBotFragment extends Fragment {

    private LinearLayout linearLayout;
    private EditText inputMessage;
    private ImageView sendBtn;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private static List<ChatBotModel> chatList = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();

    private static final String BASE_URL = "http://192.168.1.4:5000/generate-advisory"; // change to your backend IP

    private DatabaseReference sensorRef;
    private int latestTemp = 0;
    private int latestHumidity = 0;
    public ChatBotFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_bot, container, false);

        linearLayout = view.findViewById(R.id.linearLayout);

        inputMessage = view.findViewById(R.id.inputMessage);
        sendBtn = view.findViewById(R.id.sentBtn);
        recyclerView = view.findViewById(R.id.recyclerView); // 👈 make sure you add this in XML

        chatAdapter = new ChatAdapter(getContext(), chatList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);

        // ✅ Get reference to Firebase node
        sensorRef = FirebaseDatabase.getInstance().getReference("sensors");

        // ✅ Listen for changes in temperature & humidity
        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChild("temperature")) {
                        latestTemp = snapshot.child("temperature").getValue(Integer.class);
                    }
                    if (snapshot.hasChild("humidity")) {
                        latestHumidity = snapshot.child("humidity").getValue(Integer.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Firebase Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        sendBtn.setOnClickListener(v -> {
            String message = inputMessage.getText().toString().trim();
            if (TextUtils.isEmpty(message)) {
                Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1️⃣ Add user message
            addMessage(message, false);

            // 2️⃣ Call backend with latest Firebase values
            sendToBackend(latestTemp, latestHumidity);

            inputMessage.setText("");
            linearLayout.setVisibility(View.GONE);
        });

        return view;
    }

    private void addMessage(String msg, boolean isBot) {
        chatList.add(new ChatBotModel(msg, isBot));
        chatAdapter.notifyItemInserted(chatList.size() - 1);
        recyclerView.smoothScrollToPosition(chatList.size() - 1);
    }

    private void sendToBackend(int temp, int humidity) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("temperature", temp);
            json.put("humidity", humidity);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        addMessage("Backend error: " + e.getMessage(), true)
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            addMessage("Unexpected response: " + response.code(), true)
                    );
                } else {
                    String resBody = response.body().string();
                    try {
                        JSONObject resJson = new JSONObject(resBody);
                        String advisory = resJson.getString("advisory");

                        new Handler(Looper.getMainLooper()).post(() ->
                                addMessage(advisory, true) // Bot reply
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
