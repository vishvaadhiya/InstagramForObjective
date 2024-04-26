package com.example.instagram.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


/*@AndroidEntryPoint*/
abstract class BaseActivity : AppCompatActivity() {
    abstract fun initComponents()

    @LayoutRes
    abstract fun defineLayout(): Int

    abstract fun postDataBinding(binding: ViewDataBinding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ViewDataBinding = DataBindingUtil.setContentView(this, defineLayout())
        postDataBinding(binding)
        initComponents()

    }


    fun replaceFragment(container: Int, fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(container, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    fun addFragment(container: Int, targetFragment: Fragment, sourceFragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(container, targetFragment, targetFragment.javaClass.simpleName)
            .hide(sourceFragment)
            .addToBackStack(targetFragment.javaClass.simpleName)
            .commit()
    }


}