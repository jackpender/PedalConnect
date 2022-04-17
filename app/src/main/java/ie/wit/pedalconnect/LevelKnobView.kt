package ie.wit.pedalconnect

import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.GestureDetectorCompat
import kotlin.math.atan2
import kotlinx.android.synthetic.main.level_knob_view.view.*

class LevelKnobView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), GestureDetector.OnGestureListener {
    private val gestureDetector: GestureDetectorCompat
    private var maxValue = 99
    private var minValue = 0
    var levelListener: LevelKnobListener? = null
    var value = 50
    private var levelKnobDrawable: Drawable? = null
    private var divider = 300f / (maxValue - minValue)

//    var knobImageView = findViewById<ImageView>(R.id.knobImageView) as ImageView

    init {
        this.maxValue = maxValue + 1

        LayoutInflater.from(context)
            .inflate(R.layout.level_knob_view, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LevelKnobView,
            0,
            0
        ).apply {
            try {
                minValue = getInt(R.styleable.LevelKnobView_levelMinValue, 0)
                maxValue = getInt(R.styleable.LevelKnobView_levelMaxValue, 100) + 1
                divider = 300f / (maxValue - minValue)
                value = getInt(R.styleable.LevelKnobView_levelInitialValue, 50)
                levelKnobDrawable = getDrawable(R.styleable.LevelKnobView_levelKnobDrawable)
//                knobImageView.setImageDrawable(knobDrawable)
            } finally {
                recycle()
            }
        }
        gestureDetector = GestureDetectorCompat(context, this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (gestureDetector.onTouchEvent(event))
            true
        else
            super.onTouchEvent(event)
    }

    override fun onDown(event: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onFling(arg0: MotionEvent, arg1: MotionEvent, arg2: Float, arg3: Float)
            : Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {}

    override fun onShowPress(e: MotionEvent) {}

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float)
            : Boolean {

        val rotationDegrees = calculateAngle(e2.x, e2.y)
        // use only -150 to 150 range (knob min/max points
        if (rotationDegrees >= -150 && rotationDegrees <= 150) {
            setKnobPosition(rotationDegrees)

            // Calculate rotary value
            // The range is the 300 degrees between -150 and 150, so we'll add 150 to adjust the
            // range to 0 - 300
            val valueRangeDegrees = rotationDegrees + 150
            value = ((valueRangeDegrees / divider) + minValue).toInt()
            if (levelListener != null) levelListener!!.onLevelRotate(value)
        }
        return true
    }

    private fun calculateAngle(x: Float, y: Float): Float {
        val px = (x / width.toFloat()) - 0.5
        val py = ( 1 - y / height.toFloat()) - 0.5
        var angle = -(Math.toDegrees(atan2(py, px)))
            .toFloat() + 90
        if (angle > 180) angle -= 360
        return angle
    }

    private fun setKnobPosition(angle: Float) {
        val matrix = Matrix()
        levelKnobImageView.scaleType = ImageView.ScaleType.MATRIX
        matrix.postRotate(angle, width.toFloat() / 2, height.toFloat() / 2)
        levelKnobImageView.imageMatrix = matrix
    }

    interface LevelKnobListener {
        fun onLevelRotate(value: Int)
    }
}