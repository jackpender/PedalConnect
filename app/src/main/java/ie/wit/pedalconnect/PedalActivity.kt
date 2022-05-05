package ie.wit.pedalconnect

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.ActionBar
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.content.*
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.*
import ie.wit.pedalconnect.databinding.ActivityPedalBinding
import ie.wit.pedalconnect.models.PresetModel
import kotlinx.android.synthetic.main.activity_pedal.*
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import ie.wit.pedalconnect.MainActivity

//import ie.wit.pedalconnect.SelectDeviceActivity

import ie.wit.pedalconnect.BluetoothLeService;
import ie.wit.pedalconnect.models.PresetJSONStore
import ie.wit.pedalconnect.models.PresetStore
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class PedalActivity : AppCompatActivity(), RotaryKnobView.RotaryKnobListener, LevelKnobView.LevelKnobListener,
    ToneKnobView.ToneKnobListener {

    private lateinit var binding: ActivityPedalBinding
    var preset = PresetModel()
    lateinit var presets: PresetStore

    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    val main: MainActivity = MainActivity()

    //BLE VARIABLES
    private val TAG: String? =
        PedalActivity::class.java.getSimpleName()

    private val REQUEST_BLUETOOTH_ADMIN_ID: Int = 1
    private val REQUEST_LOCATION_ID = 2
    private val REQUEST_BLUETOOTH_ID = 3
    private var bleAdapter: BluetoothAdapter? = null

    private var deviceName: String? = null
    private var deviceAddress: String? = null
    private var bleService: BluetoothLeService? = null;
    private var connected = false
    private val reconnectView: ImageView? = null
    private val batteryStatus: ProgressBar? = null
    private val scanProgressBar: ProgressBar? = null
    private val scanView: TextView? = null
    private var sharedPrefBLE: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedal)
        presets = PresetJSONStore(applicationContext)

        var presetList = mutableListOf<PresetModel>()

        presetList = presets.findAll()

        var notInList: Boolean

        notInList = true

        val btnAdd = findViewById<Button>(R.id.btnAdd) as Button

//    val knob = findViewById<RotaryKnobView>(R.id.knob) as RotaryKnobView
        val volumeText = findViewById<TextView>(R.id.volumeText) as TextView
        val levelText = findViewById<TextView>(R.id.levelText) as TextView
        val toneText = findViewById<TextView>(R.id.toneText) as TextView

        volumeKnob.listener = this
        levelKnob.levelListener = this
        toneKnob.toneListener = this

        volumeText.text = volumeKnob.value.toString()
        levelText.text = levelKnob.value.toString()
        toneText.text = toneKnob.value.toString()

        val spinner = findViewById<Spinner>(R.id.presets) as Spinner
        val presetName = findViewById<TextView>(R.id.presetName) as TextView

        val presetMenu = arrayOf("Preset 1", "Preset 2", "Preset 3", "Preset 4", "Preset 5")

        val adapter =
            ArrayAdapter(this@PedalActivity, android.R.layout.simple_spinner_dropdown_item, presetMenu)

        spinner.adapter = adapter

        loadPresets()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                Toast.makeText(
                    this@PedalActivity,
                    "You have Selected " + presetMenu[p2],
                    Toast.LENGTH_LONG
                )
                    .show()

                presetName.setText(presetMenu[p2])
                for(i in presetList){
                    if(i.title == presetMenu[p2]) {
                        volumeText.text = i.volume.toString()
                        levelText.text = i.level.toString()
                        toneText.text = i.tone.toString()
                        onVolumeRotate(i.volume.toInt())
//                        onLevelRotate(i.level.toInt())
                    }
                }
                Log.w(TAG, "All saved presets: " + "${presets.findAll()}")
            }

        }

//        for(i in presets)
//        }

        sharedPrefBLE = getSharedPreferences(getString(R.string.ble_device_key), MODE_PRIVATE)
        deviceName = sharedPrefBLE!!.getString("name", null)
        deviceAddress = sharedPrefBLE!!.getString("address", null)

        bleCheck()
        locationCheck()


        // Use this check to determine whether BLE is supported on the device.
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
            finish()
        }

        // Initializes a Bluetooth adapter.

        // Initializes a Bluetooth adapter.
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bleAdapter = bluetoothManager.adapter
        // Checks if Bluetooth is supported on the device.
        // Checks if Bluetooth is supported on the device.
        if (bleAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initializeLayout()
        establishServiceConnection()


        btnAdd.setOnClickListener() {
//            Log.w(TAG, "Save Button Pressed: " + "${preset}")
            for(i in presetList) {
                if(i.title == presetName.text.toString()) {
//                    preset.title = presetName.text.toString()
                    i.volume = volumeKnob.value.toDouble()
                    i.level = levelKnob.value.toDouble()
                    i.tone = toneKnob.value.toDouble()
                    presets.update(i.copy())
                    notInList = true
                }
                else{
                    notInList = false
                }
            }
            if(notInList == false){
                preset.title = presetName.text.toString()
                preset.volume = volumeKnob.value.toDouble()
                preset.level = levelKnob.value.toDouble()
                preset.tone = toneKnob.value.toDouble()
                presets.create(preset.copy())
            }
            onVolumeRotate(volumeText.text.toString().toInt())
//            onLevelRotate(levelText.text.toString().toInt())
//            onToneRotate(preset.tone.toInt())
            Log.w(TAG, "Save Button Pressed: " + "${preset}")
            Log.w(TAG, "All saved presets: " + "${presets.findAll()}")
        }
    }

    private fun loadPresets() {
        presets.findAll()
    }

    override fun onVolumeRotate(value: Int) {
        volumeText.text = value.toString()
//        bleService!!.writeCustomService("7a860abe-d7c1-437a-b477-8241f95afdaa")

//        bleService!!.writeKnobCharacteristic("v")
        if(connected) {
            var uuid = UUID.fromString("7a860abe-d7c1-437a-b477-8241f95afdaa")
            bleService!!.writeCustomCharacteristic(uuid, value)
        }
    }

    override fun onLevelRotate(value: Int){
        levelText.text = value.toString()

        if(connected) {
            var uuid = UUID.fromString("1d23c7a0-c3f1-4b7e-a103-66d47427ba14")
            bleService!!.writeCustomCharacteristic(uuid, value)
        }
    }

    override fun onToneRotate(value: Int){
        toneText.text = value.toString()

        if(connected) {
            var uuid = UUID.fromString("da53ecb6-8a0f-4142-b7e1-40a241a55fc0")
            bleService!!.writeCustomCharacteristic(uuid, value)
        }
    }



    //BLE METHODS
    override protected open fun onDestroy() {
        super.onDestroy()
        if (bleService != null) {
            unbindService(serviceConnection)
            bleService = null
        }
    }

    // Code to manage Service lifecycle.
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            bleService = (service as BluetoothLeService.LocalBinder).getService()
            if (!bleService!!.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth")
                finish()
            }
            // Automatically connects to the device upon successful start-up initialization.
            bleService!!.connect(deviceAddress)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            bleService = null
        }
    }

    // Handles various events fired by the Service.
    private val mGattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                connected = true
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                connected = false
            }
        }
    }

    override protected open fun onResume() {
        super.onResume()
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter())
        if (bleService != null) {
            val result: Boolean = bleService!!.connect(deviceAddress)
            Log.d(TAG, "Connect request result=$result")
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mGattUpdateReceiver)
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter? {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED)
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED)
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE)
        return intentFilter
    }

    private fun establishServiceConnection() {
        if (deviceName != null && deviceAddress != null) {
            val gattServiceIntent = Intent(this, BluetoothLeService::class.java)
            bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE)
        }
    }

    private fun bleCheck() {
        if (ActivityCompat.checkSelfPermission(
                this,
                permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Bluetooth permission has not been granted.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.BLUETOOTH),
                REQUEST_BLUETOOTH_ID
            )
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Bluetooth admin permission has not been granted.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.BLUETOOTH_ADMIN),
                REQUEST_BLUETOOTH_ADMIN_ID
            )
        }
    }

    private fun locationCheck() {
        if (ActivityCompat.checkSelfPermission(
                this,
                permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Location permission has not been granted.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_ID
            )
        }
    }

    fun openBleScanner() {
        val i = Intent(this, RecyclerBleDeviceActivity::class.java)
        startActivity(i)
    }

    @SuppressLint("WrongConstant")
    fun initializeLayout() {
        this.supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.actionbar)
        val actionView = supportActionBar!!.customView
        val scanView = actionView.findViewById<TextView>(R.id.scan)
        val scanProgressBar = actionView.findViewById<ProgressBar>(R.id.scanInProgress)
        val reconnectView = actionView.findViewById<ImageView>(R.id.reconnect)
        scanView.setOnClickListener(View.OnClickListener { openBleScanner() })
        reconnectView.setOnClickListener(View.OnClickListener {
            scanProgressBar!!.setVisibility(View.VISIBLE)
            scanView.setVisibility(View.GONE)
            reconnectView.setVisibility(View.GONE)
            bleService!!.connect(deviceAddress)
        })
    }
}