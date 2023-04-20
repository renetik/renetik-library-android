package renetik.android.ui.extensions

import renetik.android.core.lang.Func
import renetik.android.event.common.CSLaterOnceFunc.Companion.laterOnce
import renetik.android.event.registration.CSHasChange
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistrationsList
import renetik.android.ui.protocol.CSViewInterface


inline fun <Argument> CSHasChange<Argument>.onChangeAfterLayout(
    parent: CSViewInterface,
    crossinline function: Func
): CSRegistration {
    val registrations = CSRegistrationsList(this)
    val laterOnceFunction = registrations.laterOnce { function() }
    registrations.register(onChange {
        registrations.register(parent.registerAfterGlobalLayout {
            laterOnceFunction()
        })
    })
    return registrations
}