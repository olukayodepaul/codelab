package com.mobbile.paul.shrine.models;

import com.google.android.gms.auth.TokenData;

/**
 */
public class TokenResponse {

    private String status;
    private TokenData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TokenData getData() {
        return data;
    }

    public void setData(TokenData data) {
        this.data = data;
    }
}
