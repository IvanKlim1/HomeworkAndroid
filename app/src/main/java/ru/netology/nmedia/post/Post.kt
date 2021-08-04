package ru.netology.nmedia.post

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 0,
    var shares: Int = 0,
    val likedByMe: Boolean ,
    //val sharedByMe: Boolean,
    val video: String? = null
)