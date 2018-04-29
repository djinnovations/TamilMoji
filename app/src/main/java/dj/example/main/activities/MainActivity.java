package dj.example.main.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dj.example.main.R;
import dj.example.main.fragments.MainFragment;
import dj.example.main.utils.MyPrefManager;

public class MainActivity extends BaseActivity {

    /*@Override
    public ArrayList<Pair<Class, String>> getTabFragmentsList() {
        ArrayList<Pair<Class, String>> list = new ArrayList<>();
        list.add(new Pair<Class, String>(TabFragment1.class, "Tab - 1"));
        list.add(new Pair<Class, String>(TabFragment2.class, "Tab - 2"));
        return list;
    }*/

    @Override
    public ProgressBar getProgressBar() {
        return new ProgressBar(this);
    }

    @Override
    public Activity getSelf() {
        return this;
    }

    @Override
    public View getViewForLayoutAccess() {
        return null;
    }

    @BindView(R.id.activity_main)
    FrameLayout activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MyPrefManager.getInstance().updateSessionCounts();
        getSupportFragmentManager().beginTransaction().replace(activity_main.getId(), new MainFragment()).commit();
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
