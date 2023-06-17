package renetik.android.controller.base.dialog

import android.view.Gravity.START
import android.view.Gravity.TOP
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import androidx.core.view.updateLayoutParams
import renetik.android.controller.R
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.dialog.DialogAnimation.Fade
import renetik.android.controller.base.dialog.DialogAnimation.None
import renetik.android.controller.base.dialog.DialogAnimation.Slide
import renetik.android.controller.base.dialog.DialogAnimation.SlideFade
import renetik.android.controller.base.dialog.DialogPopupSide.Bottom
import renetik.android.controller.base.dialog.DialogPopupSide.Right
import renetik.android.controller.base.dialog.DialogPopupSide.Top
import renetik.android.controller.common.CSNavigationAnimation
import renetik.android.controller.common.CSNavigationItem
import renetik.android.controller.extensions.height
import renetik.android.controller.extensions.width
import renetik.android.core.extensions.content.color
import renetik.android.core.extensions.content.dpToPixelF
import renetik.android.core.kotlin.unexpected
import renetik.android.core.lang.CSLayoutRes
import renetik.android.core.lang.CSLayoutRes.Companion.layout
import renetik.android.core.lang.variable.isTrue
import renetik.android.core.lang.variable.setFalse
import renetik.android.core.lang.variable.setTrue
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.destruct
import renetik.android.event.fire
import renetik.android.event.listen
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.listenOnce
import renetik.android.ui.R.color
import renetik.android.ui.extensions.registerAfterGlobalLayout
import renetik.android.ui.extensions.registerHasSize
import renetik.android.ui.extensions.view.add
import renetik.android.ui.extensions.view.background
import renetik.android.ui.extensions.view.bottomFloat
import renetik.android.ui.extensions.view.heightWrap
import renetik.android.ui.extensions.view.leftFloat
import renetik.android.ui.extensions.view.locationInWindow
import renetik.android.ui.extensions.view.matchParent
import renetik.android.ui.extensions.view.onClick
import renetik.android.ui.extensions.view.rightFloat
import renetik.android.ui.extensions.view.topFloat

open class CSNavigationWindow<ViewType : View>(
    val parent: CSActivityView<out ViewGroup>, dialogContentLayout: CSLayoutRes
) : CSActivityView<FrameLayout>(
    parent.navigation!!, layout(R.layout.cs_navigation_dialog)
), CSNavigationItem {

    val dialogContent: ViewType = inflate(dialogContentLayout.id)
    var isPopup = false
    override var isFullscreenNavigationItem = property(false)
    var animation = Fade
    private val marginDp = 5

    private val eventOnClose = event()
    fun onClose(function: () -> Unit) = eventOnClose.listen(function)

    private var cancelOnTouchOut = true
    fun cancelOnTouchOut(cancel: Boolean = true) = apply { cancelOnTouchOut = cancel }

    init {
        passClicksUnder(false)
        listenOnce(parent.eventDestruct) {
            if (!isShowingInPager && lifecycleStopOnRemoveFromParentView) unexpected()
            if (isShowingInPager) close()
            if (!lifecycleStopOnRemoveFromParentView) destruct()
        }
    }

    override fun onViewReady() {
        super.onViewReady()
        view.background(color(color.cs_dialog_background))
        view.add(dialogContent)
    }

    override fun onViewShowingFirstTime() {
        super.onViewShowingFirstTime()
        if (cancelOnTouchOut) view.onClick { dismiss() }
    }

    protected open fun dismiss() = close()

    override fun onRemovedFromParentView() {
        super.onRemovedFromParentView()
        eventOnClose.fire()
    }

    override val pushAnimation
        get() = when (animation) {
            Slide, SlideFade -> CSNavigationAnimation.SlideInRight
            Fade -> CSNavigationAnimation.FadeIn
            None -> CSNavigationAnimation.None
        }

    override val popAnimation
        get() = when (animation) {
            Slide -> CSNavigationAnimation.SlideOutLeft
            Fade, SlideFade -> CSNavigationAnimation.FadeOut
            None -> CSNavigationAnimation.None
        }

    fun from(button: View, side: DialogPopupSide = Bottom) = apply {
        isPopup = true
        selectedButton(button)
        isFullscreenNavigationItem.setFalse()
        animation = Fade
        dialogContent.updateLayoutParams<LayoutParams> { gravity = START or TOP }
        registerHasSize {
            if (side == Bottom) positionDialogContentFromViewBottom(button)
            else if (side == Right) positionDialogContentFromViewRight(button)
            else if (side == Top) positionDialogContentFromViewTop(button)
            correctContentOverflow()
        }
        view.background(color(color.cs_dialog_popup_background))
    }

    private fun positionDialogContentFromViewBottom(fromView: View) {
        val fromViewLocation = fromView.locationInWindow
        val fromViewTopCenterX = fromViewLocation.x + (fromView.width / 2)
        var desiredX = fromViewTopCenterX.toFloat() - (dialogContent.width / 2)
        if (desiredX + dialogContent.width > width - dpToPixelF(marginDp))
            desiredX -= (desiredX + dialogContent.width) - (width - dpToPixelF(marginDp))
        if (desiredX < dpToPixelF(marginDp)) desiredX = dpToPixelF(marginDp)
        dialogContent.x = desiredX

        dialogContent.y = fromViewLocation.y.toFloat() + fromView.height
    }

    private val screenAvailableHeight get() = height - dpToPixelF(marginDp)
    private val screenAvailableWidth get() = width - dpToPixelF(marginDp)

    private fun positionDialogContentFromViewTop(fromView: View) {
        val fromViewLocation = fromView.locationInWindow
        val fromViewTopCenterX = fromViewLocation.x + (fromView.width / 2)
        var desiredX = fromViewTopCenterX.toFloat() - (dialogContent.width / 2)
        if (desiredX + dialogContent.width > screenAvailableWidth)
            desiredX -= (desiredX + dialogContent.width) - screenAvailableWidth
        if (desiredX < dpToPixelF(marginDp)) desiredX = dpToPixelF(marginDp)
        dialogContent.x = desiredX

        dialogContent.y = fromViewLocation.y.toFloat() - dialogContent.height - dpToPixelF(marginDp)
    }

    private fun correctContentOverflow() {
        if (dialogContent.bottomFloat > screenAvailableHeight)
            dialogContent.topFloat -= dialogContent.bottomFloat - screenAvailableHeight

        if (dialogContent.rightFloat > screenAvailableWidth)
            dialogContent.leftFloat -= dialogContent.rightFloat - screenAvailableWidth
    }

    private fun positionDialogContentFromViewRight(fromView: View) {
        val fromViewLocation = fromView.locationInWindow
        val fromViewLeftCenterY = fromViewLocation.y + (fromView.height / 2)
        var desiredY = fromViewLeftCenterY.toFloat() - (dialogContent.height / 2)
        if (desiredY + dialogContent.height > screenAvailableHeight)
            desiredY -= (desiredY + dialogContent.height) - screenAvailableHeight
        if (desiredY < dpToPixelF(marginDp)) desiredY = dpToPixelF(marginDp)
        dialogContent.x = fromViewLocation.x.toFloat() + fromView.width
        dialogContent.y = desiredY
    }

    fun fullScreen() = apply {
        isFullscreenNavigationItem.setTrue()
        animation = Slide
        dialogContent.matchParent()
    }

    fun wrapContentIfNotFullscreen() {
        if (isFullscreenNavigationItem.isTrue) return
        dialogContent.heightWrap()
        registerAfterGlobalLayout(::correctContentOverflow)
    }

}