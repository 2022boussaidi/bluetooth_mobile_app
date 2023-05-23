package com.cambotutorial.sovary.qrscanner;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class MainActivity3 extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    Button send;
    Button listen, listDevices;
    ListView listView;
    TextView msg_box,status;
    EditText writeMsg;
    ImageView cal1;
    ImageView cal2;
    ImageView cal3;
    EditText receptionDate;
    int year;
    int month;
    int day;
    EditText extractionDate;

    EditText datee;
    EditText name;
    EditText quantity;
    EditText code;
    EditText chainid;


     BluetoothAdapter bluetoothAdapter;
     BluetoothDevice[] btArray;
     SendReceive sendReceive;
     static final int STATE_LISTENING=1;
     static final int STATE_CONNECTING=2;
     static final int STATE_CONNECTED=3;
     static final int STATE_CONNECTION_FAILED=4;
     static final int STATE_MESSAGE_RECEIVED=5;
     int REQUEST_ENABLE_BLUETOOTH=1;

    private static final String APP_NAME = "olive";
     private static final UUID MY_UUID=UUID.fromString("3a22633a-d078-11ed-afa1-0242ac120002");
     Intent btEnablingIntent;
     int requestCodeForEnable;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        receptionDate=findViewById(R.id.reception);
        cal1=findViewById(R.id.datepicker);
        cal2=findViewById(R.id.datepicker2);
        cal3=findViewById(R.id.datepicker3);
        extractionDate=findViewById(R.id.extraction);
        datee=findViewById(R.id.date);
        name=findViewById(R.id.Name);
        quantity=findViewById(R.id.quantity);
        code=findViewById(R.id.code);
        chainid=findViewById(R.id.chainid);



       final  Calendar calendar=Calendar.getInstance();
        year =calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DATE);




        receptionDate.setOnClickListener(view -> {
              DatePickerDialog datePickerDialog= new DatePickerDialog(MainActivity3.this, (datePicker, year, month, date) -> receptionDate.setText(date+"-"+month+"-"+year),year,month,day);
              datePickerDialog.show();
         });

        extractionDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog= new DatePickerDialog(MainActivity3.this, (datePicker, year, month, date) -> extractionDate.setText(date+"-"+month+"-"+year),year,month,day);
            datePickerDialog.show();
        });

        datee.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog= new DatePickerDialog(MainActivity3.this, (datePicker, year, month, date) -> datee.setText(date+"-"+month+"-"+year),year,month,day);
            datePickerDialog.show();
        });


        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BLUETOOTH);
        }
        findViewByIdes();
        implementListeners();


    }

    private void implementListeners() {

        listen=(Button) findViewById(R.id.listen);
        listen.setBackgroundColor(Color.WHITE);
        send=(Button) findViewById(R.id.send);
        send.setBackgroundColor(Color.WHITE);
        listView=(ListView) findViewById(R.id.listview);

        //msg_box =(TextView) findViewById(R.id.msg);
        status=(TextView) findViewById(R.id.status);
        writeMsg=(EditText) findViewById(R.id.writemsg);
        listDevices=(Button) findViewById(R.id.listDevices);
        listDevices.setBackgroundColor(Color.WHITE);
        listDevices.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                @SuppressLint("MissingPermission") Set<BluetoothDevice> bt=bluetoothAdapter.getBondedDevices();
                String[] strings=new String[bt.size()];
                btArray=new BluetoothDevice[bt.size()];
                int index=0;

                if( bt.size()>0)
                {
                    for(BluetoothDevice device : bt)
                    {
                        btArray[index]= device;
                        strings[index]=device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerClass serverClass=new ServerClass();
                serverClass.start();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClientClass clientClass=new ClientClass(btArray[i]);
                clientClass.start();

                status.setText("Connecting");
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = String.valueOf(name.getText());
                String reception = String.valueOf(receptionDate.getText());
                String extraction = String.valueOf(extractionDate.getText());
                String chain = String.valueOf(chainid.getText());
                String quantity1 = String.valueOf(quantity.getText());
                String code1 = String.valueOf(code.getText());
                String datee1 = String.valueOf(datee.getText());
                List<String> strings = new ArrayList<>();
                strings.add(name1);
                strings.add(reception);
                strings.add(extraction);
                strings.add(chain);
                strings.add(quantity1);
                strings.add(code1);
                strings.add(datee1);

                // Concaténer les chaînes de caractères en une seule chaîne
                String concatenatedString = String.join("*", strings);

                // Convertir la chaîne en tableau de bytes
                byte[] bytes = concatenatedString.getBytes();

                // Envoyer les bytes
                sendReceive.write(bytes);


                //sendReceive.write(string.getBytes());
            }
        });
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what)
            {
                case STATE_LISTENING:
                    status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);
                    msg_box.setText(tempMsg);




                    break;
            }
            return true;
        }
    });

    private void findViewByIdes() {
        Button listen = (Button) findViewById(R.id.listen);

        listen.setBackgroundColor(Color.WHITE);


        listView=(ListView) findViewById(R.id.listview);


       // listView.setBackgroundColor(Color.WHITE);
       // TextView msg_box = (TextView) findViewById(R.id.msg);
        status=(TextView) findViewById(R.id.status);
        status.setTextColor(Color.WHITE);
        EditText writeMsg = (EditText) findViewById(R.id.writemsg);
        Button listDevices = (Button) findViewById(R.id.listDevices);
    }

    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        @SuppressLint("MissingPermission")
        public ServerClass(){
            try {
                serverSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME,MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            BluetoothSocket socket=null;

            while (socket==null)
            {
                try {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);

                    socket=serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }

                if(socket!=null)
                {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);

                    sendReceive=new SendReceive(socket);
                    sendReceive.start();

                    break;
                }
            }
        }
    }

    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        @SuppressLint("MissingPermission")
        public ClientClass (BluetoothDevice device1)
        {
            device=device1;

            try {
                socket=device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @SuppressLint("MissingPermission")
        public void run()
        {
            try {
                socket.connect();
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);

                sendReceive=new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }

    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream=tempIn;
            outputStream=tempOut;
        }



        public void run()
        {
            byte[] buffer=new byte[1024];
            int bytes;

            while (true)
            {
                try {
                    bytes=inputStream.read(buffer);

                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Récupération de l'ID de l'item cliqué
        int id = item.getItemId();

        // Vérification de l'ID de l'item cliqué
        if (id == R.id.QR) {
            // Le clic a été effectué sur l'item "mon_item_de_menu"
            // Ajouter ici le code à exécuter lorsque l'item est cliqué
            Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
            startActivity(intent);

            return true;
        }

        // Si l'ID de l'item cliqué ne correspond à aucun item du menu,
        // on appelle la méthode de la classe parente
        return super.onOptionsItemSelected(item);
    }

}





