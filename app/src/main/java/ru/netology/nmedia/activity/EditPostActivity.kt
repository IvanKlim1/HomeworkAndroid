package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.databinding.ActivityEditPostBinding
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
import ru.netology.nmedia.viewmodel.empty

internal val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    sharedByMe = false,

    )

class EditPostActivity : AppCompatActivity() {
    fun onEdit(savedInstanceState: ViewModel) {
        val repository: PostRepository = PostRepositoryInMemoryImpl()
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.edit.setOnClickListener {
            val edited = MutableLiveData(empty)
            fun edit(post: Post) {
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
        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }
}