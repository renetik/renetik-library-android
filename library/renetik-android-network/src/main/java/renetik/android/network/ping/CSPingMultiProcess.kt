package renetik.android.network.ping

import renetik.android.network.data.CSServerMapData
import renetik.android.network.process.CSMultiProcessBase
import renetik.android.network.process.CSProcessBase
import renetik.android.network.process.CSServerWithPing

open class CSPingMultiProcess<Data : Any> : CSMultiProcessBase<Data> {

    constructor(server: CSServerWithPing, onPingDone: () -> CSProcessBase<Data>)
            : this(server, null, { addLast(onPingDone()) })

    constructor(server: CSServerWithPing, data: Data?, onPingDone: CSMultiProcessBase<Data>.() -> Unit)
            : super(data) {
        addedProcess = server.ping().onSuccess(onPingSuccess(onPingDone))
                .onFailed { addedProcess = server.ping().onDone(onPingSuccess(onPingDone)) }
    }

    private fun onPingSuccess(onPingDone: CSMultiProcessBase<Data>.() -> Unit)
            : (CSProcessBase<CSServerMapData>) -> Unit = { if (!it.isCanceled) onPingDone(this) }
}