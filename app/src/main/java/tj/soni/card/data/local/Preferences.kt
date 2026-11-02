package tj.soni.card.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "soni_preferences")
class Preferences(private val context: Context) {
    private val languageKey = stringPreferencesKey("language")
    val language: Flow<String> = context.dataStore.data.map { it[languageKey] ?: "tg" }
    suspend fun setLanguage(value: String) { context.dataStore.edit { it[languageKey] = value } }
}
