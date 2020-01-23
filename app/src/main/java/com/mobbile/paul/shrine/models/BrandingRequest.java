package com.mobbile.paul.shrine.models;

import java.io.Serializable;

public class BrandingRequest implements Serializable {

    private String content;

    private String logoUrl;

    private String fonts;

    private String colors;

    private String dimension;

    private String brandingType;

    private String amountOfCopies;

    private String deliveryInformation;

    public BrandingRequest() {
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

    public String getBrandingType() {
        return brandingType;
    }

    public void setBrandingType(String brandingType) {
        this.brandingType = brandingType;
    }

    public String getAmountOfCopies() {
        return amountOfCopies;
    }

    public void setAmountOfCopies(String amountOfCopies) {
        this.amountOfCopies = amountOfCopies;
    }

    public String getDeliveryInformation() {
        return deliveryInformation;
    }

    public void setDeliveryInformation(String deliveryInformation) {
        this.deliveryInformation = deliveryInformation;
    }
}
