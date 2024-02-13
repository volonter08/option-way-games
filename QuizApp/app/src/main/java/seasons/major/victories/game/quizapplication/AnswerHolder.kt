package seasons.major.victories.game.quizapplication

import androidx.recyclerview.widget.RecyclerView
import seasons.major.victories.databinding.AnswerBinding

class AnswerHolder(val binding: AnswerBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(name:String,position:Int,userAnswers:BooleanArray){
        if(position==0)
        println(userAnswers.toList())
        binding.questionChoice.text = name
        binding.questionChoice.setOnCheckedChangeListener(null)
        binding.questionChoice.isChecked =userAnswers[position]
        binding.questionChoice.setOnCheckedChangeListener { btnView, isChecked ->
            userAnswers[position] = isChecked
        }
    }
}