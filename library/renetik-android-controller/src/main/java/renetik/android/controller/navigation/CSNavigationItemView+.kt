package renetik.android.controller.navigation

import android.view.Gravity.CENTER
import android.view.View
import android.widget.FrameLayout.LayoutParams
import androidx.core.view.updateLayoutParams
import renetik.android.controller.navigation.CSNavigationItemAnimation.Fade
import renetik.android.controller.navigation.CSNavigationItemAnimation.None
import renetik.android.core.lang.CSLeakCanary
import renetik.android.ui.extensions.view.matchParent
import renetik.android.ui.extensions.view.passClicksUnder

fun <T : CSNavigationItemView> T.selected(button: View) = apply {
    button.isSelected = true
    onClose { button.isSelected = false }
}

fun <T : CSNavigationItemView> T.show(animation: CSNavigationItemAnimation = Fade) = apply {
    this.animation = if (CSLeakCanary.isEnabled) None else animation
    navigation!!.push(this)
    updateVisibility()
}

fun <T : CSNavigationItemView> T.center() = apply {
    isFullScreen = false
    animation = Fade
    viewContent.updateLayoutParams<LayoutParams> { gravity = CENTER }
}

fun <T : CSNavigationItemView> T.passClicksUnder(pass: Boolean = true) = apply {
    viewContent.passClicksUnder(pass)
}

val <T : CSNavigationItemView> T.isClicksBlocked get() = viewContent.isClickable

fun <T : CSNavigationItemView> T.fullScreen() = apply {
    isFullScreen = true
    viewContent.matchParent()
}
