package dpr.svich.filer.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dpr.svich.filer.R
import dpr.svich.filer.model.FileWrapper
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes

class ListFileAdapter(private var dataSet: List<FileWrapper>) :
    RecyclerView.Adapter<ListFileAdapter.ViewHolder>() {

    var onItemClick: ((FileWrapper) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileName: TextView
        val fileAttr: TextView
        val fileIcon: ImageView

        init {
            fileName = view.findViewById(R.id.fileNameTextView)
            fileAttr = view.findViewById(R.id.fileAttrTextView)
            fileIcon = view.findViewById(R.id.fileIcoImageView)
            view.setOnClickListener {
                onItemClick?.invoke(dataSet[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.files_row_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = dataSet[position].file
        val attr = Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)
        holder.fileName.text = file.name
        holder.fileAttr.text = buildString {
            if (!file.isDirectory) {
                append(attr.size().format())
                append(" | ")
            }
            append(attr.creationTime())
        }
        if (file.isDirectory) {
            holder.fileIcon.setImageResource(R.drawable.folder)
        } else {
            when (file.extension) {
                "jpg", "gif", "png" -> holder.fileIcon.setImageResource(R.drawable.file_image)
                "mp3", "wav" -> holder.fileIcon.setImageResource(R.drawable.file_audio)
                "3gp", "mp4" -> holder.fileIcon.setImageResource(R.drawable.file_video)
                else -> holder.fileIcon.setImageResource(R.drawable.file)
            }
        }
    }

    fun data(dataSet: List<FileWrapper>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun Long.format(): String {
        if (this > 1_048_576) return "${String.format("%4.2f", this / 1_048_576.0)} MB"
        else if (this > 1024) return "${String.format("%4.2f", this / 1024.0)} KB"
        return "$this byte"
    }
}