package com.example.Project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ProjectSWiM.R;

public class Main extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        findViewById(R.id.btnCon).setOnClickListener(v -> startActivity(new Intent(this, BluetoothGattServer.class)));

        findViewById(R.id.btnDis).setOnClickListener(v -> startActivity(new Intent(this, DisconnectScreen.class)));

        findViewById(R.id.btnStreer).setOnClickListener(v -> startActivity(new Intent(this, SteeringScreen.class)));
    }
}
