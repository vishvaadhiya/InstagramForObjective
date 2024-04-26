package com.example.instagram.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    @LayoutRes
    abstract fun defineLayout(): Int

    abstract fun postDataBinding(binding: ViewDataBinding): ViewDataBinding

    abstract fun initComponent()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var mViewDataBinding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, defineLayout(), container, false)
        mViewDataBinding = postDataBinding(mViewDataBinding)
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
    }


    protected fun replaceFragment(container: Int, fragment: Fragment) {
        (requireActivity() as BaseActivity).replaceFragment(container, fragment)
    }

    protected fun addFragment(container: Int,targetFragment: Fragment, sourceFragment: Fragment) {
        (requireActivity() as BaseActivity).addFragment(container, targetFragment,sourceFragment)
    }


}