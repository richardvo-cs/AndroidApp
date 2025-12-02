package com.example.fitnessprojectneal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_QUEST = 1;

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private Button buttonAttachQuest;
    private MessageAdapter messageAdapter;
    private List<Object> messageList; // Use a list of objects to hold messages and quests
    private String conversationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        conversationId = getIntent().getStringExtra("CONVERSATION_NAME");

        recyclerViewMessages = findViewById(R.id.recycler_view_messages);
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);
        buttonAttachQuest = findViewById(R.id.button_attach_quest);

        // Get messages from the MessageStore
        messageList = (List<Object>) (List<?>) MessageStore.getMessages(conversationId);

        messageAdapter = new MessageAdapter(messageList);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);

        buttonSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString().trim();
            if (!messageText.isEmpty()) {
                Message newMessage = new Message(messageText, System.currentTimeMillis(), true);
                MessageStore.addMessage(conversationId, newMessage);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                editTextMessage.setText("");
            }
        });

        buttonAttachQuest.setOnClickListener(v -> {
            Intent intent = new Intent(MessagesActivity.this, QuestSelectionActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_QUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_QUEST && resultCode == RESULT_OK && data != null) {
            Quest selectedQuest = (Quest) data.getSerializableExtra("SELECTED_QUEST");
            if (selectedQuest != null) {
                messageList.add(selectedQuest);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerViewMessages.scrollToPosition(messageList.size() - 1);
            }
        }
    }
}
