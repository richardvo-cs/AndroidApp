package com.example.fitnessprojectneal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_PICTURE = 1;

    private ImageView ivProfilePic;
    private Button btnChangePic;
    private EditText etName;
    private EditText etBio;
    private Button btnSuggestFeature;
    private Button btnSave, btnQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        ivProfilePic = findViewById(R.id.ivProfilePic);
        btnChangePic = findViewById(R.id.btnChangePic);
        etName = findViewById(R.id.etName);
        etBio = findViewById(R.id.etBio);
        btnSuggestFeature = findViewById(R.id.btnSuggestFeature);
        btnSave = findViewById(R.id.btnSave);
        btnQuit = findViewById(R.id.btnQuit);

        etName.setText(GameState.player.name);
        // TODO: Load bio from GameState

        btnChangePic.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_SELECT_PICTURE);
        });

        btnSuggestFeature.setOnClickListener(v -> {
            Toast.makeText(this, "Thank you for your suggestion!", Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(v -> {
            GameState.player.name = etName.getText().toString();
            // TODO: Save bio to GameState
            finish();
        });

        btnQuit.setOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            ivProfilePic.setImageURI(selectedImage);
            // TODO: Save the selected image URI to GameState
        }
    }
}
