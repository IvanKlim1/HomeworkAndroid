package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.ExtensionForAdapterFunctions
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.post.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )
        val adapter =
            ExtensionForAdapterFunctions.PostsAdapter(object : ExtensionForAdapterFunctions {
                override fun onLiked(post: Post) {
                    viewModel.like(post.id)
                }

                override fun onShared(post: Post) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "*/*"
                    }
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                }

                override fun onEdit(post: Post) {
                    findNavController().navigate(R.id.editPostFragment, Bundle().apply {
                        textArg = post.content
                    })
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }
                override fun singlePostById(post: Post) {
                    val bundle = Bundle()
                    bundle.putInt("single", post.id.toInt())
                    viewModel.singlePost(post.id.toInt())
                    findNavController().navigate(R.id.action_feedFragment_to_singlePostFragment, bundle)
                }

                override fun playMedia(uri: String) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=WhWc3b3KhnY")
                        )
                    );
                }

            })
        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
            binding.list.smoothScrollToPosition(0)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id == 0L) {
                binding.content.visibility = View.GONE
                binding.content.visibility = View.INVISIBLE
                return@observe
            }
            binding.content.visibility = View.VISIBLE
            with(binding.content) {
                requestFocus()
                setText(post.content)
            }
        }

        binding.clear.setOnClickListener {
            with(binding.content) {
                setText(" ")
            }
        }
        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        requireContext(),
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }
        return binding.root
    }
}





