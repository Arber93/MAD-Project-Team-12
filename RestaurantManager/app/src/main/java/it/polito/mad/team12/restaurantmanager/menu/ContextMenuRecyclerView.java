package it.polito.mad.team12.restaurantmanager.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;

public class ContextMenuRecyclerView extends RecyclerView {

    private RecyclerViewContextMenuInfo mContextMenuInfo;

    public ContextMenuRecyclerView(Context context) {
        super(context);
    }

    public ContextMenuRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContextMenuRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return mContextMenuInfo;
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        final int longPressPosition = getChildAdapterPosition(originalView);
        getChildViewHolder(originalView);

        if (longPressPosition >= 0) {
            final long longPressId = getAdapter().getItemId(longPressPosition);
            final RecyclerView.ViewHolder longPressViewHolder = getChildViewHolder(originalView);
            mContextMenuInfo = new RecyclerViewContextMenuInfo(longPressPosition, longPressId, longPressViewHolder);
            return super.showContextMenuForChild(originalView);
        }
        return false;
    }

    public static class RecyclerViewContextMenuInfo implements ContextMenu.ContextMenuInfo {
        final private int position;
        final private long id;
        final private RecyclerView.ViewHolder viewHolder;

        public RecyclerViewContextMenuInfo(int position, long id, RecyclerView.ViewHolder viewHolder) {
            this.position = position;
            this.id = id;
            this.viewHolder = viewHolder;
        }

        public int getPosition() { return position; }
        public long getId() { return id; }
        public RecyclerView.ViewHolder getViewHolder() { return viewHolder; }
    }
}