package id.derysudrajat.docscan.ui.screen

import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning

object DocumentScanner {
    /**
     * [options] is client builder for [GmsDocumentScanning]
     * you can configure some option on client scanning document builder such as
     *  -Import Image from Gallery
     * [GmsDocumentScannerOptions.Builder.setGalleryImportAllowed] to set is allowed to take image from gallery
     *  - Set Page Limit
     * [GmsDocumentScannerOptions.Builder.setPageLimit] for set limit of your document scanner
     *  - Document Result Format
     * [GmsDocumentScannerOptions.Builder.setResultFormats] contain output format option there are
     * two option are [GmsDocumentScannerOptions.RESULT_FORMAT_JPEG] and [GmsDocumentScannerOptions.RESULT_FORMAT_PDF]
     * - Scanner Features
     * [GmsDocumentScannerOptions.Builder.setScannerMode] is scanner feature option that can you choose
     * there are some option are:
     * [GmsDocumentScannerOptions.SCANNER_MODE_BASE] is contain basic editing capabilities (crop, rotate, reorder pages, etc…)
     * [GmsDocumentScannerOptions.SCANNER_MODE_BASE_WITH_FILTER] is contain adds image filters (grayscale, auto image enhancement, etc…)
     * also contain the SCANNER_MODE_BASE mode.
     * [GmsDocumentScannerOptions.SCANNER_MODE_FULL] is (default): contain adds ML-enabled image cleaning capabilities (erase stains, fingers, etc…)
     * also contain the SCANNER_MODE_BASE_WITH_FILTER mode.
     * This mode will also allow future major features to be automatically added along with
     * Google Play services updates, while the other two modes will maintain their current feature sets and only receive minor refinements.
     */
    private val options = GmsDocumentScannerOptions.Builder()
        .setGalleryImportAllowed(true)
        .setPageLimit(10)
        .setResultFormats(RESULT_FORMAT_PDF, RESULT_FORMAT_JPEG)
        .setScannerMode(SCANNER_MODE_FULL)
        .build()

    /**
     * [scanner] is client for launch getStartScanIntent to get intentSender
     */
    val scanner = GmsDocumentScanning.getClient(options)
}