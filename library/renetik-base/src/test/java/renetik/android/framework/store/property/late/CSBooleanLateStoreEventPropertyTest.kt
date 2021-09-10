package renetik.android.framework.store.property.late

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import renetik.android.framework.json.data.CSJsonObject

class CSBooleanLateStoreEventPropertyTest {

    private val store = CSJsonObject()

    @Test
    fun test() {
        var _value = true
        val property = CSBooleanLateStoreEventProperty(store, "key") {
            _value = it
        }
        assertTrue(_value)
        property.value = false
        assertFalse(_value)
        assertFalse(property.value)
    }
}