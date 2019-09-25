package com.e.ishar.supplychainmodel2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static java.util.Arrays.asList;
public class FinalFarmerStatus extends Activity {
    private ListView fvList;
    private Button btnOn, btnOff;
    private TextView txtArduino, txtString, txtStringLength, sensorView0, sensorView1, sensorView2, sensorView3;
    private Handler bluetoothIn;

    final private int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<String>fvArrayList;
    private ConnectedThread mConnectedThread;
    private ArrayAdapter myAdapter;
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_farmer_status);
        fvList = (ListView)findViewById(R.id.vfList);
        fvArrayList = new ArrayList<>(asList("Tomatos IT:18 IH:90 notgood","Onion IT:30 IH:25 good","Potato IT:30 IH:25 good","Cabbage IT:5 IH:95 notgood",
                "Apples IT:8 IH:90 bad","DryBeans IT:10 IH:90 good","Watermelon IT:12 IH:90 good","Garlic IT:5 IH:65 bad","Cucumber IT:12 IH:95 bad"));
        myAdapter = new ArrayAdapter(FinalFarmerStatus.this,android.R.layout.simple_list_item_1,fvArrayList);
        fvList.setAdapter(myAdapter);
        //Link the buttons and textViews to respective views
        btnOn = (Button) findViewById(R.id.buttonOn);
        btnOff = (Button) findViewById(R.id.buttonOff);
        txtString = (TextView) findViewById(R.id.txtString);
        txtStringLength = (TextView) findViewById(R.id.testView1);
        sensorView0 = (TextView) findViewById(R.id.sensorView0);
        sensorView1 = (TextView) findViewById(R.id.sensorView1);
        sensorView2 = (TextView) findViewById(R.id.sensorView2);
        sensorView3 = (TextView) findViewById(R.id.sensorView3);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                try {
                    if (msg.what == handlerState) {                                     //if message is what we want

                        String readMessage = msg.obj.toString();                           // msg.arg1 = bytes from connect thread
                        recDataString.append(readMessage);                                      //keep appending to string until ~
                        int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                        Toast.makeText(FinalFarmerStatus.this, "Entered Handler " + endOfLineIndex + readMessage, Toast.LENGTH_SHORT).show();
                        if (endOfLineIndex > 0) {                                           // make sure there data before ~
                            String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                            txtString.setText("Data Received = " + dataInPrint);
                            int dataLength = dataInPrint.length();                          //get length of data received
                            txtStringLength.setText("String Length = " + String.valueOf(dataLength));
                            Toast.makeText(FinalFarmerStatus.this, "Entered Handler innner  " + recDataString.charAt(0), Toast.LENGTH_SHORT).show();
                            if (recDataString.charAt(0) == '#')                             //if it starts with # we know it is what we are looking for
                            {
                                String sensor0 = recDataString.substring(1, 6);             //get sensor value from string between indices 1-5
                                String sensor1 = recDataString.substring(7, 12);            //same again...
                                //String sensor2 = recDataString.substring(13, 18);
                                //String sensor3 = recDataString.substring(19, 24);
                                String data[] = sensor1.split("\\+");
                                sensor1 = data[0];
                                sensorView0.setText(" Temperature = " + sensor0 + "C");    //update the textviews with sensor values
                                sensorView1.setText(" Humidity = " + sensor1);
                                //sensorView2.setText(" Temperature = " + sensor2 + "C");
                                //sensorView3.setText(" Humidity = " + sensor3);
                                float Tempval = Float.parseFloat(sensor0);
                                float HumdiditityVal = Float.parseFloat(sensor1);

                                try {
                                    for (int ind = 0; ind < fvArrayList.size(); ind++) {
                                        String CompleteElem = fvArrayList.get(ind);
                                        String[] Stringdata = CompleteElem.split(" ");
                                        //int idealTemp = Integer.parseInt(Stringdata[]);
                                        String[] tempcomb = Stringdata[1].split(":");
                                        int idealTemp = Integer.parseInt(tempcomb[1]);
                                        String[] tempHum = Stringdata[2].split(":");
                                        int idealHumdidty = Integer.parseInt(tempHum[1]);
                                        //Toast.makeText(vendorStatus.this, idealTemp+" "+idealHumdidty, Toast.LENGTH_SHORT).show();
                                        float difftemp = Tempval - idealTemp;
                                        if (difftemp < 0) {
                                            difftemp = difftemp * (-1);
                                        }
                                        float diffhum = HumdiditityVal - idealHumdidty;
                                        if (diffhum < 0) {
                                            diffhum = diffhum * (-1);
                                        }
                                        //Toast.makeText(vendorStatus.this, difftemp+" "+diffhum, Toast.LENGTH_SHORT).show();
                                        if (diffhum > 10 || diffhum > 10) {
                                            fvArrayList.set(ind, Stringdata[0] + " " + Stringdata[1] + " " + Stringdata[2] + " " + "notgood");

                                        } else {
                                            fvArrayList.set(ind, Stringdata[0] + " " + Stringdata[1] + " " + Stringdata[2] + " " + "good");
                                        }
                                    }
                                    myAdapter = new ArrayAdapter(FinalFarmerStatus.this, android.R.layout.simple_list_item_1, fvArrayList);
                                    fvList.setAdapter(myAdapter);

                                } catch (Exception e) {
                                    Toast.makeText(FinalFarmerStatus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                            recDataString.delete(0, recDataString.length());                    //clear all string data
                            // strIncom =" ";
                            dataInPrint = " ";
                        }
                    }
                }
                catch (Exception e){
                    Toast.makeText(FinalFarmerStatus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        // Set up onClick listeners for buttons to send 1 or 0 to turn on/off LED
        btnOff.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("0");    // Send "0" via Bluetooth
                Toast.makeText(getBaseContext(), "Turn off LED", Toast.LENGTH_SHORT).show();
            }
        });

        btnOn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("1");    // Send "1" via Bluetooth
                Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //create device and set the MAC address
        BluetoothDevice device=null;
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice mbdevice : pairedDevices){

                device = mbdevice;

            }
        }

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    //Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }
}
