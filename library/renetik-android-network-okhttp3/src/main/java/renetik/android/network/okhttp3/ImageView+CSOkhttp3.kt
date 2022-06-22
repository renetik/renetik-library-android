package renetik.android.network.okhttp3

import android.widget.ImageView
import com.androidnetworking.error.ANError
import com.androidnetworking.internal.ANImageLoader
import com.androidnetworking.internal.ANImageLoader.ImageContainer
import com.androidnetworking.internal.ANImageLoader.ImageListener
import renetik.android.ui.extensions.view.hasSize

fun <T : ImageView> T.image(url: String, defaultImageId: Int? = null) = hasSize {
    ANImageLoader.getInstance().get(url,
        object : ImageListener {
            override fun onResponse(response: ImageContainer, isImmediate: Boolean) {
                response.bitmap?.let { setImageBitmap(response.bitmap) }
                    ?: defaultImageId?.apply { setImageResource(this) }
            }

            override fun onError(error: ANError) {
                defaultImageId?.apply { setImageResource(this) }
            }
        }, width, height, scaleType)
}