package com.mobbile.paul.shrine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.mobbile.paul.codelab.R;
import com.mobbile.paul.shrine.activity.SettingsActivity;
import com.mobbile.paul.shrine.adapters.SideMenuAdapter;
import com.mobbile.paul.shrine.formActivities.BannersRequestActivity;
import com.mobbile.paul.shrine.formActivities.BrandingRequestActivity;
import com.mobbile.paul.shrine.formActivities.CampaignRequestActivity;
import com.mobbile.paul.shrine.formActivities.IllustrationActivity;
import com.mobbile.paul.shrine.formActivities.InfluencerRequestActivity;
import com.mobbile.paul.shrine.formActivities.LogoRequestActivity;
import com.mobbile.paul.shrine.formActivities.SocialMediaActivity;
import com.mobbile.paul.shrine.formActivities.StationaryActivity;
import com.mobbile.paul.shrine.formActivities.StoryboardRequestActivity;
import com.mobbile.paul.shrine.listeners.onFragmentActivatedListener;
import com.mobbile.paul.shrine.network.ProductEntry;
import com.mobbile.paul.shrine.staggeredgridlayout.StaggeredProductCardRecyclerViewAdapter;

public class ProductGridFragment extends Fragment implements StaggeredProductCardRecyclerViewAdapter.onProductClickedListener, NavigationHost {

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    StaggeredProductCardRecyclerViewAdapter.onProductClickedListener onProductClickedListener;

    onFragmentActivatedListener onFragmentActivatedListener;

    public ProductGridFragment() {

    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment with the ProductGrid theme
        view = inflater.inflate(R.layout.shr_product_grid_fragment, container, false);

        // Set up the toolbar
//        setUpToolbar(view);





        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return position % 3 == 2 ? 2 : 1;
//            }
//        });
        recyclerView.setLayoutManager(linearLayoutManager);
        StaggeredProductCardRecyclerViewAdapter adapter = new StaggeredProductCardRecyclerViewAdapter(
                ProductEntry.initProductEntryList(getResources()), this);
        recyclerView.setAdapter(adapter);

        try {
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_bottom);
            recyclerView.setLayoutAnimation(controller);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        adapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_staggered_product_grid_spacing_large);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_staggered_product_grid_spacing_small);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
//        recyclerView.addOnScrollListener(new ParallaxScrollListener());
        if (getActivity() != null) {
            (getActivity()).findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onFragmentActivatedListener.onFragmentDefaultNavigate();
                }
            });
        }

//        view.findViewById(R.id.settingsButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), SettingsActivity.class));
//            }
//        });



//        mMenuAdapter.setViewSelected(0, true);
//        DuoMenuView duoMenuView = view.findViewById(R.id.menu);
//        duoMenuView.setAdapter(mMenuAdapter);
        return view;
    }


    private void setUpToolbar(View view) {
//        Toolbar toolbar = view.findViewById(R.id.app_bar);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        if (activity != null) {
//            activity.setSupportActionBar(toolbar);
//        }
//        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
//                getContext(),
//                view.findViewById(R.id.product_grid),
//                new AccelerateDecelerateInterpolator(),
//                getContext().getResources().getDrawable(R.drawable.shr_branded_menu), // Menu open icon
//                getContext().getResources().getDrawable(R.drawable.shr_close_menu))); // Menu close icon
    }

    @Override
    public void onStart() {
        super.onStart();
        onFragmentActivatedListener.onFragmentReset();
    }

    @Override
    public void onProductClicked(int position) {

        if(getContext() == null || getActivity() == null || !isAdded())
            return;

        Intent intent;
        String transitionName = getString(R.string.transition_card);
        View viewStart = view.findViewById(R.id.cardView);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        viewStart,   // Starting view
                        transitionName    // The String
                );

        switch (position) {
            case 0:
                intent = new Intent(getContext(), LogoRequestActivity.class);
                ActivityCompat.startActivity(getContext(), intent, options.toBundle());
                break;
            case 1:
                intent = new Intent(getContext(), SocialMediaActivity.class);
                ActivityCompat.startActivity(getContext(), intent, options.toBundle());
                break;
            case 2:
                intent = new Intent(getContext(), InfluencerRequestActivity.class);
                ActivityCompat.startActivity(getContext(), intent, options.toBundle());
                break;
            case 3:
                intent = new Intent(getContext(), BannersRequestActivity.class);
                ActivityCompat.startActivity(getContext(), intent, options.toBundle());
                break;
            case 4:
                intent = new Intent(getContext(), StoryboardRequestActivity.class);
                ActivityCompat.startActivity(getContext(), intent, options.toBundle());
                break;
            case 5:
                intent = new Intent(getContext(), BrandingRequestActivity.class);
                ActivityCompat.startActivity(getContext(), intent, options.toBundle());
                break;
            case 6:
                intent = new Intent(getContext(), IllustrationActivity.class);
                ActivityCompat.startActivity(getContext(), intent, options.toBundle());
                break;
            case 7:
                intent = new Intent(getContext(), CampaignRequestActivity.class);
                ActivityCompat.startActivity(getContext(), intent, options.toBundle());
                break;
            case 8:
                intent = new Intent(getContext(), StationaryActivity.class);
                ActivityCompat.startActivity(getContext(), intent, options.toBundle());
                break;

//            case 4:
//                navigateTo(new InfluencerBookingFragment(), true);
//                break;
//            case 5:
//                navigateTo(new StoryBoardFragment(), true);
//                break;
//            case 6:
//                navigateTo(new BannersFragment(), true);
//                break;
//            case 7:
//                navigateTo(new BrandingFragment(), true);
//                break;
//            case 8:
//                navigateTo(new IllustrationFragment(), true);
//                break;
//            case 9:
//                navigateTo(new CampaignFragment(), true);
//                break;
//            default:
//                navigateTo(new ProductGridFragment(), true);
//                break;
        }
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        if (getActivity() != null) {
            FragmentTransaction transaction =
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, fragment);

            if (addToBackstack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
//        menuInflater.inflate(R.menu.shr_toolbar_menu, menu);
//        super.onCreateOptionsMenu(menu, menuInflater);
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onFragmentActivatedListener = (onFragmentActivatedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }




}
