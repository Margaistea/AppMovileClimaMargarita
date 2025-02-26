import android.content.Context

object UserPreferences {
    private const val PREFERENCES_NAME = "user_preferences"
    private const val KEY_CITY_NAME = "favorite_city_name"

    fun CuidadesFavoritasGuardadas(context: Context, cityName: String) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(KEY_CITY_NAME, cityName).apply()
    }

    fun GetCuidadesFavoritas(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_CITY_NAME, null)
    }

    fun BorrarCuidadesFavoritas(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(KEY_CITY_NAME).apply()
    }
}