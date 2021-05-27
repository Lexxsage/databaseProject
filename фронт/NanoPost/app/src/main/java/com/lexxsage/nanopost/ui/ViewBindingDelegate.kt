package com.lexxsage.nanopost.ui

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T : ViewBinding> Fragment.viewBinding(bindingProvider: (View) -> T): ReadOnlyProperty<Any, T> {
    return ViewBindingDelegate(this, bindingProvider)
}

private class ViewBindingDelegate<T : ViewBinding>(
    private val fragment: Fragment,
    private val bindingProvider: (View) -> T
) : ReadOnlyProperty<Any, T> {

    var binding: T? = null

    init {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
            viewLifecycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy() {
                    binding = null
                }
            })
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        val aBinding = binding
        if (aBinding != null) return aBinding
        val view = fragment.view ?: error("Attempt to use binding before onViewCreated()")
        return bindingProvider(view).also { binding = it }
    }
}
