package com.accenture.signify.ui.camera

import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.accenture.signify.R
import com.accenture.signify.extensions.padWithDisplayCutout
import com.accenture.signify.extensions.showImmersive
import com.android.example.camerax.fragments.GalleryFragmentArgs
import kotlinx.android.synthetic.main.fragment_preview.*
import java.io.File
import java.util.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


val EXTENSION_WHITELIST = arrayOf("JPG")

class PreviewFragment internal constructor() : Fragment() {

    private val args: GalleryFragmentArgs by navArgs()

    private lateinit var mediaList: MutableList<File>

    inner class MediaPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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

                //todo convert it to base64 and send
                val bm = BitmapFactory.decodeFile(mediaFile.absolutePath)
                val resized = Bitmap.createScaledBitmap(bm, 600, 800, true)
                val baos = ByteArrayOutputStream()
                resized.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val base64 = baos.toByteArray()

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
                            view.context, arrayOf(mediaFile.absolutePath), null, null)

                        mediaList.removeAt(mediaViewPager.currentItem)
                        mediaViewPager.adapter?.notifyDataSetChanged()

                        if (mediaList.isEmpty()) {
                            fragmentManager?.popBackStack()
                        }
                    }}

                .setNegativeButton(android.R.string.no, null)
                .create().showImmersive()
        }
    }
}
