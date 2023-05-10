package dpr.svich.filer

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dpr.svich.filer.db.AppDatabase
import dpr.svich.filer.model.FileHash
import java.io.File

class FilesWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params){
    override suspend fun doWork(): Result {
        val  saved = AppDatabase.getInstance(context).fileHashDao().getAll()
        for(file in File("path").listFiles()!!)
            if(saved.any { it.name == file.name }.not())
                AppDatabase.getInstance(context).fileHashDao().insert(
                    FileHash(0, file.name, file.hashCode()))
        return Result.success()
    }
}