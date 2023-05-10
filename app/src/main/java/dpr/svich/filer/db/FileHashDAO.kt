package dpr.svich.filer.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dpr.svich.filer.model.FileHash

@Dao
interface FileHashDAO {
    @Query("SELECT * FROM hash")
    fun getAll(): List<FileHash>
    @Insert
    fun insertAll(vararg files: FileHash)
    @Insert
    fun insert(fileHash: FileHash)
    @Query("DELETE FROM hash")
    fun nukeTable()
}