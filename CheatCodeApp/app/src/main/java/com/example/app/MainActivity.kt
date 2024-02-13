package com.example.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cheatcodeapp.databinding.ActivityMainBinding

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