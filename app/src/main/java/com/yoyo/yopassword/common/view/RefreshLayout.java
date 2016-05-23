package com.yoyo.yopassword.common.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.common.config.AppConfig;

/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 * 
 * @author mrsimple
 */
public class RefreshLayout extends SwipeRefreshLayout implements OnScrollListener {

    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop;
    /**
     * listview实例
     */
    private ListView mListView;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadListener mOnLoadListener;

    /**
     * ListView的加载中footer
     */
    private View mListViewFooter;

    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;

    private int limitRefreshSize = -1;

    private RefreshLayoutOnScrollListener refreshLayoutOnScrollListener;

    /**
     * @param context
     */
    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.listview_refresh_footer, null,
                false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // 初始化ListView对象
        if (mListView == null) {
            getListView();
        }
        setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * 获取ListView对象
     */
    private void getListView() {
        int childs = RefreshLayout.this.getChildCount();
       
        if (childs > 0) {
        	
            View childView = RefreshLayout.this.getChildAt(0);
             if (childView instanceof ListView) {
            
                mListView = (ListView) childView;
                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                mListView.setOnScrollListener(this);
               // Log.d(VIEW_LOG_TAG, "### 找到listview");
            }else  if (RefreshLayout.this.getChildAt(1)!=null&&RefreshLayout.this.getChildAt(1) instanceof ListView) {
            
                mListView = (ListView) RefreshLayout.this.getChildAt(1);
                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                mListView.setOnScrollListener(this);
              //  Log.d(VIEW_LOG_TAG, "### 找到listview");
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                // 抬起
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
     * 
     * @return
     */
    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {
        int limitSize = limitRefreshSize;
        if(limitSize==-1){
            limitSize = AppConfig.minPageSize;
        }
        if (mListView != null && mListView.getAdapter() != null&&mListView.getAdapter().getCount()>=limitSize) {
            return mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
        }
        return false;
    }

    /**
     * 是否是上拉操作
     * 
     * @return
     */
   public boolean isPullUp() {
	   //PrintlnOrLog.MyLogE("isPullUp--"+mYDown +"--"+ mLastY+"--"+mTouchSlop);
        return (mYDown - mLastY)>= mTouchSlop&&mLastY>0;
    }
   

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            // 设置状态
            setLoading(true);
            //
            mOnLoadListener.onLoad();
        }
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
    	if(mListView==null){
    		getListView();
    	}
        isLoading = loading;
        if (isLoading) {
            mListView.addFooterView(mListViewFooter);
        } else {
            mListView.removeFooterView(mListViewFooter);
            mYDown = 0;
            mLastY = 0;
        }
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(refreshLayoutOnScrollListener!=null){
            refreshLayoutOnScrollListener.onScrollStateChanged(view,scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        if(refreshLayoutOnScrollListener!=null){
            refreshLayoutOnScrollListener.onScroll(view, firstVisibleItem,visibleItemCount,totalItemCount);
        }

        // 滚动时到了最底部也可以加载更多
        if (canLoad()) {
            loadData();
        }
    }



    public interface  RefreshLayoutOnScrollListener{

        public void onScrollStateChanged(AbsListView view, int scrollState);

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount);
    }


    public RefreshLayoutOnScrollListener getRefreshLayoutOnScrollListener() {
        return refreshLayoutOnScrollListener;
    }

    public void setRefreshLayoutOnScrollListener(RefreshLayoutOnScrollListener refreshLayoutOnScrollListener) {
        this.refreshLayoutOnScrollListener = refreshLayoutOnScrollListener;
    }

    /**
     * 加载更多的监听器
     * 
     * @author mrsimple
     */
    public static interface OnLoadListener {
        public void onLoad();
    }

    public int getLimitRefreshSize() {
        return limitRefreshSize;
    }

    public void setLimitRefreshSize(int limitRefreshSize) {
        this.limitRefreshSize = limitRefreshSize;
    }
}