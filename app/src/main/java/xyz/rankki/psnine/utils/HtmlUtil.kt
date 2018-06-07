package xyz.rankki.psnine.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.uiThread


class HtmlUtil {

    companion object {

        private fun getHtml(source: String, imageGetter: HtmlImageGetter?): Spanned {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT, imageGetter, null)
            } else {
                Html.fromHtml(source, imageGetter, null)
            }
        }

        fun setHtml(mContext: Context, tv: TextView, source: String) {
            doAsync {
                val newSource: String = source
                val cleanedHtml: Spanned = getHtml(newSource, null)
                uiThread {
                    tv.text = cleanedHtml
                }
                if (newSource.contains("img")) {
                    val html: Spanned = getHtml(newSource, HtmlImageGetter(mContext, tv))
                    uiThread {
                        tv.text = html
                    }
                }
            }
        }
    }


    class HtmlImageGetter(private val mContext: Context, private val tv: TextView) : Html.ImageGetter {

        override fun getDrawable(source: String?): Drawable {
//            Log.i("load image $source")
            val bitmap: Bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(source).submit().get()
            return UrlDrawable(mContext.resources,
                    doAsyncResult {
                        return@doAsyncResult resize(bitmap)
                    }.get()
            )
        }

        private fun resize(bitmap: Bitmap): Bitmap {
            val width = bitmap.width
            val height = bitmap.height
            val maxWidth = ScreenUtils.getScreenWidth() / 3
            val ratio = if (width <= 100 || height <= 100) {
                2.0f
            } else if (width > maxWidth) {
                maxWidth / width.toFloat()
            } else {
                1.0f
            }
            val scaledWidth = (ratio * width).toInt()
            val scaledHeight = (ratio * height).toInt()
            return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false)
        }

        class UrlDrawable(_res: Resources, private val _bitmap: Bitmap) : BitmapDrawable(_res, _bitmap) {

            init {
                setBounds(0, 0, _bitmap.width, _bitmap.height)
            }

            override fun draw(canvas: Canvas?) {
                super.draw(canvas)
                canvas?.drawBitmap(_bitmap, 0f, 0f, paint)
            }

        }
    }
}