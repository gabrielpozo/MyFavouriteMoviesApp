package com.light.finder.ui.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.light.finder.BuildConfig
import com.light.finder.R
import com.light.finder.extensions.newInstance
import com.light.finder.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_permission.*
import timber.log.Timber


class PermissionsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_permission, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        if (!hasPermissions(requireContext())) {
            initView()
        } else {
            mFragmentNavigation.pushFragment(CameraFragment.newInstance())
        }
    }

    private fun initView() {
        textViewEnableAccess.setOnClickListener {
            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                Timber.d("permission", "granted")
                mFragmentNavigation.pushFragment(CameraFragment.newInstance())
            } else {
                // deep link to settings

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_CALL_LOG
                    )
                ) {
                    Timber.d("permission", "denied")

                } else {


                    startActivity(
                        Intent(
                            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                        )
                    )

                }
            }
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 10
        private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

        //todo  move to extension
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}