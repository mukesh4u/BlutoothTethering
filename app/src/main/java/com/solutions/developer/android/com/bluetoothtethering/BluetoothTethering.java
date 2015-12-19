package com.solutions.developer.android.com.bluetoothtethering;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


public class BluetoothTethering extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter = null;
    Class<?> classBluetoothPan = null;
    Constructor<?> BTPanCtor = null;
    Object BTSrvInstance = null;
    Class<?> noparams[] = {};
    Method mIsBTTetheringOn;
    public static Switch toggle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_tethering);

        toggle = (Switch) findViewById(R.id.wifi_switch);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    mBluetoothAdapter = getBTAdapter();
                    mBluetoothAdapter.enable();
                    Thread.sleep(100);
                    toggleTethering();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth_tethering, menu);
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

    public void toggleTethering() {
        Context MyContext = getApplicationContext();
        mBluetoothAdapter = getBTAdapter();
        String sClassName = "android.bluetooth.BluetoothPan";
        try {
            classBluetoothPan = Class.forName("android.bluetooth.BluetoothPan");
            mIsBTTetheringOn = classBluetoothPan.getDeclaredMethod("isTetheringOn", noparams);
            BTPanCtor = classBluetoothPan.getDeclaredConstructor(Context.class, BluetoothProfile.ServiceListener.class);
            BTPanCtor.setAccessible(true);

            BTSrvInstance = BTPanCtor.newInstance(MyContext, new BTPanServiceListener(MyContext));
            Thread.sleep(250);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private BluetoothAdapter getBTAdapter() {
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
            return BluetoothAdapter.getDefaultAdapter();
        else {
            BluetoothManager bm = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            return bm.getAdapter();
        }
    }

    public  static void changeToggleState(boolean state) {
        try{
            if(state){
                toggle.setChecked(BTPanServiceListener.state);
            }else {
                toggle.setChecked(BTPanServiceListener.state);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
