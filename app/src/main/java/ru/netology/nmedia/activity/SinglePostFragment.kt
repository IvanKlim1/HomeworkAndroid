package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentSinglePostBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import java.text.DecimalFormat

private val df = DecimalFormat("#.#")
private val dff = DecimalFormat("#")

class SinglePostFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSinglePostBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment
        )

        val id = arguments?.getLong("single") ?: -1

        viewModel.data.observe(this) { posts ->
            val post = posts.find { it.id == id }
            binding.apply {
                author.text = post?.author.toString()
                published.text = post?.published
                content.text = post?.content
                like.text = post?.likes.toString()
                share.text = post?.shares.toString()
                like.isChecked = post?.likedByMe == true


                like.setOnClickListener {
                    like.text = post?.likes.toString()
                    if (post != null) {
                        viewModel.like(post.id)
                        like.text = post.likes.toString()
                    }
                }


                share.setOnClickListener {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        if (post != null) {
                            putExtra(Intent.EXTRA_TEXT, post.content)
                        }
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                    if (post != null) {
                        viewModel.singlePost(post.id.toInt())
                    }
                    share.text = post?.shares.toString()
                }

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.remove -> {
                                    findNavController().popBackStack()
                                    true
                                }
                                R.id.edit -> {
                                    findNavController().navigate(
                                        R.id.action_singlePostFragment_to_editPostFragment,
                                    )
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
                like.text = post?.let { countFormat(it.likes) }
                share.text = post?.let { countFormat(it.shares) }
            }
        }
        return binding.root
    }

    private fun countFormat(count: Int): String =

        when {
            count < 1_000 -> count.toString()
            count == 1_000 || count < 1_100 -> dff.format(count / 1000) + "K"
            count in 1_100..9_999 -> df.format(count / 1000) + "K"
            count in 10_000..999_999 -> dff.format(count / 1000) + "K"
            count in 1000000..1099999 -> dff.format(count / 1000000) + "M"
            count > 1099999 -> df.format(count / 1000000) + "M"
            else -> count.toString()
        }

}



