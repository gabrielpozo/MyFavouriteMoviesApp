package com.light.source.local


interface LocalMediaDataSource {
    fun getImageFilePath(absolutePath: String): String
}