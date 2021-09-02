import org.junit.Test

import org.junit.Assert.*

class NoteServiceTest {

    @Test
    fun add() {
        val service = NoteService()
        val note = Note(2, "Тест", 1, false)
        val expected = note.copy(id = 1)
        val output = service.add(note)
        assertEquals(expected, output)
    }

    @Test
    fun delete() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        service.add(Note(3, "Тест1", 1, false))
        assertTrue(service.delete(1))
    }

    @Test (expected = NoteNotFoundException::class)
    fun deleteFailed() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        service.add(Note(3, "Тест1", 1, false))
        assertTrue(service.delete(5))
    }

    @Test
    fun edit() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        service.add(Note(3, "Тест1", 1, false))
        assertTrue(service.edit(Note(1, "Тест3", 1, false)))
    }

    @Test (expected = NoteNotFoundException::class)
    fun editFailed() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        service.add(Note(3, "Тест1", 1, false))
        assertTrue(service.edit(Note(5, "Тест3", 1, false)))
    }

    @Test
    fun createComment() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        service.add(Note(3, "Тест1", 1, false))
        val comment = Comment(1,"Content", 1, false, 1)
        assertTrue(service.createComment(comment))
    }

    @Test (expected = NoteNotFoundException::class)
    fun createCommentFailed() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        service.add(Note(3, "Тест1", 1, false))
        val comment = Comment(1,"Content", 1, false, 5)
        assertTrue(service.createComment(comment))
    }

    @Test
    fun editComment() {
        val service = NoteService()
        service.add(Note(1, "Тест", 1, false))
        val note = Note(2, "Тест1", 1, false)
        service.add(note)
        val comment = Comment(1,"Content", 1, false, 2)
        service.createComment(Comment(1,"Content111", 1, false, 2))
        assertTrue(service.editComment(comment))
    }

    @Test (expected = NoteNotFoundException::class)
    fun editCommentFailedNoNote() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        val note = Note(3, "Тест1", 1, false)
        service.add(note)
        val comment = Comment(1,"Content", 1, false, 4)
        service.createComment(Comment(3,"Content111", 1, false, 2))
        service.editComment(comment)
    }

    @Test (expected = CommentNotFoundException::class)
    fun editCommentFailedDeleted() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        val note = Note(3, "Тест1", 1, false)
        service.add(note)
        val comment = Comment(1,"Content", 1, false, 2)
        service.createComment(Comment(3,"Content111", 1, true, 2))
        service.editComment(comment)
    }

    @Test
    fun get() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        val expected = listOf(Note(1, "Тест", 1, false))
        assertEquals(expected, service.get())
    }

    @Test
    fun getById() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        val expected = Note(1, "Тест", 1, false)
        assertEquals(expected, service.getById(1))
    }

    @Test (expected = NoteNotFoundException::class)
    fun getByIdFailed() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        val expected = Note(1, "Тест", 1, false)
        assertEquals(expected, service.getById(2))
    }

    @Test
    fun getComments() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        service.createComment(Comment(1,"Content111", 1, false, 1))
        val expected = listOf(Comment(1,"Content111", 1, false, 1))
        assertEquals(expected, service.getComments(1))
    }

    @Test (expected = NoteNotFoundException::class)
    fun getCommentsFailed() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        service.createComment(Comment(1,"Content111", 1, false, 1))
        service.getComments(6)
    }

    @Test
    fun deleteComment() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        val comment = Comment(1,"Content", 1, false, 1)
        service.createComment(Comment(1,"Content111", 1, false, 1))
        service.deleteComment(comment)
    }

    @Test (expected = NoteNotFoundException::class)
    fun deleteCommentFailedNoNote() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        val comment = Comment(2,"Content", 1, false, 2)
        service.createComment(Comment(1,"Content111", 1, false, 2))
        service.deleteComment(comment)
    }

    @Test (expected = CommentNotFoundException::class)
    fun deleteCommentFailedNoComment() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        val comment = Comment(3,"Content", 1, false, 1)
        service.createComment(Comment(1,"Content111", 1, false, 1))
        service.deleteComment(comment)
    }

    @Test (expected = CommentNotFoundException::class)
    fun deleteCommentFailedDeleted() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        val comment = Comment(1,"Content", 1, false, 1)
        service.createComment(Comment(1,"Content111", 1, true, 1))
        service.deleteComment(comment)
    }

    @Test
    fun restoreComment() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        val comment = Comment(1,"Content", 1, false, 1)
        service.createComment(comment)
        service.deleteComment(service.notes[0].comments[0])
        assertTrue(service.restoreComment(comment))
    }

    @Test (expected = CommentNotDeletedException::class)
    fun restoreCommentDeleted() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        val comment = Comment(1,"Content", 1, false, 1)
        service.createComment(comment)
        service.restoreComment(comment)
    }

    @Test (expected = NoteNotFoundException::class)
    fun restoreCommentNoNote() {
        val service = NoteService()
        service.add(Note(2, "Тест", 1, false))
        val comment = Comment(1,"Content", 1, false, 3)
        service.createComment(comment)
        service.restoreComment(comment)
    }
}