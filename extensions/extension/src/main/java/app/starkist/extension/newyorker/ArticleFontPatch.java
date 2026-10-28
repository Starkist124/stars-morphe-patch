package app.starkist.extension.newyorker;

import android.webkit.WebView;

import org.json.JSONObject;

@SuppressWarnings("unused")
public final class ArticleFontPatch {

    /** Text size as a percentage of normal. 100 = unchanged, 130 = 30% bigger. */
    private static final int TEXT_ZOOM_PERCENT = 120;

    /**
     * CSS font for article body text. Fonts bundled with the app:
     *   'TNY Adobe Caslon Pro' (default), 'Graphik App', 'Irvin-Text'
     * Fonts built into Android:
     *   serif, sans-serif, monospace
     */
    private static final String FONT_FAMILY = "'Graphik App', sans-serif";

    private static final String STYLE_ELEMENT_ID = "starkist-font-patch";

    /** Injected at the start of the article WebViewClient's onPageStarted. */
    public static void onPageStarted(WebView webView) {
        if (webView == null) return;
        webView.getSettings().setTextZoom(TEXT_ZOOM_PERCENT);
    }

    /** Injected at the start of the article WebViewClient's onPageFinished. */
    public static void onPageFinished(WebView webView) {
        if (webView == null) return;

        String css = "div.article-content, div.article-content p, div.article-content li {"
                + " font-family: " + FONT_FAMILY + " !important; }";

        // Add a <style> element once, or update it if the page already has one.
        String js = "(function() {"
                + "var s = document.getElementById('" + STYLE_ELEMENT_ID + "');"
                + "if (!s) { s = document.createElement('style'); s.id = '" + STYLE_ELEMENT_ID + "';"
                + " document.head.appendChild(s); }"
                + "s.textContent = " + JSONObject.quote(css) + ";"
                + "})();";

        webView.evaluateJavascript(js, null);
    }
}
