package bg.uni.fmi.plugins;

import bg.uni.fmi.tasks.CheckEssaysTask;
import bg.uni.fmi.tasks.CleanEssays;
import bg.uni.fmi.tasks.UnzipFilesTask;
import bg.uni.fmi.tasks.GenerateQRFromTextTask;
import bg.uni.fmi.tasks.GenerateQRFromFilesTask;
import bg.uni.fmi.tasks.GeneratePDFTask;
import bg.uni.fmi.tasks.GenerateQRVCardFromTextTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;



public class  EssayPlugin implements Plugin<Project> {
    public static final String UNZIP_TASK_NAME = "unzip";
    public static final String CHECK_ESSAY_TASK_NAME = "essaysCheck";
    public static final String CLEAN_ESSAY_TASK_NAME = "essaysClean";
    public static final String GENERATE_QR_FROM_FILES_TASK_NAME = "generateQRFromFiles";
    public static final String GENERATE_QR_FROM_TEXT_TASK_NAME = "generateQRFromText";
    public static final String GENERATE_QR_VCARD_FROM_TEXT_TASK_NAME = "generateQRVcardFromText";
    public static final String GENERATE_PDF_TASK_NAME = "generatePDF";

    @Override
    public void apply(Project project) {
        UnzipFilesTask getInfoTask = project.getTasks().create(UNZIP_TASK_NAME, UnzipFilesTask.class);
        getInfoTask.setDescription("Unzip specified archives.");
        getInfoTask.setGroup("essay");

        CheckEssaysTask checkEssaysTask = project.getTasks().create(CHECK_ESSAY_TASK_NAME, CheckEssaysTask.class);
        checkEssaysTask.setDescription("Check essays.");
        checkEssaysTask.setGroup("essay");

        CleanEssays cleanEssaysTask = project.getTasks().create(CLEAN_ESSAY_TASK_NAME, CleanEssays.class);
        cleanEssaysTask.setDescription("Clean essays output.");
        cleanEssaysTask.setGroup("essay");

        GenerateQRFromFilesTask generateQRFromFiles = project.getTasks().create(GENERATE_QR_FROM_FILES_TASK_NAME, GenerateQRFromFilesTask.class);
        generateQRFromFiles.setDescription("Generate QR code from Files");
        generateQRFromFiles.setGroup("essay");

        GenerateQRFromTextTask generateQRFromText = project.getTasks().create(GENERATE_QR_FROM_TEXT_TASK_NAME, GenerateQRFromTextTask.class);
        generateQRFromText.setDescription("Generate QR code from text");
        generateQRFromText.setGroup("essay");

        GenerateQRVCardFromTextTask generateQRVcardFromText = project.getTasks().create(GENERATE_QR_VCARD_FROM_TEXT_TASK_NAME, GenerateQRVCardFromTextTask.class);
        generateQRVcardFromText.setDescription("Generate VCard QR code from text");
        generateQRVcardFromText.setGroup("essay");

        GeneratePDFTask generatePDF = project.getTasks().create(GENERATE_PDF_TASK_NAME, GeneratePDFTask.class);
        generatePDF.setDescription("Generate PDF file");
        generatePDF.setGroup("essay");
    }
}

