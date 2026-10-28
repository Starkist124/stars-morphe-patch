package app.starkist.patches.newyorker

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.starkist.patches.newyorker.NewYorkerConstants.COMPATIBILITY_NEW_YORKER

private const val EXTENSION_CLASS = "Lapp/starkist/extension/newyorker/ArticleFontPatch;"

@Suppress("unused")
val articleFontPatch = bytecodePatch(
    name = "Article font",
    description = "Changes the text size and font of article text.",
    default = true
) {
    compatibleWith(COMPATIBILITY_NEW_YORKER)

    // Adds our Java extension classes into the app.
    extendWith("extensions/extension.mpe")

    execute {
        // p1 is the method's first parameter: the article's WebView.
        ArticleOnPageStartedFingerprint.method.addInstruction(
            0,
            "invoke-static { p1 }, $EXTENSION_CLASS->onPageStarted(Landroid/webkit/WebView;)V"
        )

        ArticleOnPageFinishedFingerprint.method.addInstruction(
            0,
            "invoke-static { p1 }, $EXTENSION_CLASS->onPageFinished(Landroid/webkit/WebView;)V"
        )
    }
}
