package renetik.android.network.process

import renetik.android.core.lang.CSTimeConstants.Second
import renetik.android.event.process.CSProcess
import renetik.android.event.registration.registerLater
import renetik.android.json.obj.CSJsonObject
import renetik.android.json.obj.load
import renetik.android.json.array.CSJsonArray
import renetik.android.json.array.load

fun <T : CSJsonObject> T.mockProcessSuccess(data: String) =
	CSProcess<T>().apply {
		registerLater(1 * Second) { success(load(data)) }
	}

fun <T : CSJsonObject> T.mockProcessSuccess() =
	CSProcess<T>().apply {
		registerLater(1 * Second) { success(this@mockProcessSuccess) }
	}

fun <T : CSJsonArray> T.mockProcessSuccess(data: String) =
	CSProcess<T>().apply {
		registerLater(1 * Second) { success(load(data)) }
	}