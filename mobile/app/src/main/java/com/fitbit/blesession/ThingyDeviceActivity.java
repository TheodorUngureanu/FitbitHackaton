package com.fitbit.blesession;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import no.nordicsemi.android.thingylib.ThingyListener;
import no.nordicsemi.android.thingylib.ThingyListenerHelper;
import no.nordicsemi.android.thingylib.ThingySdkManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fitbit.blesession.bluetooth.ButtonThingyListener;
import com.fitbit.blesession.bluetooth.MyThingyService;

import java.util.ArrayList;
import java.util.List;

public class ThingyDeviceActivity extends AppCompatActivity implements  ThingySdkManager.ServiceConnectionListener {
    private static final String TAG = ThingyDeviceActivity.class.getName();
    private ThingySdkManager thingySdkManager;
    private BluetoothDevice bluetoothDevice;
    private ThingyListener thingyListener;
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    List<String> myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thingy_device);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();
        bluetoothDevice = intent.getParcelableExtra(MainActivity.BLUETOOTH_DEVICE_KEY);

        thingySdkManager = ThingySdkManager.getInstance();
        Log.d(TAG, "Got this in extras: " + bluetoothDevice);
        Toast.makeText(getApplicationContext(),"This is my page",Toast.LENGTH_LONG);


        thingySdkManager.enableAirQualityNotifications(bluetoothDevice, true);
        thingyListener = new ButtonThingyListener(thingySdkManager, this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<String>> getCall = Api.Service.Get().getWeather();
                getCall.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        myDataset = response.body();
                        mAdapter.setList(myDataset);
                        mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        thingySdkManager.bindService(this, MyThingyService.class);
       ThingyListenerHelper.registerThingyListener(this, thingyListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
        thingySdkManager.unbindService(this);
        ThingyListenerHelper.unregisterThingyListener(this, thingyListener);
    }

    @Override
    public void onServiceConnected() {
        Log.d(TAG, "Thingy Service Connected");
        thingySdkManager.connectToThingy(this, bluetoothDevice, MyThingyService.class);
    }


}
