package hit.big.gold

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import hit.big.gold.databinding.ActivityResultGameBinding

class ResultGameActivity : AppCompatActivity() {
    lateinit var resultGameActivityBinding: ActivityResultGameBinding
    var myCount= 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultGameActivityBinding = ActivityResultGameBinding.inflate(layoutInflater)
        myCount = intent.getIntExtra("my_count",0)
        Glide.with(this).load( R.drawable.loss_giphy).into(resultGameActivityBinding.imageview)
        resultGameActivityBinding.textview.text =  "You scored $myCount points"
        setContentView(resultGameActivityBinding.root)
    }

}