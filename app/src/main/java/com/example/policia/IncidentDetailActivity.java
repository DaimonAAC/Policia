package com.example.policia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class IncidentDetailActivity extends AppCompatActivity {
    private TextView title, date, description;
    private ImageView photo;
    private Button playAudio;
    private Incident incident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_detail);

        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        description = findViewById(R.id.description);
        photo = findViewById(R.id.photo);
        playAudio = findViewById(R.id.playAudio);

        incident = (Incident) getIntent().getSerializableExtra("incident");

        title.setText(incident.getTitle());
        date.setText(incident.getDate());
        description.setText(incident.getDescription());

        Bitmap bitmap = BitmapFactory.decodeFile(incident.getPhotoPath());
        photo.setImageBitmap(bitmap);

        playAudio.setOnClickListener(v -> {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(incident.getAudioPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

