package dpr.svich.filer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hash")
data class FileHash(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String?,
    val hash: Int?
)
