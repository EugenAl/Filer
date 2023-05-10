package dpr.svich.filer

import android.content.Context
import android.os.Environment
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dpr.svich.filer.db.AppDatabase
import dpr.svich.filer.model.FileHash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files

class FilesWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params){
    override suspend fun doWork(): Result {
        val  saved = AppDatabase.getInstance(context).fileHashDao().getAll()
        return try {
            for(file in File(Environment.getExternalStorageDirectory().toString()).listFiles()!!)
                if(saved.any { it.name == file.name }.not() && !file.isDirectory) {
                    val hash = withContext(Dispatchers.IO) {
                        Files.readAllBytes(file.toPath()).contentHashCode()
                    }
                    AppDatabase.getInstance(context).fileHashDao().insert(
                        FileHash(0, file.name, hash))
                }
            Result.success()
        } catch (e: Exception){
            Result.failure()
        }
    }
}