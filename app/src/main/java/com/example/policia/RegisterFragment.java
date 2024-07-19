package com.example.policia;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterFragment extends Fragment {
    private EditText editTextTitle, editTextDate, editTextDescription;
    private Button buttonTakePhoto, buttonRecordAudio, buttonSave;
    private String photoPath, audioPath;
    private AppDatabase db;
    private Uri photoURI;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_AUDIO_CAPTURE = 2;
    static final int REQUEST_PERMISSIONS = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);

        db = Room.databaseBuilder(getContext(), AppDatabase.class, "incident_db").allowMainThreadQueries().build();

        editTextTitle = root.findViewById(R.id.editTextTitle);
        editTextDate = root.findViewById(R.id.editTextDate);
        editTextDescription = root.findViewById(R.id.editTextDescription);
        buttonTakePhoto = root.findViewById(R.id.buttonTakePhoto);
        buttonRecordAudio = root.findViewById(R.id.buttonRecordAudio);
        buttonSave = root.findViewById(R.id.buttonSave);

        // Solicitar permisos
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                    REQUEST_PERMISSIONS);
        }

        buttonTakePhoto.setOnClickListener(v -> dispatchTakePictureIntent());
        buttonRecordAudio.setOnClickListener(v -> dispatchRecordAudioIntent());
        buttonSave.setOnClickListener(v -> saveIncident());

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                // Permisos concedidos
            } else {
                Toast.makeText(getContext(), "Permisos necesarios no concedidos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.policia.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        photoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchRecordAudioIntent() {
        File audioFile = null;
        try {
            audioFile = createAudioFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (audioFile != null) {
            audioPath = audioFile.getAbsolutePath();
            Intent recordAudioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            recordAudioIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(audioFile));
            startActivityForResult(recordAudioIntent, REQUEST_AUDIO_CAPTURE);
        }
    }

    private File createAudioFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String audioFileName = "AUDIO_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File audio = File.createTempFile(
                audioFileName,  /* prefix */
                ".3gp",         /* suffix */
                storageDir      /* directory */
        );
        return audio;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Toast.makeText(getContext(), "Foto tomada y guardada en: " + photoPath, Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_AUDIO_CAPTURE && data != null) {
                Uri audioUri = data.getData();
                if (audioUri != null) {
                    audioPath = getRealPathFromURI(audioUri);
                } else {
                    Toast.makeText(getContext(), "Error al grabar audio", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
    }

    private void saveIncident() {
        String title = editTextTitle.getText().toString();
        String date = editTextDate.getText().toString();
        String description = editTextDescription.getText().toString();

        if (title.isEmpty() || date.isEmpty() || description.isEmpty() || photoPath == null || audioPath == null) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos y tome una foto y un audio", Toast.LENGTH_SHORT).show();
            return;
        }

        Incident incident = new Incident();
        incident.setTitle(title);
        incident.setDate(date);
        incident.setDescription(description);
        incident.setPhotoPath(photoPath);
        incident.setAudioPath(audioPath);

        db.incidentDao().insert(incident);

        Toast.makeText(getContext(), "Incidencia guardada", Toast.LENGTH_SHORT).show();
    }
}
