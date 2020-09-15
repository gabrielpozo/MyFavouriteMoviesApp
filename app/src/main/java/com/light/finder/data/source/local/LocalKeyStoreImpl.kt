package com.light.finder.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.light.domain.model.Bearer
import com.light.finder.extensions.fromJson
import com.light.source.local.LocalKeyStore


class LocalKeyStoreImpl(context: Context) : LocalKeyStore {

    companion object {
        private const val PREF_KEY_STORE = "prefBearerKeyStore"
        private const val ACCESS_TOKEN = "accessToken"
    }

    private val PRIVATE_MODE = 0
    private val pref: SharedPreferences =
        context.getSharedPreferences(PREF_KEY_STORE, PRIVATE_MODE)
    private val editor: SharedPreferences.Editor = pref.edit().also { it.apply() }

    override fun saveBearerToken(credentials: Bearer) {
        editor.putString(ACCESS_TOKEN, Gson().toJson(credentials)).commit()
    }


    override fun loadBearerToken(): Bearer = Gson().fromJson(
        pref.getString(ACCESS_TOKEN, null) ?: ""
    )

    override fun removeToken() {
        editor.remove(ACCESS_TOKEN).commit()
    }
}