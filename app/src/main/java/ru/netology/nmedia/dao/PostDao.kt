package ru.netology.nmedia.dao

import ru.netology.nmedia.post.Post
import java.io.Closeable

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likeById(id: Long)
    fun removeById(id: Long)
    fun shareById(id: Long)
}