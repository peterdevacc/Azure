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
import com.peter.azure.data.util.PDF_NAME_PREFIX
import com.peter.azure.data.util.PDF_NUM_LIMIT
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
    @ApplicationContext private val appContext: Context,
) {

    suspend fun generateSudokuPdf(printGameList: List<PrintGame>): DataResult<SudokuPdf> =
        withContext(Dispatchers.IO) {

            // A4
            val pageHeight = 842
            val pageWidth = 595
            val pdfDocument = PdfDocument()

            printGameList.forEachIndexed { index, printGame ->
                val sudokuPageInfo = PdfDocument
                    .PageInfo
                    .Builder(pageWidth, pageHeight, index)
                    .create()
                val sudokuPage = pdfDocument.startPage(sudokuPageInfo)
                val canvas = sudokuPage.canvas

                val infoTextPaint = Paint()
                infoTextPaint.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                infoTextPaint.color = Color.BLACK
                infoTextPaint.textSize = 12f

                // header
                val appName = "Azure Sudoku"
                val appNameHeight =
                    infoTextPaint.fontMetrics.descent - infoTextPaint.fontMetrics.ascent
                val appNameStartY = 52f + (appNameHeight / 3)
                canvas.drawText(appName, 60f, appNameStartY, infoTextPaint)

                val dateTime = DateFormat.getDateInstance()
                    .format(Calendar.getInstance().time)
                val dateTimeWidth = infoTextPaint.measureText(dateTime)
                val dateTimeStartX = 535f - dateTimeWidth
                val dateTimeHeight =
                    infoTextPaint.fontMetrics.descent - infoTextPaint.fontMetrics.ascent
                val dateTimeStartY = 52f + (dateTimeHeight / 3)
                canvas.drawText(dateTime, dateTimeStartX, dateTimeStartY, infoTextPaint)

                // footer
                val gameLevel = "Game Level - ${printGame.gameLevel.name}"
                val gameLevelHeight =
                    infoTextPaint.fontMetrics.descent - infoTextPaint.fontMetrics.ascent
                val gameLevelStartY = 790f + (gameLevelHeight / 3)
                canvas.drawText(gameLevel, 60f, gameLevelStartY, infoTextPaint)

                val pageNum = "${index + 1} / ${printGameList.size}"
                val pagePaint = Paint()
                pagePaint.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                pagePaint.color = Color.BLACK
                pagePaint.textSize = 12f
                val pageNumWidth = infoTextPaint.measureText(pageNum)
                val pageNumStartX = 535f - pageNumWidth
                val pageNumHeight =
                    infoTextPaint.fontMetrics.descent - infoTextPaint.fontMetrics.ascent
                val pageNumStartY = 790f + (pageNumHeight / 3)
                canvas.drawText(pageNum, pageNumStartX, pageNumStartY, pagePaint)

                // board frame
                val rectStartX = 60f
                val rectStartY = 146f
                val rectEndX = 535f
                val rectEndY = 696f
                val rectBoarderPaint = Paint()
                rectBoarderPaint.style = Paint.Style.STROKE
                rectBoarderPaint.color = Color.BLACK
                rectBoarderPaint.strokeWidth = 1f
                canvas.drawRect(rectStartX, rectStartY, rectEndX, rectEndY, rectBoarderPaint)

                // board cell
                val boardPaint = Paint()
                boardPaint.color = Color.BLACK
                boardPaint.strokeWidth = 1f
                val heightGap = (rectEndY - rectStartY) / 9
                for (i in 1..8) {
                    val horizontal = i * heightGap + rectStartY
                    canvas.drawLine(rectStartX, horizontal, rectEndX, horizontal, boardPaint)
                }
                val widthGap = (rectEndX - rectStartX) / 9
                for (i in 1..8) {
                    val vertical = i * widthGap + rectStartX
                    canvas.drawLine(vertical, rectStartY, vertical, rectEndY, boardPaint)
                }

                // fill num
                val numPaint = Paint()
                numPaint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                numPaint.color = Color.BLACK
                numPaint.textSize = 20f
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
                        val textWidth = numPaint.measureText(text)
                        val textStartX = i * widthGap + rectStartX + textWidthGap - (textWidth / 2)
                        val textHeight = numPaint.fontMetrics.descent - numPaint.fontMetrics.ascent
                        val textStartY = startY + textHeightGap + (textHeight / 3)

                        canvas.drawText(text, textStartX, textStartY, numPaint)
                    }
                }

                pdfDocument.finishPage(sudokuPage)
            }

            try {
                val fileName = "$PDF_NAME_PREFIX-${UUID.randomUUID()}.pdf"
                val file = File(appContext.filesDir, fileName)

                pdfDocument.writeTo(FileOutputStream(file))
                pdfDocument.close()

                limitPdfNum(appContext.filesDir)

                val preview = getPageImageList(file)

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

    fun deleteCachePDF() {
        appContext.filesDir.listFiles()?.forEach {
            if (it.name.startsWith(PDF_NAME_PREFIX)) {
                it.delete()
            }
        }
    }

    private fun limitPdfNum(filesDir: File) {
        val files = filesDir.listFiles()
        files?.let { fileList ->
            val pdfList = fileList
                .filter { it.name.startsWith(PDF_NAME_PREFIX) }
                .toMutableList()
            if (pdfList.size > PDF_NUM_LIMIT) {
                pdfList.sortByDescending { it.lastModified() }
                val removeList = pdfList.subList(2, pdfList.size)
                removeList.forEach {
                    it.delete()
                }
            }
        }
    }

    private fun getPageImageList(pdf: File): List<Bitmap> {
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