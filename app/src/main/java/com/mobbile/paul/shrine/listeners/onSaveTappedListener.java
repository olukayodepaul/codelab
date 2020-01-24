package com.mobbile.paul.shrine.listeners;

import com.mobbile.paul.shrine.models.BannerRequest;
import com.mobbile.paul.shrine.models.BrandingRequest;
import com.mobbile.paul.shrine.models.CampaignRequest;
import com.mobbile.paul.shrine.models.FlyerRequest;
import com.mobbile.paul.shrine.models.IllustrationRequest;
import com.mobbile.paul.shrine.models.InfluencerRequest;
import com.mobbile.paul.shrine.models.LogoRequest;
import com.mobbile.paul.shrine.models.SocialMediaRequest;

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
