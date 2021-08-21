package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.value.CSValueStoreEventProperty

class CSBooleanValueStoreEventProperty(
    store: CSStoreInterface, key: String, value: Boolean,
    onChange: ((value: Boolean) -> Unit)?)
    : CSValueStoreEventProperty<Boolean>(store, key, value, onChange) {
    override var _value = firstLoad()
    override fun loadNullable(store: CSStoreInterface) = store.getBoolean(key)
    override fun save(store: CSStoreInterface, value: Boolean) = store.save(key, value)
}