package dpr.svich.filer.model

import java.io.File

data class FileWrapper(
    val file: File,
    var changed: Boolean = false
)