package com.example.policia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        // Configurar los elementos de la vista
        ImageView imageOfficer = root.findViewById(R.id.image_officer);
        TextView textOfficerName = root.findViewById(R.id.text_officer_name);
        TextView textOfficerBadge = root.findViewById(R.id.text_officer_badge);
        TextView textReflection = root.findViewById(R.id.text_reflection);

        // Aquí deberías obtener los datos del oficial desde alguna fuente, por ejemplo:
        // Officer officer = obtenerDatosDelOficial(); // Implementa según tus necesidades

        // Ejemplo de configuración estática:
        imageOfficer.setImageResource(R.drawable.default_officer_image); // Asigna la imagen del oficial
        textOfficerName.setText("Daimon Aquino"); // Asigna el nombre del oficial
        textOfficerBadge.setText("Matrícula: 2022-2135"); // Asigna la matrícula del oficial
        textReflection.setText("Uno de los aspectos más importantes de la seguridad en comunidades de vecinos es la protección tanto de bienes materiales como de las personas. Sistemas de vigilancia como cámaras de seguridad, control de accesos y presencia de personal de seguridad pueden disuadir actividades delictivas. Esto es especialmente vital en zonas urbanas donde las tasas de criminalidad tienden a ser más altas."); // Asigna la reflexión

        return root;
    }
}
