package edu.fjnu.cse.lostandfound.adapt;
/**
 * lost适配器
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import edu.fjnu.cse.lostandfound.R;
import edu.fjnu.cse.lostandfound.activity.AppContext;
import edu.fjnu.cse.lostandfound.activity.DetailActivity;
import edu.fjnu.cse.lostandfound.entities.LostItem;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LostRecyclerViewAdapter extends RecyclerView.Adapter<LostRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<LostItem> mValues;
    private Context mContext;
    private LayoutInflater mInflater;
    private int count = 1;
    private Bitmap mBitmap; //

    private LruCache<String, BitmapDrawable> mMemoryCache;//

    public void setDatas(LostItem[] data) {
        count = 1;
        mValues.clear();
        for (int i = 0; i < data.length; i++) {
            mValues.add(data[i]);
        }
    }

    public LostRecyclerViewAdapter(Context context, List<LostItem> items) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        //默认显示的图片
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_lost);
        //计算内存，并且给Lrucache 设置缓存大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 6;
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
        mValues = new ArrayList<>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    public void addDatas(LostItem[] data) {
        count++;
        for (int i = 0; i < data.length; i++) {
            mValues.add(data[i]);
        }
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);
        holder.mAddressView.setText(holder.mItem.getPlace());
        holder.mDetailView.setText(holder.mItem.getDetail());
        holder.mTimeView.setText(holder.mItem.getCreateTime());
        holder.mLabelView.setText(holder.mItem.getLabel());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppContext mAppContext = (AppContext) ((Activity)mContext).getApplication();
                mAppContext.setCurrentItem(holder.mItem);
                mContext.startActivity(new Intent(mContext, DetailActivity.class));
            }
        });
        if (holder.mItem.getPic().length != 0) {
            String imageUrl = holder.mItem.getPic()[0].getThumbnailUrl();

            BitmapDrawable drawable = getBitmapDrawableFromMemoryCache(imageUrl);
            if (drawable != null) {
                holder.imageView.setImageDrawable(drawable);
            } else if (cancelPotentialTask(imageUrl, holder.imageView)) {
                //执行下载操作
                DownLoadTask task = new DownLoadTask(holder.imageView);
                AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), mBitmap, task);
                holder.imageView.setImageDrawable(asyncDrawable);
                task.execute(imageUrl);
            }
        }else{
            holder.imageView.setImageResource(R.mipmap.error_pic);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final View mView;
        public final TextView mAddressView;
        public final TextView mDetailView;
        public final TextView mTimeView;
        public final TextView mLabelView;
        public LostItem mItem;
        public final CardView mCardView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAddressView = (TextView) view.findViewById(R.id.addressTextView);
            mDetailView = (TextView) view.findViewById(R.id.detailTextView);
            mTimeView = (TextView) view.findViewById(R.id.timeTextView);
            mLabelView = (TextView) view.findViewById(R.id.labelTextView);
            imageView = (ImageView) view.findViewById(R.id.itemImageView);
            mCardView = (CardView) view.findViewById(R.id.cardView);
        }
    }

    /**
     * 检查复用的ImageView中是否存在其他图片的下载任务，如果存在就取消并且返回ture 否则返回 false
     *
     * @param imageUrl
     * @param imageView
     * @return
     */
    private boolean cancelPotentialTask(String imageUrl, ImageView imageView) {
        DownLoadTask task = getDownLoadTask(imageView);
        if (task != null) {
            String url = task.url;
            if (url == null || !url.equals(imageUrl)) {
                task.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 從缓存中获取已存在的图片
     *
     * @param imageUrl
     * @return
     */
    private BitmapDrawable getBitmapDrawableFromMemoryCache(String imageUrl) {
        return mMemoryCache.get(imageUrl);
    }

    /**
     * 添加图片到缓存中
     *
     * @param imageUrl
     * @param drawable
     */
    private void addBitmapDrawableToMemoryCache(String imageUrl, BitmapDrawable drawable) {
        if (getBitmapDrawableFromMemoryCache(imageUrl) == null) {
            mMemoryCache.put(imageUrl, drawable);
        }
    }

    /**
     * 获取当前ImageView 的图片下载任务
     *
     * @param imageView
     * @return
     */
    private DownLoadTask getDownLoadTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                return ((AsyncDrawable) drawable).getDownLoadTaskFromAsyncDrawable();
            }
        }
        return null;
    }

    /**
     * 新建一个类 继承BitmapDrawable
     * 目的： BitmapDrawable 和DownLoadTask建立弱引用关联
     */
    class AsyncDrawable extends BitmapDrawable {
        private WeakReference<DownLoadTask> downLoadTaskWeakReference;

        public AsyncDrawable(Resources resources, Bitmap bitmap, DownLoadTask downLoadTask) {
            super(resources, bitmap);
            downLoadTaskWeakReference = new WeakReference<DownLoadTask>(downLoadTask);
        }

        private DownLoadTask getDownLoadTaskFromAsyncDrawable() {
            return downLoadTaskWeakReference.get();
        }
    }

    /**
     * 异步加载图片
     * DownLoadTash 和 ImagaeView建立弱引用关联。
     */
    class DownLoadTask extends AsyncTask<String, Void, BitmapDrawable> {
        String url;
        private WeakReference<ImageView> imageViewWeakReference;

        public DownLoadTask(ImageView imageView) {
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            url = params[0];
            Bitmap bitmap = downLoadBitmap(url);
            BitmapDrawable drawable = null;
            if (bitmap != null) {
                drawable = new BitmapDrawable(mContext.getResources(), bitmap);
                addBitmapDrawableToMemoryCache(url, drawable);
            }
            return drawable;
        }

        /**
         * 验证ImageView 中的下载任务是否相同 如果相同就返回
         *
         * @return
         */
        private ImageView getAttachedImageView() {
            ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                DownLoadTask task = getDownLoadTask(imageView);
                if (this == task) {
                    return imageView;
                }
            }
            return null;
        }

        /**
         * 下载图片 这里使用google 推荐使用的OkHttp
         *
         * @param url
         * @return
         */
        private Bitmap downLoadBitmap(String url) {
            Bitmap bitmap = null;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                //if(response.code()==200){
                bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                //}
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            super.onPostExecute(drawable);
            ImageView imageView = getAttachedImageView();
            if (imageView != null && drawable != null) {
                imageView.setImageDrawable(drawable);
            }
        }


    }
}
