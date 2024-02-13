package pixel.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.testcaseapplication.R

class NetworkDisconnectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ImageView(this).apply {
            setImageResource(R.drawable.network_disconnect)
        })
    }

    override fun onBackPressed() {
    }
}