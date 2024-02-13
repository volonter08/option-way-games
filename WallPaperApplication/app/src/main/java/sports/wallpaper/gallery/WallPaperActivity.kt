package sports.wallpaper.gallery

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.waynejo.androidndkgif.GifDecoder
import sports.wallpaper.gallery.databinding.WallpaperActivityBinding


class WallPaperActivity : AppCompatActivity() {
    lateinit var wallpaperActivityBinding: WallpaperActivityBinding
    lateinit var gridLayoutManager:StaggeredGridLayoutManager
    lateinit var wallPaperAdapter :WallPaperAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wallpaperActivityBinding = WallpaperActivityBinding.inflate(layoutInflater)
        wallpaperActivityBinding.listWallpapers.post {
            wallPaperAdapter = WallPaperAdapter(
                Array<WallPaper>(20){
                    WallPaper("wallpapers/wallpaper${it+1}.jpeg")
                }
            )
            gridLayoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.grid_column_count),
                resources.getInteger(R.integer.orientation)
            )
            wallpaperActivityBinding.listWallpapers.run {
                layoutManager = gridLayoutManager
                adapter = wallPaperAdapter
            }
            wallpaperActivityBinding.listWallpapers
        }
        setContentView(wallpaperActivityBinding.root)
    }
    override fun onBackPressed() {
        setResult(RESULT_OK, Intent())
        finish()
    }

}