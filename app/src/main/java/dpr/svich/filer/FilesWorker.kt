package dpr.svich.filer

import android.content.Context
import android.os.Environment
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import dpr.svich.filer.db.AppDatabase
import dpr.svich.filer.model.FileHash
import java.io.File
import java.lang.Exception

class FilesWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params){
    override suspend fun doWork(): Result {
        val  saved = AppDatabase.getInstance(context).fileHashDao().getAll()
        return try {
            for(file in File(Environment.getExternalStorageDirectory().toString()).listFiles()!!)
                if(saved.any { it.name == file.name }.not())
                    AppDatabase.getInstance(context).fileHashDao().insert(
                        FileHash(0, file.name, file.hashCode()))
            Result.success()
        } catch (e: Exception){
            Result.failure()
        }
    }
}