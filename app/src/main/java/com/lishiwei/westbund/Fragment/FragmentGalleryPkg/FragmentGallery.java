package com.lishiwei.westbund.Fragment.FragmentGalleryPkg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.hannesdorfmann.mosby.mvp.delegate.FragmentMvpDelegate;
import com.lishiwei.model.Gallery;
import com.lishiwei.westbund.Adapter.GalleryRecyclerAdapter;
import com.lishiwei.westbund.Fragment.BaseMvpLceFragment;
import com.lishiwei.westbund.Presenter.GalleryPresenter;
import com.lishiwei.westbund.R;
import com.lishiwei.westbund.ViewInterface.GalleryView;
import com.lishiwei.westbund.WestBundApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link #newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGallery extends BaseMvpLceFragment<SwipeRefreshLayout, List<Gallery>, GalleryView, GalleryPresenter> implements GalleryView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.ptr_Gallery)
    PullToRefreshRecyclerView ptrGallery;
    @Bind(R.id.contentView)
    SwipeRefreshLayout contentView;
    GalleryRecyclerAdapter galleryRecyclerAdapter;
List<Gallery>  galleryList=new ArrayList<>();
    @Override
    public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
        ptrGallery.onRefreshComplete();
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        ptrGallery.onRefreshComplete();
        contentView.setRefreshing(false);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public GalleryPresenter createPresenter() {
        return new GalleryPresenter(WestBundApplication.getInstance());
    }


    @Override
    public void setData(List<Gallery> data) {
        if (galleryRecyclerAdapter == null) {
            galleryRecyclerAdapter = new GalleryRecyclerAdapter(getActivity());
            ptrGallery.getRefreshableView().setAdapter(galleryRecyclerAdapter);

        }
        if (isLoadMore)
        {
            galleryList.addAll(data);
        } else {
            galleryList = data;
        }

        galleryRecyclerAdapter.setGalleryList((ArrayList<Gallery>) galleryList);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        Log.d("aaaaaaaaaaaaa", "loadData: "+currentPageNumber);
        presenter.loadGallery(10,currentPageNumber,pullToRefresh);
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FragmentGallery() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment .
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGallery newInstance(String param1, String param2) {
        FragmentGallery fragment = new FragmentGallery();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @NonNull
    @Override
    protected FragmentMvpDelegate<GalleryView, GalleryPresenter> getMvpDelegate() {
        return super.getMvpDelegate();
    }

    public void initViews(Bundle savedInstanceState) {
        contentView.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        ptrGallery.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        ptrGallery.getRefreshableView().setLayoutFrozen(false);
        ptrGallery.getRefreshableView().setLayoutManager(new LinearLayoutManager(getActivity()));
        ptrGallery.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                currentPageNumber++;
                isLoadMore=true;
                loadData(true);
            }
        });
        contentView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPageNumber=1;
                loadData(true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
