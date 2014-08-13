package bg.uni.fmi.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;

import org.apache.commons.lang.StringUtils;

import java.lang.String;

public class PrintContent extends DefaultTask {
    @Input
    private String personName;

    @Input
    @Optional
    private String title;

    @Input
    private String salutation = "Dear";

    @TaskAction
    public void compose() {
        System.out.println(generateMessage());
    }

    String generateMessage() {
        return String.format("%s %s %s I just want to say hello to you.", capitalizeFirstLetter(getSalutation()), getTitle(), getPersonName());
    }

    String capitalizeFirstLetter(String word) {
        return StringUtils.capitalize(word);
    }

    public String getTitle() {
        return (title == null) ? "" : title;
    }

    public String getPersonName() {
        return personName;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}