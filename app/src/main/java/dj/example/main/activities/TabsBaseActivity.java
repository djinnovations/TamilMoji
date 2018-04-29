package dj.example.main.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dj.example.main.R;
import dj.example.main.model.NavigationDataObject;
import dj.example.main.modules.appupdater.MyAppRaterUpdateHelper;
import dj.example.main.modules.sociallogins.SocialLoginUtil;
import dj.example.main.redundant.BaseFragment;
import dj.example.main.uiutils.ColoredSnackbar;
import dj.example.main.utils.MyPrefManager;

/**
 * Created by DJphy on 10-07-2017.
 */

public abstract class TabsBaseActivity extends BaseDrawerActivity {

    protected BaseFragment activePage;
    protected NavigationDataObject activePageData;
    protected List<NavigationDataObject> history = new ArrayList<>();
    @BindView(R.id.disableApp)
    View disableApp;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.layoutParent)
    ViewGroup layoutParent;

    RelativeLayout rlMain;
    @BindView(R.id.indicator)
    View indicator;


    public abstract ArrayList<Pair<Class, String>> getTabFragmentsList();

    public View getPageIndicator() {
        return indicator;
    }

    private boolean backEntry;

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public Activity getSelf() {
        return this;
    }

    @Override
    public View getViewForLayoutAccess() {
        return layoutParent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        NavigationDataObject navigationDataObject = getPrimaryNavigationObj();
        if (navigationDataObject != null)
            action(navigationDataObject);

        //MyPrefManager.getInstance().updateSessionCounts();
    }

    protected abstract NavigationDataObject getPrimaryNavigationObj();

    public abstract boolean isDisableTabIndication();

    /*private AppTourGuideHelper mTourHelper;

    private void tourThisScreen() {

        *//*resRdr = ResourceReader.getInstance(getApplicationContext());
        coachMarkMgr = DjphyPreferenceManager.getInstance(getApplicationContext());*//*
        mTourHelper = AppTourGuideHelper.getInstance(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                *//*if (!coachMarkMgr.isHomeScreenTourDone())
                    testTourGuide();*//*
                mTourHelper.displayHomeTour(MainActivity.this, new View[]{transView, transViewPeople, transViewSearch,
                        transViewNotify, transViewPost});
            }
        }, 2000);
    }*/


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (activePage != null) {
            promptBeforeExit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onExitYesClick() {
        SocialLoginUtil.getInstance().performFbLogout();
        SocialLoginUtil.getInstance().performGoogleLogout();
        SocialLoginUtil.getInstance().indicateSignedOut();
        super.onExitYesClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private final String TAG = "MainActivity";

    private final String TAG_FOR_HOME_FRAGMENT = "finder.homefragment";

    public boolean action(NavigationDataObject navigationDataObject) {
        if (navigationDataObject.getTargetType() == NavigationDataObject.FRAGMENT_VIEW) {
            Boolean isAdded = false;
            if (activePageData != null && activePageData.getActionType().equals(navigationDataObject.getActionType())) {
                isAdded = true;
            }

            if (!isAdded) {
                BaseFragment fragment = BaseFragment.newInstance(navigationDataObject);
                if (fragment != null) {
                    //setTitle(navigationDataObject.getTitle());
                    setTitle("");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fragment, TAG_FOR_HOME_FRAGMENT);
                    ft.commit();
                    activePage = fragment;
                    activePageData = navigationDataObject;
                    if (!backEntry)
                        history.add(activePageData);
                    backEntry = false;
                    return true;
                }
            }
        }
        return super.action(navigationDataObject);
    }

    protected void onResume() {
        super.onResume();
    }
}