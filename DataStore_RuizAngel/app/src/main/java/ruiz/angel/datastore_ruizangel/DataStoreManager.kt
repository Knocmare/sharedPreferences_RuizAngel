package ruiz.angel.datastore_ruizangel

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore by preferencesDataStore(name = "mis_prefs")

class DataStoreManager(private val context: Context) {
    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val CART_KEY = stringPreferencesKey("items_carrito")
    }

    val isLoggedIn: Flow<Boolean> = context.datastore.data
        .map { preferences -> preferences[IS_LOGGED_IN] ?: false }

    suspend fun setLoggedIn(value: Boolean) {
        context.datastore.edit { preferences ->
            preferences[IS_LOGGED_IN] = value
        }
    }

    suspend fun logout() {
        context.datastore.edit { it.clear() }
    }

    val cartFlow: Flow<String> = context.datastore.data
        .map { preferences -> preferences[CART_KEY] ?: "" }

    suspend fun saveCartIds(idsString: String) {
        context.datastore.edit { preferences ->
            preferences[CART_KEY] = idsString
        }
    }
}