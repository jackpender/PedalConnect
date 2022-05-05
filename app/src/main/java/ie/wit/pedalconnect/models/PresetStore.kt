package ie.wit.pedalconnect.models

interface PresetStore {
    fun findAll(): MutableList<PresetModel>
    fun findOne(id: Long): PresetModel?
    fun create(preset: PresetModel)
    fun update(preset: PresetModel)
    fun delete(preset: PresetModel)
    fun serialize()
}