package edu.fjnu.cse.lostandfound.fragment;
/**
 * lost fragment
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
import edu.fjnu.cse.lostandfound.adapt.LostRecyclerViewAdapter;
import edu.fjnu.cse.lostandfound.entities.API_GetLost;
import edu.fjnu.cse.lostandfound.entities.API_GetLost_Ret;
import edu.fjnu.cse.lostandfound.entities.API_Return;
import edu.fjnu.cse.lostandfound.entities.LostItem;
import edu.fjnu.cse.lostandfound.net.api;
import edu.fjnu.cse.lostandfound.view.LoadMoreRecyclerView;


public class LostFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    private List<LostItem> mDatas;
    private LoadMoreRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LostRecyclerViewAdapter lostRecyclerViewAdapter;
    private int count = 1;

    public LostFragment() {
    }



}
