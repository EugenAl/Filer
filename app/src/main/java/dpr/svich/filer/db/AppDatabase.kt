package dpr.svich.filer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dpr.svich.filer.model.FileHash

@Database(entities = [FileHash::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun fileHashDao(): FileHashDAO

    companion object{
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context) .also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "hashdb")
                .build()
    }
}