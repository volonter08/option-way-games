package pl.bk20.forest.app

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import pl.bk20.forest.databinding.CheatItemBinding

class CheatListHolder(val binding: CheatItemBinding): ViewHolder(binding.root) {
    fun bind(cheat:String){
        binding.textView.text = cheat
    }

}