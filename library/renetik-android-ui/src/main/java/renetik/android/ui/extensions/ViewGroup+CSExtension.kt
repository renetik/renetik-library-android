package renetik.android.ui.extensions

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import renetik.android.ui.protocol.CSViewInterface
import renetik.android.ui.extensions.view.add
import renetik.android.ui.extensions.view.remove

fun <View : CSViewInterface> ViewGroup.add(view: View): View {
    add(view.view)
    return view
}

fun <View : CSViewInterface> ViewGroup.add(view: View, index: Int = -1): View {
    add(view.view, index)
    return view
}

fun <View : CSViewInterface> ViewGroup.add(
    view: View, layout: LayoutParams, index: Int = -1): View {
    add(view.view, layout, index)
    return view
}

fun <View : CSViewInterface> ViewGroup.remove(view: View): View {
    remove(view.view)
    return view
}
