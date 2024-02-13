package pl.bk20.forest.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.bk20.forest.databinding.CheatItemBinding

class CheatListAdapter(private val cheatList:List<String>) :RecyclerView.Adapter<CheatListHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheatListHolder {
        val binding = CheatItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CheatListHolder(binding)
    }

    override fun getItemCount(): Int {
        return cheatList.size
    }

    override fun onBindViewHolder(holder: CheatListHolder, position: Int) {
        holder.bind(cheatList[position])
    }
}