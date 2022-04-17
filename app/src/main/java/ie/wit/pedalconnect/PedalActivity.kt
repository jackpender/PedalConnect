package ie.wit.pedalconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import ie.wit.pedalconnect.databinding.ActivityPedalBinding
import ie.wit.pedalconnect.models.PresetModel
import kotlinx.android.synthetic.main.activity_pedal.*
import androidx.activity.result.ActivityResultLauncher
//import ie.wit.pedalconnect.SelectDeviceActivity

class PedalActivity : AppCompatActivity(), RotaryKnobView.RotaryKnobListener, LevelKnobView.LevelKnobListener,
    ToneKnobView.ToneKnobListener {

    private lateinit var binding: ActivityPedalBinding
    var preset = PresetModel()

    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedal)

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

        val preset = arrayOf("Preset 1", "Preset 2", "Preset 3", "Preset 4", "Preset 5")

        val adapter =
            ArrayAdapter(this@PedalActivity, android.R.layout.simple_spinner_dropdown_item, preset)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                Toast.makeText(this@PedalActivity, "You have Selected " + preset[p2], Toast.LENGTH_LONG)
                    .show()
                presetName.setText(preset[p2])
            }

        }


        val buttonClick = findViewById<Button>(R.id.btnAdd)

        buttonClick.setOnClickListener {
            val launcherIntent = Intent(this, MainActivity::class.java)
//            refreshIntentLauncher.launch(launcherIntent)
            startActivity(launcherIntent)
        }
//        TODO this is the issue, won't run when this is included, figure out how to do it correctly    ALSO FIGURE OUT HOW TO INSERT SCROLLVIEW IN LAYOUT FOR THE BUTTONS
    }

    override fun onVolumeRotate(value: Int) {
        volumeText.text = value.toString()
    }

    override fun onLevelRotate(value: Int){
        levelText.text = value.toString()
    }

    override fun onToneRotate(value: Int){
        toneText.text = value.toString()
    }
}