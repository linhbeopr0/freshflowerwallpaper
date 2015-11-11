package vn.cbtech.hdwallpaper.activity;

import java.util.ArrayList;

import vn.cbtech.hdwallpaper.adapter.ListImagesAdapter;
import vn.cbtech.hdwallpaper.model.CategoryData;

import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayAdapterView.OnItemClickListener;
import com.jess.ui.TwoWayGridView;

import vn.cbtech.hdwallpaper.R;

import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ListImagesActivity extends Activity {

	public static TwoWayGridView mLstHrzGridView;
	private Context mContext;
	private ListImagesAdapter mLstAdapter;
	private ArrayList<String> mFullData;
	private ArrayList<String> mReducedData;
	private String mTitle;
	private int mLength;

	public static int UPDATE_MESSAGE = 99;
	public int size = 0;
	
	private static int count = 0;
	private StartAppAd startAppAd = new StartAppAd(this);

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == UPDATE_MESSAGE) {
				Log.d("LINH", "UPDATE DATA...!");
				new LoadMoreAsyncTask().execute();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mContext = this;
		setContentView(R.layout.activity_list_images);
		mLstHrzGridView = (TwoWayGridView) findViewById(R.id.lstHrzGrid);
		mTitle = getIntent().getStringExtra("title");
		Log.d("LINH", "title = " + mTitle);
		ArrayList<CategoryData> data = MainActivity.mAllData.get(mTitle);
		mFullData = new ArrayList<String>();

		if (data != null) {
			for (CategoryData tmp : data) {
				mFullData.add(tmp.getthumb());
			}
		}
		mLength = mFullData.size();
		Log.d("LINH", "size = " + size);
		Log.d("LINH", "mLength = " + data.size());
		mReducedData = new ArrayList<String>();
		int newSize = size + 30;
		if (newSize > mLength && mLength > 0) {
			newSize = mLength;
		}
		for (int i = size; i < newSize; i++) {
			mReducedData.add(mFullData.get(i));
		}
		size += 30;
		Log.d("LINH", "mReducedLength = " + mReducedData.size());
		mLstAdapter = new ListImagesAdapter(mContext, mReducedData, mHandler);

		mLstHrzGridView.setAdapter(mLstAdapter);
		mLstHrzGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(TwoWayAdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(ListImagesActivity.this,
						FullScreenActivity.class);
				i.putExtra("title", mTitle);
				i.putExtra("index", position);
				startActivity(i);
			}
		});
		
		initAd();
		Log.d("LINH", "List count = " + count);
	}

	private class LoadMoreAsyncTask extends AsyncTask<Void, Void, Void> {
		Dialog pDialog;

		@Override
		protected void onPreExecute() {
			pDialog = new Dialog(mContext,
					R.style.TransparentProgressDialogWithPngImage);
			View mn = LayoutInflater.from(mContext).inflate(
					R.layout.remove_border_pdialog, null);
			Window window = pDialog.getWindow();
			window.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			window.setBackgroundDrawableResource(R.color.transparent);

			pDialog.setContentView(mn);
			pDialog.setCancelable(false);
			pDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			int newSize = size + 30;
			if (newSize > mLength && mLength > 0) {
				newSize = mLength;
			}
			for (int i = size; i < newSize; i++) {
				mReducedData.add(mFullData.get(i));
			}
			size += 30;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (pDialog.isShowing()) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						pDialog.dismiss();
					}
				}, 1000);

			}
			mLstAdapter.notifyDataSetChanged();
			// pdBar.setVisibility(View.INVISIBLE);
			super.onPostExecute(result);
		}

	}
	
	private void initAd() {
		StartAppSDK.init(this, "109866585", "201154763", false);
		this.registerReceiver(this.mConnReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
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
				if (count != 0 && (count % 3) == 0) {
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

	@Override
	protected void onDestroy() {
		size = 0;
		count++;
		unregisterReceiver(mConnReceiver);
		super.onDestroy();
	}
}
