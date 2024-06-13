package com.example.Project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ProjectSWiM.R;

public class DisconnectScreen extends AppCompatActivity {
    BluetoothGattServer gattServer = new BluetoothGattServer();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.disconnect_screen);
            gattServer.disconnect();
        }catch(NullPointerException e){
            new Notification().showToast(this, "Nie jesteś połączony z pojazdem");
        }finally{ finish(); }
    }
}
