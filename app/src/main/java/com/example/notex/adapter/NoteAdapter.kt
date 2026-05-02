package com.example.notex.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notex.databinding.NoteItemLayoutBinding
import com.example.notex.model.Note
import io.noties.markwon.Markwon

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val binding: NoteItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Note, newItem: Note) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffCallback)

    var onItemClick: ((Note) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = differ.currentList[position]

        holder.binding.noteItemTitle.text = note.title
        holder.binding.noteDate.text = note.date

        // Markwon setup
        val markwon = Markwon.create(holder.itemView.context)
        markwon.setMarkdown(holder.binding.noteContentItem, note.content)

        // Ensure child views don't intercept clicks
        holder.binding.noteItemTitle.isClickable = false
        holder.binding.noteContentItem.isClickable = false
        holder.binding.noteDate.isClickable = false

        holder.binding.noteItemLayoutParent.setOnClickListener {
            onItemClick?.invoke(note)
        }
    }
}
