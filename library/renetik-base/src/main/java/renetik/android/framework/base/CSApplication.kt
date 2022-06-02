package renetik.android.framework.base

import android.app.Application
import android.content.Context
import android.os.Environment.getExternalStorageDirectory
import renetik.android.content.applicationLabel
import renetik.android.framework.json.store.CSFileJsonStore
import renetik.android.framework.logging.AndroidLogger
import renetik.android.framework.logging.CSLog.logInfo
import renetik.android.framework.logging.CSLog.logWarn
import renetik.android.framework.logging.CSLogger
import renetik.android.framework.store.CSStore
import renetik.kotlin.unexpected
import java.io.File

open class CSApplication : Application() {
    companion object {
        lateinit var app: CSApplication
    }

    open val name: String by lazy { applicationLabel }
    open val log: CSLogger by lazy { AndroidLogger() }

    open val externalFilesDir: File
        get() = getExternalFilesDir(null) ?: getExternalStorageDirectory()
    open val isDebugBuild: Boolean
        get() = unexpected("You need to override this if like to use it " +
                "in your implementation of CSApplication," +
                " because BuildConfig.DEBUG returns true only in debugged module")
    open val isDevelopmentMode get() = isDebugBuild

    lateinit var store: CSStore

    override fun attachBaseContext(context: Context) {
        store = CSFileJsonStore(context, "app", isJsonPretty = true)
        super.attachBaseContext(onAttachBaseContext(context))
    }

    protected open fun onAttachBaseContext(context: Context) = context

    override fun onCreate() {
        super.onCreate()
        app = this
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logWarn("onLowMemory")
    }

    override fun onTerminate() {
        super.onTerminate()
        logInfo("onTerminate")
    }
}