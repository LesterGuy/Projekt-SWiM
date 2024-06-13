package com.example.Project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.ProjectSWiM.R;


public class BluetoothGattServer extends AppCompatActivity {
    Notification toast = new Notification();
    Notification.Notify notify;


    private BluetoothLeScanner bluetoothLeScanner;
    boolean scan = true;
    String TAG = "Wyniki";

    static int data = 128;
    static BluetoothGatt bluetoothGatt;
    static public BluetoothGattCharacteristic bluetoothCharacteristic;
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    String []permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
    int requestCode;

    LocationManager locationManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_screen);

        notify = new Notification.Notify(getApplicationContext());
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(checkPhoneFunction()) {
            if(hasPermissions(this, permissions))
                scanLeDevice();
            else
                requestPermissions(permissions, requestCode);
        }else{
            finish();
        }
    }

    boolean hasPermissions(Context context, String []permissions){
        for(String permission : permissions){
            if(ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            scanLeDevice();
    }

    @SuppressLint("MissingPermission")
    private void scanLeDevice() {

        bluetoothLeScanner = btAdapter.getBluetoothLeScanner();

        if (scan) {
            bluetoothLeScanner.startScan(leScanCallback);
        } else {
            bluetoothLeScanner.stopScan(leScanCallback);
        }
    }

    private ScanCallback leScanCallback = new ScanCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (scan) {
                Log.i(TAG, "onScanResult: " + result.getDevice().getAddress() + ": " + result.getDevice().getName());
                if (result.getDevice().getAddress().equals("01:23:45:67:9D:CF")) {
                    scan = false;
                    bluetoothLeScanner.stopScan(leScanCallback);
                    bluetoothGatt = result.getDevice().connectGatt(BluetoothGattServer.this, true, mGattCallback);
                }
            }
        }

        private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);

                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    notify.showNotification( "ProjectSWiM", "Jesteś aktualnie połączony z pojazdem.", 1);
                    if(bluetoothCharacteristic != null)
                        connectSound();
                    gatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    bluetoothCharacteristic = null;
                    notify.hideNotification(1);
                    scan = true;
                }
            }

            @SuppressLint("MissingPermission")
            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                bluetoothCharacteristic = gatt.getServices().get(3).getCharacteristics().get(0);
                connectSound();
                finish();
            }
        };

        public void onScanFailed(int errorCode){
            super.onScanFailed(errorCode);
        }
    };

    @SuppressLint("MissingPermission")
    public void sendData(){
        try {
            bluetoothCharacteristic.setValue(data, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            bluetoothGatt.writeCharacteristic(bluetoothCharacteristic);
            Log.v(TAG, ""+data);
        }catch(NullPointerException e){
            toast.showToast(getApplicationContext(), "Nie można było wysłać danych do pojazdu.");
        }finally{ finish(); }
    }

    @SuppressLint("MissingPermission")
    public void disconnect(){
            bluetoothGatt.disconnect();
            scan = true;
    }

    public void connectSound(){
        data = 128;
        sendData();
        data = 0;
    }

    public void setSpeed(int speed){
        data = speed;
    }

    public void modifyDirection(int direction, boolean state){
        if(state)
            data += direction;
        else
            data -= direction;
    }

    private boolean checkPhoneFunction(){
        if(!btAdapter.isEnabled()){
            toast.showToast(getApplicationContext(), "Nie masz włączonego Bluetooth. Przed przejściem do połączenia się z pojazdem, włącz Bluetooth");
            return false;
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            toast.showToast(getApplicationContext(), "Nie masz włączonej lokalizacji. Przed przejściem do połączenia się z pojazdem, włącz lokalizację");
            return false;
        }
        return true;
    }
}