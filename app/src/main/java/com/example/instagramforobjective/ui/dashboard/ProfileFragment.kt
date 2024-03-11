package com.example.instagramforobjective.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseFragment
import com.example.instagramforobjective.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileBinding


    override fun initComponent() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.findViewById<TextView>(R.id.customTV).text = getString(R.string.profile)
        binding.shareProfile.setOnClickListener {
            Toast.makeText(context, "test", Toast.LENGTH_SHORT).show()
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody =
                getString(R.string.here_is_the_share_content_body_download_our_app_from_google_play_store_https_play_google_com_store_apps)
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }
        binding.editProfile.setOnClickListener {
            val intent = Intent(activity, FragmentContainerActivity::class.java)
            intent.putExtra("loadUpdateProfile", true)
            startActivity(intent)
        }
    }

    override fun defineLayout(): Int {
        return R.layout.fragment_profile
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentProfileBinding
        return this.binding
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                addPostClick()
                return true
            }

            R.id.setting -> {
                startActivity(Intent(context, FragmentContainerActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("CommitTransaction")
    private fun addPostClick() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view =
            layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        val attachmentOptionsClickListener = View.OnClickListener { view ->
            if (bottomSheetDialog.isShowing) bottomSheetDialog.dismiss()
            when (view.id) {
                R.id.addPostTv -> {
                    (requireActivity() as MainActivity).binding.bottomNavigationView.selectedItemId =
                        R.id.addPost
                    val fragmentTransaction = requireFragmentManager().beginTransaction()
                    fragmentTransaction.replace(R.id.addPost, AddPostFragment())
                }
            }
        }
        view.findViewById<TextView>(R.id.addPostTv)
            .setOnClickListener(attachmentOptionsClickListener)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

}