package com.mobbile.paul.shrine.models;

import java.io.Serializable;

public class BannerRequest implements Serializable {

    private String content;

    private String logoUrl;

    private String fonts;

    private String colors;

    private String dimension;

    private Boolean isBannerStatic = false;

    private String bannerType;

    public BannerRequest() {
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

    public Boolean getBannerStatic() {
        return isBannerStatic;
    }

    public void setBannerStatic(Boolean bannerStatic) {
        isBannerStatic = bannerStatic;
    }

    public String getBannerType() {
        return bannerType;
    }

    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }
}
