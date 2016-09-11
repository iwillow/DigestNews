package iwillow.com.gaodemap;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

/**
 * Created by Administrator on 2016/6/3.
 */
public class ExpandableListViewEx extends ExpandableListView implements AbsListView.OnScrollListener {
    private final String TAG = ExpandableListViewEx.class.getSimpleName();

    private View pinHeaderView;
    private OnHeaderUpdateListener onHeaderUpdateListener;
    private int preFirstVisibleGroupPosition = -1;
    private int preFirstVisibleChildPosition = -1;
    private int pinHeaderViewWidth;
    private int pinHeaderViewHeight;

    public ExpandableListViewEx(Context context) {
        super(context);
        initPinHeaderView(context);
    }


    public ExpandableListViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPinHeaderView(context);
    }

    public ExpandableListViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPinHeaderView(context);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandableListViewEx(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPinHeaderView(context);
    }


    private void initPinHeaderView(Context context) {

        setFadingEdgeLength(0);
        setOnScrollListener(this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (pinHeaderView != null) {
            measureChild(pinHeaderView, widthMeasureSpec, heightMeasureSpec);
            pinHeaderViewWidth = pinHeaderView.getMeasuredWidth();
            pinHeaderViewHeight = pinHeaderView.getMeasuredHeight();
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (pinHeaderView != null) {
            int delta = pinHeaderView.getTop();
            pinHeaderView.layout(0, delta, pinHeaderViewWidth, pinHeaderViewHeight + delta);
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (pinHeaderView != null) {
            drawChild(canvas, pinHeaderView, getDrawingTime());
        }
    }

    public View getPinHeaderView() {
        return pinHeaderView;
    }

    public void setOnHeaderUpdateListener(OnHeaderUpdateListener onHeaderUpdateListener) {
        this.onHeaderUpdateListener = onHeaderUpdateListener;
        if (onHeaderUpdateListener == null) {
            pinHeaderView = null;
            pinHeaderViewWidth = pinHeaderViewHeight = 0;
            return;
        }
        pinHeaderView = onHeaderUpdateListener.getPinnedHeader();
        int firstVisiblePos = getFirstVisiblePosition();
        int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
        onHeaderUpdateListener.updatePinnedHeader(pinHeaderView, firstVisibleGroupPos, firstVisiblePos);
        requestLayout();
        postInvalidate();

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (pinHeaderView == null) {
            return;
        }
        int firstVisibleChildPosition = getPackedPositionChild(getExpandableListPosition(firstVisibleItem));
        int firstVisibleGroupPosition = getPackedPositionGroup(getExpandableListPosition(firstVisibleItem));
        if (preFirstVisibleGroupPosition != firstVisibleGroupPosition) {
            preFirstVisibleGroupPosition = firstVisibleGroupPosition;
        }
        if (preFirstVisibleChildPosition != firstVisibleChildPosition) {
            preFirstVisibleChildPosition = firstVisibleChildPosition;
        }


        int nextGroup = getPackedPositionGroup(getExpandableListPosition(firstVisibleItem + 1));
        if (nextGroup == firstVisibleGroupPosition + 1) {
            View nextView = getChildAt(1);
            if (nextView == null) {
                Log.w(TAG, "Warning : refreshHeader getChildAt(1)=null");
                return;
            }
            if (nextView.getTop() <= pinHeaderViewHeight) {
                int delta = pinHeaderViewHeight - nextView.getTop();
                pinHeaderView.layout(0, -delta, pinHeaderViewWidth, pinHeaderViewHeight - delta);
            } else {
                //TODO : note it, when cause bug, remove it
                pinHeaderView.layout(0, 0, pinHeaderViewWidth, pinHeaderViewHeight);
            }

        } else {
            pinHeaderView.layout(0, 0, pinHeaderViewWidth, pinHeaderViewHeight);
        }

        if (onHeaderUpdateListener != null) {
            onHeaderUpdateListener.updatePinnedHeader(getPinHeaderView(), preFirstVisibleGroupPosition, preFirstVisibleChildPosition);
        }
    }

    public interface OnHeaderUpdateListener {
        View getPinnedHeader();

        void updatePinnedHeader(View headerView, int firstVisibleGroupPosition, int firstVisibleChildPosition);
    }


}
