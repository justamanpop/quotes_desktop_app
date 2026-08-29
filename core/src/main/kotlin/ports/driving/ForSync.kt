package ports.driving

interface ForSync {
    fun exportQuotesToJson(): String
    fun importFromJson(jsonString: String, overwrite: Boolean)
}
