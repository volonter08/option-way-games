package tiger.collecting.coins

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

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