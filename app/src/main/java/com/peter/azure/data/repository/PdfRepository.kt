package com.peter.azure.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.peter.azure.data.entity.DataResult
import com.peter.azure.data.entity.PrintGame
import com.peter.azure.data.entity.SudokuPdf
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class PdfRepository @Inject constructor(
    @ApplicationContext
    private val appContext: Context,
) {

    /**
     * Generate a sudoku pdf
     * @return A SudokuPdf object.
     */
    suspend fun generateSudokuPdf(printGameList: List<PrintGame>): DataResult<SudokuPdf> =
        withContext(Dispatchers.IO) {

            // A4
            val pageHeight = 842
            val pageWidth = 595
            val pdfDocument = PdfDocument()

            printGameList.forEachIndexed { index, printGame ->
                val myPageInfo = PdfDocument
                    .PageInfo
                    .Builder(pageWidth, pageHeight, index)
                    .create()
                val myPage = pdfDocument.startPage(myPageInfo)
                val canvas = myPage.canvas

                val docInfo = Paint()
                docInfo.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                docInfo.color = Color.BLACK
                docInfo.textSize = 12f

                // header
                val appName = "Azure Sudoku"
                val appNameHeight = (docInfo.fontMetrics.descent - docInfo.fontMetrics.ascent)
                val appNameStartY = 52f + (appNameHeight / 3)
                canvas.drawText(appName, 60f, appNameStartY, docInfo)

                val dateTime = DateFormat.getDateInstance()
                    .format(Calendar.getInstance().time)
                val dateTimeWidth = docInfo.measureText(dateTime)
                val dateTimeStartX = 535f - dateTimeWidth
                val dateTimeHeight = (docInfo.fontMetrics.descent - docInfo.fontMetrics.ascent)
                val dateTimeStartY = 52f + (dateTimeHeight / 3)
                canvas.drawText(dateTime, dateTimeStartX, dateTimeStartY, docInfo)

                // footer
                val gameLevel = "Game Level - ${printGame.gameLevel.name}"
                val gameLevelHeight = (docInfo.fontMetrics.descent - docInfo.fontMetrics.ascent)
                val gameLevelStartY = 790f + (gameLevelHeight / 3)
                canvas.drawText(gameLevel, 60f, gameLevelStartY, docInfo)

                val pageNum = "${index + 1} / ${printGameList.size}"
                val page = Paint()
                page.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                page.color = Color.BLACK
                page.textSize = 12f
                val pageNumWidth = docInfo.measureText(pageNum)
                val pageNumStartX = 535f - pageNumWidth
                val pageNumHeight = (docInfo.fontMetrics.descent - docInfo.fontMetrics.ascent)
                val pageNumStartY = 790f + (pageNumHeight / 3)
                canvas.drawText(pageNum, pageNumStartX, pageNumStartY, page)

                // board frame
                val rectStartX = 60f
                val rectStartY = 146f
                val rectEndX = 535f
                val rectEndY = 696f
                val rectBoarder = Paint()
                rectBoarder.style = Paint.Style.STROKE
                rectBoarder.color = Color.BLACK
                rectBoarder.strokeWidth = 1f
                canvas.drawRect(rectStartX, rectStartY, rectEndX, rectEndY, rectBoarder)

                // board cell
                val line = Paint()
                line.color = Color.BLACK
                line.strokeWidth = 1f
                val heightGap = (rectEndY - rectStartY) / 9
                for (i in 1..8) {
                    val horizontal = i * heightGap + rectStartY
                    canvas.drawLine(rectStartX, horizontal, rectEndX, horizontal, line)
                }
                val widthGap = (rectEndX - rectStartX) / 9
                for (i in 1..8) {
                    val vertical = i * widthGap + rectStartX
                    canvas.drawLine(vertical, rectStartY, vertical, rectEndY, line)
                }

                // fill num
                val num = Paint()
                num.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                num.color = Color.BLACK
                num.textSize = 20f
                val textWidthGap = widthGap / 2
                val textHeightGap = heightGap / 2
                printGame.sudoku.forEachIndexed { row, numList ->
                    val startY = row * heightGap + rectStartY
                    numList.forEachIndexed { i, textNum ->
                        val text = if (textNum != 0) {
                            "$textNum"
                        } else {
                            ""
                        }
                        val textWidth = num.measureText(text)
                        val textStartX = i * widthGap + rectStartX + textWidthGap - (textWidth / 2)
                        val textHeight = (num.fontMetrics.descent - num.fontMetrics.ascent)
                        val textStartY = startY + textHeightGap + (textHeight / 3)

                        canvas.drawText(text, textStartX, textStartY, num)
                    }
                }

                pdfDocument.finishPage(myPage)
            }


            val files = appContext.filesDir.listFiles()
            files?.let { fileList ->
                if (fileList.size > 4) {
                    fileList.sortByDescending { it.lastModified() }
                    val removeList = fileList.asList().subList(1, fileList.size - 1)
                    removeList.forEach {
                        it.delete()
                    }
                }
            }

            try {
                val fileName = "azure-sudoku-${UUID.randomUUID()}.pdf"
                val file = File(appContext.filesDir, fileName)

                pdfDocument.writeTo(FileOutputStream(file))
                pdfDocument.close()

                val preview = getPreviewImageList(file)

                return@withContext DataResult.Success(SudokuPdf(file, preview))
            } catch (exception: Exception) {
                val code = if (exception is IOException) {
                    DataResult.Error.Code.IO
                } else {
                    DataResult.Error.Code.UNKNOWN
                }

                return@withContext DataResult.Error(code)
            }

        }

    fun deleteCacheFiles() {
        appContext.filesDir.deleteRecursively()
    }

    private fun getPreviewImageList(pdf: File): List<Bitmap> {
        val input = ParcelFileDescriptor.open(pdf, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(input)
        val width = 1080
        val height = (width * sqrt(2f)).toInt()

        val bitmapList = mutableListOf<Bitmap>()
        val size = renderer.pageCount
        for (index in 0 until size) {
            val pagePic = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            renderer.openPage(index).use { page ->
                page.render(
                    pagePic,
                    null,
                    null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                )
            }
            bitmapList.add(pagePic)
        }

        renderer.close()

        return bitmapList
    }

}