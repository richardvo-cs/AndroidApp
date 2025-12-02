package com.example.fitnessprojectneal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ConversationsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NEW_MESSAGE = 1;

    private RecyclerView recyclerViewConversations;
    private ConversationAdapter conversationAdapter;
    private List<Conversation> conversationList;
    private FloatingActionButton fabNewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        recyclerViewConversations = findViewById(R.id.recycler_view_conversations);
        fabNewMessage = findViewById(R.id.fab_new_message);

        conversationList = new ArrayList<>();
        // Dummy data
        conversationList.add(new Conversation("John Doe", "See you tomorrow!", System.currentTimeMillis() - 86400000, true));

        conversationAdapter = new ConversationAdapter(this, conversationList);
        recyclerViewConversations.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewConversations.setAdapter(conversationAdapter);

        fabNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConversationsActivity.this, NewMessageActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_MESSAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NEW_MESSAGE && resultCode == RESULT_OK && data != null) {
            Conversation newConversation = (Conversation) data.getSerializableExtra("NEW_CONVERSATION");
            if (newConversation != null) {
                conversationList.add(0, newConversation);
                conversationAdapter.notifyItemInserted(0);
            }
        }
    }
}
