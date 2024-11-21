import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tech_task.NewFragment.NewViewModel
import com.example.tech_task.R
import com.example.tech_task.databinding.FragmentNewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class NewFragment : Fragment() {

    private var _binding: FragmentNewBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: NewViewModel by viewModels()
    private lateinit var adapter: NewFragmentAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            3
        } else {
            2
        }

        binding.recycler_New.layoutManager = GridLayoutManager(context, spanCount)

        adapter = NewFragmentAdapter { selectedCharacter ->
            val bundle = Bundle().apply {
                putString("image", selectedCharacter.image)
                putString("description", selectedCharacter.name)
            }
            findNavController().navigate(R.id.action_newFragment_to_detailFragment, bundle)
        }

        binding.recycler_New.adapter = adapter

        binding.recycler_New.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!binding.recycler_New.canScrollVertically(1) && dy > 0){
                    binding.newProgressBar.visibility = View.VISIBLE
                    adapter.retry()
                }
            }
        })

        adapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                is LoadState.Loading -> {
                    binding.newProgressBar.visibility = View.VISIBLE
                }
                is LoadState.NotLoading -> {
                    binding.newProgressBar.visibility = View.GONE
                }
                is LoadState.Error -> {
                    binding.newProgressBar.visibility = View.GONE
                }
            }

            val isListEmpty = loadState.source.refresh is LoadState.NotLoading && adapter.itemCount == 0
            binding.recycler_New.isVisible = !isListEmpty

        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.characters.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
