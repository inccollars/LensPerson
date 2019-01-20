package inc.collars.lensperson.Classes;

import java.util.ArrayList;

//Class for photographersdetails that involves all the information

public class PhotographersDetails {
    String name;
    String num;
    String location;
    String weblinks;
    String packages;
    String logoUrl;
    String sample1Url;
    String sample2Url;
    String sample3Url;

    public PhotographersDetails(String name, String num, String location, String weblinks, String packages) {
        this.name = name;
        this.num = num;
        this.location = location;
        this.weblinks = weblinks;
        this.packages = packages;
    }

    public PhotographersDetails() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeblinks() {
        return weblinks;
    }

    public void setWeblinks(String weblinks) {
        this.weblinks = weblinks;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getSample1Url() {
        return sample1Url;
    }

    public void setSample1Url(String sample1Url) {
        this.sample1Url = sample1Url;
    }

    public String getSample2Url() {
        return sample2Url;
    }

    public void setSample2Url(String sample2Url) {
        this.sample2Url = sample2Url;
    }

    public String getSample3Url() {
        return sample3Url;
    }

    public void setSample3Url(String sample3Url) {
        this.sample3Url = sample3Url;
    }
}