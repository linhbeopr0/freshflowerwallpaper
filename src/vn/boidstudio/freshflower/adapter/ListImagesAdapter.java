package vn.boidstudio.freshflower.adapter;

import java.util.ArrayList;

import vn.boidstudio.freshflower.R;
import vn.boidstudio.freshflower.activity.ListImagesActivity;
import vn.boidstudio.freshflower.volley.utils.AppController;
import vn.boidstudio.freshflower.volley.utils.MyNetworkImage;
import vn.boidstudio.freshflower.volley.utils.MyNetworkImageWithResponse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.android.volley.toolbox.ImageLoader;

public class ListImagesAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> data;

	// HorzGridView stuff
	private final int childLayoutResourceId = R.layout.activity_list_images_item;
	private int columns;// Used to set childSize in TwoWayGridView
	private int rows;// used with TwoWayGridView
	private int itemPadding;
	private int columnWidth;
	private int rowHeight;
	private ImageLoader imageLoader = AppController.getInstance()
			.getImageLoader();

	private Handler mHandler;
	private int count;
	private int scrolling_count;

	public ListImagesAdapter(Context context, ArrayList<String> data,
			Handler mHandler) {
		this.mContext = context;
		this.data = data;
		this.mHandler = mHandler;
		this.count = 1;
		this.scrolling_count = 0;
		// Get dimensions from values folders; note that the value will change
		// based on the device size but the dimension mName will remain the same
		Resources res = mContext.getResources();
		itemPadding = (int) res.getDimension(R.dimen.horz_item_padding);
		// int[] rowsColumns = res.getIntArray(R.array.horz_gv_rows_columns);
		// rows = rowsColumns[0];
		// columns = rowsColumns[1];
		rows = 3;
		columns = 3;

		// Initialize the layout params
		ListImagesActivity.mLstHrzGridView.setNumRows(rows);

		// HorzGridView size not established yet, so need to set it using a
		// viewtreeobserver
		ViewTreeObserver vto = ListImagesActivity.mLstHrzGridView
				.getViewTreeObserver();

		OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				// First use the gridview height and width to determine child
				// values
				rowHeight = (int) ((float) (ListImagesActivity.mLstHrzGridView
						.getHeight() / rows) - 2 * itemPadding);
				columnWidth = (int) ((float) (ListImagesActivity.mLstHrzGridView
						.getWidth() / columns) - 2 * itemPadding);

				ListImagesActivity.mLstHrzGridView.setRowHeight(rowHeight);
				ListImagesActivity.mLstHrzGridView.setColumnWidth(columnWidth);

				// Then remove the listener
				ViewTreeObserver vto = ListImagesActivity.mLstHrzGridView
						.getViewTreeObserver();

				/*
				 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
				 * { vto.removeOnGlobalLayoutListener(this); } else {
				 * vto.removeGlobalOnLayoutListener(this); }
				 */
				vto.removeGlobalOnLayoutListener(this);

			}
		};

		vto.addOnGlobalLayoutListener(onGlobalLayoutListener);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// Get the data for the given position in the array

		if (position == ((count * 30) - 5)) {
			mHandler.sendEmptyMessage(ListImagesActivity.UPDATE_MESSAGE);
			count++;
			// return null;
		}

		String thisData = data.get(position);

		// Use a viewHandler to improve performance
		final ViewHolder holder;

		// If reusing a view get the handler info; if view is null, create it
		if (convertView == null) {

			// Only get the inflater when it's needed, then release it-which
			// isn't frequently
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(childLayoutResourceId, parent, false);

			// User findViewById only when first creating the child view
			holder = new ViewHolder();
			holder.iv = (MyNetworkImageWithResponse) convertView
					.findViewById(R.id.lstImgView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (imageLoader == null) {
			imageLoader = AppController.getInstance().getImageLoader();
		}

		holder.iv.setImageUrl(data.get(position), imageLoader);
		// Set the data outside once the handler and view are instantiated
		holder.iv
				.setResponseObserver(new MyNetworkImageWithResponse.ResponseObserver() {

					@Override
					public void onSuccess() {
						if (scrolling_count == 0) {
							Log.d("LINH", "success, send message to scroll to "
									+ position);
							Message msg = new Message();
							msg.arg1 = position;
							msg.what = 100;
							mHandler.sendMessage(msg);
							scrolling_count++;
						}
					}

					@Override
					public void onError() {
						Log.d("LINH", "success, send message to scroll to "
								+ position);
						// TODO Auto-generated method stub

					}
				});

		
		// handler.iv.setImageResource(R.drawable.ct1);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(columnWidth,
				rowHeight);// convertView.getLayoutParams();
		holder.iv.setLayoutParams(lp);

		Log.d("HorzGVAdapter",
				"Position:" + position + ",children:" + parent.getChildCount());
		return convertView;
	}

	private static class ViewHolder {
		MyNetworkImageWithResponse iv;
	}

	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public Object getItem(int position) {

		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
