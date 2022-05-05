package ie.wit.pedalconnect;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BLEController {
    private static BLEController instance;

    private BluetoothManager bluetoothManager;

    private HashMap<String, BluetoothDevice> devices = new HashMap<>();
    private BluetoothLeScanner scanner;


    private BLEController(Context ctx) {
        this.bluetoothManager = (BluetoothManager) ctx.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    public static BLEController getInstance(Context ctx) {
        if (null == instance)
            instance = new BLEController((ctx));

        return instance;
    }

    public interface BLEControllerListener {
        public void BLEControllerConnected();
        public void BLEControllerDisconnected();
        public void BLEDeviceFound(String name, String address);
    }


    private BluetoothDevice device;

    private ArrayList<BLEControllerListener> listeners = new ArrayList<>();

    public void addBLEControllerListener(BLEControllerListener l) {
        if(!this.listeners.contains(l))
            this.listeners.add(l);
    }

    public void removeBLEControllerListener(BLEControllerListener l) {
        this.listeners.remove(l);
    }

    private void fireDisconnected() {
        for(BLEControllerListener l : this.listeners)
            l.BLEControllerDisconnected();

        this.device = null;
    }

    private void fireConnected() {
        for(BLEControllerListener l : this.listeners)
            l.BLEControllerConnected();
    }

    private void fireDeviceFound(BluetoothDevice device) {
        for(BLEControllerListener l : this.listeners)
            l.BLEDeviceFound(device.getName().trim(), device.getAddress());
    }

    private ScanCallback bleCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            if(!devices.containsKey(device.getAddress()) && isThisTheDevice(device)) {
                deviceFound(device);
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for(ScanResult sr : results) {
                BluetoothDevice device = sr.getDevice();
                if(!devices.containsKey(device.getAddress()) && isThisTheDevice(device)) {
                    deviceFound(device);
                }
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.i("[BLE]", "scan failed with errorcode: " + errorCode);
        }
    };

    private boolean isThisTheDevice(BluetoothDevice device) {
        return null != device.getName() && device.getName().startsWith("BlueCArd");
    }

    private void deviceFound(BluetoothDevice device) {
        this.devices.put(device.getAddress(), device);
        fireDeviceFound(device);
    }

    public void init() {
        this.devices.clear();
        this.scanner = this.bluetoothManager.getAdapter().getBluetoothLeScanner();
        scanner.startScan(bleCallback);
    }
}


