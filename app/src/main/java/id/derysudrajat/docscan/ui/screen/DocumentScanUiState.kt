package id.derysudrajat.docscan.ui.screen

import android.net.Uri
import androidx.core.net.toUri

data class DocumentScanUiState(
    var imageUri: String,
    var docsUri: Uri,
    var docName: String,
    var docPage: String,
    var isResultOk: Boolean,
    var onScanNewDoc: () -> Unit,
    var onSharePdf: (name: String, uri: Uri) -> Unit,
    var onDownloadDocument: (name: String, uri: Uri) -> Unit
) {
    companion object {
        val Empty = DocumentScanUiState(
            "", "".toUri(), "", "", false, {}, { _, _ -> }, { _, _ -> }
        )
    }
}