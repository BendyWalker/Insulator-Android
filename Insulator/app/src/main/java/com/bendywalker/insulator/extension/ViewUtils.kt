package com.bendywalker.insulator.extension

import android.app.Activity
import android.support.v4.app.Fragment
import android.view.View
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by Ben David Walker on 18/05/2017.
 */

fun <V : View> Fragment.viewId(id: Int) = BoundView<V>(id)

@Suppress("UNCHECKED_CAST")
class BoundView<V: View>(val id: Int): ReadOnlyProperty<Fragment, View> {
    private var view: V? = null

    override operator fun getValue(thisRef: Fragment, property: KProperty<*>): V {
        if (view == null) {
            view = thisRef.view?.findViewById(id) as V
        }

        return view!!
    }
}