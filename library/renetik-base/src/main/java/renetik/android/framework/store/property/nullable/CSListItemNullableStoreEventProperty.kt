package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

class CSListItemNullableStoreEventProperty<T>(
    store: CSStoreInterface, key: String,
    val values: List<T>, default: T? = null, onChange: ((value: T?) -> Unit)? = null
) : CSNullableStoreEventProperty<T>(store, key, default, onChange) {
    override fun load(store: CSStoreInterface) = store.getValue(key, values)
    override fun save(store: CSStoreInterface,value: T?) {
        if (value == null) store.clear(key) else store.save(key, value.toId())
    }
}