package bi.vovota.eac.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.text.get

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class TokenManager (private val dataStore: DataStore<Preferences>) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = token
        }
    }

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN_KEY] = token
        }
    }

    val accessToken: Flow<String?> = dataStore.data
        .map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }

    val refreshToken: Flow<String?> = dataStore.data
        .map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }

    suspend fun clear() {
        dataStore.edit { it.clear()  }
    }
}