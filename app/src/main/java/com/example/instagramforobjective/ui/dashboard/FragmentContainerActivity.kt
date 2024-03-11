package com.example.instagramforobjective.ui.dashboard

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivityFragmentContainerBinding

class FragmentContainerActivity : BaseActivity() {

    lateinit var binding: ActivityFragmentContainerBinding
    override fun initComponents() {
        val loadUpdateProfile = intent.getBooleanExtra("loadUpdateProfile", false)
        if (loadUpdateProfile) {
            setCurrentFragment(UpdateProfileFragment())
        } else {
            setCurrentFragment(SettingFragment())
        }
    }

    override fun defineLayout(): Int {
        return R.layout.activity_fragment_container
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityFragmentContainerBinding
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
}