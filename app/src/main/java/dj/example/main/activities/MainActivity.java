package dj.example.main.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dj.example.main.R;
import dj.example.main.fragments.MainFragment;
import dj.example.main.utils.MyPrefManager;

public class MainActivity extends BaseDrawerActivity {

    /*@Override
    public ArrayList<Pair<Class, String>> getTabFragmentsList() {
        ArrayList<Pair<Class, String>> list = new ArrayList<>();
        list.add(new Pair<Class, String>(TabFragment1.class, "Tab - 1"));
        list.add(new Pair<Class, String>(TabFragment2.class, "Tab - 2"));
        return list;
    }*/

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
        return flHolder;
    }

    @BindView(R.id.activity_main)
    RelativeLayout activity_main;
    @BindView(R.id.flHolder)
    FrameLayout flHolder;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        onCreateDelegate();
        //MyPrefManager.getInstance().updateSessionCounts();
    }

    protected void onCreateDelegate(){
        getSupportFragmentManager().beginTransaction().replace(flHolder.getId(), new MainFragment()).commit();
    }

    @Override
    protected void onGarbageCollection() {

    }

    /*@Override
    protected void onGarbageCollection() {
        activePageData = null;
        history = null;
        activePage = null;
    }*/

    /*@Override
    protected NavigationDataObject getPrimaryNavigationObj() {
        return MyApplication.getInstance().getNavigationObj(R.id.nav_home);
    }*/
}
