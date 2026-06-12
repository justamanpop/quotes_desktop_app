import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    val id: Int,
    val content: String,
    val source: String,
    val tags: List<Tag> = listOf()
)