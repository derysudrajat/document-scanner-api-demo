package id.derysudrajat.docscan.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import id.derysudrajat.docscan.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


val Context.appDirectory
    get(): File {
        return if (Environment.getExternalStorageState() == null) {
            val f = File(
                Environment.getDataDirectory().absolutePath
                        + "/Download/${this.getString(R.string.app_name)}/"
            )
            if (!f.exists()) f.mkdirs()
            f
        } else {
            val f = File(
                Environment.getExternalStorageDirectory()
                    .absolutePath + "/Download/${this.getString(R.string.app_name)}/"
            )
            if (!f.exists()) f.mkdirs()
            f
        }
    }

fun saveFile(context: Context, fileName: String, uri: Uri, onFinish: (Uri) -> Unit) {
    val name = "$fileName.pdf"
    val file = File(context.appDirectory, name)
    val uriResult = FileProvider.getUriForFile(
        context, "${context.packageName}.provider", file
    )
    try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream -> inputStream.copyTo(outputStream) }
            onFinish.invoke(uriResult)
        }
    } catch (e: IOException) {
        Log.d("Exception", "File write failed: $e")
    }
}

fun sharePdf(context: Context, uri: Uri) {
    val shareIntent =
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "application/pdf"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    context.startActivity(Intent.createChooser(shareIntent, "share pdf"))
}