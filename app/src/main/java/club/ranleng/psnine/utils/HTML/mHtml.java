package club.ranleng.psnine.utils.html;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.ParagraphStyle;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 可以强行Handle所有标签.
 * enforce handle all tags.
 * Usage 用法
 * Boolean handleTag()
 * return ture; ( programmer will handle it)
 */

public class mHtml {

    public static Spanned fromHtml(String source, Html.ImageGetter imageGetter,
                                   TagHandler tagHandler) {
        Parser parser = new Parser();
        try {
            parser.setProperty(Parser.schemaProperty, HtmlParser.schema);
        } catch (org.xml.sax.SAXNotRecognizedException | org.xml.sax.SAXNotSupportedException e) {
            // Should not happen.
            throw new RuntimeException(e);
        }
        HtmlToSpannedConverter converter =
                new HtmlToSpannedConverter(source, imageGetter, tagHandler, parser);
        return converter.convert();
    }

    public interface TagHandler {
        void handleTag(boolean opening, String tag,
                       Editable output, Attributes attributes);
    }

    private static class HtmlParser {
        private static final HTMLSchema schema = new HTMLSchema();
    }

}

class HtmlToSpannedConverter implements ContentHandler {

    private static final float[] HEADING_SIZES = {
            1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f,
    };
    /**
     * Name-value mapping of HTML/CSS colors which have different values in {@link Color}.
     */
    private static final Map<String, Integer> sColorMap;
    private static Pattern sTextAlignPattern;
    private static Pattern sForegroundColorPattern;
    private static Pattern sBackgroundColorPattern;
    private static Pattern sTextDecorationPattern;

    static {
        sColorMap = new HashMap<>();
        sColorMap.put("black", 0xFF000000);
        sColorMap.put("silver", 0xFFc0c0c0);
        sColorMap.put("gray", 0xFF808080);
        sColorMap.put("white", 0xFFffffff);
        sColorMap.put("maroon", 0xFF800000);
        sColorMap.put("red", 0xFFff0000);
        sColorMap.put("purple", 0xFF800080);
        sColorMap.put("fuchsia", 0xFFff00ff);
        sColorMap.put("green", 0xFF008000);
        sColorMap.put("lime", 0xFF00ff00);
        sColorMap.put("olive", 0xFF808000);
        sColorMap.put("yellow", 0xFFffff00);
        sColorMap.put("navy", 0xFF000080);
        sColorMap.put("blue", 0xFF0000ff);
        sColorMap.put("teal", 0xFF008080);
        sColorMap.put("aqua", 0xFF00ffff);
        sColorMap.put("orange", 0xFFffa500);
        sColorMap.put("aliceblue", 0xFFf0f8ff);
        sColorMap.put("antiquewhite", 0xFFfaebd7);
        sColorMap.put("aquamarine", 0xFF7fffd4);
        sColorMap.put("azure", 0xFFf0ffff);
        sColorMap.put("beige", 0xFFf5f5dc);
        sColorMap.put("bisque", 0xFFffe4c4);
        sColorMap.put("blanchedalmond", 0xFFffebcd);
        sColorMap.put("blueviolet", 0xFF8a2be2);
        sColorMap.put("brown", 0xFFa52a2a);
        sColorMap.put("burlywood", 0xFFdeb887);
        sColorMap.put("cadetblue", 0xFF5f9ea0);
        sColorMap.put("chartreuse", 0xFF7fff00);
        sColorMap.put("chocolate", 0xFFd2691e);
        sColorMap.put("coral", 0xFFff7f50);
        sColorMap.put("cornflowerblue", 0xFF6495ed);
        sColorMap.put("cornsilk", 0xFFfff8dc);
        sColorMap.put("crimson", 0xFFdc143c);
        sColorMap.put("cyan", 0xFF00ffff);
        sColorMap.put("darkblue", 0xFF00008b);
        sColorMap.put("darkcyan", 0xFF008b8b);
        sColorMap.put("darkgoldenrod", 0xFFb8860b);
        sColorMap.put("darkgray", 0xFFa9a9a9);
        sColorMap.put("darkgreen", 0xFF006400);
        sColorMap.put("darkgrey", 0xFFa9a9a9);
        sColorMap.put("darkkhaki", 0xFFbdb76b);
        sColorMap.put("darkmagenta", 0xFF8b008b);
        sColorMap.put("darkolivegreen", 0xFF556b2f);
        sColorMap.put("darkorange", 0xFFff8c00);
        sColorMap.put("darkorchid", 0xFF9932cc);
        sColorMap.put("darkred", 0xFF8b0000);
        sColorMap.put("darksalmon", 0xFFe9967a);
        sColorMap.put("darkseagreen", 0xFF8fbc8f);
        sColorMap.put("darkslateblue", 0xFF483d8b);
        sColorMap.put("darkslategray", 0xFF2f4f4f);
        sColorMap.put("darkslategrey", 0xFF2f4f4f);
        sColorMap.put("darkturquoise", 0xFF00ced1);
        sColorMap.put("darkviolet", 0xFF9400d3);
        sColorMap.put("deeppink", 0xFFff1493);
        sColorMap.put("deepskyblue", 0xFF00bfff);
        sColorMap.put("dimgray", 0xFF696969);
        sColorMap.put("dimgrey", 0xFF696969);
        sColorMap.put("dodgerblue", 0xFF1e90ff);
        sColorMap.put("firebrick", 0xFFb22222);
        sColorMap.put("floralwhite", 0xFFfffaf0);
        sColorMap.put("forestgreen", 0xFF228b22);
        sColorMap.put("gainsboro", 0xFFdcdcdc);
        sColorMap.put("ghostwhite", 0xFFf8f8ff);
        sColorMap.put("gold", 0xFFffd700);
        sColorMap.put("goldenrod", 0xFFdaa520);
        sColorMap.put("greenyellow", 0xFFadff2f);
        sColorMap.put("grey", 0xFF808080);
        sColorMap.put("honeydew", 0xFFf0fff0);
        sColorMap.put("hotpink", 0xFFff69b4);
        sColorMap.put("indianred", 0xFFcd5c5c);
        sColorMap.put("indigo", 0xFF4b0082);
        sColorMap.put("ivory", 0xFFfffff0);
        sColorMap.put("khaki", 0xFFf0e68c);
        sColorMap.put("lavender", 0xFFe6e6fa);
        sColorMap.put("lavenderblush", 0xFFfff0f5);
        sColorMap.put("lawngreen", 0xFF7cfc00);
        sColorMap.put("lemonchiffon", 0xFFfffacd);
        sColorMap.put("lightblue", 0xFFadd8e6);
        sColorMap.put("lightcoral", 0xFFf08080);
        sColorMap.put("lightcyan", 0xFFe0ffff);
        sColorMap.put("lightgoldenrodyellow", 0xFFfafad2);
        sColorMap.put("lightgray", 0xFFd3d3d3);
        sColorMap.put("lightgreen", 0xFF90ee90);
        sColorMap.put("lightgrey", 0xFFd3d3d3);
        sColorMap.put("lightpink", 0xFFffb6c1);
        sColorMap.put("lightsalmon", 0xFFffa07a);
        sColorMap.put("lightseagreen", 0xFF20b2aa);
        sColorMap.put("lightskyblue", 0xFF87cefa);
        sColorMap.put("lightslategray", 0xFF778899);
        sColorMap.put("lightslategrey", 0xFF778899);
        sColorMap.put("lightsteelblue", 0xFFb0c4de);
        sColorMap.put("lightyellow", 0xFFffffe0);
        sColorMap.put("limegreen", 0xFF32cd32);
        sColorMap.put("linen", 0xFFfaf0e6);
        sColorMap.put("magenta", 0xFFff00ff);
        sColorMap.put("mediumaquamarine", 0xFF66cdaa);
        sColorMap.put("mediumblue", 0xFF0000cd);
        sColorMap.put("mediumorchid", 0xFFba55d3);
        sColorMap.put("mediumpurple", 0xFF9370db);
        sColorMap.put("mediumseagreen", 0xFF3cb371);
        sColorMap.put("mediumslateblue", 0xFF7b68ee);
        sColorMap.put("mediumspringgreen", 0xFF00fa9a);
        sColorMap.put("mediumturquoise", 0xFF48d1cc);
        sColorMap.put("mediumvioletred", 0xFFc71585);
        sColorMap.put("midnightblue", 0xFF191970);
        sColorMap.put("mintcream", 0xFFf5fffa);
        sColorMap.put("mistyrose", 0xFFffe4e1);
        sColorMap.put("moccasin", 0xFFffe4b5);
        sColorMap.put("navajowhite", 0xFFffdead);
        sColorMap.put("oldlace", 0xFFfdf5e6);
        sColorMap.put("olivedrab", 0xFF6b8e23);
        sColorMap.put("orangered", 0xFFff4500);
        sColorMap.put("orchid", 0xFFda70d6);
        sColorMap.put("palegoldenrod", 0xFFeee8aa);
        sColorMap.put("palegreen", 0xFF98fb98);
        sColorMap.put("paleturquoise", 0xFFafeeee);
        sColorMap.put("palevioletred", 0xFFdb7093);
        sColorMap.put("papayawhip", 0xFFffefd5);
        sColorMap.put("peachpuff", 0xFFffdab9);
        sColorMap.put("peru", 0xFFcd853f);
        sColorMap.put("pink", 0xFFffc0cb);
        sColorMap.put("plum", 0xFFdda0dd);
        sColorMap.put("powderblue", 0xFFb0e0e6);
        sColorMap.put("rosybrown", 0xFFbc8f8f);
        sColorMap.put("royalblue", 0xFF4169e1);
        sColorMap.put("saddlebrown", 0xFF8b4513);
        sColorMap.put("salmon", 0xFFfa8072);
        sColorMap.put("sandybrown", 0xFFf4a460);
        sColorMap.put("seagreen", 0xFF2e8b57);
        sColorMap.put("seashell", 0xFFfff5ee);
        sColorMap.put("sienna", 0xFFa0522d);
        sColorMap.put("skyblue", 0xFF87ceeb);
        sColorMap.put("slateblue", 0xFF6a5acd);
        sColorMap.put("slategray", 0xFF708090);
        sColorMap.put("slategrey", 0xFF708090);
        sColorMap.put("snow", 0xFFfffafa);
        sColorMap.put("springgreen", 0xFF00ff7f);
        sColorMap.put("steelblue", 0xFF4682b4);
        sColorMap.put("tan", 0xFFd2b48c);
        sColorMap.put("thistle", 0xFFd8bfd8);
        sColorMap.put("tomato", 0xFFff6347);
        sColorMap.put("turquoise", 0xFF40e0d0);
        sColorMap.put("violet", 0xFFee82ee);
        sColorMap.put("wheat", 0xFFf5deb3);
        sColorMap.put("whitesmoke", 0xFFf5f5f5);
        sColorMap.put("yellowgreen", 0xFF9acd32);
        sColorMap.put("rebeccapurple", 0xFF663399);
    }

    private String mSource;
    private XMLReader mReader;
    private SpannableStringBuilder mSpannableStringBuilder;
    private Html.ImageGetter mImageGetter;
    private mHtml.TagHandler mTagHandler;
    private Attributes attr;

    public HtmlToSpannedConverter(String source, Html.ImageGetter imageGetter,
                                  mHtml.TagHandler tagHandler, Parser parser) {
        mSource = source;
        mSpannableStringBuilder = new SpannableStringBuilder();
        mImageGetter = imageGetter;
        mTagHandler = tagHandler;
        mReader = parser;
    }

    private static Pattern getTextAlignPattern() {
        if (sTextAlignPattern == null) {
            sTextAlignPattern = Pattern.compile("(?:\\s+|\\A)text-align\\s*:\\s*(\\S*)\\b");
        }
        return sTextAlignPattern;
    }

    private static Pattern getForegroundColorPattern() {
        if (sForegroundColorPattern == null) {
            sForegroundColorPattern = Pattern.compile(
                    "(?:\\s+|\\A)color\\s*:\\s*(\\S*)\\b");
        }
        return sForegroundColorPattern;
    }

    private static Pattern getBackgroundColorPattern() {
        if (sBackgroundColorPattern == null) {
            sBackgroundColorPattern = Pattern.compile(
                    "(?:\\s+|\\A)background(?:-color)?\\s*:\\s*(\\S*)\\b");
        }
        return sBackgroundColorPattern;
    }

    private static Pattern getTextDecorationPattern() {
        if (sTextDecorationPattern == null) {
            sTextDecorationPattern = Pattern.compile(
                    "(?:\\s+|\\A)text-decoration\\s*:\\s*(\\S*)\\b");
        }
        return sTextDecorationPattern;
    }

    private static void appendNewlines(Editable text, int minNewline) {
        final int len = text.length();
        if (len == 0) {
            return;
        }
        int existingNewlines = 0;
        for (int i = len - 1; i >= 0 && text.charAt(i) == '\n'; i--) {
            existingNewlines++;
        }
        for (int j = existingNewlines; j < minNewline; j++) {
            text.append("\n");
        }
    }

    private static void startBlockElement(Editable text, Attributes attributes, int margin) {
        final int len = text.length();
        if (margin > 0) {
            appendNewlines(text, margin);
            start(text, new Newline(margin));
        }
        String style = attributes.getValue("", "style");
        if (style != null) {
            Matcher m = getTextAlignPattern().matcher(style);
            if (m.find()) {
                String alignment = m.group(1);
                if (alignment.equalsIgnoreCase("start")) {
                    start(text, new Alignment(Layout.Alignment.ALIGN_NORMAL));
                } else if (alignment.equalsIgnoreCase("center")) {
                    start(text, new Alignment(Layout.Alignment.ALIGN_CENTER));
                } else if (alignment.equalsIgnoreCase("end")) {
                    start(text, new Alignment(Layout.Alignment.ALIGN_OPPOSITE));
                }
            }
        }
    }

    private static void endBlockElement(Editable text) {
        Newline n = getLast(text, Newline.class);
        if (n != null) {
            appendNewlines(text, n.mNumNewlines);
            text.removeSpan(n);
        }
        Alignment a = getLast(text, Alignment.class);
        if (a != null) {
            setSpanFromMark(text, a, new AlignmentSpan.Standard(a.mAlignment));
        }
    }

    private static void handleBr(Editable text) {
        text.append('\n');
    }

    private static void endLi(Editable text) {
        endCssStyle(text);
        endBlockElement(text);
        end(text, Bullet.class, new BulletSpan());
    }

    private static void endBlockquote(Editable text) {
        endBlockElement(text);
        end(text, Blockquote.class, new QuoteSpan());
    }

    private static void endHeading(Editable text) {
        // RelativeSizeSpan and StyleSpan are CharacterStyles
        // Their ranges should not include the newlines at the end
        Heading h = getLast(text, Heading.class);
        if (h != null) {
            setSpanFromMark(text, h, new RelativeSizeSpan(HEADING_SIZES[h.mLevel]),
                    new StyleSpan(Typeface.BOLD));
        }
        endBlockElement(text);
    }

    private static <T> T getLast(Spanned text, Class<T> kind) {
        /*
         * This knows that the last returned object from getSpans()
         * will be the most recently added.
         */
        T[] objs = text.getSpans(0, text.length(), kind);
        if (objs.length == 0) {
            return null;
        } else {
            return objs[objs.length - 1];
        }
    }

    private static void setSpanFromMark(Spannable text, Object mark, Object... spans) {
        int where = text.getSpanStart(mark);
        text.removeSpan(mark);
        int len = text.length();
        if (where != len) {
            for (Object span : spans) {
                text.setSpan(span, where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private static void start(Editable text, Object mark) {
        int len = text.length();
        text.setSpan(mark, len, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private static void end(Editable text, Class kind, Object repl) {
        int len = text.length();
        Object obj = getLast(text, kind);
        if (obj != null) {
            setSpanFromMark(text, obj, repl);
        }
    }

    private static void endCssStyle(Editable text) {
        Strikethrough s = getLast(text, Strikethrough.class);
        if (s != null) {
            setSpanFromMark(text, s, new StrikethroughSpan());
        }
        Background b = getLast(text, Background.class);
        if (b != null) {
            setSpanFromMark(text, b, new BackgroundColorSpan(b.mBackgroundColor));
        }

        Foreground f = getLast(text, Foreground.class);
        if (f != null) {
            setSpanFromMark(text, f, new ForegroundColorSpan(f.mForegroundColor));
        }

    }

    private static void startImg(Editable text, Attributes attributes, Html.ImageGetter img) {
        String src = attributes.getValue("", "src");
        Drawable d = null;
        if (img != null) {
            d = img.getDrawable(src);
        }
        if (d == null) {
            d = Resources.getSystem().getDrawable(android.R.drawable.stat_notify_error);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        }
        int len = text.length();
        text.append("\uFFFC");
        text.setSpan(new ImageSpan(d, src), len, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static void endFont(Editable text) {
        Font font = getLast(text, Font.class);
        if (font != null) {
            setSpanFromMark(text, font, new TypefaceSpan(font.mFace));
        }
        Foreground foreground = getLast(text, Foreground.class);
        if (foreground != null) {
            setSpanFromMark(text, foreground,
                    new ForegroundColorSpan(foreground.mForegroundColor));
        }
    }

    private static void startA(Editable text, Attributes attributes) {
        String href = attributes.getValue("", "href");
        start(text, new Href(href));
    }

    private static void endA(Editable text) {
        Href h = getLast(text, Href.class);
        if (h != null) {
            if (h.mHref != null) {
                setSpanFromMark(text, h, new URLSpan((h.mHref)));
            }
        }
    }

    public Spanned convert() {
        mReader.setContentHandler(this);
        try {
            mReader.parse(new InputSource(new StringReader(mSource)));
        } catch (IOException | SAXException e) {
            // We are reading from a string. There should not be IO problems.
            throw new RuntimeException(e);
        }
        // Fix flags and range for paragraph-type markup.
        Object[] obj = mSpannableStringBuilder.getSpans(0, mSpannableStringBuilder.length(), ParagraphStyle.class);
        for (Object anObj : obj) {
            int start = mSpannableStringBuilder.getSpanStart(anObj);
            int end = mSpannableStringBuilder.getSpanEnd(anObj);
            // If the last line of the range is blank, back off by one.
            if (end - 2 >= 0) {
                if (mSpannableStringBuilder.charAt(end - 1) == '\n' &&
                        mSpannableStringBuilder.charAt(end - 2) == '\n') {
                    end--;
                }
            }
            if (end == start) {
                mSpannableStringBuilder.removeSpan(anObj);
            } else {
                mSpannableStringBuilder.setSpan(anObj, start, end, Spannable.SPAN_PARAGRAPH);
            }
        }
        return mSpannableStringBuilder;
    }

    private void handleStartTag(String tag, Attributes attributes) {
        attr = attributes;
        if (tag.equalsIgnoreCase("br")) {
            // We don't need to handle this. TagSoup will ensure that there's a </br> for each <br>
            // so we can safely emit the linebreaks when we handle the close tag.
        } else if (tag.equalsIgnoreCase("p")) {
            startBlockElement(mSpannableStringBuilder, attributes, getMarginParagraph());
            startCssStyle(mSpannableStringBuilder, attributes);
        } else if (tag.equalsIgnoreCase("ul")) {
            startBlockElement(mSpannableStringBuilder, attributes, getMarginList());
        } else if (tag.equalsIgnoreCase("li")) {
            startLi(mSpannableStringBuilder, attributes);
        } else if (tag.equalsIgnoreCase("div")) {
            startBlockElement(mSpannableStringBuilder, attributes, getMarginDiv());
        } else if (tag.equalsIgnoreCase("span")) {
            startCssStyle(mSpannableStringBuilder, attributes);
        } else if (tag.equalsIgnoreCase("strong")) {
            start(mSpannableStringBuilder, new Bold());
        } else if (tag.equalsIgnoreCase("b")) {
            start(mSpannableStringBuilder, new Bold());
        } else if (tag.equalsIgnoreCase("em")) {
            start(mSpannableStringBuilder, new Italic());
        } else if (tag.equalsIgnoreCase("cite")) {
            start(mSpannableStringBuilder, new Italic());
        } else if (tag.equalsIgnoreCase("dfn")) {
            start(mSpannableStringBuilder, new Italic());
        } else if (tag.equalsIgnoreCase("i")) {
            start(mSpannableStringBuilder, new Italic());
        } else if (tag.equalsIgnoreCase("big")) {
            start(mSpannableStringBuilder, new Big());
        } else if (tag.equalsIgnoreCase("small")) {
            start(mSpannableStringBuilder, new Small());
        } else if (tag.equalsIgnoreCase("font")) {
            startFont(mSpannableStringBuilder, attributes);
        } else if (tag.equalsIgnoreCase("blockquote")) {
            startBlockquote(mSpannableStringBuilder, attributes);
        } else if (tag.equalsIgnoreCase("tt")) {
            start(mSpannableStringBuilder, new Monospace());
        } else if (tag.equalsIgnoreCase("a")) {
            startA(mSpannableStringBuilder, attributes);
        } else if (tag.equalsIgnoreCase("u")) {
            start(mSpannableStringBuilder, new Underline());
        } else if (tag.equalsIgnoreCase("del")) {
            start(mSpannableStringBuilder, new Strikethrough());
        } else if (tag.equalsIgnoreCase("s")) {
            start(mSpannableStringBuilder, new Strikethrough());
        } else if (tag.equalsIgnoreCase("strike")) {
            start(mSpannableStringBuilder, new Strikethrough());
        } else if (tag.equalsIgnoreCase("sup")) {
            start(mSpannableStringBuilder, new Super());
        } else if (tag.equalsIgnoreCase("sub")) {
            start(mSpannableStringBuilder, new Sub());
        } else if (tag.length() == 2 &&
                Character.toLowerCase(tag.charAt(0)) == 'h' &&
                tag.charAt(1) >= '1' && tag.charAt(1) <= '6') {
            startHeading(mSpannableStringBuilder, attributes, tag.charAt(1) - '1');
        } else if (tag.equalsIgnoreCase("img")) {
            startImg(mSpannableStringBuilder, attributes, mImageGetter);
        } else if (tag.equalsIgnoreCase("tbody")) {
            startTbody(mSpannableStringBuilder, attributes);
        } else {
            mTagHandler.handleTag(true, tag, mSpannableStringBuilder, attr);
        }
    }

    private void handleEndTag(String tag) {
        if (tag.equalsIgnoreCase("br")) {
            handleBr(mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("p")) {
            endCssStyle(mSpannableStringBuilder);
            endBlockElement(mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("ul")) {
            endBlockElement(mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("li")) {
            endLi(mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("div")) {
            endBlockElement(mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("span")) {
            endCssStyle(mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("strong")) {
            end(mSpannableStringBuilder, Bold.class, new StyleSpan(Typeface.BOLD));
        } else if (tag.equalsIgnoreCase("b")) {
            end(mSpannableStringBuilder, Bold.class, new StyleSpan(Typeface.BOLD));
        } else if (tag.equalsIgnoreCase("em")) {
            end(mSpannableStringBuilder, Italic.class, new StyleSpan(Typeface.ITALIC));
        } else if (tag.equalsIgnoreCase("cite")) {
            end(mSpannableStringBuilder, Italic.class, new StyleSpan(Typeface.ITALIC));
        } else if (tag.equalsIgnoreCase("dfn")) {
            end(mSpannableStringBuilder, Italic.class, new StyleSpan(Typeface.ITALIC));
        } else if (tag.equalsIgnoreCase("i")) {
            end(mSpannableStringBuilder, Italic.class, new StyleSpan(Typeface.ITALIC));
        } else if (tag.equalsIgnoreCase("big")) {
            end(mSpannableStringBuilder, Big.class, new RelativeSizeSpan(1.25f));
        } else if (tag.equalsIgnoreCase("small")) {
            end(mSpannableStringBuilder, Small.class, new RelativeSizeSpan(0.8f));
        } else if (tag.equalsIgnoreCase("font")) {
            endFont(mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("blockquote")) {
            endBlockquote(mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("tt")) {
            end(mSpannableStringBuilder, Monospace.class, new TypefaceSpan("monospace"));
        } else if (tag.equalsIgnoreCase("a")) {
            endA(mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("u")) {
            end(mSpannableStringBuilder, Underline.class, new UnderlineSpan());
        } else if (tag.equalsIgnoreCase("del")) {
            end(mSpannableStringBuilder, Strikethrough.class, new StrikethroughSpan());
        } else if (tag.equalsIgnoreCase("s")) {
            end(mSpannableStringBuilder, Strikethrough.class, new StrikethroughSpan());
        } else if (tag.equalsIgnoreCase("strike")) {
            end(mSpannableStringBuilder, Strikethrough.class, new StrikethroughSpan());
        } else if (tag.equalsIgnoreCase("sup")) {
            end(mSpannableStringBuilder, Super.class, new SuperscriptSpan());
        } else if (tag.equalsIgnoreCase("sub")) {
            end(mSpannableStringBuilder, Sub.class, new SubscriptSpan());
        } else if (tag.length() == 2 &&
                Character.toLowerCase(tag.charAt(0)) == 'h' &&
                tag.charAt(1) >= '1' && tag.charAt(1) <= '6') {
            endHeading(mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("tbody")) {
            endTbody(mSpannableStringBuilder);
        } else {
            mTagHandler.handleTag(false, tag, mSpannableStringBuilder, attr);
        }
    }

    private int getMarginParagraph() {
        return getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH);
    }

    private int getMarginHeading() {
        return getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING);
    }

    private int getMarginListItem() {
        return getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM);
    }

    private int getMarginList() {
        return getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST);
    }

    private int getMarginDiv() {
        return getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV);
    }

    private int getMarginBlockquote() {
        return getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE);
    }

    /**
     * Returns the minimum number of newline characters needed before and after a given block-level
     * element.
     *
     * @param flag the corresponding option flag defined in {@link Html} of a block-level element
     */
    private int getMargin(int flag) {
        if ((flag) != 0) {
            return 1;
        }
        return 2;
    }

    private void startTbody(Editable text, Attributes attributes) {
        start(text, new Background(0xFF008000));
    }

    private void endTbody(Editable text) {
        Background b = getLast(text, Background.class);
        if (b != null) {
            setSpanFromMark(text, b, new BackgroundColorSpan(b.mBackgroundColor));
        }
    }

    private void startLi(Editable text, Attributes attributes) {
        startBlockElement(text, attributes, getMarginListItem());
        start(text, new Bullet());
        startCssStyle(text, attributes);
    }

    private void startBlockquote(Editable text, Attributes attributes) {
        startBlockElement(text, attributes, getMarginBlockquote());
        start(text, new Blockquote());
    }

    private void startHeading(Editable text, Attributes attributes, int level) {
        startBlockElement(text, attributes, getMarginHeading());
        start(text, new Heading(level));
    }

    private void startCssStyle(Editable text, Attributes attributes) {
        String style = attributes.getValue("", "style");
        String classs = attributes.getValue("", "class");
        if (style != null) {
            Matcher m = getForegroundColorPattern().matcher(style);
            if (m.find()) {
                int c = getHtmlColor(m.group(1));
                if (c != -1) {
                    start(text, new Foreground(c | 0xFF000000));
                }
            }
            m = getBackgroundColorPattern().matcher(style);
            if (m.find()) {
                int c = getHtmlColor(m.group(1));
                if (c != -1) {
                    start(text, new Background(c | 0xFF000000));
                }
            }
            m = getTextDecorationPattern().matcher(style);
            if (m.find()) {
                String textDecoration = m.group(1);
                if (textDecoration.equalsIgnoreCase("line-through")) {
                    start(text, new Strikethrough());
                }
            }
        }

        if (classs != null) {
            if (classs.equals("mark")) {
                start(text, new Background(0xFF999999));
            } else if (classs.equals("pf_ps3")) {
                start(text, new Background(0xFF0AAAE9));
            } else if (classs.equals("pf_ps4")) {
                start(text, new Background(0xFF8662DD));
            } else if (classs.equals("pf_psv")) {
                start(text, new Background(0xFFF05561));
            }
        }
    }

    private void startFont(Editable text, Attributes attributes) {
        String color = attributes.getValue("", "color");
        String face = attributes.getValue("", "face");
        if (!TextUtils.isEmpty(color)) {
            int c = getHtmlColor(color);
            if (c != -1) {
                start(text, new Foreground(c | 0xFF000000));
            }
        }
        if (!TextUtils.isEmpty(face)) {
            start(text, new Font(face));
        }
    }

    private int getHtmlColor(final String color) {
        Integer i = sColorMap.get(color.toLowerCase(Locale.US));
        if (i != null) {
            return i;
        }
        if (color.contains("#")) {
            String new_color = color.replace("#", "#FF");
            return Color.parseColor(new_color);
        }
        throw new IllegalArgumentException("Unknown color: " + color);
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        handleStartTag(localName, attributes);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        handleEndTag(localName);
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        StringBuilder sb = new StringBuilder();
        /*
         * Ignore whitespace that immediately follows other whitespace;
         * newlines count as spaces.
         */
        for (int i = 0; i < length; i++) {
            char c = ch[i + start];
            if (c == ' ' || c == '\n') {
                char pred;
                int len = sb.length();
                if (len == 0) {
                    len = mSpannableStringBuilder.length();
                    if (len == 0) {
                        pred = '\n';
                    } else {
                        pred = mSpannableStringBuilder.charAt(len - 1);
                    }
                } else {
                    pred = sb.charAt(len - 1);
                }
                if (pred != ' ' && pred != '\n') {
                    sb.append(' ');
                }
            } else {
                sb.append(c);
            }
        }
        mSpannableStringBuilder.append(sb);
    }

    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
    }

    public void processingInstruction(String target, String data) throws SAXException {
    }

    public void skippedEntity(String name) throws SAXException {
    }

    private static class Bold {
    }

    private static class Italic {
    }

    private static class Underline {
    }

    private static class Strikethrough {
    }

    private static class Big {
    }

    private static class Small {
    }

    private static class Monospace {
    }

    private static class Blockquote {
    }

    private static class Super {
    }

    private static class Sub {
    }

    private static class Bullet {
    }

    private static class Font {
        public String mFace;

        public Font(String face) {
            mFace = face;
        }
    }

    private static class Href {
        public String mHref;

        public Href(String href) {
            mHref = href;
        }
    }

    private static class Foreground {
        private int mForegroundColor;

        public Foreground(int foregroundColor) {
            mForegroundColor = foregroundColor;
        }
    }

    private static class Background {

        private int mBackgroundColor;

        public Background(int backgroundColor) {
            mBackgroundColor = backgroundColor;
        }
    }

    private static class Heading {
        private int mLevel;

        public Heading(int level) {
            mLevel = level;
        }
    }

    private static class Newline {
        private int mNumNewlines;

        public Newline(int numNewlines) {
            mNumNewlines = numNewlines;
        }
    }

    private static class Alignment {
        private Layout.Alignment mAlignment;

        public Alignment(Layout.Alignment alignment) {
            mAlignment = alignment;
        }
    }

}