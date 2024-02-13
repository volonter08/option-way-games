package sports.wallpaper.gallery

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.Gravity.*
import android.view.View
import android.widget.PopupMenu
import androidx.core.graphics.scale
import androidx.recyclerview.widget.RecyclerView
import sports.wallpaper.gallery.databinding.ItemRecycleViewBinding
import sports.wallpaper.gallery.R


class WallPaperHolder(
    val context: Context,
    private val binding: ItemRecycleViewBinding,

) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("ClickableViewAccessibility")
    fun bind(wallPaper: WallPaper){
        val wallPaperManager = WallpaperManager.getInstance(context)
        lateinit var bitmap: Bitmap
        binding.image.apply {
            post {
                bitmap = BitmapFactory.decodeStream(context.assets.open(wallPaper.name))
                    .scale(width, height)
                setImageBitmap(bitmap)
            }
        }
        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.wallpaper_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.put_wallpaper -> {
                            try{
                                wallPaperManager.setBitmap(bitmap)
                            }
                            catch (_:Exception){
                            }
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
    }
}