package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cheatcodeapp.databinding.ActivityCheatListBinding
import java.util.LinkedList

class CheatListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cheatBinding= ActivityCheatListBinding.inflate(layoutInflater)
        setContentView(cheatBinding.root)
        val typePlatform = intent.getSerializableExtra("type_platform") as TypePlatform
        val listCheatCode: List<String> = when (typePlatform) {
            TypePlatform.PS -> getListCheatCodeFromTxt("ps_list.txt")
            TypePlatform.XBOX -> getListCheatCodeFromTxt("xbox_list.txt")
            TypePlatform.PC -> getListCheatCodeFromTxt("pc_list.txt")
        }
        cheatBinding.recycleView.adapter = CheatListAdapter(listCheatCode)
    }

    fun getListCheatCodeFromTxt(pathTxt: String): List<String> {
        val listCheatCode: LinkedList<String> = LinkedList()
        val bufferReader = assets.open(pathTxt).bufferedReader()
        while (true) {
            val cheatCode = bufferReader.readLine()
            if (cheatCode == null)
                break
            else
                listCheatCode.add(cheatCode)
        }
        return listCheatCode
    }
}