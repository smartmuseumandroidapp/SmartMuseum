package it.sapienza.pervasivesystems.smartmuseum.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by andrearanieri on 04/05/16.
 */
public class WorkofartModel implements Serializable{
    private int idWork;
    private String title;
    private String shortDescription;
    private String longDescription;
    private String longDescriptionURL;
    private String image;
    private String audioURL;
    private ExhibitModel exhibitModel;
    private String color;
    private Date timeStamp;

    public int getIdWork() {
        return idWork;
    }

    public void setIdWork(int idWork) {
        this.idWork = idWork;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getLongDescriptionURL() {
        return longDescriptionURL;
    }

    public void setLongDescriptionURL(String longDescriptionURL) {
        this.longDescriptionURL = longDescriptionURL;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public ExhibitModel getExhibitModel() {
        return exhibitModel;
    }

    public void setExhibitModel(ExhibitModel exhibitModel) {
        this.exhibitModel = exhibitModel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
