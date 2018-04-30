package dj.example.main.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dj.example.main.R;
import dj.example.main.activities.ExploreActivity;
import dj.example.main.adapters.ThumbnailAdapter;

/**
 * Created by CSC on 4/30/2018.
 */

public class ExploreFragment extends GenericFragment {

    //private ThumbnailAdapter adapter;

    @BindView(R.id.etItem)
    EditText etItem;

    @Override
    protected int getLayoutResId() {
        //return super.getLayoutResId();
        return R.layout.fragment_explore;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (getActivity()instanceof ExploreActivity){
            ((ExploreActivity) getActivity()).performSearch("");
        }
        etItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String txt = etItem.getText().toString();
                    if (getActivity() instanceof ExploreActivity) {
                        if (TextUtils.isEmpty(txt)) {
                            ((ExploreActivity) getActivity()).setInfoMsg("enter something", Snackbar.LENGTH_LONG);
                        }
                        else ((ExploreActivity) getActivity()).performSearch(txt);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /*@Override
    protected void onGarbageCollection() {
        adapter = null;
    }*/

    @Override
    public boolean isAddSnapper() {
        return false;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        float scalefactor = getResources().getDisplayMetrics().density * 150;
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int columns = (int) ((float)size.x / (float) scalefactor);
        return new GridLayoutManager(getActivity().getApplicationContext(), columns,
                LinearLayoutManager.VERTICAL, false);
    }

    /*@Override
    protected RecyclerView.Adapter getAdapter() {
        return adapter = new ThumbnailAdapter();
    }

    @Override
    public void changeData(List dataList) {
        if (adapter != null){
            try {
                adapter.changeData(dataList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}
