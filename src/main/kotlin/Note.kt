data class Note (
    val id: Long,
    val content: String,
    val authorId: Long,
    var isDeleted: Boolean = false,
    var comments: MutableList<Comment> = mutableListOf()
        )