package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.post.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun like(id: Long)
    fun repost(id: Long)
    fun save(post: Post)
    fun removeById(id: Long)
}

