package com.example.electrum.electrum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button conectar;
    Button sobre;
    BluetoothSPP bt;
    byte bandeira [] = new byte[0];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        conectar = (Button)
                findViewById(R.id.button_conectar);
        conectar.setOnClickListener(this);
        sobre = (Button)
                findViewById(R.id.button_sobre);
        sobre.setOnClickListener(this);
    }
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        bt = new BluetoothSPP(this);
        switch (id) {
            case R.id.button_conectar:
                if(!bt.isBluetoothAvailable()) {
                    Toast.makeText(getApplicationContext()
                            , "Bluetooth não está disponível"
                            , Toast.LENGTH_SHORT).show();
                    finish();
                }if (!bt.isBluetoothEnabled()) {
                bt.enable();
                }
                intent = new Intent(getApplicationContext(), DeviceListActivity.class);
                startActivity(intent);
                break;
            case R.id.button_sobre:
                intent = new Intent(getApplicationContext(), Sobre.class);
                startActivity(intent);
                break;
        }
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
}
