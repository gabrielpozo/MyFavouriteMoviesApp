package com.accenture.signify.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.accenture.signify.R
import com.accenture.util.FILE_NAME
import com.bumptech.glide.Glide
import java.io.File

class GalleryFragment internal constructor() : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) = ImageView(context)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments ?: return
        val resource = args.getString(FILE_NAME)?.let { File(it) } ?: R.drawable.ic_highlight_24px
        Glide.with(view).load(resource).into(view as ImageView)
    }

    companion object {

        fun create(image: File) = GalleryFragment().apply {
            arguments = Bundle().apply {
                putString(FILE_NAME, image.absolutePath)
            }
        }
    }
}