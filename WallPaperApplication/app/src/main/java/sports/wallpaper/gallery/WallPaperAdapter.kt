package sports.wallpaper.gallery

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sports.wallpaper.gallery.databinding.ItemRecycleViewBinding

@SuppressLint("NotifyDataSetChanged")
class WallPaperAdapter(val listWallPaper: Array<WallPaper>) :
    RecyclerView.Adapter<WallPaperHolder>() {
    init {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallPaperHolder {
        val itemRecycleViewBinding =
            ItemRecycleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        parent.apply {
            val heightWallPaper = if (height > width) height / 2 else
                height
            val widthWallpaper =
                if (height > width) width / 2 else ((height / width.toFloat()) * heightWallPaper).toInt()
            itemRecycleViewBinding.root.layoutParams =
                MarginLayoutParams(widthWallpaper, heightWallPaper).apply {
                    setMargins(width / 16, height / 16, width / 16, height / 16)
                }

        }
        return WallPaperHolder(parent.context, itemRecycleViewBinding)
    }

    override fun getItemCount(): Int {
        return listWallPaper.size
    }

    override fun onBindViewHolder(holder: WallPaperHolder, position: Int) {
        val item = listWallPaper[position]
        holder.bind(wallPaper = item)
    }
}