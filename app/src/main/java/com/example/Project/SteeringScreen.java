package com.example.Project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ProjectSWiM.R;

public class SteeringScreen extends AppCompatActivity {
    String []speed = {"Zerowa", "Wolno", "Szybko"};
    BluetoothGattServer send = new BluetoothGattServer();
    Notification toast = new Notification();


    @SuppressLint("ClickableViewAccessibility")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            BluetoothGattServer.bluetoothCharacteristic.getProperties();
        }catch(NullPointerException e){
            toast.showToast(getApplicationContext(), "Odmowa dostępu. Nie połączyłeś się z pojazdem.");
            finish();
        }

        setContentView(R.layout.steering_menu);

        TextView speedText = findViewById(R.id.speedText);
        Button btnChangeSpeed = findViewById(R.id.btnChangeSpeed);
        Button btnForward = findViewById(R.id.btnForward);
        Button btnBackward = findViewById(R.id.btnBackward);
        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnRight = findViewById(R.id.btnRight);

        speedText.setText(speed[1]);
        send.setSpeed(1);

        btnChangeSpeed.setOnClickListener(new View.OnClickListener() {
            int i = 1;
            @Override
            public void onClick(View v) {
                i++;

                if(i > 2){
                    i = 0;
                }

                send.setSpeed(i);
                speedText.setText(speed[i]);
            }
        });

        btnForward.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                send.modifyDirection(4,true);
                send.sendData();
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                send.modifyDirection(4,false);
                send.sendData();
            }
            return false;
        });

        btnBackward.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                send.modifyDirection(8,true);
                send.sendData();
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                send.modifyDirection(8,false);
                send.sendData();
            }
            return false;
        });

        btnLeft.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                send.modifyDirection(16,true);
                send.sendData();
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                send.modifyDirection(16,false);
                send.sendData();
            }
            return false;
        });

        btnRight.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                send.modifyDirection(32,true);
                send.sendData();
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                send.modifyDirection(32,false);
                send.sendData();
            }
            return false;
        });
    }
}
