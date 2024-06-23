package com.example.instagramforobjective.ui.home

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.instagramforobjective.base.BaseActivity
import com.example.instagramforobjective.ui.post.bottomSheets.AddPostBottomSheetFragment
import com.example.instagramforobjective.ui.profile.fragment.ProfileFragment
import com.example.instagramforobjective.ui.reel.reelPreview.ReelFragment
import com.example.instagramforobjective.ui.search.SearchFragment
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var bottomNav: BottomNavigationView

    private lateinit var bottomSheetFragment: AddPostBottomSheetFragment

    override fun initComponents() {
        Log.d(javaClass.simpleName, "initComponents: MainActivity ")
        bottomNav = binding.bottomNavigationView
        setCurrentFragment(HomeFragment())


        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    setCurrentFragment(HomeFragment())
                    true
                }

                R.id.search -> {
                    setCurrentFragment(SearchFragment())
                    true
                }

                R.id.addPost -> {
                    loadDialogFragment()
                    true
                }

                R.id.reel -> {
                    setCurrentFragment(ReelFragment())
                    true
                }

                R.id.user -> {
                    setCurrentFragment(ProfileFragment())
                    true
                }

                else -> {
                    true
                }
            }
        }
    }

    override fun defineLayout(): Int {
        return R.layout.activity_main
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityMainBinding
    }

    private fun setCurrentFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
    }

    private fun loadDialogFragment() {
        bottomSheetFragment = AddPostBottomSheetFragment()
        bottomSheetFragment.show(supportFragmentManager, "AddFragment")
    }
}