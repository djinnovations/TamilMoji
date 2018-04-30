package dj.example.main.activities;

import android.graphics.drawable.LayerDrawable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dj.example.main.MyApplication;
import dj.example.main.R;
import dj.example.main.model.NavigationDataObject;
import dj.example.main.uiutils.DisplayProperties;

/**
 * Created by DJphy
 */
public abstract class BaseDrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.vNavigation)
    NavigationView vNavigation;
    Toolbar toolbar;

    protected DisplayProperties displayProperties;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_drawer);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        ButterKnife.bind(this);
        int orient = this.getResources().getConfiguration().orientation;
        displayProperties = DisplayProperties.getInstance(orient);
        setupToolbar();
        bindViews();
        setupHeader();
    }

    @OnClick({R.id.nav_menu_home,
            R.id.nav_menu_explore, R.id.nav_menu_profile,
            R.id.nav_menu_settings, R.id.nav_menu_about, R.id.nav_menu_logout, R.id.nav_menu_contact_us})
    public void menuButtonClick(View view) {
        int id = view.getId();
        menuAction(id);
    }

    public void menuAction(int id) {

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START, false);

        NavigationDataObject navigationDataObject = MyApplication.getInstance().getNavigationObj(id);
        if (navigationDataObject != null) {
            action(navigationDataObject);
        }

        /*if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);*/
    }

    private final String TAG = "BaseDrawerActivity";

    @Override
    public void serverCallEnds(int id, String url, Object json, AjaxStatus status) {
        Log.d(TAG, "url queried- "+TAG+": " + url);
        Log.d(TAG, "response- "+TAG+": " + json);
        stopProgress();
        super.serverCallEnds(id, url, json, status);
    }

    static LayerDrawable icon;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setMenuCustom(menu);
        return true;
    }


    private void setMenuCustom(Menu menu) {
        if (menu != null) {
            menu.clear();
        }
        MenuInflater inflater = getMenuInflater();
        /*inflater.inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.nav_my_notifications);

        if (item != null) {
            MenuItemCompat.setActionView(item, R.layout.notification_badge);

            TextView textView = (TextView) item.getActionView()
                    .findViewById(R.id.tvNotifyCount);
            item.getActionView().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(item);
                }
            });

            String unreadCount = RandomUtils.getUnreadCount();
            if (TextUtils.isEmpty(*//*mCount*//*unreadCount)) {
                if (textView != null) {
                    textView.setVisibility(View.INVISIBLE);
                }
            } else if (*//*mCount*//*unreadCount.equalsIgnoreCase("0")) {
                if (textView != null) {
                    textView.setVisibility(View.INVISIBLE);
                }
            } else {
                if (textView != null) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(*//*mCount*//*unreadCount);
                }
            }
        }*/
    }


    private String mCount = "";

    protected void bindViews() {

    }

    boolean isFirstTime = true;

    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
       /* if (id == R.id.nav_my_search) {
            startActivity(new Intent(this, SearchActivity.class));
            //Toast.makeText(getApplication(), "Feature Coming Soon", Toast.LENGTH_SHORT).show();
            return true;
        }
        boolean returnVal = false;
        NavigationDataObject navigationDataObject = (NavigationDataObject) getApp().getMainMenu().get(id);
        if (navigationDataObject != null)
            returnVal = action(navigationDataObject);

        if (returnVal)
            return returnVal;
        return super.onOptionsItemSelected(item);*/
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        boolean returnVal = false;
        NavigationDataObject navigationDataObject = (NavigationDataObject) MyApplication.getInstance().getNavigationObj(id);
        if (navigationDataObject != null)
            returnVal = action(navigationDataObject);
        drawerLayout.closeDrawer(GravityCompat.START);
        return returnVal;
    }

    protected boolean isPointsUpdateInprogress = false;

    private void setupHeader() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        vNavigation.setNavigationItemSelectedListener(this);
        View headerLayout = vNavigation.getHeaderView(0);
        //// TODO: 09-07-2017  load name and user images here in silder view 
        TextView userName = (TextView) headerLayout.findViewById(R.id.userName);
        userName.setText("Welcome");
        ImageView userImage = (ImageView) headerLayout.findViewById(R.id.userImage);
        Picasso.with(this).load(R.drawable.vector_icon_profile_white_outline).placeholder(R.drawable
                .vector_icon_profile_white_outline).into(userImage);
        //User user = getApp().getUser();
        /*if (user != null) {
            userName.setText(user.getName());
            ImageView userImage = (ImageView) headerLayout.findViewById(R.id.userImage);
            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationDataObject navigationDataObject = (NavigationDataObject) getApp().getMainMenu().get(R.id.nav_my_profile);
                    if (navigationDataObject != null) {
                        action(navigationDataObject);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
            });
            Picasso.with(this).load(user.getImageUrl()).placeholder(R.drawable
                    .vector_image_place_holder_profile_dark).into(userImage);
        }*/

    }

}
