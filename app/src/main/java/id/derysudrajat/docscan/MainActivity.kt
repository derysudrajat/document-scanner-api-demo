package id.derysudrajat.docscan

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import id.derysudrajat.docscan.ui.screen.DocumentScanUiState
import id.derysudrajat.docscan.ui.screen.DocumentScanner
import id.derysudrajat.docscan.ui.screen.MainScreen
import id.derysudrajat.docscan.ui.theme.DocumenScannerTheme
import id.derysudrajat.docscan.utils.saveFile
import id.derysudrajat.docscan.utils.sharePdf
import java.io.File

class MainActivity : ComponentActivity() {

    private val scanner by lazy { DocumentScanner.scanner }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DocumenScannerTheme {
                var resultUri by remember { mutableStateOf("".toUri()) }
                var documentScanUiState by remember { mutableStateOf(DocumentScanUiState.Empty) }
                val scannerLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                        if (result.resultCode == RESULT_OK) {
                            documentScanUiState = documentScanUiState.copy(isResultOk = true)
                            val resultDocs =
                                GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                            resultDocs?.pages?.get(0)?.imageUri?.let { uriImage ->
                                documentScanUiState = documentScanUiState.copy(
                                    imageUri = uriImage.toString()
                                )
                            }
                            resultDocs?.pdf?.uri?.path?.let { path ->
                                val docsFile = File(path)
                                val pageSize = resultDocs.pages?.size?:0
                                val externalUri = FileProvider.getUriForFile(
                                    this,
                                    "${packageName}.provider",
                                    docsFile
                                )
                                documentScanUiState = documentScanUiState.copy(
                                    docName = docsFile.name.replace(".pdf", ""),
                                    docPage = "$pageSize Page${if (pageSize > 1) "s" else ""}",
                                    docsUri = externalUri
                                )
                            }
                        }
                    }

                documentScanUiState.onScanNewDoc = {
                    documentScanUiState = documentScanUiState.copy(isResultOk = false)
                    resultUri = "".toUri()
                    scanner.getStartScanIntent(this)
                        .addOnSuccessListener { intentSender ->
                            scannerLauncher.launch(
                                IntentSenderRequest.Builder(intentSender).build()
                            )
                        }
                        .addOnFailureListener {
                            documentScanUiState = documentScanUiState.copy(isResultOk = false)
                            Toast.makeText(this, "Failed Scan Document", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
                documentScanUiState.onSharePdf = { name, uri ->
                    if (resultUri.toString().isNotBlank()) sharePdf(this, resultUri)
                    else saveFile(this, name, uri) { result ->
                        resultUri = result
                        sharePdf(this, resultUri)
                    }
                }

                documentScanUiState.onDownloadDocument = { name, uri ->
                    saveFile(this, name, uri) { result ->
                        Toast.makeText(this, "$name.pdf is successful saved", Toast.LENGTH_SHORT)
                            .show()
                        resultUri = result
                    }
                }

                MainScreen(documentScanUiState = documentScanUiState)
            }
        }
    }
}