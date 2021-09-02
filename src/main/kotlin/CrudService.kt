interface CrudService <E> {
    fun add(entity: E): E
    fun delete(id: Long): Boolean
    fun edit(entity: E): Boolean
}