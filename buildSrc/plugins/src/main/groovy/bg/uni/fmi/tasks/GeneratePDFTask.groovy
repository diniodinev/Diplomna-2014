package bg.uni.fmi.tasks

import org.apache.commons.io.FilenameUtils
import org.gradle.api.GradleException
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Font
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.Image
import com.itextpdf.text.DocumentException
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.FontSelector
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.Chunk
import com.itextpdf.text.Rectangle
import com.itextpdf.text.PageSize

public class GeneratePDFTask extends SourceTask {
    public static final String EXTENSION_TYPE = "pdf"
    @Input
    @Optional
    String pageTitle

    @Optional
    String pdfFileName = "generatedPdf"

    @OutputDirectory
    def outputPDFPath = new File('build/essays/pdf')

    @OutputFile
    @Optional
    File pdfFile


    Integer picturesPerRow = 1

    Integer repeatTimes = 1

    @Input
    boolean useDefaultFonts = true

    public static final String AZBUKA_NAME = 'Azbuka04.ttf'
    /**
     * Fonts will be added as Unicode encoding with horizontal writing.(IDENTITY_H property) will be set atuomatically
     */
    @Input
    @Optional
    Map<String, String> fontToRegister

    Font catFont

    @TaskAction
    def generate() {
        pdfFile = new File(getOutputPDFPath(), pdfFileName + '.' + EXTENSION_TYPE)
        useFonts()

        Document pdfDocument = createFile(PageSize.A4)
        pdfDocument.open()
        addTitle(pdfDocument)
        addImages(pdfDocument)
        close(pdfDocument)
    }

    private def useFonts() {
        if (useDefaultFonts) {
            File azbukaFont = new File("${project.rootDir}/src/main/style/fonts", AZBUKA_NAME)
            registerFont(azbukaFont.absolutePath, AZBUKA_NAME)
        } else {
            fontToRegister.each { path, name ->
                registerFont(path, name)
            }
        }
    }

    /**
     *  Register a font file path and use an alias for the font contained in it.
     * @param path the path to a font. Files must be ttf- or ttc-
     * @param alias the alias you want to use for the font
     * @return
     */
    def registerFont(String path, String alias) {
        FontFactory.register(path, alias)
    }

    /**
     * Specify how many pictures per row to be put
     */
    @Input
    def setPicturesPerRow(Integer rows) {
        if (rows < 1) {
            throw GradleException("${rows} is invalid value. The property picturesPerRow must have value greater than 0. ")
        }
        picturesPerRow = rows

    }

    /**
     * Specify how many times to repeat the pictures from source in the generated pdf
     */
    @Input
    def setRepeatTimes(Integer times) {
        if (times < 1) {
            throw GradleException("${times} is invalid value. The property repeatTimes must have value greter than 0.")
        }
        repeatTimes = times
    }

    /**
     * Add title to the pdf page. Text from pageTitle will be set as text
     * @param document
     * @return
     */
    def addTitle(Document document) {
        FontSelector selector = addFontsToSelector();
        //selector.addFont(FontFactory.getFont("Azbuka04", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED));

        Paragraph par = new Paragraph()
        Phrase ph = selector.process((pageTitle != null) ? pageTitle : '')
        document.add(new Paragraph(ph))
        document.add(Chunk.NEWLINE)
    }

    /**
     * Return font selector <strong>FontSelector</strong> in which are included</br>
     * 1. defaultFonts if useDefaultFonts = true</br>
     * 2. fontToRegister if useDefaultFonts = false</br>
     * @return FontSelector
     * @see <a href="http://api.itextpdf.com/itext/com/itextpdf/text/pdf/FontSelector.html"/>
     */
    private FontSelector addFontsToSelector() {
        FontSelector selector = new FontSelector();
        if (useDefaultFonts) {
            selector.addFont(FontFactory.getFont(FilenameUtils.removeExtension(AZBUKA_NAME), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED));
        } else {
            fontToRegister.each { path, name ->
                selector.addFont(FontFactory.getFont(FilenameUtils.removeExtension(name), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED));
            }
        }
        selector
    }

    /**
     * Create new Document file
     * @param size Rectangle object that represents the paper size.
     * @return
     */
    Document createFile(Rectangle size) {
        Document document = new Document(size)
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile))
        return document
    }

    def addImages(Document pdfDocument) {
        PdfPTable table = new PdfPTable(getPicturesPerRow())
        table.setWidthPercentage(100)
        repeatTimes.times {
            source.each {
                table.addCell(createImageCell(it.absolutePath))
            }
        }
        pdfDocument.add(table);
    }

    PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path)
        PdfPCell cell = new PdfPCell(img, true)
        cell.setBorder(Rectangle.NO_BORDER)
        return cell
    }

    def getOutputPDFPath() {
        project.file(outputPDFPath)
    }

    Integer getPicturesPerRow() {
        picturesPerRow
    }

    def close(Document document) {
        document.close()
    }
}
