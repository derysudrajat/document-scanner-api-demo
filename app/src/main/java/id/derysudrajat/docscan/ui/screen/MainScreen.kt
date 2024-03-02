package id.derysudrajat.docscan.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import id.derysudrajat.docscan.R
import id.derysudrajat.docscan.ui.theme.DocumenScannerTheme

@Composable
fun MainScreen(
    documentScanUiState: DocumentScanUiState
) {

    var isEditTable by remember { mutableStateOf(false) }
    var documentName by remember { mutableStateOf(documentScanUiState.docName) }

    LaunchedEffect(documentScanUiState.docName) { documentName = documentScanUiState.docName }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val (image, btnShare, btnSave, btnScan, btnEdit, tfName, btnOk, docsInfo, docsPage) = createRefs()
            if (documentScanUiState.isResultOk) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(documentScanUiState.imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .border(2.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .constrainAs(image) {
                            top.linkTo(parent.top, 72.dp)
                            start.linkTo(parent.start, 32.dp)
                            end.linkTo(parent.end, 32.dp)
                            width = Dimension.value(250.dp)
                            height = Dimension.ratio("9:16")
                        }
                )
                Text(
                    text = documentScanUiState.docPage,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(8.dp)
                        .constrainAs(docsPage) {
                            bottom.linkTo(image.bottom, 12.dp)
                            start.linkTo(image.start, 12.dp)
                        })
                IconButton(
                    onClick = {
                        documentScanUiState.onDownloadDocument.invoke(
                            documentName, documentScanUiState.docsUri
                        )
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(),
                    modifier = Modifier.constrainAs(btnSave) {
                        bottom.linkTo(image.bottom, 8.dp)
                        end.linkTo(btnShare.start, 8.dp)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = "Download"
                    )
                }

                IconButton(
                    onClick = {
                        documentScanUiState.onSharePdf.invoke(
                            documentName,
                            documentScanUiState.docsUri
                        )
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(),
                    modifier = Modifier.constrainAs(btnShare) {
                        bottom.linkTo(image.bottom, 8.dp)
                        end.linkTo(image.end, 8.dp)
                    }
                ) {
                    Icon(imageVector = Icons.Rounded.Share, contentDescription = "Share")
                }

                if (!isEditTable) {
                    IconButton(
                        onClick = { isEditTable = true },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(),
                        modifier = Modifier.constrainAs(btnEdit) {
                            start.linkTo(docsInfo.end, 8.dp)
                            top.linkTo(docsInfo.top)
                            bottom.linkTo(docsInfo.bottom)
                        }
                    ) {
                        Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit")
                    }

                    Text(
                        text = "$documentName.pdf",
                        modifier = Modifier.constrainAs(docsInfo) {
                            top.linkTo(image.bottom, 16.dp)
                            start.linkTo(image.start)
                            end.linkTo(image.end, 48.dp)
                            width = Dimension.fillToConstraints
                        },
                        textAlign = TextAlign.Center
                    )
                } else {
                    TextField(
                        value = documentName, onValueChange = { newName ->
                            documentName = newName
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(32.dp))
                            .constrainAs(tfName) {
                                top.linkTo(image.bottom, 16.dp)
                                start.linkTo(parent.start, 16.dp)
                                end.linkTo(btnOk.start, 8.dp)
                                width = Dimension.fillToConstraints
                            }
                    )
                    IconButton(
                        onClick = {
                            isEditTable = false
                        },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(),
                        modifier = Modifier.constrainAs(btnOk) {
                            top.linkTo(tfName.top)
                            end.linkTo(parent.end, 16.dp)
                            bottom.linkTo(tfName.bottom)
                        }
                    ) {
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = "Check")
                    }
                }
            }

            Button(
                onClick = documentScanUiState.onScanNewDoc,
                modifier = Modifier.constrainAs(btnScan) {
                    when {
                        documentScanUiState.isResultOk && !isEditTable -> top.linkTo(docsInfo.bottom, 16.dp)
                        isEditTable -> top.linkTo(tfName.bottom, 16.dp)
                        else -> top.linkTo(parent.top)
                    }
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    if (!documentScanUiState.isResultOk) bottom.linkTo(parent.bottom)
                }
            ) {
                Text(text = "Scan New Document")
            }
        }
    }
}


@Preview
@Composable
private fun PreviewMainScreen() {
    DocumenScannerTheme {
        MainScreen(DocumentScanUiState.Empty.copy(
            imageUri = "",
            docsUri = "".toUri(),
            docName = "File Name That Soo Long More Long And More Long Again",
            docPage = "1 Page",
            isResultOk = true,
            onScanNewDoc = {}, onSharePdf = { _, _ -> }, onDownloadDocument = { _, _ -> }
        ))
    }
}