package com.rahmadina.ika.movieapp_kotlin.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "user")

class SharedPref(private val context : Context) {

    private val idUser = stringPreferencesKey("idUser")
    private val isLogin = booleanPreferencesKey("isLogin")

    suspend fun saveUser(isLogins: Boolean, idUsers : String){
        context.dataStore.edit {
            it[isLogin] = isLogins
            it[idUser] = idUsers
        }
    }

    val getIdUser : Flow<String> = context.dataStore.data
        .map {
            it[idUser] ?: "Undefined"
        }

    val getSession : Flow<Boolean> = context.dataStore.data
        .map {
            it[isLogin] ?: false
        }


    suspend fun removeSession(){
        context.dataStore.edit {
            it.clear()
        }
    }

}