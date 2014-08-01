package bg.uni.fmi.tasks

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import net.glxn.qrgen.vcard.VCard

public class GenerateQRVCardFromTextTask extends GenerateQRTask {
    @Input
    String personName

    //The name under which the file will be saved
    @Input
    String vCardFileName = "vCardQR"

    @Input
    @Optional
    String company

    @Input
    @Optional
    String title

    @Input
    @Optional
    String phonenumber

    @Input
    @Optional
    String email

    @Input
    @Optional
    String address

    @Input
    @Optional
    String website

    @TaskAction
    def generate() {
        generatePicture()
    }

    @Override
    def generatePicture() {
        createPicture(fillVcard(), new File(getOutputQRPath(), "${vCardFileName}" + '.' + "${getImageType()}"))
    }

    VCard fillVcard() {
        VCard card = new VCard(personName)
        if (email != null) {
            card.setEmail(email)
        }
        if (address != null) {
            card.setAddress(address)
        }
        if (title != null) {
            card.setTitle(title)
        }
        if (company != null) {
            card.setCompany(company)
        }
        if (phonenumber != null) {
            card.setPhonenumber(phonenumber)
        }
        if (website != null) {
            card.setWebsite(website)
        }
        return card
    }
}
