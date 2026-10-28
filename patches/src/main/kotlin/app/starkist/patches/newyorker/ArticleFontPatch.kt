package app.starkist.patches.newyorker

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import app.starkist.patches.newyorker.NewYorkerConstants.COMPATIBILITY_NEW_YORKER
import app.starkist.patches.newyorker.settings.starSettingsPatch
import app.starkist.patches.shared.sharedExtensionPatch

private const val EXTENSION_CLASS = "Lapp/starkist/extension/newyorker/ArticleFontPatch;"

@Suppress("unused")
val articleFontPatch = bytecodePatch(
    name = "Article font",
    description = "Adds article font and text size options, including your own font files, to Star settings.",
    default = true
) {
    compatibleWith(COMPATIBILITY_NEW_YORKER)

    dependsOn(sharedExtensionPatch, starSettingsPatch)

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

        // p1 is the WebView and p2 the request. If the extension returns a response
        // (the custom font, or the stylesheet with our CSS), return it right away.
        ArticleShouldInterceptRequestFingerprint.method.apply {
            addInstructionsWithLabels(
                0,
                """
                    invoke-static { p1, p2 }, $EXTENSION_CLASS->interceptRequest(Landroid/webkit/WebView;Landroid/webkit/WebResourceRequest;)Landroid/webkit/WebResourceResponse;
                    move-result-object v0
                    if-eqz v0, :star_continue
                    return-object v0
                """,
                ExternalLabel("star_continue", getInstruction(0))
            )
        }
    }
}
