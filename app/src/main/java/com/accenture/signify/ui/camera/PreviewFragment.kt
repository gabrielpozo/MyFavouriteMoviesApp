package com.accenture.signify.ui.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.accenture.signify.R
import com.accenture.signify.extensions.padWithDisplayCutout
import com.accenture.signify.extensions.showImmersive
import com.accenture.signify.ui.lightfinder.CategoriesFragment
import kotlinx.android.synthetic.main.fragment_preview.*
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.*


val EXTENSION_WHITELIST = arrayOf("JPG")

class PreviewFragment internal constructor() : Fragment() {

    private val args: PreviewFragmentArgs by navArgs()

    private lateinit var mediaList: MutableList<File>

    inner class MediaPagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = mediaList.size
        override fun getItem(position: Int): Fragment = GalleryFragment.create(mediaList[position])
        override fun getItemPosition(obj: Any): Int = POSITION_NONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        val rootDirectory = File(args.rootDirectory)

        mediaList = rootDirectory.listFiles { file ->
            EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT))
        }?.sortedDescending()?.toMutableList() ?: mutableListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_preview, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mediaViewPager = photoViewPager.apply {
            offscreenPageLimit = 2
            adapter = MediaPagerAdapter(childFragmentManager)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            cutoutSafeArea.padWithDisplayCutout()
        }

        backButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigateUp()
        }

        sendButton.setOnClickListener {
            mediaList.getOrNull(mediaViewPager.currentItem)?.let { mediaFile ->
                val base64 = encodeImage(mediaFile.absolutePath)
                navigateToProductList(base64)

            }
        }


        deleteButton.setOnClickListener {
            AlertDialog.Builder(view.context, android.R.style.Theme_Material_Dialog)
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
                .create().showImmersive()
        }
    }


    private fun navigateToProductList(base64: String) {
        findNavController().navigate(
            R.id.action_preview_fragment_to_categoriesFragment,
            bundleOf(CategoriesFragment.CATEGORIES_ID_KEY to base64)
        )
    }

    //todo move this to use case
    private fun encodeImage(path: String): String {
        val imageFile = File(path)
        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(imageFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        val bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)


    }

}
