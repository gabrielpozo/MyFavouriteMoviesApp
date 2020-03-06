package com.light.finder.data.source.local

import com.light.finder.extensions.encodeImage
import com.light.source.local.LocalMediaDataSource


class LocalMediaDataSourceImpl : LocalMediaDataSource {
    override fun getImageFilePath(absolutePath: String): String {
        return absolutePath.encodeImage()
    }
}