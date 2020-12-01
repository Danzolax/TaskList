package ru.zolax.tasklist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    private val context: Context,
    private var data: ArrayList<Item> = ArrayList()

) : RecyclerView.Adapter<ItemAdapter.ViewHolder>(), ItemTouchHelperAdapter {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val intent = Intent(context, AddItemActivity::class.java)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.titleTextView)
        private val descr: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val image: ImageView = itemView.findViewById(R.id.priorityImageView)
        fun bind(row: Item) {
            title.text = row.title
            descr.text = row.description
            if (!row.priority) {
                image.visibility = View.GONE
            } else {
                image.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
//            intent.putExtra("id", data[position].id)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setItems(data: List<Item>) {
        this.data.clear()
        this.data.addAll(data)
//        this.data = data ?: this.data
        notifyDataSetChanged()

    }

    override fun onItemDismiss(position: Int) {
        val item = data[position]
        data.removeAt(position)
        if (context is ItemViewModelDelete) {
            context.delete(item)
        }
        notifyItemRemoved(position)
    }
}