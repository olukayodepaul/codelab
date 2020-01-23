package com.mobbile.paul.shrine.models;

import java.io.Serializable;

public class LogoRequest implements Serializable {

    private String brandName;

    private String brandInspiration;

    private String brandColor;

    public LogoRequest() {
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandInspiration() {
        return brandInspiration;
    }

    public void setBrandInspiration(String brandInspiration) {
        this.brandInspiration = brandInspiration;
    }

    public String getBrandColor() {
        return brandColor;
    }

    public void setBrandColor(String brandColor) {
        this.brandColor = brandColor;
    }
}
