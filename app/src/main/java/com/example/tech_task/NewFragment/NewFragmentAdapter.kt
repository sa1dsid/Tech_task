import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.tech_task.R
import com.example.tech_task.databinding.FragmentItemBinding
import com.example.tech_task.data.CharacterData

class NewFragmentAdapter(
    private val onItemClicked: (CharacterData) -> Unit,
) : PagingDataAdapter<CharacterData, NewFragmentAdapter.NewFragmentViewHolder>(DiffCallback) {

    inner class NewFragmentViewHolder(private val bindingImg: FragmentItemBinding) :
        RecyclerView.ViewHolder(bindingImg.root) {

        fun bind(character: CharacterData) {
            println("fun bind(character: ResultCharacter){ ${character.image}")

            Glide.with(bindingImg.root)
                .load(character.image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_launcher_foreground)
                .into(bindingImg.imageView)


            bindingImg.root.setOnClickListener {
                onItemClicked(character)
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewFragmentViewHolder {
        val binding =
            FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewFragmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewFragmentViewHolder, position: Int) {
        val character = getItem(position)
        if (character != null) {
            holder.bind(character)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CharacterData>() {
        override fun areItemsTheSame(oldItem: CharacterData, newItem: CharacterData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CharacterData,
            newItem: CharacterData
        ): Boolean {
            return oldItem == newItem
        }


    }

}