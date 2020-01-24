package com.mobbile.paul.shrine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;


import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.activity.SettingsActivity;
import com.mobbile.paul.shrine.adapters.SideMenuAdapter;
import com.mobbile.paul.shrine.fragments.BottomNavigationDrawerFragment;
import com.mobbile.paul.shrine.fragments.InfluencerBookingFragment;
import com.mobbile.paul.shrine.fragments.LogoRequestFragment;
import com.mobbile.paul.shrine.listeners.onFragmentActivatedListener;
import com.mobbile.paul.shrine.listeners.onSaveTappedListener;
import com.mobbile.paul.shrine.models.BannerRequest;
import com.mobbile.paul.shrine.models.BrandingRequest;
import com.mobbile.paul.shrine.models.CampaignRequest;
import com.mobbile.paul.shrine.models.FlyerRequest;
import com.mobbile.paul.shrine.models.IllustrationRequest;
import com.mobbile.paul.shrine.models.InfluencerRequest;
import com.mobbile.paul.shrine.models.LogoRequest;
import com.mobbile.paul.shrine.models.OrderObject;
import com.mobbile.paul.shrine.models.SocialMediaRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobbile.paul.shrine.adapters.SideMenuAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.kommunicate.KmConversationBuilder;
import io.kommunicate.callbacks.KmCallback;
import io.kommunicate.users.KMUser;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class MainActivity extends AppCompatActivity implements NavigationHost, onFragmentActivatedListener, onSaveTappedListener, DuoMenuView.OnMenuClickListener {

    BottomAppBar bottomAppBar;

    FloatingActionButton floatingActionButton;

    int currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER;

    private SideMenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    private ArrayList<String> mTitles = new ArrayList<>();

    CollectionReference ordersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shr_main_activity);

        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
        Toolbar toolbar = findViewById(R.id.app_bar);
        DuoDrawerLayout drawerLayout = findViewById(R.id.drawer);
        DuoDrawerToggle drawerToggle = new DuoDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        ordersCollection = FirebaseFirestore.getInstance().collection("orders");

        bottomAppBar = findViewById(R.id.bar);

        floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                floatingActionButton.hide(visibilityChangedListener);
                invalidateOptionsMenu();
//                navigateTo(new LogoRequestFragment(), true);

                if (currentFabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
                    bottomAppBar.setFabAlignmentMode(currentFabAlignmentMode);
                    bottomAppBar.setNavigationIcon(R.drawable.ic_arrow_drop_up_black_24dp);
                    bottomAppBar.replaceMenu(R.menu.bottomappbar_menu_primary);
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));

                    navigateTo(new LogoRequestFragment(), true);

                }

            }
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new ProductGridFragment())
                    .commit();
        }


        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the navigation click by showing a BottomDrawer etc.
                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomNavigationDrawerFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_back:
                        onBackPressed();
                        return true;
                    case R.id.app_bar_clear:
                        clearForm((ViewGroup) findViewById(R.id.product_grid));
                        return true;
                    default:
                        return false;

                }
            }
        });

        mViewHolder = new ViewHolder();


        mMenuAdapter = new SideMenuAdapter(mTitles);

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
//        mMenuAdapter.setViewSelected(0, true);
    }


    FloatingActionButton.OnVisibilityChangedListener visibilityChangedListener = new FloatingActionButton.OnVisibilityChangedListener() {
        @Override
        public void onShown(FloatingActionButton fab) {
            super.onShown(fab);

        }

        @Override
        public void onHidden(FloatingActionButton fab) {
            super.onHidden(fab);
            toggleFabAlignment();

            bottomAppBar.setNavigationIcon(null);

            if (currentFabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
                bottomAppBar.setNavigationIcon(R.drawable.ic_arrow_drop_up_black_24dp);
                navigateTo(new ProductGridFragment(), true);

            } else {
                bottomAppBar.setNavigationIcon(null);
                navigateTo(new LogoRequestFragment(), true);
            }

            bottomAppBar.replaceMenu((currentFabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) ? R.menu.bottomappbar_menu_primary : R.menu.bottomappbar_menu_secondary);
            fab.setImageDrawable((currentFabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) ? getResources().getDrawable(R.drawable.ic_add_black_24dp) : getResources().getDrawable(R.drawable.ic_save_black_24dp));


            fab.show();

        }
    };


    private void toggleFabAlignment() {

        if (currentFabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
            currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END;
        } else {
            currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER;
        }

        bottomAppBar.setFabAlignmentMode(currentFabAlignmentMode);

    }


    /**
     * Navigate to the given fragment.
     *
     * @param fragment       Fragment to navigate to.
     * @param addToBackstack Whether or not the current fragment should be added to the backstack.
     */
    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomNavigationDrawerFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentEntered() {
        if (floatingActionButton != null) {
            invalidateOptionsMenu();
            currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END;
            bottomAppBar.setFabAlignmentMode(currentFabAlignmentMode);
            bottomAppBar.setNavigationIcon(null);
            bottomAppBar.replaceMenu(R.menu.bottomappbar_menu_secondary);
            floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_black_24dp));

        }
    }

    @Override
    public void onFragmentReset() {
        invalidateOptionsMenu();
        floatingActionButton.setEnabled(true);
        currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER;
        bottomAppBar.setFabAlignmentMode(currentFabAlignmentMode);
        bottomAppBar.setNavigationIcon(R.drawable.ic_arrow_drop_up_black_24dp);
        bottomAppBar.replaceMenu(R.menu.bottomappbar_menu_primary);
        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));

    }

    @Override
    public void onFragmentDefaultNavigate() {

        navigateTo(new InfluencerBookingFragment(), true);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onFlyerRequested(FlyerRequest request, int type) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(floatingActionButton, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        floatingActionButton.setEnabled(false);
        String uniqueID = UUID.randomUUID().toString();

        Map<String, Object> data = new HashMap<>();
        data.put("content", request.getContent());
        data.put("logoUrl", request.getLogoUrl());

        if (type == 1) {
            //OWN IT OPTION WAS SELECTED
            data.put("fonts", request.getFonts());
            data.put("colors", request.getColors());
            data.put("dimension", request.getDimension());

        }

        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "Flyer");

        ordersCollection.document(uniqueID).set(orderObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, R.string.success_request, Snackbar.LENGTH_LONG).show();

                clearForm((ViewGroup) findViewById(R.id.product_grid));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onBannerRequested(BannerRequest request, int type) {


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(floatingActionButton, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }


        floatingActionButton.setEnabled(false);

        String uniqueID = UUID.randomUUID().toString();

        Map<String, Object> data = new HashMap<>();
        data.put("content", request.getContent());
        data.put("logoUrl", request.getLogoUrl());
        data.put("fonts", request.getFonts());
        data.put("colors", request.getColors());
        data.put("bannerType", request.getBannerType());
        if (type == 0) {
            data.put("isBannerStatic", request.getBannerStatic());
        }
        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "Banner");

        ordersCollection.document(uniqueID).set(orderObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, R.string.success_request, Snackbar.LENGTH_LONG).show();

                clearForm((ViewGroup) findViewById(R.id.product_grid));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onBrandingRequested(BrandingRequest request, int type) {


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(floatingActionButton, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        floatingActionButton.setEnabled(false);

        String uniqueID = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("content", request.getContent());
        data.put("logoUrl", request.getLogoUrl());
        data.put("brandingType", request.getBrandingType());

        if (type == 1) {
            data.put("fonts", request.getFonts());
            data.put("colors", request.getColors());
            data.put("dimension", request.getDimension());
            data.put("amountOfCopies", request.getAmountOfCopies());
            data.put("deliveryInformation", request.getDeliveryInformation());


        }

        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "Branding/Merchandising");

        ordersCollection.document(uniqueID).set(orderObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, R.string.success_request, Snackbar.LENGTH_LONG).show();

                clearForm((ViewGroup) findViewById(R.id.product_grid));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onCampaignRequested(CampaignRequest request) {


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(floatingActionButton, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }
        floatingActionButton.setEnabled(false);

        String uniqueID = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("background", request.getBackground());
        data.put("objectives", request.getObjectives());
        data.put("consumerInsight", request.getConsumerInsight());
        data.put("features", request.getFeatures());
        data.put("competitors", request.getCompetitors());
        data.put("benefit", request.getBenefit());
        data.put("budget", request.getBudget());
        data.put("timings", request.getTimings());
        data.put("deliverable", request.getDeliverable());


        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "Campaign");

        ordersCollection.document(uniqueID).set(orderObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, R.string.success_request, Snackbar.LENGTH_LONG).show();

                clearForm((ViewGroup) findViewById(R.id.product_grid));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onIllustrationRequested(IllustrationRequest request) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(floatingActionButton, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        floatingActionButton.setEnabled(false);

        String uniqueID = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("content", request.getContent());

        if (request.getIsHaveCharacter()) {
            data.put("characterImage", request.getCharacterImage());

        }

        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "Illustration");

        ordersCollection.document(uniqueID).set(orderObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, R.string.success_request, Snackbar.LENGTH_LONG).show();

                clearForm((ViewGroup) findViewById(R.id.product_grid));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onInfluencerRequested(InfluencerRequest request) {


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(floatingActionButton, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        floatingActionButton.setEnabled(false);

        String uniqueID = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("influencer", request.getInfluencer());
        data.put("socialMedia", request.getSocialMedia());
        data.put("mode", request.getMode());

        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "Influencer");

        ordersCollection.document(uniqueID).set(orderObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, R.string.success_request, Snackbar.LENGTH_LONG).show();

                clearForm((ViewGroup) findViewById(R.id.product_grid));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onLogoRequestd(LogoRequest request) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(floatingActionButton, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        floatingActionButton.setEnabled(false);

        String uniqueID = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("brandName", request.getBrandName());
        data.put("brandColors", request.getBrandColor());
        data.put("brandInspiration", request.getBrandInspiration());


        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "Logo");

        ordersCollection.document(uniqueID).set(orderObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, R.string.success_request, Snackbar.LENGTH_LONG).show();

                clearForm((ViewGroup) findViewById(R.id.product_grid));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onSocialMediaRequested(SocialMediaRequest request) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(floatingActionButton, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        floatingActionButton.setEnabled(false);

        String uniqueID = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("brandInfo", request.getBrandInfo());
        data.put("socialMedia", request.getSocialMedia());
        data.put("achievement", request.getAchievement());
        data.put("achievementGoal", request.getAcheivementGoal());

        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "Social Media Management");

        ordersCollection.document(uniqueID).set(orderObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, R.string.success_request, Snackbar.LENGTH_LONG).show();

                clearForm((ViewGroup) findViewById(R.id.product_grid));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onStoryBoardRequested(String scriptUrl) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(floatingActionButton, R.string.not_logged_in_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        floatingActionButton.setEnabled(false);

        String uniqueID = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("script", scriptUrl);

        OrderObject orderObject = new OrderObject(uniqueID, FirebaseAuth.getInstance().getCurrentUser().getUid(), "REQUEST", data, "Story Board");

        ordersCollection.document(uniqueID).set(orderObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, R.string.success_request, Snackbar.LENGTH_LONG).show();

                clearForm((ViewGroup) findViewById(R.id.product_grid));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                floatingActionButton.setEnabled(true);

                Snackbar.make(floatingActionButton, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }


    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setText("");
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = findViewById(R.id.app_bar);
        }
    }

    @Override
    public void onFooterClicked() {

    }

    @Override
    public void onHeaderClicked() {

    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
//        setTitle(mTitles.get(position));

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {
            case 1:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case 2:

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {

                    KMUser chatUser = new KMUser();
                    chatUser.setUserId(user.getUid()); // Pass a unique key
                    new KmConversationBuilder(this).setKmUser(chatUser).launchConversation(new KmCallback() {
                        @Override
                        public void onSuccess(Object message) {
//                           Log.d("Conversation", "Success : " + message);
                        }

                        @Override
                        public void onFailure(Object error) {
//                           Log.d("Conversation", "Failure : " + error);
                        }
                    });
                } else {
                    new KmConversationBuilder(this).launchConversation(new KmCallback() {
                        @Override
                        public void onSuccess(Object message) {
//                           Log.d("Conversation", "Success : " + message);
                        }

                        @Override
                        public void onFailure(Object error) {
//                           Log.d("Conversation", "Failure : " + error);
                        }
                    });
                }


                break;
            case 3:
//                String url = "https://paystack.com/pay/flgt9cgm8h";
                String url = "https://paystack.com/pay/-g8vlp4197";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(url));
                break;
            default:
                goToFragment(new ProductGridFragment(), false);
                mViewHolder.mDuoDrawerLayout.closeDrawer();

                break;
        }

        // Close the drawer
    }


    private void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = null;
        if (getFragmentManager() != null) {
            transaction = getSupportFragmentManager().beginTransaction();
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }

            transaction.add(R.id.container, fragment).commit();
        }


    }
}
