package ie.wit.pedalconnect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.le.BluetoothLeScanner;


//public class MainActivity extends AppCompatActivity {
//    private Button search;
//    private Button connect;
//    private ListView listView;
//    private BluetoothAdapter mBTAdapter;
//    private static final int BT_ENABLE_REQUEST = 10; // This is the code we use for BT Enable
//    private static final int SETTINGS = 20;
//    private UUID mDeviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    private int mBufferSize = 50000; //Default
//    public static final String DEVICE_EXTRA = "com.example.lightcontrol.SOCKET";
//    public static final String DEVICE_UUID = "com.example.lightcontrol.uuid";
//    private static final String DEVICE_LIST = "com.example.lightcontrol.devicelist";
//    private static final String DEVICE_LIST_SELECTED = "com.example.lightcontrol.devicelistselected";
//    public static final String BUFFER_SIZE = "com.example.lightcontrol.buffersize";
//    private static final String TAG = "BlueTest5-MainActivity";
//
////    private BluetoothLeScanner bluetoothLeScanner = mBTAdapter.getBluetoothLeScanner();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        search = (Button) findViewById(R.id.search);
//        connect = (Button) findViewById(R.id.connect);
//
//        listView = (ListView) findViewById(R.id.listview);
//
//        if (savedInstanceState != null) {
//            ArrayList<BluetoothDevice> list = savedInstanceState.getParcelableArrayList(DEVICE_LIST);
//            if (list != null) {
//                initList(list);
//                MyAdapter adapter = (MyAdapter) listView.getAdapter();
//                int selectedIndex = savedInstanceState.getInt(DEVICE_LIST_SELECTED);
//                if (selectedIndex != -1) {
//                    adapter.setSelectedIndex(selectedIndex);
//                    connect.setEnabled(true);
//                }
//            } else {
//                initList(new ArrayList<BluetoothDevice>());
//            }
//
//        } else {
//            initList(new ArrayList<BluetoothDevice>());
//        }
//        search.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                mBTAdapter = BluetoothAdapter.getDefaultAdapter();
//
//                if (mBTAdapter == null) {
//                    Toast.makeText(getApplicationContext(), "Bluetooth not found", Toast.LENGTH_SHORT).show();
//                } else if (!mBTAdapter.isEnabled()) {
//                    Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableBT, BT_ENABLE_REQUEST);
//                } else {
//                    new SearchDevices().execute();
//                }
//            }
//        });
//
//        connect.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                BluetoothDevice device = ((MyAdapter) (listView.getAdapter())).getSelectedItem();
//                Intent intent = new Intent(getApplicationContext(), Controlling.class);
//                intent.putExtra(DEVICE_EXTRA, device);
//                intent.putExtra(DEVICE_UUID, mDeviceUUID.toString());
//                intent.putExtra(BUFFER_SIZE, mBufferSize);
//                startActivity(intent);
//            }
//        });
//
//
//
//    }
//
//    protected void onPause() {
//// TODO Auto-generated method stub
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//// TODO Auto-generated method stub
//        super.onStop();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case BT_ENABLE_REQUEST:
//                if (resultCode == RESULT_OK) {
//                    msg("Bluetooth Enabled successfully");
//                    new SearchDevices().execute();
//                } else {
//                    msg("Bluetooth couldn't be enabled");
//                }
//
//                break;
//            case SETTINGS: //If the settings have been updated
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//                String uuid = prefs.getString("prefUuid", "Null");
//                mDeviceUUID = UUID.fromString(uuid);
//                Log.d(TAG, "UUID: " + uuid);
//                String bufSize = prefs.getString("prefTextBuffer", "Null");
//                mBufferSize = Integer.parseInt(bufSize);
//
//                String orientation = prefs.getString("prefOrientation", "Null");
//                Log.d(TAG, "Orientation: " + orientation);
//                if (orientation.equals("Landscape")) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                } else if (orientation.equals("Portrait")) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                } else if (orientation.equals("Auto")) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//                }
//                break;
//            default:
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    /**
//     * Quick way to call the Toast
//     * @param str
//     */
//    private void msg(String str) {
//        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
//    }
//
//    /**
//     * Initialize the List adapter
//     * @param objects
//     */
//    private void initList(List<BluetoothDevice> objects) {
//        final MyAdapter adapter = new MyAdapter(getApplicationContext(), R.layout.list_item, R.id.lstContent, objects);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                adapter.setSelectedIndex(position);
//                connect.setEnabled(true);
//            }
//        });
//    }
//
//    /**
//     * Searches for paired devices. Doesn't do a scan! Only devices which are paired through Settings->Bluetooth
//     * will show up with this. I didn't see any need to re-build the wheel over here
//     * @author ryder
//     *
//     */
//    private class SearchDevices extends AsyncTask<Void, Void, List<BluetoothDevice>> {
//
//        @Override
//        protected List<BluetoothDevice> doInBackground(Void... params) {
//            Set<BluetoothDevice> pairedDevices = mBTAdapter.getBondedDevices();
//            List<BluetoothDevice> listDevices = new ArrayList<BluetoothDevice>();
//
//            for (BluetoothDevice device : pairedDevices) {
//                listDevices.add(device);
//            }
//            return listDevices;
//
//        }
//
//        @Override
//        protected void onPostExecute(List<BluetoothDevice> listDevices) {
//            super.onPostExecute(listDevices);
//            if (listDevices.size() > 0) {
//                MyAdapter adapter = (MyAdapter) listView.getAdapter();
//                adapter.replaceItems(listDevices);
//            } else {
//                msg("No paired devices found, please pair your serial BT device and try again");
//            }
//        }
//
//    }
//
//    /**
//     * Custom adapter to show the current devices in the list. This is a bit of an overkill for this
//     * project, but I figured it would be good learning
//     * Most of the code is lifted from somewhere but I can't find the link anymore
//     * @author ryder
//     *
//     */
//    private class MyAdapter extends ArrayAdapter<BluetoothDevice> {
//        private int selectedIndex;
//        private Context context;
//        private int selectedColor = Color.parseColor("#abcdef");
//        private List<BluetoothDevice> myList;
//
//        public MyAdapter(Context ctx, int resource, int textViewResourceId, List<BluetoothDevice> objects) {
//            super(ctx, resource, textViewResourceId, objects);
//            context = ctx;
//            myList = objects;
//            selectedIndex = -1;
//        }
//
//        public void setSelectedIndex(int position) {
//            selectedIndex = position;
//            notifyDataSetChanged();
//        }
//
//        public BluetoothDevice getSelectedItem() {
//            return myList.get(selectedIndex);
//        }
//
//        @Override
//        public int getCount() {
//            return myList.size();
//        }
//
//        @Override
//        public BluetoothDevice getItem(int position) {
//            return myList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        private class ViewHolder {
//            TextView tv;
//        }
//
//        public void replaceItems(List<BluetoothDevice> list) {
//            myList = list;
//            notifyDataSetChanged();
//        }
//
//        public List<BluetoothDevice> getEntireList() {
//            return myList;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View vi = convertView;
//            ViewHolder holder;
//            if (convertView == null) {
//                vi = LayoutInflater.from(context).inflate(R.layout.list_item, null);
//                holder = new ViewHolder();
//
//                holder.tv = (TextView) vi.findViewById(R.id.lstContent);
//
//                vi.setTag(holder);
//            } else {
//                holder = (ViewHolder) vi.getTag();
//            }
//
//            if (selectedIndex != -1 && position == selectedIndex) {
//                holder.tv.setBackgroundColor(selectedColor);
//            } else {
//                holder.tv.setBackgroundColor(Color.WHITE);
//            }
//            BluetoothDevice device = myList.get(position);
//            holder.tv.setText(device.getName() + "\n " + device.getAddress());
//
//            return vi;
//        }
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//// Inflate the menu; this adds items to the action bar if it is present.
//        //getMenuInflater().inflate(R.menu.homescreen, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
//                startActivityForResult(intent, SETTINGS);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}




//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothManager;
//import android.bluetooth.le.BluetoothLeScanner;
//import android.bluetooth.le.ScanCallback;
//import android.bluetooth.le.ScanFilter;
//import android.bluetooth.le.ScanResult;
//import android.bluetooth.le.ScanSettings;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Handler;
////import android.support.v7.app.AppCompatActivity;
//import android.text.method.ScrollingMovementMethod;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity implements BLEController.BLEControllerListener {
//
//    private BluetoothAdapter mBluetoothAdapter;
//    private BluetoothLeScanner mBluetoothLeScanner;
//
//    private boolean mScanning;
//
//    private static final int RQS_ENABLE_BLUETOOTH = 1;
//
//    Button btnScan;
//    ListView listViewLE;
//
//    List<BluetoothDevice> listBluetoothDevice;
//    ListAdapter adapterLeScanResult;
//
//    private Handler mHandler;
//    private static final long SCAN_PERIOD = 100000;
//
//    private TextView logView;
//
//    private BLEController bleController;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        this.logView = findViewById(R.id.logView);
//        this.logView.setMovementMethod(new ScrollingMovementMethod());
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//        this.bleController = BLEController.getInstance(this);
//
//        // Check if BLE is supported on the device.
//        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(this,
//                    "BLUETOOTH_LE not supported in this device!",
//                    Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//        getBluetoothAdapterAndLeScanner();
//
//        // Checks if Bluetooth is supported on the device.
//        if (mBluetoothAdapter == null) {
//            Toast.makeText(this,
//                    "bluetoothManager.getAdapter()==null",
//                    Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
////        btnScan = (Button)findViewById(R.id.scan);
////        btnScan.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                scanLeDevice(true);
////            }
////        });
//        listViewLE = (ListView)findViewById(R.id.lelist);
//
//        listBluetoothDevice = new ArrayList<>();
//        adapterLeScanResult = new ArrayAdapter<BluetoothDevice>(
//                this, android.R.layout.simple_list_item_1, listBluetoothDevice);
//        listViewLE.setAdapter(adapterLeScanResult);
//        listViewLE.setOnItemClickListener(scanResultOnItemClickListener);
//
//        mHandler = new Handler();
//
//        checkPermissions();
//
//    }
//
//    protected void onPause() {
//// TODO Auto-generated method stub
//        super.onPause();
//        this.bleController.removeBLEControllerListener(this);
//    }
//
//    //
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
//            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBTIntent, 1);
//        }
//        checkPermissions();
//    }
//
//    private void log(final String text) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                logView.setText(logView.getText() + "\n" + text);
//            }
//        });
//    }
//
//    private void checkBLESupport() {
//        // Check if BLE is supported on the device.
//        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
////                Toast.makeText(this, "BLE not supported!", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }
//
//    private void checkPermissions() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            log("\"Access Fine Location\" permission not granted yet!");
//            log("Whitout this permission Blutooth devices cannot be searched!");
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    42);
//        }
//    }
//
//    @Override
//    public void BLEControllerConnected() {
//        log("[BLE]\tConnected");
//    }
//
//    @Override
//    public void BLEControllerDisconnected() {
//        log("[BLE]\tDisconnected");
//    }
//
//    @Override
//    public void BLEDeviceFound(String name, String address) {
//        log("Device " + name + " found with address " + address);
//    }
//
//    //
//
//    AdapterView.OnItemClickListener scanResultOnItemClickListener =
//            new AdapterView.OnItemClickListener(){
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    final BluetoothDevice device = (BluetoothDevice) parent.getItemAtPosition(position);
//
//                    String msg = device.getAddress() + "\n"
//                            + device.getBluetoothClass().toString() + "\n"
//                            + getBTDevieType(device);
//
//                    new AlertDialog.Builder(MainActivity.this)
//                            .setTitle(device.getName())
//                            .setMessage(msg)
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            })
//                            .show();
//
//                }
//            };
//
//    private String getBTDevieType(BluetoothDevice d){
//        String type = "";
//
//        switch (d.getType()){
//            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
//                type = "DEVICE_TYPE_CLASSIC";
//                break;
//            case BluetoothDevice.DEVICE_TYPE_DUAL:
//                type = "DEVICE_TYPE_DUAL";
//                break;
//            case BluetoothDevice.DEVICE_TYPE_LE:
//                type = "DEVICE_TYPE_LE";
//                break;
//            case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
//                type = "DEVICE_TYPE_UNKNOWN";
//                break;
//            default:
//                type = "unknown...";
//        }
//
//        return type;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (!mBluetoothAdapter.isEnabled()) {
//            if (!mBluetoothAdapter.isEnabled()) {
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBtIntent, RQS_ENABLE_BLUETOOTH);
//            }
//        }
//
//        this.bleController.addBLEControllerListener(this);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            log("[BLE]\tSearching for BlueCArd...");
//            this.bleController.init();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == RQS_ENABLE_BLUETOOTH && resultCode == Activity.RESULT_CANCELED) {
//            finish();
//            return;
//        }
//
//        getBluetoothAdapterAndLeScanner();
//
//        // Checks if Bluetooth is supported on the device.
//        if (mBluetoothAdapter == null) {
//            Toast.makeText(this,
//                    "bluetoothManager.getAdapter()==null",
//                    Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private void getBluetoothAdapterAndLeScanner(){
//        // Get BluetoothAdapter and BluetoothLeScanner.
//        final BluetoothManager bluetoothManager =
//                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//        mBluetoothAdapter = bluetoothManager.getAdapter();
//        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
//
//        mScanning = false;
//    }
//
//    /*
//    to call startScan (ScanCallback callback),
//    Requires BLUETOOTH_ADMIN permission.
//    Must hold ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission to get results.
//     */
//    private void scanLeDevice(final boolean enable) {
//        if (enable) {
//            listBluetoothDevice.clear();
//            listViewLE.invalidateViews();
//
//            // Stops scanning after a pre-defined scan period.
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mBluetoothLeScanner.stopScan(scanCallback);
//                    listViewLE.invalidateViews();
//
//                    Toast.makeText(MainActivity.this,
//                            "Scan timeout",
//                            Toast.LENGTH_LONG).show();
//
//                    mScanning = false;
//                    btnScan.setEnabled(true);
//                }
//            }, SCAN_PERIOD);
//
////            mBluetoothLeScanner.startScan(scanCallback);
//
//            //scan specified devices only with ScanFilter
//            ScanFilter scanFilter =
//                    new ScanFilter.Builder()
//                            .setServiceUuid(BluetoothLeService.ParcelUuid_GENUINO101_ledService)
//                            .build();
//            List<ScanFilter> scanFilters = new ArrayList<ScanFilter>();
//            scanFilters.add(scanFilter);
//
//            ScanSettings scanSettings =
//                    new ScanSettings.Builder().build();
//
//            mBluetoothLeScanner.startScan(scanFilters, scanSettings, scanCallback);
//
//            mScanning = true;
//            btnScan.setEnabled(false);
//        } else {
//            mBluetoothLeScanner.stopScan(scanCallback);
//            mScanning = false;
//            btnScan.setEnabled(true);
//        }
//    }
//
//    private ScanCallback scanCallback = new ScanCallback() {
//        @Override
//        public void onScanResult(int callbackType, ScanResult result) {
//            super.onScanResult(callbackType, result);
//
//            addBluetoothDevice(result.getDevice());
//        }
//
//        @Override
//        public void onBatchScanResults(List<ScanResult> results) {
//            super.onBatchScanResults(results);
//            for(ScanResult result : results){
//                addBluetoothDevice(result.getDevice());
//            }
//        }
//
//        @Override
//        public void onScanFailed(int errorCode) {
//            super.onScanFailed(errorCode);
//            Toast.makeText(MainActivity.this,
//                    "onScanFailed: " + String.valueOf(errorCode),
//                    Toast.LENGTH_LONG).show();
//        }
//
//        private void addBluetoothDevice(BluetoothDevice device){
//            if(!listBluetoothDevice.contains(device)){
//                listBluetoothDevice.add(device);
////                listViewLE.invalidateViews();
//            }
//        }
//    };
//}

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.app.ActionBar.DISPLAY_SHOW_CUSTOM;

import ie.wit.pedalconnect.BluetoothLeService;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_BLUETOOTH_ADMIN_ID = 1;
    private static final int REQUEST_LOCATION_ID = 2;
    private static final int REQUEST_BLUETOOTH_ID = 3;
    private BluetoothAdapter bleAdapter;

    private String deviceName;
    private String deviceAddress;
    private BluetoothLeService bleService;
    private boolean connected = false;
    private ImageView reconnectView;
    private ProgressBar batteryStatus;
    private ProgressBar scanProgressBar;
    private TextView scanView;
    private SharedPreferences sharedPrefBLE;

    private ProgressBar xAccProgressView;
    private ProgressBar yAccProgressView;
    private ProgressBar zAccProgressView;
    private TextView xAccView;
    private TextView yAccView;
    private TextView zAccView;
    private TextView activityView;

    Button write;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefBLE = getSharedPreferences(getString(R.string.ble_device_key),Context.MODE_PRIVATE);
        deviceName = sharedPrefBLE.getString("name",null);
        deviceAddress = sharedPrefBLE.getString("address",null);

        bleCheck();
        locationCheck();

        // Use this check to determine whether BLE is supported on the device.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bleAdapter = bluetoothManager.getAdapter();
        // Checks if Bluetooth is supported on the device.
        if (bleAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeLayout();
        establishServiceConnection();

//        write =(Button)findViewById(R.id.write);
//        write.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("Hello there");
//                bleService.writeCustomCharacteristic(1);
//            }
//        });


//        BluetoothGatt gatt = null;
//
//        BluetoothGattCharacteristic one = null;
//        one.setValue("1");
//        gatt.writeCharacteristic(one);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bleService != null) {
            unbindService(serviceConnection);
            bleService = null;
        }
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bleService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!bleService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            bleService.connect(deviceAddress);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bleService = null;
        }
    };

    // Handles various events fired by the Service.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                connected = true;
                scanProgressBar.setVisibility(View.VISIBLE);
                scanView.setVisibility(View.GONE);
                reconnectView.setVisibility((View.GONE));

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                connected = false;
                scanProgressBar.setVisibility(View.GONE);
                scanView.setVisibility(View.GONE);
                reconnectView.setVisibility((View.VISIBLE));

            }

//            else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
//        }
//            else if (BluetoothLeService.ACTION_DATA_READ_COMPLETED.equals(action)) {
//                Log.d(TAG, "Data Read Completed");
//
//                updateUI();
//
//            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                if (intent.getStringExtra(BluetoothLeService.ACTION_BATTERY_LEVEL) != null) {
//                    Log.d(TAG, "Battery level on main activity: " + intent.getStringExtra(BluetoothLeService.ACTION_BATTERY_LEVEL));
//                }
//            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (bleService != null) {
            final boolean result = bleService.connect(deviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
//        intentFilter.addAction(BluetoothLeService.ACTION_DATA_READ_COMPLETED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public void establishServiceConnection() {
        if (deviceName != null && deviceAddress != null) {
            scanProgressBar.setVisibility(View.VISIBLE);
            scanView.setVisibility(View.GONE);
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    private void bleCheck() {
        if (ActivityCompat.checkSelfPermission(this, BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            // Bluetooth permission has not been granted.
            ActivityCompat.requestPermissions(this,new String[]{BLUETOOTH},REQUEST_BLUETOOTH_ID);
        }
        if (ActivityCompat.checkSelfPermission(this, BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            // Bluetooth admin permission has not been granted.
            ActivityCompat.requestPermissions(this, new String[]{BLUETOOTH_ADMIN}, REQUEST_BLUETOOTH_ADMIN_ID);
        }
    }

    private void locationCheck() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Location permission has not been granted.
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION_ID);
        }
    }

    private void updateUI() {
        SharedPreferences sharedPrefAgm = getSharedPreferences(getString(R.string.agm_key),Context.MODE_PRIVATE);
        String xAccAvg = sharedPrefAgm.getString("x_acc_avg", null);
        String yAccAvg = sharedPrefAgm.getString("y_acc_avg", null);
        String zAccAvg = sharedPrefAgm.getString("z_acc_avg", null);

        xAccView.setText(xAccAvg);
        yAccView.setText(yAccAvg);
        zAccView.setText(zAccAvg);

        xAccProgressView.setProgress(Integer.parseInt(xAccAvg));
        yAccProgressView.setProgress(Integer.parseInt(yAccAvg));
        zAccProgressView.setProgress(Integer.parseInt(zAccAvg));

        if (Integer.parseInt(xAccAvg) > 50 || Integer.parseInt(yAccAvg) > 50 || Integer.parseInt(zAccAvg) > 50) {
            activityView.setText("Moving");
        } else {
            activityView.setText("Still");
        }

        scanProgressBar.setVisibility(View.GONE);
    }

    public void openBleScanner() {
        Intent i = new Intent(this, RecyclerBleDeviceActivity.class);
        startActivity(i);
    }

    @SuppressLint("WrongConstant")
    public void initializeLayout() {
        this.getSupportActionBar().setDisplayOptions(DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        final View actionView = getSupportActionBar().getCustomView();

        batteryStatus = actionView.findViewById(R.id.batteryProgressBar);
        scanView = actionView.findViewById(R.id.scan);
        scanProgressBar = actionView.findViewById(R.id.scanInProgress);
        reconnectView = actionView.findViewById(R.id.reconnect);

//        xAccView = findViewById(R.id.x_acc);
//        yAccView = findViewById(R.id.y_acc);
//        zAccView = findViewById(R.id.z_acc);
//
//        xAccProgressView = findViewById(R.id.x_acc_progress);
//        yAccProgressView = findViewById(R.id.y_acc_progress);
//        zAccProgressView = findViewById(R.id.z_acc_progress);
//
//        activityView = findViewById(R.id.activity_summary);

        scanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBleScanner();
            }
        });

        reconnectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanProgressBar.setVisibility(View.VISIBLE);
                scanView.setVisibility(View.GONE);
                reconnectView.setVisibility(View.GONE);
                bleService.connect(deviceAddress);
            }
        });
    }
}