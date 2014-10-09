package com.linchpin.periodtracker.widget;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;

public class MPTExpendibleListView extends ExpandableListView
{
	private AnimatedExpandableListAdapter adapter;
	public MPTExpendibleListView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}
	public boolean expandGroupWithAnimation(int groupPos) {
        int groupFlatPos = getFlatListPosition(getPackedPositionForGroup(groupPos));
        if (groupFlatPos != -1) {
            int childIndex = groupFlatPos - getFirstVisiblePosition();
            if (childIndex < getChildCount()) {
               
                View v = getChildAt(childIndex);
                if (v.getBottom() >= getBottom()) {
                 
                    adapter.notifyGroupExpanded(groupPos);
                    return expandGroup(groupPos);
                }
            }
        }

        adapter.startExpandAnimation(groupPos, 0);
      
        return expandGroup(groupPos);
    }

    public boolean collapseGroupWithAnimation(int groupPos) {
        int groupFlatPos = getFlatListPosition(getPackedPositionForGroup(groupPos));
        if (groupFlatPos != -1) {
            int childIndex = groupFlatPos - getFirstVisiblePosition();
            if (childIndex >= 0 && childIndex < getChildCount()) {
                // Get the view for the group is it is on screen...
                View v = getChildAt(childIndex);
                if (v.getBottom() >= getBottom()) {
                    // If the user is not going to be able to see the animation
                    // we just collapse the group without an animation.
                    // This resolves the case where getChildView will not be
                    // called if the children of the group is not on screen
                    return collapseGroup(groupPos);
                }
            } else {
                // If the group is offscreen, we can just collapse it without an
                // animation...
                return collapseGroup(groupPos);
            }
        }

}
