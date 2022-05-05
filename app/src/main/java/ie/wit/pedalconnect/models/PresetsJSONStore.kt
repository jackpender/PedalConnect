package ie.wit.pedalconnect.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
//import ie.wit.pedalconnect.PedalActivity
import ie.wit.pedalconnect.helpers.*
import ie.wit.pedalconnect.PedalActivity
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "presets.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<List<PresetModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class PresetJSONStore(private val context: Context) : PresetStore {

    var presets = mutableListOf<PresetModel>()
//    var courses = mutableListOf<CourseModel>()
//    var modules = mutableListOf<ModuleModel>()
//    var collegeListActivity = CollegeListActivity()

    lateinit var app: PedalActivity

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<PresetModel> {
        logAll()
        return presets
    }

    override fun findOne(id: Long) : PresetModel? {
        var foundCollege: PresetModel? = presets.find { p -> p.id == id }
        return foundCollege
    }

    override fun create(preset: PresetModel) {
        preset.id = generateRandomId()
        presets.add(preset)
        serialize()
    }

//    override fun createCourse(course: CourseModel) {
////        college.id = generateRandomId()
//        var college = app.colleges.findOne(collegeListActivity.collegeId1)
//        college!!.courses.add(course)
//        serialize()
//    }

    override fun update(preset: PresetModel) {
        var foundPreset = findOne(preset.id!!)
        if (foundPreset != null) {
            foundPreset.title = foundPreset.title
            foundPreset.volume = foundPreset.volume
            foundPreset.level = foundPreset.level
            foundPreset.tone = foundPreset.tone
        }
        serialize()
    }

    override fun delete(preset: PresetModel) {
        var foundPreset = findOne(preset.id!!)
        if(foundPreset != null){
            presets.remove(foundPreset)
        }
        serialize()
    }

    override fun serialize() {
        val jsonString = gsonBuilder.toJson(presets, listType)
        write(context, JSON_FILE, jsonString)
//        write(context,JSON_FILE,jsonCourseString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        val jsonCourseString = read(context,JSON_FILE)
        presets = gsonBuilder.fromJson(jsonString, listType)
//        courses = gsonBuilder.fromJson(jsonCourseString, listCourseType)
    }

    private fun logAll() {
        presets.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}