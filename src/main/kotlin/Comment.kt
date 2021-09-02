data class Comment (
    val id: Long,
    val content: String,
    val authorId: Long,
    var isDeleted: Boolean = false,
    val noteId: Long
        ) {
}