package com.example.picleaf

import android.content.res.AssetManager
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class Categorization (assetManager: AssetManager, modelPath: String, labelPath: String,inputSize: Int) {
    private val GVN_INP_SZ: Int = inputSize
    private val PHOTO_SDEVIATE = 255.0f
    private val GREAT_OUTCOM_MXX = 3
    private val PITNR: Interpreter
    private var ROW_LINE = whereistopped
    private val IMAGE_PXL_SZ: Int = 3
    private val  PHOTO_MEN = 0
    private val POINT_THRHLDD = 0.4f

    data class Categorization(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 0F
    ){
        override fun toString(): String {
            return "Title = $title, Confidence = $confidence)"
        }
    }
    init {
        PITNR = Interpreter(loadModelFile(assetManager,modelPath))
        ROW_LINE = loadlabellist(assetManager,labelPath)
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {

        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val starOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode)

    }
}