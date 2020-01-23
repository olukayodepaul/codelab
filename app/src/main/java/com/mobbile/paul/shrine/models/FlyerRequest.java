package com.mobbile.paul.shrine.models;

import java.io.Serializable;

public class FlyerRequest implements Serializable {

    private String content;

    private String logoUrl;

    private String fonts;

    private String colors;

    private String dimension;


    public FlyerRequest() {
    }

    public FlyerRequest(String content, String logoUrl) {
        this.content = content;
        this.logoUrl = logoUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }


    public FlyerRequest(String content, String logoUrl, String fonts, String colors, String dimension) {
        this.content = content;
        this.logoUrl = logoUrl;
        this.fonts = fonts;
        this.colors = colors;
        this.dimension = dimension;
    }

    public String getFonts() {
        return fonts;
    }

    public void setFonts(String fonts) {
        this.fonts = fonts;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }
}
