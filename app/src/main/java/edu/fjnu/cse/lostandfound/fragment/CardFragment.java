package edu.fjnu.cse.lostandfound.fragment;
/**
 * homefragment
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import edu.fjnu.cse.lostandfound.R;
import edu.fjnu.cse.lostandfound.adapt.LostRecyclerViewAdapter;
import edu.fjnu.cse.lostandfound.entities.LostItem;
import edu.fjnu.cse.lostandfound.view.LoadMoreRecyclerView;


public class CardFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    private List<LostItem> mDatas;
    private LoadMoreRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LostRecyclerViewAdapter lostRecyclerViewAdapter;


    //包裹点点的LinearLayout
    private ViewGroup pageGroup;
    private ImageView imageView;
    private View pageItem;
    private ViewPager viewPager;
    private ImageView[] indicatorImages;//存放引到图片数组
    private LayoutInflater inflater;

    public CardFragment() {
    }

    @SuppressWarnings("unused")
    public static CardFragment newInstance(int columnCount) {
        CardFragment fragment = new CardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {

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