package renetik.android.framework.preset.property.value

import renetik.android.framework.CSEventOwnerHasDestroy
import renetik.android.framework.preset.CSPreset
import renetik.android.framework.store.CSStoreInterface

class CSIntListValuePresetEventProperty(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>, key: String,
    override val default: List<Int>,
    onChange: ((value: List<Int>) -> Unit)? = null)
    : CSValuePresetEventProperty<List<Int>>(parent, preset, key, onChange) {

    override var _value = load()

    override fun get(store: CSStoreInterface) = store.get(key)?.split(",")
        ?.map { it.toInt() } ?: default

    override fun set(store: CSStoreInterface, value: List<Int>) =
        store.set(key, value.joinToString(",") { it.toString() })
}