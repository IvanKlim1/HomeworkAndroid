package ru.netology.nmedia.repository

import androidx.lifecycle.Transformations
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryRoomImpl(
    private val dao: PostDao,
) : PostRepository {
    override fun getAll() = Transformations.map(dao.getAll()) { list ->
        list.map {
            Post(it.id, it.author, it.content, it.published, it.likes,it.shares,it.likedByMe)
        }
    }

    override fun share(id: Long) {
        dao.shareById(id)
    }

    override fun like(id: Long) {
        dao.likeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

//    override fun singlePost(id: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override fun video(id: Int) {
//        TODO("Not yet implemented")
//    }
}