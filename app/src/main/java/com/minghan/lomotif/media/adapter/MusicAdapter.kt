package com.minghan.lomotif.media.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minghan.lomotif.media.R
import com.minghan.lomotif.media.data.Music
import kotlinx.android.synthetic.main.item_music.view.*

class MusicAdapter(private val onClick: (music: Music) -> Unit) :
    RecyclerView.Adapter<MusicAdapter.AppVH>() {
    private val apps: MutableList<Music> = mutableListOf()

    fun swap(newList: List<Music>) {
        apps.clear()
        apps.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount() = apps.size

    override fun onBindViewHolder(holder: AppVH, position: Int) {
        holder.bind(apps[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppVH {
        return AppVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_music, parent, false)
        )
    }

    inner class AppVH(private val v: View) : RecyclerView.ViewHolder(v) {
        @SuppressLint("SetTextI18n")
        fun bind(music: Music) {
            try {
                v.apply {
                    image.setImageURI(music.albumArtUri, image.context)
                    tv_title.text = music.title
                    tv_album.text = music.albumName
                    tv_artist.text = music.artist
                    tv_duration.text = music.duration

                    setOnClickListener { onClick(music) }
                }
            } catch (e: Throwable) {
            }
        }
    }
}