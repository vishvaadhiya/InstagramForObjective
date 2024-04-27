package com.example.instagramforobjective.ui.profileManagement.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.instagramforobjective.base.BaseFragment
import com.example.instagramforobjective.base.FragmentContainerActivity
import com.example.instagramforobjective.ui.postManagement.addStory.StoryActivity
import com.example.instagramforobjective.ui.shared.AddPixActivity
import com.example.instagramforobjective.ui.profileManagement.adapter.ProfileViewPagerAdapter
import com.example.instagramforobjective.ui.profileManagement.shared.UserViewModel
import com.example.instagramforobjective.utils.customViews.ProgressDialog
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var profileViewPagerAdapter: ProfileViewPagerAdapter
    private val userInfoViewModel: UserViewModel by viewModels()
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val intent = Intent(requireContext(), StoryActivity::class.java)
                intent.putExtra(Constants.IMAGE_URI, uri.toString())
                activity?.startActivity(intent)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun initComponent() {
        ProgressDialog.getInstance(requireContext()).show()
//        ProgressDialog.showDialog(requireContext() as AppCompatActivity)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.findViewById<TextView>(R.id.customTV).text = getString(R.string.profile)
        getUserInfo()


        binding.shareProfile.setOnClickListener {
            Toast.makeText(context, "test", Toast.LENGTH_SHORT).show()
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody =
                getString(R.string.here_is_the_share_content_body_download_our_app_from_google_play_store_https_play_google_com_store_apps)
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }
        binding.editProfile.setOnClickListener {
            val intent = Intent(activity, FragmentContainerActivity::class.java)
            intent.putExtra("loadUpdateProfile", true)
            startActivity(intent)
        }

        profileViewPagerAdapter = ProfileViewPagerAdapter(childFragmentManager)
        profileViewPagerAdapter.addFragments(UserPostFragment(), getString(R.string.post))
        profileViewPagerAdapter.addFragments(UserReelFragment(), getString(R.string.reel))
        profileViewPagerAdapter.addFragments(UserSavedFragment(), getString(R.string.saved))
        binding.viewPager.adapter = profileViewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        ProgressDialog.getInstance(requireContext()).hide()
//        ProgressDialog.hideDialog()

    }

    private fun getUserInfo() {
        userInfoViewModel.fetchUserProfile()
        userInfoViewModel.userInfoLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.usernameTV.text = it.name
                if (it.image.isNotEmpty()) {
                    Glide.with(requireContext()).load(it.image).into(binding.profilePic)
                }
            }
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
                R.id.addPostProfileTv -> {
                    startActivity(Intent(requireActivity(), AddPixActivity::class.java))
                }

                R.id.storyProfileTv -> {
                    val intent = Intent(requireContext(), AddPixActivity::class.java)
                    intent.putExtra(Constants.SOURCE, Constants.STORY)
                    activity?.startActivity(intent)
                 /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        val intent = Intent(requireContext(), AddPixActivity::class.java)
                        intent.putExtra(Constants.SOURCE, Constants.STORY)
                        activity?.startActivity(intent)
                    }*/
                }
            }
        }
        view.findViewById<TextView>(R.id.addPostProfileTv)
            .setOnClickListener(attachmentOptionsClickListener)
        view.findViewById<TextView>(R.id.storyProfileTv)
            .setOnClickListener(attachmentOptionsClickListener)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }
}