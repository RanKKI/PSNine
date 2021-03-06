package xyz.rankki.psnine.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class HtmlUtil {

    companion object {

        private val executor = Executors.newScheduledThreadPool(5)

        private fun getHtml(source: String, imageGetter: HtmlImageGetter?): Spanned {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT, imageGetter, null)
            } else {
                Html.fromHtml(source, imageGetter, null)
            }
        }

        fun fromHtml(mContext: Context, source: String, tv: TextView) {
            doAsync {
                var spanned: Spanned = getHtml(source, null)
                uiThread {
                    tv.text = spanned
                }
                if (source.contains("img")) {
                    spanned = getHtml(source, HtmlImageGetter(mContext, tv, executor))
                    uiThread {
                        tv.text = spanned
                    }
                }
            }
        }
    }


    class HtmlImageGetter(private val mContext: Context, private val tv: TextView, private val executorPool: ExecutorService) : Html.ImageGetter {

        override fun getDrawable(source: String?): Drawable {

            val urlDrawable = UrlDrawable()
            doAsync(exceptionHandler = {
                ToastUtils.showShort(it.message)
            }, task = {
                val bitmap = Glide.with(mContext).asBitmap().load(source).submit().get()
                urlDrawable.set(resize(bitmap))
                uiThread {
                    tv.text = tv.text
                    tv.invalidate()
                }
            }, executorService = executorPool)
            return urlDrawable

        }

        private fun resize(bitmap: Bitmap): Bitmap {
            val width = bitmap.width
            val height = bitmap.height
            val maxWidth = ScreenUtils.getScreenWidth()
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

    }

    class UrlDrawable : BitmapDrawable() {

        private var _bitmap: Bitmap? = null

        fun set(bitmap: Bitmap) {
            _bitmap = bitmap
            setBounds(0, 0, _bitmap!!.width, _bitmap!!.height)
        }


        override fun draw(canvas: Canvas?) {
            super.draw(canvas)
            if (_bitmap != null) {
                canvas?.drawBitmap(_bitmap, 0f, 0f, paint)
            }
        }
    }
}