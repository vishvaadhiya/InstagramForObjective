package com.example.instagramforobjective.ui.settingsModule

import android.content.Intent
import android.net.Uri
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.base.BaseFragment
import com.example.instagramforobjective.ui.userManagement.login.LoginActivity
import com.example.instagramforobjective.utils.helpers.PreferenceHelper
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.FragmentSettingBinding
import com.google.firebase.auth.FirebaseAuth


class SettingFragment : BaseFragment() {

    lateinit var binding:FragmentSettingBinding
    val pHelper by lazy {
        PreferenceHelper(requireContext())
    }

    override fun defineLayout(): Int {
        return R.layout.fragment_setting
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentSettingBinding
        return this.binding
    }

    override fun initComponent() {

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.findViewById<TextView>(R.id.settingTV).text = getString(R.string.settings)


        binding.logOutCv.setOnClickListener {
            openAlertDialog()
        }
        binding.helpCv.setOnClickListener {
            openHelp()
        }
        binding.aboutCv.setOnClickListener {
            openAboutUs()
        }
    }
    private fun openAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(getString(R.string.log_out))
        alertDialogBuilder.setMessage(getString(R.string.are_you_sure_you_want_to_logout))

        alertDialogBuilder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            FirebaseAuth.getInstance().signOut()
            pHelper.clearPref()
            val intent = Intent(activity, LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }

        alertDialogBuilder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }



    private fun openAboutUs(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"))
        activity?.startActivity(intent)
    }

    private fun openHelp(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://help.instagram.com/155833707900388"))
        activity?.startActivity(intent)
    }
}