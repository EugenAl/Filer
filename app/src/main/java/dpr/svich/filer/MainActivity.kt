package dpr.svich.filer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import dpr.svich.filer.list.ListFileAdapter
import dpr.svich.filer.model.FileWrapper
import java.io.File

class MainActivity : AppCompatActivity() {

    private var adapter: ListFileAdapter? = null
    private val path = Environment.getExternalStorageDirectory().toString()
    private var currentFile: File? = null
    private lateinit var folderName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cacheFiles()

        val recyclerView = findViewById<RecyclerView>(R.id.filesRecyclerView)
        folderName = findViewById(R.id.folderTextView)
        folderName.text = "Root"

        adapter = ListFileAdapter(sortFiles(File(path).listFiles()!!))
        adapter?.let {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter!!.onItemClick = { item ->
                if(item.file.isDirectory){
                    Log.d("MainActivity", item.file.absolutePath)
                    currentFile = item.file
                    folderName.text = currentFile?.name
                    adapter!!.data(sortFiles(item.file.listFiles()!!))
                    recyclerView.scrollToPosition(0)
                }
            }
        }
    }

    override fun onBackPressed() {
        if(currentFile != null && currentFile?.parentFile != null){
            Log.d("MainActivity", currentFile!!.absolutePath)
            adapter!!.data(sortFiles(currentFile?.parentFile?.listFiles()!!))
            currentFile = currentFile?.parentFile
            folderName.text = currentFile?.name
        } else{
            super.onBackPressed()
        }
    }

    private fun sortFiles(files: Array<File>) : List<FileWrapper>{
        val dirs = files.filter { it.isDirectory }
        val other = files.filterNot { it.isDirectory }

        return (dirs.sortedBy { it.name.lowercase() }+other.sortedBy { it.name.lowercase() })
            .map { FileWrapper(it) }
    }

    private fun cacheFiles(){
        val worker = OneTimeWorkRequest.Builder(FilesWorker::class.java)
        WorkManager.getInstance(this).enqueue(worker.build())
    }
}