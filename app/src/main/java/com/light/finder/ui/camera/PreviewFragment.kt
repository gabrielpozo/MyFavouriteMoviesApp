package com.light.finder.ui.camera

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.light.finder.R
import com.light.finder.extensions.padWithDisplayCutout
import com.light.finder.ui.lightfinder.CategoriesFragment
import kotlinx.android.synthetic.main.fragment_preview.*
import java.io.File
import java.util.*
import android.graphics.BitmapFactory
import com.light.finder.extensions.EXTENSION_WHITELIST
import com.light.finder.extensions.newInstance
import com.light.finder.ui.BaseFragment
import java.io.ByteArrayOutputStream




class PreviewFragment internal constructor() : BaseFragment() {

    companion object {
        const val PREVIEW_ID_KEY = "PreviewFragment::id"
    }


    private lateinit var mediaList: MutableList<File>
    private var rootDirectory: File? = null

    inner class MediaPagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = mediaList.size
        override fun getItem(position: Int): Fragment = GalleryFragment.create(mediaList[position])
        override fun getItemPosition(obj: Any): Int = POSITION_NONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_preview, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            bundle.getString(PREVIEW_ID_KEY).let { path ->
                rootDirectory = File(path)
            }
        }

        mediaList = rootDirectory?.listFiles { file ->
            EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT))
        }?.sortedDescending()?.toMutableList() ?: mutableListOf()

        val mediaViewPager = photoViewPager.apply {
            offscreenPageLimit = 2
            adapter = MediaPagerAdapter(childFragmentManager)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            cutoutSafeArea.padWithDisplayCutout()
        }

        sendButton.setOnClickListener {
            mediaList.getOrNull(mediaViewPager.currentItem)?.let { mediaFile ->
                val base64 = encodeImage(mediaFile.absolutePath)
                navigateToProductList(base64)
            }
        }


        deleteButton.setOnClickListener {
            //TODO change it to cancel and navigate
            /* AlertDialog.Builder(view.context, android.R.style.Theme_Material_Dialog)
                 .setTitle(getString(R.string.delete_title))
                 .setMessage(getString(R.string.delete_dialog))
                 .setIcon(android.R.drawable.ic_dialog_alert)
                 .setPositiveButton(android.R.string.yes) { _, _ ->
                     mediaList.getOrNull(mediaViewPager.currentItem)?.let { mediaFile ->

                         mediaFile.delete()

                         MediaScannerConnection.scanFile(
                             view.context, arrayOf(mediaFile.absolutePath), null, null
                         )

                         mediaList.removeAt(mediaViewPager.currentItem)
                         mediaViewPager.adapter?.notifyDataSetChanged()

                         if (mediaList.isEmpty()) {
                             fragmentManager?.popBackStack()
                         }
                     }
                 }

                 .setNegativeButton(android.R.string.no, null)
                 .create().showImmersive()*/
        }
    }


    private fun navigateToProductList(base64: String) {
        mFragmentNavigation.pushFragment(CategoriesFragment.newInstance(base64))
    }

    //todo move this to use case
    private fun encodeImage(path: String): String {
        val bytes = File(path).readBytes()
        return resizeBase64Image(Base64.encodeToString(bytes, 0))
    }


    private fun resizeBase64Image(base64image: String): String {
        val encodeByte = Base64.decode(base64image.toByteArray(), Base64.DEFAULT)
        val options = BitmapFactory.Options()
        var image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size, options)


        if (image.height <= 600 && image.width <= 800) {
            return base64image
        }
        image = Bitmap.createScaledBitmap(image, 800, 600, false)

        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val b = baos.toByteArray()
        System.gc()
        return Base64.encodeToString(b, Base64.NO_WRAP)

    }
}
