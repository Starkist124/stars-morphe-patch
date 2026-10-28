package app.starkist.patches.newyorker

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

/**
 * Constructor of the WebViewClient used by the article screen
 * (de.h$c in 10.7.2, originally inside ArticleHtmlContentViewHolder).
 * Only used to locate the class; matched by two strings unique to it.
 */
object ArticleWebViewClientFingerprint : Fingerprint(
    name = "<init>",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    returnType = "V",
    parameters = listOf("L", "L", "L", "Landroid/webkit/WebView;", "L", "Ljava/lang/String;"),
    filters = listOf(
        string("appEnvironment"),
        string("/culture/cover-story/christoph-niemanns-enchanted-forest"),
    )
)

/** Called when an article page starts loading. */
object ArticleOnPageStartedFingerprint : Fingerprint(
    classFingerprint = ArticleWebViewClientFingerprint,
    name = "onPageStarted",
    returnType = "V",
    parameters = listOf("Landroid/webkit/WebView;", "Ljava/lang/String;", "Landroid/graphics/Bitmap;"),
)

/** Called for every request the article page makes, including its stylesheet. */
object ArticleShouldInterceptRequestFingerprint : Fingerprint(
    classFingerprint = ArticleWebViewClientFingerprint,
    name = "shouldInterceptRequest",
    returnType = "Landroid/webkit/WebResourceResponse;",
    parameters = listOf("Landroid/webkit/WebView;", "Landroid/webkit/WebResourceRequest;"),
)

/** Called when an article page has finished loading. */
object ArticleOnPageFinishedFingerprint : Fingerprint(
    classFingerprint = ArticleWebViewClientFingerprint,
    name = "onPageFinished",
    returnType = "V",
    parameters = listOf("Landroid/webkit/WebView;", "Ljava/lang/String;"),
)
