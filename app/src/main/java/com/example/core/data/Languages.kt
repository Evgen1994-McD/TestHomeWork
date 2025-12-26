package com.example.core.data

data class Language(
    val code: String,
    val name: String
)

object Languages {
    val supportedLanguages = listOf(
        Language("en", "English"),
        Language("ru", "Русский"),
        Language("fr", "Français"),
        Language("de", "Deutsch"),
        Language("es", "Español"),
        Language("it", "Italiano"),
        Language("pt", "Português"),
        Language("ja", "日本語"),
        Language("ko", "한국어"),
        Language("zh", "中文"),
        Language("ar", "العربية"),
        Language("hi", "हिन्दी"),
        Language("tr", "Türkçe"),
        Language("pl", "Polski"),
        Language("nl", "Nederlands"),
        Language("sv", "Svenska"),
        Language("cs", "Čeština"),
        Language("uk", "Українська"),
        Language("vi", "Tiếng Việt"),
        Language("th", "ไทย")
    )
    
    fun getLanguageName(code: String): String {
        return supportedLanguages.find { it.code == code }?.name ?: code
    }
}

