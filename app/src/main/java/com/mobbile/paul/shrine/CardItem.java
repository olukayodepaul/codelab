package com.mobbile.paul.shrine;

/**
 * Created by Ehigiator David on 14/04/2019.
 */
public class CardItem {

    private int mId;
    private String mTitleResource;
    private String mDescription;

    public CardItem(int mId, String mTitleResource, String mDescription) {
        this.mId = mId;
        this.mTitleResource = mTitleResource;
        this.mDescription = mDescription;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitleResource() {
        return mTitleResource;
    }

    public void setmTitleResource(String mTitleResource) {
        this.mTitleResource = mTitleResource;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public CardItem() {

    }
}
