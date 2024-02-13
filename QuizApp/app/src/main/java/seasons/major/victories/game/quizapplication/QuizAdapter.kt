package seasons.major.victories.game.quizapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import seasons.major.victories.databinding.AnswerBinding

class QuizAdapter(val listNames:List<String>,val trueAnswer:BooleanArray) : RecyclerView.Adapter<AnswerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerHolder {
        val answerBinding = AnswerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AnswerHolder(answerBinding)
    }

    override fun getItemCount(): Int {
        return listNames.size
    }

    override fun onBindViewHolder(holder: AnswerHolder, position: Int) {
        val answer = listNames[position]
        holder.bind(answer,position,trueAnswer)
    }

}