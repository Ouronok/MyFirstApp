package myfirstapp.mydomain.com.myfirstapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    InputStream inputStream;
    OutputStream outputStream;
    Button conButton;
    Button disButton;
    Button forButton;
    Button backButton;
    Button leftButton;
    Button rightButton;
    TextView statusLabel;
    boolean bluetoothActive;
    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
            deviceList.add(remoteDevice);
            Log.d("MyFirstApp", "Discovered" + remoteDeviceName);
            Log.d("MyFirstApp", "RSSI" + rssi + "dBm");
            if (remoteDeviceName.equals("CURSOARDUINO03")) {
                Log.d("onRecieve", "Discovered CURSOARDUINO03:connecting");
                connect(remoteDevice);
            }
        }
    };


    private ArrayList<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conButton = (Button) findViewById(R.id.conButton);
        disButton = (Button) findViewById(R.id.disButton);
        forButton = (Button) findViewById(R.id.forButton);
        backButton = (Button) findViewById(R.id.backButton);
        leftButton = (Button) findViewById(R.id.leftButton);
        rightButton = (Button) findViewById(R.id.rightButton);
        statusLabel = (TextView) findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickConnectButton(View view) {
        statusLabel.setText("Connect pressed");
        if (bluetooth.isEnabled()) {
            String address = bluetooth.getAddress();
            String name = bluetooth.getName();
            statusLabel.setText(address);
            bluetoothActive = true;
            startDiscovery();
        } else {
            bluetoothActive = false;
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
            if (resultCode == RESULT_OK) {
                statusLabel.setText("User Enabled Bluetooth" + requestCode);
                bluetoothActive = true;
            } else {
                statusLabel.setText("User Did not enable Bluetooth" + requestCode);
                bluetoothActive = false;
            }
    }

    private void startDiscovery() {
        if (bluetoothActive) {
            deviceList.clear();
            registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            statusLabel.setText("BT startDiscovert");
            Log.d("MyFirstApp", "startDiscovery");
            bluetooth.startDiscovery();
        }
    }

    protected void connect(BluetoothDevice device) {
        try {
            BluetoothSocket btSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            btSocket.connect();
            Log.d("connect", "Client connected");

            inputStream = btSocket.getInputStream();
            outputStream = btSocket.getOutputStream();
        } catch (Exception e) {
            Log.e("ERROR: connect", ">>", e);
        }
    }

    public void forward() {
        try {
            String tmpStr = "WHEELS+70+110";
            byte bytes[] = tmpStr.getBytes();
            if (outputStream != null) outputStream.write(bytes);
            if (outputStream != null) outputStream.flush();
        } catch (Exception e) {
            Log.e("forward", "ERROR:" + e);
        }
    }

    public void backward() {
        try {
            String tmpStr = "WHEELS+1+1";
            byte bytes[] = tmpStr.getBytes();
            if (outputStream != null) outputStream.write(bytes);
            if (outputStream != null) outputStream.flush();
        } catch (Exception e) {
            Log.e("backward", "ERROR:" + e);
        }

    }

    public void left() {
        try {
            String tmpStr = "WHEELS+110+90";
            byte bytes[] = tmpStr.getBytes();
            if (outputStream != null) outputStream.write(bytes);
            if (outputStream != null) outputStream.flush();
        } catch (Exception e) {
            Log.e("left", "ERROR:" + e);
        }
    }

    public void right() {
        try {
            String tmpStr = "WHEELS+90+110";
            byte bytes[] = tmpStr.getBytes();
            if (outputStream != null) outputStream.write(bytes);
            if (outputStream != null) outputStream.flush();
        } catch (Exception e) {
            Log.e("rigth", "ERROR:" + e);
        }
    }
    public void clickOnRight(View view) {
        right();
    }
    public void clickOnForward(View view) {
        forward();
    }
    public void clickOnBackwards(View view) {
        backward();
    }
    public void clickOnLeft(View view) {
        left();
    }
}