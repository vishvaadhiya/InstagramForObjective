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
import com.bumptech.glide.Glide
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseFragment
import com.example.instagramforobjective.databinding.FragmentProfileBinding
import com.example.instagramforobjective.ui.dashboard.adapter.ViewPagerAdapter
import com.example.instagramforobjective.ui.model.UserModel
import com.example.instagramforobjective.ui.post.PostActivity
import com.example.instagramforobjective.ui.post.StoryActivity
import com.example.instagramforobjective.utility.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun initComponent() {
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
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }
        binding.editProfile.setOnClickListener {
            val intent = Intent(activity, FragmentContainerActivity::class.java)
            intent.putExtra("loadUpdateProfile", true)
            startActivity(intent)
        }

        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragments(UserPostFragment(), getString(R.string.post))
        viewPagerAdapter.addFragments(UserReelFragment(), getString(R.string.reel))
        viewPagerAdapter.addFragments(UserSavedFragment(), getString(R.string.saved))
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)


    }

    private fun getUserInfo() {
        val currentUser = getCurrentUserId() ?: return
        Firebase.firestore.collection(Constants.USER).document(currentUser).get()
            .addOnSuccessListener {
                val user: UserModel = it.toObject<UserModel>()!!
                binding.usernameTV.text = user.name
                if (!user.image.isNullOrEmpty()) {
                    Glide.with(requireContext()).load(user.image).into(binding.profilePic)
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
                    startActivity(Intent(requireActivity(), PostActivity::class.java))
                }

                R.id.storyProfileTv -> {
                    startActivity(Intent(requireActivity(), StoryActivity::class.java))
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

    private fun getCurrentUserId(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid
    }
}