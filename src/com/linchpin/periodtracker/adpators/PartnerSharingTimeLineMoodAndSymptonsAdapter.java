package com.linchpin.periodtracker.adpators;
import java.util.List;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.linchpin.myperiodtracker.R;
import com.linchpin.periodtracker.model.DayDetailModel;
import com.linchpin.periodtracker.model.MoodDataModel;
import com.linchpin.periodtracker.model.PeriodTrackerModelInterface;
import com.linchpin.periodtracker.model.SymptomsModel;
import com.linchpin.periodtracker.utlity.PeriodTrackerConstants;
import com.linchpin.periodtracker.view.AddNoteView;
import com.linchpin.periodttracker.database.AddNoteDBHandler;
public class PartnerSharingTimeLineMoodAndSymptonsAdapter extends BaseAdapter
{
	
		List<PeriodTrackerModelInterface> interfaces;
		String className;
		Context context;
		public PartnerSharingTimeLineMoodAndSymptonsAdapter(List<PeriodTrackerModelInterface> interfaces, String className,
				Context context) {
			// TODO Auto-generated constructor stub
			this.interfaces = interfaces;
			this.className = className;
			this.context = context;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return interfaces.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return interfaces.get(position);
			
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View rowView = convertView;
			ImageView imageView = null;
			RatingBar ratingBar=null;
			if (rowView == null) {
				//AddNoteView addNoteView = (AddNoteView) context;
				//dayDetailModel = addNoteView.dayDetailModel;
			
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.partnersharing_mood_sympton_list_row, null);
			}
			imageView = (ImageView) rowView.findViewById(R.id.listitemonCalendar);
			ratingBar=(RatingBar)rowView.findViewById(R.id.ratingbarmoodsymptons);
				if (className.equals(PeriodTrackerConstants.SYMTOM_BASE_FRAGMENT)) {
				SymptomsModel symptomsModel = (SymptomsModel) interfaces.get(position);
				if (null != symptomsModel.getImageUri()) {
					Uri uri = Uri.parse("android.resource://com.linchpin.myperiodtracker/drawable/"
							+ symptomsModel.getImageUri());
					imageView.setImageURI(uri);
				ratingBar.setRating(2);
				}
			} else {
				MoodDataModel dataModel = (MoodDataModel) interfaces.get(position);
				Uri uri = Uri
						.parse("android.resource://com.linchpin.myperiodtracker/drawable/" + dataModel.getImageUri());
				imageView.setImageURI(uri);
		        ratingBar.setRating(3);
			}
			return rowView;
		}
		
	}
