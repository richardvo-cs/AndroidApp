package com.example.fitnessprojectneal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NewMessageActivity extends AppCompatActivity {

    private EditText editTextRecipientId;
    private EditText editTextNewMessage;
    private Button buttonSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        editTextRecipientId = findViewById(R.id.edit_text_recipient_id);
        editTextNewMessage = findViewById(R.id.edit_text_new_message);
        buttonSendMessage = findViewById(R.id.button_send_new_message);

        buttonSendMessage.setOnClickListener(v -> {
            String recipientId = editTextRecipientId.getText().toString();
            String messageText = editTextNewMessage.getText().toString();

            if (recipientId.isEmpty() || messageText.isEmpty()) {
                // Show an error message if the recipient ID or message is empty
                return;
            }

            // Add the new message to the MessageStore
            Message newMessage = new Message(messageText, System.currentTimeMillis(), true);
            MessageStore.addMessage(recipientId, newMessage);

            // Create a new conversation and pass it back to the ConversationsActivity
            Conversation newConversation = new Conversation(recipientId, messageText, System.currentTimeMillis(), false);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_CONVERSATION", newConversation);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}
