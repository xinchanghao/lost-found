package edu.fjnu.cse.lostandfound.fragment;
/**
 * foundfragment
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.fjnu.cse.lostandfound.R;
import edu.fjnu.cse.lostandfound.adapt.FoundRecyclerViewAdapter;
import edu.fjnu.cse.lostandfound.entities.API_GetFound;
import edu.fjnu.cse.lostandfound.entities.API_GetFound_Ret;
import edu.fjnu.cse.lostandfound.entities.API_Return;
import edu.fjnu.cse.lostandfound.entities.LostItem;
import edu.fjnu.cse.lostandfound.net.api;
import edu.fjnu.cse.lostandfound.view.LoadMoreRecyclerView;


public class FoundFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    private List<LostItem> mDatas;
    private LoadMoreRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FoundRecyclerViewAdapter foundRecyclerViewAdapter;
    private int count = 1;

    public FoundFragment() {
    }

    @SuppressWarnings("unused")
    public static FoundFragment newInstance(int columnCount) {
        FoundFragment fragment = new FoundFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        findView(view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                api.Request(new API_GetFound(1), new API_Return<API_GetFound_Ret>() {
                    @Override
                    public void ret(int Code, API_GetFound_Ret ret) {
                        if (Code == 0) {
                            LostItem[] mDatas = ret.getLostItems();
                            swipeRefreshLayout.setRefreshing(false);
                            foundRecyclerViewAdapter.setDatas(mDatas);
                            if (ret.getTotalRows() <= 10) {
                                recyclerView.setAutoLoadMoreEnable(false);
                            } else {
                                recyclerView.setAutoLoadMoreEnable(true);
                            }
                            recyclerView.scrollToPosition(0);
                            recyclerView.setAdapter(foundRecyclerViewAdapter);
                            //foundRecyclerViewAdapter.noti
                        } else {
                            System.out.println("error:" + Code);
                        }
                    }
                }, FoundFragment.this.getActivity());

            }
        });
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        foundRecyclerViewAdapter = new FoundRecyclerViewAdapter(this.getActivity(), mDatas);
        recyclerView.setAdapter(foundRecyclerViewAdapter);
        recyclerView.setAutoLoadMoreEnable(true);
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        api.Request(new API_GetFound(count), new API_Return<API_GetFound_Ret>() {
                            @Override
                            public void ret(int Code, API_GetFound_Ret ret) {
                                if (Code == 0) {
                                    LostItem[] mDatas = ret.getLostItems();
                                    foundRecyclerViewAdapter.addDatas(mDatas);
                                    recyclerView.setAutoLoadMoreEnable(true);
                                    recyclerView.notifyMoreFinish(ret.getTotalRows() > count * 10);
                                } else {
                                    System.out.println("error:" + Code);
                                }
                            }
                        }, FoundFragment.this.getActivity());

                    }
                }, 1000);
            }
        });
        foundRecyclerViewAdapter.notifyDataSetChanged();

        initData();
        return view;
    }

    private void initData() {
        api.Request(new API_GetFound(1), new API_Return<API_GetFound_Ret>() {
            @Override
            public void ret(int Code, API_GetFound_Ret ret) {
                if (Code == 0) {
                    LostItem[] mDatas = ret.getLostItems();
                    swipeRefreshLayout.setRefreshing(false);
                    foundRecyclerViewAdapter.setDatas(mDatas);
                    if (ret.getTotalRows() <= 10) {
                        recyclerView.setAutoLoadMoreEnable(false);
                    } else {
                        recyclerView.setAutoLoadMoreEnable(true);
                    }
                    foundRecyclerViewAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(0);
                } else {
                    System.out.println("error:" + Code);
                }
            }
        }, FoundFragment.this.getActivity());
    }

    private void findView(View view) {
        recyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
