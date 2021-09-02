class NoteNotFoundException(message: String): RuntimeException(message)
class CommentNotFoundException(message: String): RuntimeException(message)
class CommentNotDeletedException(message: String): RuntimeException(message)

class NoteService : CrudService<Note> {
    var notes = mutableListOf<Note>()
    private var noteId: Long = 1

    override fun add(entity: Note): Note {
        notes += entity.copy(id = noteId)
        noteId++
        return notes.last()
    }

    override fun delete(id: Long): Boolean {
        for ((index, noteInArray) in notes.withIndex()) {
            if (id == noteInArray.id) {
                notes.removeAt(index)
                noteInArray.isDeleted = true
                return true
            }
        }
        throw NoteNotFoundException("No note with id $id")
    }

    override fun edit(entity: Note): Boolean {
        for ((index, noteInArray) in notes.withIndex()) {
            if (entity.id == noteInArray.id) {
                notes[index] = entity.copy()
                return true
            }
        }
        throw NoteNotFoundException("No note with id ${entity.id}")
    }

    fun createComment(comment: Comment): Boolean {
        for ((index, noteInArray) in notes.withIndex()) {
            if (comment.noteId == noteInArray.id) {
                val newComments: MutableList<Comment> = (noteInArray.comments + comment) as MutableList<Comment>
                notes[index] = noteInArray.copy(comments = newComments)
                return true
            }
        }
        throw NoteNotFoundException("No note with id ${comment.noteId}")
    }

    fun editComment(comment: Comment): Boolean {
        var noteFound = true
        for ((indexNote, noteInArray) in notes.withIndex()) {
            if (noteInArray.id == comment.noteId) {
                noteFound = true
                for ((index, commentInArray) in noteInArray.comments.withIndex()) {
                    if (comment.id == commentInArray.id && !commentInArray.isDeleted) {
                        noteInArray.comments[index] = comment
                        notes[indexNote] = noteInArray.copy(comments = noteInArray.comments)
                        return true
                    }
                }
            } else {
                noteFound = false
            }
        }
        if (!noteFound) {
            throw NoteNotFoundException("No note with id ${comment.noteId}")
        } else {
            throw CommentNotFoundException("No comment with id ${comment.id} or it was deleted")
        }
    }

    fun get(): MutableList<Note> {
        return notes
    }

    fun getById(id: Long): Note {
        for (noteInArray in notes) {
            if (id == noteInArray.id) {
                return noteInArray
            }
        }
        throw NoteNotFoundException("No note with id $id")
    }

    fun getComments(id: Long): MutableList<Comment> {
        for (noteInArray in notes) {
            if (id == noteInArray.id) {
                return noteInArray.comments
            }
        }
        throw NoteNotFoundException("No note with id $id")
    }

    fun deleteComment(comment: Comment): Boolean {
        var noteFound = true
        for ((indexNote, noteInArray) in notes.withIndex()) {
            if (noteInArray.id == comment.noteId) {
                noteFound = true
                for ((index, commentInArray) in noteInArray.comments.withIndex()) {
                    if (comment.id == commentInArray.id && !commentInArray.isDeleted) {
                        val newComments: MutableList<Comment> = noteInArray.comments
                        newComments.removeAt(index)
                        notes[indexNote] = noteInArray.copy(comments = newComments)
                        comment.isDeleted = true
                        return true
                    }
                }
            } else {
                noteFound = false
            }
        }
        if (!noteFound) {
            throw NoteNotFoundException("No note with id ${comment.noteId}")
        } else {
            throw CommentNotFoundException("No comment with id ${comment.id} or it was deleted")
        }
    }

    fun restoreComment(comment: Comment): Boolean {
        for ((index, noteInArray) in notes.withIndex()) {
            if (comment.noteId == noteInArray.id && comment.isDeleted) {
                val newComments: MutableList<Comment> = (noteInArray.comments + comment) as MutableList<Comment>
                comment.isDeleted = false
                notes[index] = noteInArray.copy(comments = newComments)
                return true
            } else if (!comment.isDeleted) {
                throw CommentNotDeletedException("Presented comment is not deleted")
            }
        }
        throw NoteNotFoundException("No note with id ${comment.noteId}")
    }
}
