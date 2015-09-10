package com.smapps.sgbg.activity;

import com.android.volley.toolbox.ImageLoader;
import com.smapps.p1_hdwpp.R;
import com.smapps.sgbg.volley.utils.AppController;
import com.smapps.sgbg.volley.utils.MyNetworkImage;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ViewPagerFragment extends Fragment {

	private Context mContext;
	private Bundle mData;
	private String mLink;
	private MyNetworkImage mFullScreenImg;
	private ImageLoader mImageLoader = AppController.getInstance()
			.getImageLoader();

	public ViewPagerFragment(Context context) {
		mContext = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mData = getArguments();
		mLink = mData.getString("link");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_viewpager_fragment,
				container, false);
		mFullScreenImg = (MyNetworkImage) v.findViewById(R.id.categoryImgView);
		mFullScreenImg.setImageUrl(mLink, mImageLoader);
		mFullScreenImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("LINH", "View pager item clicked!");
				Intent i = new Intent(mContext.getApplicationContext(),
						FullScreenActivity.class);
				i.putExtra("link", mLink);
				startActivity(i);
			}
		});
		return v;
	}
}