package renetik.android.framework.preset

import renetik.android.framework.CSModelBase
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

class CSPresetStoreItem<PresetItem : CSPresetItem,
        PresetList : CSPresetItemList<PresetItem>>(
    override val preset: CSPreset<PresetItem, PresetList>,
    val parentStore: CSStoreInterface
) : CSModelBase(preset), CSEventProperty<PresetItem>, CSPresetKeyData {

    override val key = "${preset.id} current"
    override fun saveTo(store: CSStoreInterface) = store.set(key, value.toId())

    val values get() = preset.list.items

    var _value: PresetItem = loadValue()

    private fun loadValue(): PresetItem {
        return parentStore.getValue(key, values) ?: values[0]
    }

    private val eventChange = event<PresetItem>()

    val parentStoreChanged = register(parentStore.eventChanged.listen {
        val newValue = loadValue()
        if (_value == newValue) return@listen
        _value = newValue
        eventChange.fire(newValue)
    })

    override fun value(newValue: PresetItem, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
        parentStoreChanged.pause().use {
            saveTo(parentStore)
            if (fire) eventChange.fire(newValue)
            preset.reload(newValue)
        }
    }

    override fun onChange(function: (PresetItem) -> Unit) = eventChange.listen(function)

    override var value: PresetItem
        get() = _value
        set(value) = value(value)

    override fun toString() = "${super.toString()}, value:$value"
}