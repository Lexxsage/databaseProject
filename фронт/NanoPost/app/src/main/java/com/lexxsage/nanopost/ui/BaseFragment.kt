package com.lexxsage.nanopost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

abstract class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment() {

    abstract val viewModel: BaseViewModel
    private var errorMessage: Snackbar? = null

    val viewLifecycleScope get() = viewLifecycleOwner.lifecycleScope

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val child = inflater.inflate(layoutId, container, false)
        return if (child is CoordinatorLayout) {
            child
        } else {
            val lparams = CoordinatorLayout.LayoutParams(child.layoutParams).apply {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
            CoordinatorLayout(inflater.context).apply {
                addView(child, lparams)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.errors().collect(::showError)
        }
    }

    open fun showError(e: Exception) {
        errorMessage?.dismiss()
        errorMessage = Snackbar.make(
            requireView(),
            e.localizedMessage ?: "An error occurred",
            Snackbar.LENGTH_SHORT,
        ).also { it.show() }
    }
}
