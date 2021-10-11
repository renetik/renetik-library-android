package renetik.android.framework.store.property.nullable

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

abstract class CSNullableStoreEventProperty<T>(
    override var store: CSStoreInterface,
    override val key: String,
    private val defaultValue: T?,
    onApply: ((value: T?) -> Unit)? = null)
    : CSEventPropertyBase<T?>(onApply), CSStoreEventProperty<T?> {

    abstract fun load(store: CSStoreInterface): T?

    var isLoaded = false

    protected var _value: T? = null

    override var value: T?
        get() {
            if (!isLoaded) {
                _value = firstLoad()
                isLoaded = true
            }
            return _value
        }
        set(value) = value(value)

    protected fun firstLoad() = if (store.has(key)) load(store) else run {
        defaultValue?.let { save(store, it) }
        defaultValue
    }

    override fun value(newValue: T?, fire: Boolean) {
        if (value == newValue) return
        val before = value
        _value = newValue
        save(store, value)
        onApply?.invoke(newValue)
        if (fire) fireChange(before, newValue)
    }
}