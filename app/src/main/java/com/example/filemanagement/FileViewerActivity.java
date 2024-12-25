package com.example.filemanagement;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileViewerActivity extends AppCompatActivity {
    private TextView tvFileContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_viewer);

        tvFileContent = findViewById(R.id.tvFileContent);

        String filePath = getIntent().getStringExtra("filePath");
        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            readFileContent(file);
        }
    }

    private void readFileContent(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            tvFileContent.setText(content.toString());
        } catch (IOException e) {
            tvFileContent.setText("Không thể đọc nội dung file.");
        }
    }
}

