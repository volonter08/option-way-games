package com.example.app

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cheatcodeapp.databinding.CheatItemBinding

class CheatListHolder(val binding: CheatItemBinding): ViewHolder(binding.root) {
    fun bind(cheat:String){
        binding.textView.text = cheat
    }

}