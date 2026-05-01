package com.example.notex.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notex.databinding.NoteItemLayoutBinding
import com.example.notex.fragments.NoteFragmentDirections
import com.example.notex.model.Note
import io.noties.markwon.Markwon

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val binding: NoteItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]

        holder.binding.noteItemTitle.text = currentNote.title
        
        val markwon = Markwon.create(holder.itemView.context)
        markwon.setMarkdown(holder.binding.noteContentItem, currentNote.content)

        holder.binding.noteDate.text = currentNote.date

        holder.itemView.setOnClickListener {
            val direction = NoteFragmentDirections.actionNoteFragmentToSaveOrUpdate(currentNote)
            it.findNavController().navigate(direction)
        }
    }
}
