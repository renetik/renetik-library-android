package renetik.android.client.request

import renetik.android.base.CSContextController
import renetik.android.java.event.event
import renetik.android.java.event.execute

open class CSOperation<Data : Any>() : CSContextController() {

    var executeProcess: (CSOperation<Data>.() -> CSProcess<Data>)? = null

    constructor(function: CSOperation<Data>.() -> CSProcess<Data>) : this() {
        executeProcess = function
    }

    open fun executeProcess(): CSProcess<Data> {
        return executeProcess!!.invoke(this)
    }

    private val eventSuccess = event<Data>()
    private val eventFailed = event<CSProcess<*>>()
    private val eventDone = event<Data?>()
    var process: CSProcess<Data>? = null
    var isForceNetwork = false

    fun forceNetwork() = apply { isForceNetwork = true }

    fun onSuccess(function: (argument: Data) -> Unit) =
            apply { eventSuccess.execute(function) }

    fun onFailed(function: (argument: CSProcess<*>) -> Unit) =
            apply { eventFailed.execute(function) }

    fun onDone(function: (argument: Data?) -> Unit) =
            apply { eventDone.execute(function) }

    fun send(): CSProcess<Data> = executeProcess().apply {
        process = this
        onSuccess {
            eventSuccess.fire(data!!)
            eventDone.fire(data)
        }
    }

    fun cancel() {
        process?.apply {
            if (isFailed) {
                eventFailed.fire(this)
                eventDone.fire(data)
            } else {
                cancel()
                eventDone.fire(data)
            }
        }
    }


}
