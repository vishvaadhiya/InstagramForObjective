package com.example.instagramforobjective.ui.reelManagement.reelPreview

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.instagramforobjective.base.BaseFragment
import com.example.instagramforobjective.data.models.Reel
import com.example.instagramforobjective.utils.customViews.ProgressDialog
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.FragmentReelBinding


class ReelFragment : BaseFragment() {

    lateinit var binding: FragmentReelBinding
    private lateinit var adapter: ReelAdapter
    private var reelList = ArrayList<Reel>()
    private val reelViewModel: ReelViewModel by viewModels()

    override fun defineLayout(): Int {
        return R.layout.fragment_reel
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentReelBinding
        return this.binding
    }

    override fun initComponent() {
        adapter = ReelAdapter(requireContext(), reelList)
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                Log.e(javaClass.simpleName, "onPageScrolled: $position")
            }
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                ProgressDialog.showDialog(requireContext() as AppCompatActivity)
                Log.e(javaClass.simpleName, "onPageSelected: $position")
            }
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                Log.e(javaClass.simpleName, "onPageScrollStateChanged: $state")

            }
        })
        reelViewModel.getUserReels()
        reelViewModel.reelLiveData.observe(viewLifecycleOwner) { reelList ->
            adapter.updateReel(reelList)
        }

    }

}
