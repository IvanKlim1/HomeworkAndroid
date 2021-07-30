package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

internal val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    sharedByMe = false,

)
class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    private val edited = MutableLiveData(empty)
    fun video(id: Int) = repository.video(id)
    fun like(id: Long) = repository.like(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun singlePost(id: Int) = repository.singlePost(id)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }
    fun edit(post: Post){
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
}