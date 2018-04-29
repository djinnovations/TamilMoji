package dj.example.main.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nshmura.recyclertablayout.RecyclerTabLayout;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dj.example.main.R;
import dj.example.main.MyApplication;
import dj.example.main.activities.TabsBaseActivity;
import dj.example.main.redundant.BaseFragment;
import dj.example.main.uiutils.DisplayProperties;

public class HomeTabFragment extends BaseFragment implements ViewPager.OnPageChangeListener{

    @BindView(R.id.disableApp)
    View disableApp;

    protected DisplayProperties displayProperties;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    protected void garbageCollectorCall() {
        pagerAdapter = null;
        tabIndicatorAdapter = null;
        positionTitle = null;
        registeredFragments = null;
    }

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    TabLayout indicator;

    FragmentStatePagerAdapter pagerAdapter;
    ArrayList<Pair<Class, String>> primaryTabFragments = new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setInitials();

        pagerAdapter = new TempPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        setUpTabLayout(view);
        viewPager.addOnPageChangeListener(this);

        if (getActivity() instanceof TabsBaseActivity){
            if(((TabsBaseActivity) getActivity()).isDisableTabIndication()){
                if((((TabsBaseActivity) getActivity()).getPageIndicator() != null)){
                    ((TabsBaseActivity) getActivity()).getPageIndicator().setVisibility(View.GONE);
                }
                indicator.setVisibility(View.GONE);
            }
        }
    }

    private void setInitials(){
        int orient = this.getResources().getConfiguration().orientation;
        displayProperties = DisplayProperties.getInstance(orient);
        if (getActivity() instanceof TabsBaseActivity) {
            numOfPages = ((TabsBaseActivity) getActivity()).getTabFragmentsList().size();
            primaryTabFragments = ((TabsBaseActivity) getActivity()).getTabFragmentsList();
        }
    }

    RecyclerTabLayout tabLayoutPrimary;

    private void setUpTabLayout(View rootView) {
        final View tabLayout = rootView.findViewById(R.id.indicator);
        if (getActivity() instanceof TabsBaseActivity){
            if (((TabsBaseActivity) getActivity()).getPageIndicator() != null) {
                if (tabLayout != null)
                    tabLayout.setVisibility(View.GONE);
                tabLayoutPrimary = (RecyclerTabLayout) ((TabsBaseActivity) getActivity()).getPageIndicator();
                RecyclerTabLayout.Adapter indicatorAdapter = getRecyclerTabIndicator(viewPager);
                if (indicatorAdapter != null)
                    tabLayoutPrimary.setUpWithAdapter(indicatorAdapter);
                tabLayoutPrimary.getAdapter().notifyDataSetChanged();
                tabLayoutPrimary.invalidate();
                return;
            }
        }
        if (tabLayout instanceof TabLayout) {
            tabLayout.setVisibility(View.VISIBLE);
            Runnable runnable = new Runnable() {
                public void run() {
                    ((TabLayout) tabLayout).setupWithViewPager(viewPager);
                }
            };
            MyApplication.getInstance().getUiHandler().postDelayed(runnable, 100);
        }
    }

    private int numOfPages = 2;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class TempPagerAdapter extends FragmentStatePagerAdapter{

        public TempPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return numOfPages;
        }

       /* @Override
        public int getItemPosition(Object object) {
            //return super.getItemPosition(object);
            if (!(object instanceof SocialFeedFragment))
                return POSITION_NONE;
            return POSITION_UNCHANGED;
        }*/

        @Override
        public Fragment getItem(int position) {
           /* if (position == 0) {
                return new TabFragment1();
            }
            else if (position == 1) {
                return new TabFragment2();
            }*/
            try {
                return  (Fragment) primaryTabFragments.get(position).first.getConstructor().newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, "destroyed items FragPageAdapter");
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d(TAG, "destroyed items FragPageAdapter");
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "TabFragment1";
            else if (position == 1)
                return "TabFragment2";
            return super.getPageTitle(position);
        }
    }

    private final String TAG = "HomeFragment";

    private SparseArray<String> positionTitle = new SparseArray<>();

    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public Fragment getRegisteredFragment(int fragmentPosition) {
        return registeredFragments.get(fragmentPosition);
    }

    TabIndicatorRecyclerViewAdapter tabIndicatorAdapter;
    protected RecyclerTabLayout.Adapter<?> getRecyclerTabIndicator(ViewPager viewPager) {
        return tabIndicatorAdapter =  new TabIndicatorRecyclerViewAdapter(viewPager);
    }

    private void gotoData(Object data) {
        if(this.viewPager != null) {
            List<String> dataList = tabIndicatorAdapter.getDataList();
            for (String txt: dataList) {
                if (txt.equals(data))
                    this.viewPager.setCurrentItem(dataList.indexOf(txt), false);
            }
        }
    }

    /*private AppTourGuideHelper mTourHelper;
    private void setUpTourGuideForPeople() {
        Log.d("djhomePage", "onClickPeopleTab");
        *//*resRdr = ResourceReader.getInstance(getApplicationContext());
        coachMarkMgr = DjphyPreferenceManager.getInstance(getApplicationContext());*//*
        mTourHelper = AppTourGuideHelper.getInstance(Application.getInstance());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                *//*if (!coachMarkMgr.isHomeScreenTourDone())
                    testTourGuide();*//*
                mTourHelper.displayPeopleScreenTour(getActivity(), ((MainActivity) getActivity()).getCenterView());
            }
        }, 800);
    }*/

    //public static ViewPager viewPager;

    public class TabIndicatorRecyclerViewAdapter extends RecyclerTabLayout.Adapter<TabIndicatorRecyclerViewAdapter.MyViewHolder>{

        public TabIndicatorRecyclerViewAdapter(ViewPager viewPager) {
            super(viewPager);
            //HomeTabFragment.viewPager = viewPager;
            dataList.clear();
            for (Pair<Class, String> pair: primaryTabFragments) {
                dataList.add(pair.second);
            }
        }

        List<String> dataList = new ArrayList<>();

        public List<String> getDataList() {
            return dataList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_custom_tab_view, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String item = dataList.get(position);
            holder.itemView.setVisibility(View.VISIBLE);
            if(position == this.getCurrentIndicatorPosition()) {
                holder.updatedSelectedItem(item);
            } else {
                holder.updatedNormalItem(item);
            }
        }

        public int getItemCount() {
            return dataList.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.textView)
            public TextView textView;
            @BindView(R.id.selected)
            public View selected;

            @BindView(R.id.tabParent)
            RelativeLayout tabParent;
            String data;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoData(data);
                    }
                });
                setTabWidth();
            }

            private void setTabWidth() {
                ViewTreeObserver vto = tabLayoutPrimary.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        try {
                            int indicatorWidth = /*tabLayoutPrimary.getWidth()*/ (int) (15 * displayProperties.getXPixelsPerCell());

                            Log.d("dj", " indicator width: " + indicatorWidth);
                            tabParent.getLayoutParams().width = /*indicatorWidth / 2*/indicatorWidth;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                tabLayoutPrimary.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else
                                tabLayoutPrimary.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            //NavigationDataObject data;

            public void updatedSelectedItem(Object o) {
                data = (String) o;
                textView.setText(data);
                textView.setTextColor(textView.getResources().getColor(R.color.colorPrimary));
                selected.setVisibility(View.VISIBLE);
            }

            public void updatedNormalItem(Object o) {
                data = (String) o;
                textView.setText(data);
                textView.setTextColor(textView.getResources().getColor(R.color.colorPrimaryTrans));
                selected.setVisibility(View.INVISIBLE);
            }
        }
    }
}
