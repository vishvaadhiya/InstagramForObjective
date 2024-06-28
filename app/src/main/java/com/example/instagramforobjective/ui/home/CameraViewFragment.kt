package com.example.instagramforobjective.ui.home

import android.util.Log
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.R
import com.example.instagramforobjective.base.BaseFragment
import com.example.instagramforobjective.databinding.FragmentCameraViewBinding
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.hide
import io.ak1.pix.helpers.pixFragment
import io.ak1.pix.models.Options

class CameraViewFragment :  BaseFragment() {

    lateinit var binding : FragmentCameraViewBinding
    private var options = Options()

    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).binding.bottomNavigationView.hide()
    }

    override fun defineLayout(): Int {
        return R.layout.fragment_camera_view
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentCameraViewBinding
        return this.binding
    }

    override fun initComponent() {
        pixFragment(options){results ->
            when(results.status){
                PixEventCallback.Status.SUCCESS->{
                    Log.e("TAG", "initComponent: ${results.data}", )
                }

                PixEventCallback.Status.BACK_PRESSED -> {
                    Log.e("TAG", "initComponent: back pressed ${results.data}", )
                }
            }
        }
    }

}