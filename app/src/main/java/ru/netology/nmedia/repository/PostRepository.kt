package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.post.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun like(id: Long)
    fun share(id: Long)
    fun save(post: Post)
    fun removeById(id: Long)
    fun singlePost(id: Int)
}

