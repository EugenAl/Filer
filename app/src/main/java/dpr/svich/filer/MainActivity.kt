package dpr.svich.filer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dpr.svich.filer.list.ListFileAdapter
import dpr.svich.filer.model.FileWrapper
import java.io.File
import java.util.Arrays

class MainActivity : AppCompatActivity() {

    var adapter: ListFileAdapter? = null
    val path = Environment.getExternalStorageDirectory().toString()
    var currentFile: File? = null
    lateinit var folderName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val files = File(path).listFiles() as Array<File>

        val recyclerView = findViewById<RecyclerView>(R.id.filesRecyclerView)
        folderName = findViewById(R.id.folderTextView)
        folderName.text = "Root"

        adapter = ListFileAdapter(files.map { FileWrapper(file = it) })
        adapter?.let {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter!!.onItemClick = { item ->
                if(item.file.isDirectory){
                    Log.d("MainActivity", item.file.absolutePath)
                    currentFile = item.file
                    folderName.text = currentFile?.name
                    val list = item.file.listFiles()
                    list.sortBy { file -> file.name }
                    //list.sortBy { file -> file.isDirectory }
                    adapter!!.data(list?.map { FileWrapper(it) }!!)
                }
            }
        }
    }

    override fun onBackPressed() {
        if(currentFile != null && currentFile?.parentFile != null){
            Log.d("MainActivity", currentFile!!.absolutePath)
            val list = currentFile?.parentFile?.listFiles()!!
            list.sortBy { file -> file.name }
            list.sortBy { file -> file.isDirectory }
            adapter!!.data(list.map { FileWrapper(it) })
            currentFile = currentFile?.parentFile
            folderName.text = currentFile?.name
        } else{
            super.onBackPressed()
        }
    }
}