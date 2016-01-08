//package vn.boidstudio.freshflower.adapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import vn.boidstudio.freshflower.R;
//import vn.boidstudio.freshflower.activity.CategoryActivity;
//import vn.boidstudio.freshflower.model.CategoryData;
//import vn.boidstudio.freshflower.model.CategoryInfo;
//import vn.boidstudio.freshflower.volley.utils.AppController;
//import vn.boidstudio.freshflower.volley.utils.MyNetworkImage;
//
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.ImageLoader.ImageContainer;
//import com.android.volley.toolbox.ImageLoader.ImageListener;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.res.Resources;
//import android.os.Build;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.view.ViewTreeObserver.OnGlobalLayoutListener;
//import android.widget.BaseAdapter;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class CategoryGridAdapter extends BaseAdapter {
//
//	private Context mContext;
//	private ArrayList<CategoryInfo> data;
//
//	// HorzGridView stuff
//	private final int childLayoutResourceId = R.layout.activity_category_item;
//	private int columns;// Used to set childSize in TwoWayGridView
//	private int rows;// used with TwoWayGridView
//	private int itemPadding;
//	private int columnWidth;
//	private int rowHeight;
//	private ImageLoader imageLoader = AppController.getInstance()
//			.getImageLoader();
//
//	public CategoryGridAdapter(Context context, ArrayList<CategoryInfo> data) {
//		this.mContext = context;
//		this.data = data;
//		// Get dimensions from values folders; note that the value will change
//		// based on the device size but the dimension mName will remain the same
//		Resources res = mContext.getResources();
//		itemPadding = (int) res.getDimension(R.dimen.horz_item_padding);
//		rows = 3;
//		columns = 2;
//
//		// Initialize the layout params
//		CategoryActivity.mHrzGridView.setNumRows(rows);
//
//		// HorzGridView size not established yet, so need to set it using a
//		// viewtreeobserver
//		ViewTreeObserver vto = CategoryActivity.mHrzGridView
//				.getViewTreeObserver();
//
//		OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {
//
//			@SuppressWarnings("deprecation")
//			@SuppressLint("NewApi")
//			@Override
//			public void onGlobalLayout() {
//				// First use the gridview height and width to determine child
//				// values
//				rowHeight = (int) ((float) (CategoryActivity.mHrzGridView
//						.getHeight() / rows) - 2 * itemPadding);
//				columnWidth = (int) ((float) (CategoryActivity.mHrzGridView
//						.getWidth() / columns) - 2 * itemPadding);
//
//				CategoryActivity.mHrzGridView.setRowHeight(rowHeight);
//
//				// Then remove the listener
//				ViewTreeObserver vto = CategoryActivity.mHrzGridView
//						.getViewTreeObserver();
//
//				/*
//				 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//				 * { vto.removeOnGlobalLayoutListener(this); } else {
//				 * vto.removeGlobalOnLayoutListener(this); }
//				 */
//				vto.removeGlobalOnLayoutListener(this);
//
//			}
//		};
//
//		vto.addOnGlobalLayoutListener(onGlobalLayoutListener);
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// Get the data for the given position in the array
//		CategoryInfo thisData = data.get(position);
//
//		// Use a viewHandler to improve performance
//		final ViewHandler handler;
//
//		// If reusing a view get the handler info; if view is null, create it
//		if (convertView == null) {
//
//			// Only get the inflater when it's needed, then release it-which
//			// isn't frequently
//			LayoutInflater inflater = (LayoutInflater) mContext
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = inflater
//					.inflate(childLayoutResourceId, parent, false);
//
//			// User findViewById only when first creating the child view
//			handler = new ViewHandler();
//			handler.iv = (MyNetworkImage) convertView
//					.findViewById(R.id.imgView);
//			handler.tv = (TextView) convertView.findViewById(R.id.title);
//			handler.num = (TextView) convertView.findViewById(R.id.numbers);
//			convertView.setTag(handler);
//
//		} else {
//			handler = (ViewHandler) convertView.getTag();
//		}
//
//		if (imageLoader == null) {
//			imageLoader = AppController.getInstance().getImageLoader();
//		}
//
//		// Set the data outside once the handler and view are instantiated
//		handler.iv.setImageUrl(data.get(position).getCoverRes(), imageLoader);
//		handler.tv.setText(refineTitle(data.get(position).getTitle()));
//		handler.num.setText(data.get(position).getNumberOfItems() + "");
//		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(columnWidth,
//				rowHeight);// convertView.getLayoutParams();
//		handler.iv.setLayoutParams(lp);
//
//		Log.d("HorzGVAdapter",
//				"Position:" + position + ",children:" + parent.getChildCount());
//		return convertView;
//	}
//
//	private static class ViewHandler {
//		MyNetworkImage iv;
//		TextView tv;
//		TextView num;
//	}
//
//	@Override
//	public int getCount() {
//
//		return data.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//
//		return data.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//
//		return 0;
//	}
//
//	private String refineTitle(String title) {
//		String result = "";
//		String str[] = title.split("_");
//		if (str.length > 0) {
//			for (String s : str) {
//				result += s.substring(0, 1).toUpperCase();
//				result += s.substring(1);
//				result += " ";
//			}
//		} else {
//			return title;
//		}
//
//		return result;
//	}
//
//}
