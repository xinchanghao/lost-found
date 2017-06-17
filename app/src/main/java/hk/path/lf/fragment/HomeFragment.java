package hk.path.lf.fragment;
/**
 * homefragment
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import hk.path.lf.R;
import hk.path.lf.adapt.LostRecyclerViewAdapter;
import hk.path.lf.entities.API_GetLost;
import hk.path.lf.entities.API_GetLost_Ret;
import hk.path.lf.entities.API_Return;
import hk.path.lf.entities.LostItem;
import hk.path.lf.net.api;
import hk.path.lf.view.LoadMoreRecyclerView;


public class HomeFragment extends Fragment {
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
    private ViewPagerAdapter adapter;
    private ImageView[] indicatorImages;//存放引到图片数组
    private LayoutInflater inflater;

    public HomeFragment() {
    }

    @SuppressWarnings("unused")
    public static HomeFragment newInstance(int columnCount) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findView(view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                api.Request(new API_GetLost(1), new API_Return<API_GetLost_Ret>() {
                    @Override
                    public void ret(int Code, API_GetLost_Ret ret) {
                        if (Code == 0) {
                            LostItem[] mDatas = ret.getLostItems();
                            swipeRefreshLayout.setRefreshing(false);
                            lostRecyclerViewAdapter.setDatas(mDatas);
                            recyclerView.setAutoLoadMoreEnable(false);
                            recyclerView.scrollToPosition(0);
                            recyclerView.setAdapter(lostRecyclerViewAdapter);
                            //lostRecyclerViewAdapter.noti
                        } else {
                            System.out.println("error:" + Code);
                        }
                    }
                }, HomeFragment.this.getActivity());

            }
        });
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        lostRecyclerViewAdapter = new LostRecyclerViewAdapter(this.getActivity(), mDatas);
        recyclerView.setAdapter(lostRecyclerViewAdapter);
        recyclerView.setAutoLoadMoreEnable(true);
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        api.Request(new API_GetLost(1), new API_Return<API_GetLost_Ret>() {
                            @Override
                            public void ret(int Code, API_GetLost_Ret ret) {
                                if (Code == 0) {
                                    LostItem[] mDatas = ret.getLostItems();
                                    swipeRefreshLayout.setRefreshing(false);
                                    lostRecyclerViewAdapter.addDatas(mDatas);
                                    recyclerView.notifyMoreFinish(true);
                                } else {
                                    System.out.println("error:" + Code);
                                }
                            }
                        }, HomeFragment.this.getActivity());

                    }
                }, 1000);
            }
        });
        lostRecyclerViewAdapter.notifyDataSetChanged();


        indicatorImages = new ImageView[2];

        inflater = LayoutInflater.from(this.getActivity());


        List<View> list = new ArrayList<>();

        for (int i = 0; i < indicatorImages.length; i++) {
            pageItem = inflater.inflate(R.layout.detail_picture_item, null);
            list.add(pageItem);
        }

        adapter = new ViewPagerAdapter(list);
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPagerListener());
        initIndicator(view);
        if (indicatorImages.length == 0) {
            view.findViewById(R.id.imagesContent).setVisibility(View.GONE);
        }
        WindowManager wm = (WindowManager) this.getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams lp = view.findViewById(R.id.imagesContent).getLayoutParams();
        lp.width = screenWidth;
        lp.height = (int)((float)screenWidth / 900 * 500);
        view.findViewById(R.id.imagesContent).setLayoutParams(lp);

        initData();
        return view;
    }

    private void initData() {
        api.Request(new API_GetLost(1), new API_Return<API_GetLost_Ret>() {
            @Override
            public void ret(int Code, API_GetLost_Ret ret) {
                if (Code == 0) {
                    LostItem[] mDatas = ret.getLostItems();
                    swipeRefreshLayout.setRefreshing(false);
                    lostRecyclerViewAdapter.setDatas(mDatas);
                    recyclerView.setAutoLoadMoreEnable(false);
                    lostRecyclerViewAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(0);
                } else {
                    System.out.println("error:" + Code);
                }
            }
        }, HomeFragment.this.getActivity());
    }

    private void findView(View view) {
        recyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        imageView = (ImageView) view.findViewById(R.id.itemImageView);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * 初始化引导图标
     * 动态创建多个小圆点，然后组装到线性布局里
     */
    private void initIndicator(View view) {

        ImageView imgView;
        View v = view.findViewById(R.id.indicator);// 线性水平布局，负责动态调整导航图标

        for (int i = 0; i < indicatorImages.length; i++) {
            imgView = new ImageView(this.getActivity());
            LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(10, 10);
            params_linear.setMargins(7, 10, 7, 10);
            imgView.setLayoutParams(params_linear);
            indicatorImages[i] = imgView;

            if (i == 0) { // 初始化第一个为选中状态
                indicatorImages[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                indicatorImages[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            ((ViewGroup) v).addView(indicatorImages[i]);
        }

    }


    /**
     * 适配器，负责装配 、销毁  数据  和  组件 。
     */
    private class ViewPagerAdapter extends PagerAdapter {

        private List<View> mList;

        public ViewPagerAdapter(List<View> list) {
            mList = list;
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return mList.size();
        }


        /**
         * Remove a page for the given position.
         * 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
         * instantiateItem(View container, int position)
         * This method was deprecated in API level . Use instantiateItem(ViewGroup, int)
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }


        /**
         * Create the page for the given position.
         */
        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {

            View view = mList.get(position);
            imageView = ((ImageView) view.findViewById(R.id.detailImageView));
            if (position == 0) {
                imageView.setImageResource(R.drawable.banner1);
            } else {
                imageView.setImageResource(R.drawable.banner2);
            }
            view.findViewById(R.id.loading_progress).setVisibility(View.GONE);
            container.removeView(mList.get(position));
            container.addView(mList.get(position));


//            Drawable cachedImage = asyncImageLoader.loadDrawable(
//                    item.getPic()[position].getSrcUrl(), new DetailActivity.AsyncImageLoader.ImageCallback() {
//
//                        public void imageLoaded(Drawable imageDrawable,
//                                                String imageUrl) {
//
//                            View view = mList.get(position);
//                            imageView = ((ImageView) view.findViewById(R.id.detailImageView));
//                            imageView.setImageDrawable(imageDrawable);
//                            view.findViewById(R.id.loading_progress).setVisibility(View.GONE);
//                            container.removeView(mList.get(position));
//                            container.addView(mList.get(position));
//                            // adapter.notifyDataSetChanged();
//
//                        }
//                    });
//
//            View view = mList.get(position);
//            imageView = ((ImageView) view.findViewById(R.id.detailImageView));
//            imageView.setImageDrawable(cachedImage);
//            //setBackground(cachedImage);
//            //view.findViewById(R.id.loading_progress).setVisibility(View.GONE);
//            container.removeView(mList.get(position));
//            container.addView(mList.get(position));
//            // adapter.notifyDataSetChanged();
//

            return mList.get(position);

        }


    }


    /**
     * 动作监听器，可异步加载图片
     */
    private class ViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub
            if (state == 0) {
                //new MyAdapter(null).notifyDataSetChanged();
            }
        }


        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {

            // 改变所有导航的背景图片为：未选中
            for (int i = 0; i < indicatorImages.length; i++) {
                indicatorImages[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            // 改变当前背景图片为：选中
            indicatorImages[position].setBackgroundResource(R.drawable.page_indicator_focused);
        }
    }

}
