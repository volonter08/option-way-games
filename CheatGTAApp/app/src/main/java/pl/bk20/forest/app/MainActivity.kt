package pl.bk20.forest.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.bk20.forest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        mainBinding.psButton.setOnClickListener {
            startActivity(Intent(this, CheatListActivity::class.java).apply {
                putExtra("type_platform", TypePlatform.PS)
            })
        }
        mainBinding.xboxButton.setOnClickListener {
            startActivity(Intent(this, CheatListActivity::class.java).apply {
                putExtra("type_platform", TypePlatform.XBOX)
            })
        }
        mainBinding.pcButton.setOnClickListener {
            startActivity(Intent(this, CheatListActivity::class.java).apply {
                putExtra("type_platform", TypePlatform.PC)
            })
        }
    }
}