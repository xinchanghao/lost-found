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
