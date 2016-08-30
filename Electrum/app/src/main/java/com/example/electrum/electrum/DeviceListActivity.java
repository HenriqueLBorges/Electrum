package com.example.electrum.electrum;

/**
 * Created by Henrique on 28/11/2015.
 */
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.OnDataReceivedListener;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class DeviceListActivity extends Activity {
    BluetoothSPP bt;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicelist);

        bt = new BluetoothSPP(this);

        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth não disponível"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Log.i("Check", "Length : " + data.length);
                Log.i("Check", "Message : " + message);
            }
        });

        Button btnConnect = (Button)findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(DeviceListActivity.this, DeviceList.class);
                    intent.putExtra("bluetooth_devices", "Dispositivo Bluetooth");
                    intent.putExtra("no_devices_found", "Nenhum dispositivo encontrado");
                    intent.putExtra("scanning", "Buscando");
                    intent.putExtra("scan_for_devices", "Buscar");
                    intent.putExtra("select_device", "Selecionar");
                    intent.putExtra("layout_list", R.layout.device_layout_list);
                    intent.putExtra("layout_text", R.layout.device_layout_text);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onStart() {
        super.onStart();
        if(!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth não iniciado."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void setup() {
        Button vermelha = (Button)findViewById(R.id.b_vermelha);
        Button amarela = (Button)findViewById(R.id.b_amarela);
        Button verde = (Button)findViewById(R.id.b_verde);
        vermelha.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.send(new byte[]{2}, false);
                Toast.makeText(getApplicationContext()
                        , "Bandeira vermelha selecionada."
                        , Toast.LENGTH_SHORT).show();
                Intent intent = null;
                intent = new Intent(getApplicationContext(), TerminalActivity.class);
                startActivity(intent);
            }
        });
        amarela.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.send(new byte[]{1}, false);
                Toast.makeText(getApplicationContext()
                        , "Bandeira amarela selecionada."
                        , Toast.LENGTH_SHORT).show();
                Intent intent = null;
                intent = new Intent(getApplicationContext(), TerminalActivity.class);
                startActivity(intent);
            }
        });
        verde.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.send(new byte[]{0}, false);
                Toast.makeText(getApplicationContext()
                        , "Bandeira verde selecionada."
                        , Toast.LENGTH_SHORT).show();
                Intent intent = null;
                intent = new Intent(getApplicationContext(), TerminalActivity.class);
                startActivity(intent);
            }
        });

    }
}

