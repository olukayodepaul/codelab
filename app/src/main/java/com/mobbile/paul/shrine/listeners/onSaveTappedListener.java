package com.mobbile.paul.shrine.listeners;

import com.google.codelabs.mdc.java.shrine.models.BannerRequest;
import com.google.codelabs.mdc.java.shrine.models.BrandingRequest;
import com.google.codelabs.mdc.java.shrine.models.CampaignRequest;
import com.google.codelabs.mdc.java.shrine.models.FlyerRequest;
import com.google.codelabs.mdc.java.shrine.models.IllustrationRequest;
import com.google.codelabs.mdc.java.shrine.models.InfluencerRequest;
import com.google.codelabs.mdc.java.shrine.models.LogoRequest;
import com.google.codelabs.mdc.java.shrine.models.SocialMediaRequest;

public interface onSaveTappedListener {


    void onFlyerRequested(FlyerRequest request, int type);

    void onBannerRequested(BannerRequest request, int type);

    void onBrandingRequested(BrandingRequest request, int type);

    void onCampaignRequested(CampaignRequest request);

    void onIllustrationRequested(IllustrationRequest request);

    void onInfluencerRequested(InfluencerRequest request);

    void onLogoRequestd(LogoRequest request);

    void onSocialMediaRequested(SocialMediaRequest request);

    void onStoryBoardRequested(String scriptUrl);

}
