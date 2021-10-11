package renetik.android.framework.event.property

import renetik.android.framework.event.event
import renetik.android.framework.event.listen

abstract class CSEventPropertyBase<T>(
    protected val onApply: ((value: T) -> Unit)? = null) : CSEventProperty<T> {
    private val eventChange = event<CSPropertyChange<T>>()

    override fun onChanged(function: (before: T, after: T) -> Unit) =
        eventChange.listen { function(it.before, it.after) }

    protected fun fireChange(before: T, after: T) =
        eventChange.fire(CSPropertyChange<T>(before, after))

    override fun toString() = value.toString()

    open fun apply() = apply { onApply?.invoke(value) }
}