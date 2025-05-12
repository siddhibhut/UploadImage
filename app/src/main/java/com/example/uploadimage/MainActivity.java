package com.example.uploadimage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    private static final int PICK_IMAGE_REQUEST = 1;
    private List<Uri> imageUris = new ArrayList<>();
    private ImageAdapter adapter;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            // Multiple images selected
                            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                imageUris.add(imageUri);
                            }
                        } else if (data.getData() != null) {
                            // Single image selected
                            Uri imageUri = data.getData();
                            imageUris.add(imageUri);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
        );
        Button buttonUpload = findViewById(R.id.buttonUpload);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ImageAdapter(this, imageUris);
        recyclerView.setAdapter(adapter);

        buttonUpload.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            imagePickerLauncher.launch(Intent.createChooser(intent, "Select Picture"));
        });
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
//            if (data.getClipData() != null) { // Multiple images
//                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
//                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                    imageUris.add(imageUri);
//                }
//            } else if (data.getData() != null) { // Single image
//                imageUris.add(data.getData());
//            }
//            adapter.notifyDataSetChanged();
//        }
//    }
}