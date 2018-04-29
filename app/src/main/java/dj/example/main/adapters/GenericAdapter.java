package dj.example.main.adapters;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dj.example.main.R;
import dj.example.main.model.HeaderThumbnailData;
import dj.example.main.uiutils.UiRandomUtils;
import dj.example.main.utils.IDUtils;

/**
 * Created by User on 02-02-2018.
 */

public class GenericAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GenericAdapterInterface {

    public static final int HOME = IDUtils.generateViewId();
    //public static final int COURSES_MINE = IDUtils.generateViewId();


    public GenericAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getRootLayout(viewType), parent, false);
        return getViewHolder(view, viewType);
    }


    @Override
    public RecyclerView.ViewHolder getViewHolder(View view, int viewType) {
        //if (viewType == HOME)
        return new HeaderThumbnailViewHolder(view);
        /*else if (viewType == COURSES_MINE)
            return new MyCourseViewHolder(view);
        else return null;*/
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseItemHolder) holder).onItemViewUpdate(dataList.get(position), holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return /*dataList.get(position).getViewType()*/0;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private List<HeaderThumbnailData> dataList = new ArrayList<>();

    @Override
    public void changeData(final List dataList) throws Exception {
        if (dataList == null)
            return;
        if (dataList.size() <= 0)
            return;
        if (!(dataList.get(0) instanceof HeaderThumbnailData))
            throw new IllegalArgumentException("Required data type \"HeaderThumbnailData\"");
        (new Handler(Looper.getMainLooper())).postDelayed(new Runnable() {
            @Override
            public void run() {
                GenericAdapter.this.dataList.clear();
                GenericAdapter.this.dataList.addAll(dataList);
                notifyDataSetChanged();
            }
        }, 100);
    }

    @Override
    public int getRootLayout(int viewType) {
        return R.layout./*viewholder_header_thumbnail*/viewholder_header_card_thumbnail;
    }

    @Override
    public void setOnClickListener(RecyclerView.ViewHolder holder) {

    }


    class HeaderThumbnailViewHolder extends BaseItemHolder implements View.OnClickListener {

        @BindView(R.id.rvMenu)
        RecyclerView rvMenu;
        @BindView(R.id.tvHeader)
        TextView tvHeader;

        private ThumbnailAdapter adapter;

        public HeaderThumbnailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //setOnClickListener(this);
            setUpRecycleView();
        }

        private void setUpRecycleView() {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            rvMenu.setHasFixedSize(false);
            rvMenu.setLayoutManager(mLayoutManager);
            rvMenu.setItemAnimator(new DefaultItemAnimator());
            UiRandomUtils.getInstance().addSnapper(rvMenu, Gravity.START);
            rvMenu.setAdapter(adapter = new ThumbnailAdapter());
        }

        @Override
        public void onClick(View view) {
            //EventBus.getDefault().post(datalist);
        }

        @Override
        public void onItemViewUpdate(Object dataObject, RecyclerView.ViewHolder holder, int position) {
            try {
                HeaderThumbnailData data = (HeaderThumbnailData) dataObject;
                tvHeader.setText(data.getHeaderTitle());
                adapter.changeData(data.getDataList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
