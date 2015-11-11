package vn.cbtech.hdwallpaper.activity;

import java.util.ArrayList;

import vn.cbtech.hdwallpaper.R;
import vn.cbtech.hdwallpaper.adapter.CategoryGridAdapter;
import vn.cbtech.hdwallpaper.model.CategoryInfo;

import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayAdapterView.OnItemClickListener;
import com.jess.ui.TwoWayGridView;

import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class CategoryActivity extends Activity {

	public static TwoWayGridView mHrzGridView;
	private Context mContext;
	private String[] mCategoryLinks;
	private ArrayList<CategoryInfo> mData;
	private CategoryGridAdapter mAdapter;
	private static int count = 0;
	private StartAppAd startAppAd = new StartAppAd(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_category);
		mHrzGridView = (TwoWayGridView) findViewById(R.id.hrzGrid);
		initCategoryInfo();
		
		mAdapter = new CategoryGridAdapter(mContext, mData);
		mHrzGridView.setAdapter(mAdapter);
		mHrzGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(TwoWayAdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(CategoryActivity.this,
						ListImagesActivity.class);
				startActivity(i);
			}
		});

		mHrzGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(TwoWayAdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(CategoryActivity.this,
						ListImagesActivity.class);
				String title = mData.get(position).getTitle();
				i.putExtra("title", title);
				startActivity(i);
			}
		});
		
		initAd();
		Log.d("LINH", "Category count = " + count);
	}
	
	private void initAd() {
		StartAppSDK.init(this, "109866585", "201154763", false);
		this.registerReceiver(this.mConnReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}
	
	@Override
	protected void onDestroy() {
		Log.d("LINH", "Destroy!");
		count++;
		unregisterReceiver(mConnReceiver);
		super.onDestroy();
	}

	private void initCategoryInfo() {
		mData = new ArrayList<CategoryInfo>();
		for (int i = 0; i < MainActivity.mAllCtName.length; i++) {
			String title = MainActivity.mAllCtName[i];
			int size = MainActivity.mAllData.get(title).size();
			String coverRes = MainActivity.mAllData.get(title)
					.get(/*new Random().nextInt(size)*/MainActivity.mCtLoadingIndex).getthumb();
			mData.add(new CategoryInfo(title, size, coverRes));
		}
	}
	
	private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			boolean noConnectivity = intent.getBooleanExtra(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
			String reason = intent
					.getStringExtra(ConnectivityManager.EXTRA_REASON);
			boolean isFailover = intent.getBooleanExtra(
					ConnectivityManager.EXTRA_IS_FAILOVER, false);

			NetworkInfo currentNetworkInfo = (NetworkInfo) intent
					.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			NetworkInfo otherNetworkInfo = (NetworkInfo) intent
					.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

			if (currentNetworkInfo.isConnected()) {
				if (count != 0 && (count % 2) == 0) {
					startAppAd.loadAd(new AdEventListener() {

						@Override
						public void onReceiveAd(Ad ad) {
							Log.d("LINH", "Ad received!");
							startAppAd.showAd();
						}

						@Override
						public void onFailedToReceiveAd(Ad ad) {
							Log.d("LINH", "Ad failed");

						}
					});
				}

			} else {
				Toast.makeText(mContext,
						"Please turn on network connection to download data!",
						Toast.LENGTH_LONG).show();
			}
		}
	};
}
