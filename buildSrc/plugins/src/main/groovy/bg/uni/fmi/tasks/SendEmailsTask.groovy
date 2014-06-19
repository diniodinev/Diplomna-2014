package bg.uni.fmi.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import javax.mail.Session
import javax.mail.internet.MimeMessage
import javax.mail.internet.InternetAddress
import javax.mail.Transport

class SendEmailsTask extends DefaultTask {

    @Input
    @Optional
    def message = ""
    @Input
    def subject
    @Input
    def toAddress
    @Input
    def host
    @Input
    def port

    @TaskAction
    def sendEmail() {
        sendMail(message, subject, toAddress, host, port)
    }

    def sendMail(String message, String subject, String toAddress, String host, String port) {
        Properties mprops = new Properties();
        mprops.put("mail.smtp.port", port);
        mprops.put("mail.smtp.auth", "true");
        mprops.put("mail.smtp.starttls.enable", "true");


        Session lSession = Session.getDefaultInstance(mprops, null);
        MimeMessage msg = new MimeMessage(lSession);

        StringTokenizer tok = new StringTokenizer(toAddress, ";");
        ArrayList emailTos = new ArrayList();
        while (tok.hasMoreElements()) {
            emailTos.add(new InternetAddress(tok.nextElement().toString()));
        }
        InternetAddress[] to = new InternetAddress[emailTos.size()];
        to = (InternetAddress[]) emailTos.toArray(to);
        msg.setRecipients(MimeMessage.RecipientType.TO, to);
        msg.setSubject(subject);
        msg.setText(message)

        Transport transporter = lSession.getTransport("smtp");
        //USER and PASS are properties which have to be specified previously
        transporter.connect(host, project.property("USER"), project.property("PASS"));
        transporter.sendMessage(msg, msg.getAllRecipients());
        transporter.close();

    }
}