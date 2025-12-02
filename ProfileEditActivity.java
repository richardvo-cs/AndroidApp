package com.example.fitnessprojectneal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditActivity extends AppCompatActivity {

    private EditText etName;
    private RadioGroup rgAvatar;
    private Button btnSave, btnQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        etName = findViewById(R.id.etName);
        rgAvatar = findViewById(R.id.rgAvatar);
        btnSave = findViewById(R.id.btnSave);
        btnQuit = findViewById(R.id.btnQuit);

        etName.setText(GameState.player.name);

        btnSave.setOnClickListener(v -> {
            GameState.player.name = etName.getText().toString();
            // TODO: Save avatar selection from rgAvatar
            finish();
        });

        btnQuit.setOnClickListener(v -> finish());
    }
}
