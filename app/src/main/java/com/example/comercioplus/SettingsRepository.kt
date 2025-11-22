package com.example.comercioplus

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    private object Keys {
        // General
        val STORE_NAME = stringPreferencesKey("store_name")
        val PHONE = stringPreferencesKey("phone")
        val WHATSAPP = stringPreferencesKey("whatsapp")
        val REPORT_EMAIL = stringPreferencesKey("report_email")
        val ADDRESS = stringPreferencesKey("address")
        val CITY = stringPreferencesKey("city")
        val IS_STORE_VISIBLE = booleanPreferencesKey("is_store_visible")

        // Appearance
        val LOGO_URI = stringPreferencesKey("logo_uri")
        val COVER_URI = stringPreferencesKey("cover_uri")
        val DARK_MODE = booleanPreferencesKey("dark_mode") // Nueva clave para el modo oscuro

        // Payments
        val INSTRUCTIONS = stringPreferencesKey("payment_instructions")

        // Shipping
        val RADIUS = stringPreferencesKey("shipping_radius")
        val BASE_COST = stringPreferencesKey("shipping_base_cost")

        // Taxes
        val PERCENTAGE = stringPreferencesKey("taxes_percentage")
        val PRICES_INCLUDE_TAX = booleanPreferencesKey("prices_include_tax")

        // Notifications
        val NOTIFICATIONS_EMAIL = stringPreferencesKey("notifications_email")
    }

    // General Settings
    val generalSettings: Flow<GeneralSettingsState> = context.dataStore.data.map { prefs ->
        GeneralSettingsState(
            storeName = prefs[Keys.STORE_NAME] ?: "Mi Tienda Online",
            phone = prefs[Keys.PHONE] ?: "",
            whatsapp = prefs[Keys.WHATSAPP] ?: "",
            reportEmail = prefs[Keys.REPORT_EMAIL] ?: "",
            address = prefs[Keys.ADDRESS] ?: "",
            city = prefs[Keys.CITY] ?: "",
            isStoreVisible = prefs[Keys.IS_STORE_VISIBLE] ?: true
        )
    }

    suspend fun saveGeneralSettings(state: GeneralSettingsState) {
        context.dataStore.edit {
            it[Keys.STORE_NAME] = state.storeName
            it[Keys.PHONE] = state.phone
            it[Keys.WHATSAPP] = state.whatsapp
            it[Keys.REPORT_EMAIL] = state.reportEmail
            it[Keys.ADDRESS] = state.address
            it[Keys.CITY] = state.city
            it[Keys.IS_STORE_VISIBLE] = state.isStoreVisible
        }
    }

    // Appearance Settings
    val appearanceSettings: Flow<AppearanceSettingsState> = context.dataStore.data.map { prefs ->
        AppearanceSettingsState(
            logoUri = prefs[Keys.LOGO_URI]?.let { android.net.Uri.parse(it) },
            coverUri = prefs[Keys.COVER_URI]?.let { android.net.Uri.parse(it) },
            isDarkMode = prefs[Keys.DARK_MODE] ?: true // Por defecto, modo oscuro
        )
    }

    suspend fun saveAppearanceSettings(state: AppearanceSettingsState) {
        context.dataStore.edit {
            it[Keys.LOGO_URI] = state.logoUri.toString()
            it[Keys.COVER_URI] = state.coverUri.toString()
            it[Keys.DARK_MODE] = state.isDarkMode
        }
    }
    
    // Payments Settings
    val paymentsSettings: Flow<PaymentsSettingsState> = context.dataStore.data.map { prefs ->
        PaymentsSettingsState(instructions = prefs[Keys.INSTRUCTIONS] ?: "")
    }

    suspend fun savePaymentsSettings(state: PaymentsSettingsState) {
        context.dataStore.edit { it[Keys.INSTRUCTIONS] = state.instructions }
    }

    // Shipping Settings
    val shippingSettings: Flow<ShippingSettingsState> = context.dataStore.data.map { prefs ->
        ShippingSettingsState(
            radius = prefs[Keys.RADIUS] ?: "",
            baseCost = prefs[Keys.BASE_COST] ?: ""
        )
    }

    suspend fun saveShippingSettings(state: ShippingSettingsState) {
        context.dataStore.edit {
            it[Keys.RADIUS] = state.radius
            it[Keys.BASE_COST] = state.baseCost
        }
    }

    // Taxes Settings
    val taxesSettings: Flow<TaxesSettingsState> = context.dataStore.data.map { prefs ->
        TaxesSettingsState(
            percentage = prefs[Keys.PERCENTAGE] ?: "",
            pricesIncludeTax = prefs[Keys.PRICES_INCLUDE_TAX] ?: false
        )
    }

    suspend fun saveTaxesSettings(state: TaxesSettingsState) {
        context.dataStore.edit {
            it[Keys.PERCENTAGE] = state.percentage
            it[Keys.PRICES_INCLUDE_TAX] = state.pricesIncludeTax
        }
    }
    
    // Notifications Settings
    val notificationsSettings: Flow<NotificationsSettingsState> = context.dataStore.data.map { prefs ->
        NotificationsSettingsState(email = prefs[Keys.NOTIFICATIONS_EMAIL] ?: "")
    }

    suspend fun saveNotificationsSettings(state: NotificationsSettingsState) {
        context.dataStore.edit { it[Keys.NOTIFICATIONS_EMAIL] = state.email }
    }
}