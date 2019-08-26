package com.minghan.lomotif.media.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minghan.lomotif.media.R
import com.minghan.lomotif.media.data.Video
import kotlinx.android.synthetic.main.item_video.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VideoAdapter(private val onClick: (video: Video) -> Unit) :
    RecyclerView.Adapter<VideoAdapter.VideoVH>() {
    private val videos: MutableList<Video> = mutableListOf()

    fun swap(newList: List<Video>) {
        videos.clear()
        videos.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: VideoVH, position: Int) {
        holder.bind(videos[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVH {
        return VideoVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_video, parent, false)
        )
    }

    inner class VideoVH(private val v: View) : RecyclerView.ViewHolder(v) {
        @SuppressLint("SetTextI18n")
        fun bind(video: Video) = GlobalScope.launch(Dispatchers.Main) {
            try {
                v.apply {
                    setOnClickListener { onClick(video) }

                    tv_title.text = video.title
                    tv_duration.text = video.duration
                    image.setImageBitmap(video.albumThumb)
                }
            } catch (e: Throwable) {
            }
        }
    }
}