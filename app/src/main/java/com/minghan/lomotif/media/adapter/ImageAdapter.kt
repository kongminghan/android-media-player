package com.minghan.lomotif.media.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.minghan.lomotif.media.R
import com.minghan.lomotif.media.data.Image
import kotlinx.android.synthetic.main.item_image.view.*


class ImageAdapter(val onClick: (image: Image) -> Unit) :
    PagedListAdapter<Image, RecyclerView.ViewHolder>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(old: Image, new: Image) = old.id == new.id

            override fun areContentsTheSame(old: Image, new: Image) = old == new
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ImageVH) {
            viewHolder.bind(getItem(position))
        }
    }

    inner class ImageVH(private val v: View) : RecyclerView.ViewHolder(v) {
        fun bind(image: Image?) {
            if (image == null) return

            try {
                v.apply {
                    this.image.setImageURI(image.previewURL)
                    v.setOnClickListener { onClick(image) }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}
