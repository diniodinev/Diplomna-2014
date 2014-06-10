import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import com.uwyn.jhighlight.renderer.XhtmlRendererFactory

transform {
    prependChild(new org.jsoup.nodes.DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", ""))
}

transform {
    head().append("""
        <meta charset='utf-8'>
        <link rel="stylesheet" href="style/base.less.css">
        <link rel="stylesheet" href="style/code.less.css" /> 
    """)
}

transform {
    def codeTags = body().select("code")
    codeTags.each { code ->
        def text = code.text()
        def input = new ByteArrayInputStream(code.text().getBytes("utf-8"))
        def renderer = XhtmlRendererFactory.getRenderer("groovy")
        def out = new ByteArrayOutputStream()
        renderer.highlight("test", input, out, "utf-8", true)
        code.html(new String(out.toByteArray(), "utf-8"))
        code.select("br").remove()
        code.childNodes().findAll { it.nodeName().equals("#comment") }*.remove()
        code.html(code.html().trim())
        def parent = code.parent()
        if (parent.tagName() == "pre") {
            parent.addClass("code")
        }
    }
}
