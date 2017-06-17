/*
用于显示详情
 */

package edu.fjnu.cse.lostandfound.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hk.path.lf.R;
import edu.fjnu.cse.lostandfound.entities.LostItem;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {
    private LostItem item;
    private AppContext appContext;
    private TextView mAddressView;
    private TextView mDetailView;
    private TextView mTimeView;
    private TextView mLabelView;

    //包裹点点的LinearLayout
    private ViewGroup pageGroup;
    private ImageView imageView;
    private View pageItem;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ImageView[] indicatorImages;//存放引到图片数组
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        appContext = (AppContext) getApplication();
        item = appContext.getCurrentItem();
        findView();
        init();
    }

    private void init() {
        mAddressView.setText(item.getPlace());
        mDetailView.setText(item.getDetail());
        mTimeView.setText(item.getCreateTime());
        mLabelView.setText(item.getLabel());

        indicatorImages = new ImageView[item.getPic().length];

        inflater = LayoutInflater.from(this);


        List<View> list = new ArrayList<>();

        for (int i = 0; i < indicatorImages.length; i++) {
            pageItem = inflater.inflate(R.layout.detail_picture_item, null);
            list.add(pageItem);
        }

        adapter = new ViewPagerAdapter(list);
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPagerListener());
        initIndicator();
        if(indicatorImages.length == 0){
            findViewById(R.id.imagesContent).setVisibility(View.GONE);
        }


    }

    private void findView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mAddressView = (TextView) findViewById(R.id.addressTextView);
        mDetailView = (TextView) findViewById(R.id.detailTextView);
        mTimeView = (TextView) findViewById(R.id.timeTextView);
        mLabelView = (TextView) findViewById(R.id.labelTextView);
        imageView = (ImageView) findViewById(R.id.itemImageView);

    }

    /**
     * 初始化引导图标
     * 动态创建多个小圆点，然后组装到线性布局里
     */
    private void initIndicator() {

        ImageView imgView;
        View v = findViewById(R.id.indicator);// 线性水平布局，负责动态调整导航图标

        for (int i = 0; i < indicatorImages.length; i++) {
            imgView = new ImageView(this);
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
        private AsyncImageLoader asyncImageLoader;

        public ViewPagerAdapter(List<View> list) {
            mList = list;
            asyncImageLoader = new AsyncImageLoader();
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

            Drawable cachedImage = asyncImageLoader.loadDrawable(
                    item.getPic()[position].getSrcUrl(), new AsyncImageLoader.ImageCallback() {

                        public void imageLoaded(Drawable imageDrawable,
                                                String imageUrl) {

                            View view = mList.get(position);
                            imageView = ((ImageView) view.findViewById(R.id.detailImageView));
                            imageView.setImageDrawable(imageDrawable);
                            view.findViewById(R.id.loading_progress).setVisibility(View.GONE);
                            container.removeView(mList.get(position));
                            container.addView(mList.get(position));
                            // adapter.notifyDataSetChanged();

                        }
                    });

            View view = mList.get(position);
            imageView = ((ImageView) view.findViewById(R.id.detailImageView));
            imageView.setImageDrawable(cachedImage);
                    //setBackground(cachedImage);
            //view.findViewById(R.id.loading_progress).setVisibility(View.GONE);
            container.removeView(mList.get(position));
            container.addView(mList.get(position));
            // adapter.notifyDataSetChanged();


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


    /**
     * 异步加载图片
     */
    static class AsyncImageLoader {

        // 软引用，使用内存做临时缓存 （程序退出，或内存不够则清除软引用）
        private HashMap<String, SoftReference<Drawable>> imageCache;

        public AsyncImageLoader() {
            imageCache = new HashMap<String, SoftReference<Drawable>>();
        }

        /**
         * 定义回调接口
         */
        public interface ImageCallback {
            void imageLoaded(Drawable imageDrawable, String imageUrl);
        }


        /**
         * 创建子线程加载图片
         * 子线程加载完图片交给handler处理（子线程不能更新ui，而handler处在主线程，可以更新ui）
         * handler又交给imageCallback，imageCallback须要自己来实现，在这里可以对回调参数进行处理
         *
         * @param imageUrl       ：须要加载的图片url
         * @param imageCallback：
         * @return
         */
        public Drawable loadDrawable(final String imageUrl,
                                     final ImageCallback imageCallback) {

            //如果缓存中存在图片  ，则首先使用缓存
            if (imageCache.containsKey(imageUrl)) {
                SoftReference<Drawable> softReference = imageCache.get(imageUrl);
                Drawable drawable = softReference.get();
                if (drawable != null) {
                    imageCallback.imageLoaded(drawable, imageUrl);//执行回调
                    return drawable;
                }
            }

            /**
             * 在主线程里执行回调，更新视图
             */
            final Handler handler = new Handler() {
                public void handleMessage(Message message) {
                    imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
                }
            };


            /**
             * 创建子线程访问网络并加载图片 ，把结果交给handler处理
             */
            new Thread() {
                @Override
                public void run() {
                    Drawable drawable = loadImageFromUrl(imageUrl);
                    // 下载完的图片放到缓存里
                    imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
                    Message message = handler.obtainMessage(0, drawable);
                    handler.sendMessage(message);
                }
            }.start();

            return null;
        }


        /**
         * 下载图片  （注意HttpClient 和httpUrlConnection的区别）
         */
        public Drawable loadImageFromUrl(String url) {
            Drawable d = null;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    d = Drawable.createFromStream(response.body().byteStream(), "src");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return d;
        }

        //清除缓存
        public void clearCache() {

            if (this.imageCache.size() > 0) {

                this.imageCache.clear();
            }

        }

    }


}
