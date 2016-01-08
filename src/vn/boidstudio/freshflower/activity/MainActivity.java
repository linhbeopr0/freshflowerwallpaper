package vn.boidstudio.freshflower.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import vn.boidstudio.freshflower.R;
import vn.boidstudio.freshflower.model.CategoryData;
import vn.boidstudio.freshflower.model.CategoryInfo;
import vn.boidstudio.freshflower.volley.utils.AppController;
import vn.boidstudio.freshflower.volley.utils.MyNetworkImage;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MyNetworkImage[] mImgList;
	private int[] mImgListIdx;
	private RelativeLayout mBtnCategory;
	private TextView mTextBanner;

	private Context mContext;
	private String[] mLoadingImgLink;
	private int SIZE = 0;
	private int ITEM_SIZE = 14;

	private Thread mThread;
	private Handler mHandler;
	public static int mCtLoadingIndex = 0;

	private ImageLoader imageLoader = AppController.getInstance()
			.getImageLoader();

	private StartAppAd startAppAd = new StartAppAd(this);

	public static final String mName = "flowers";

	public static List<String> mData;
	public static int previewCacheItems[];
	private static Typeface Rabanera_shadow_font;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		initData();
		initRes();
		initTimer();
		initListener();
		initAd();
		initLoadingIndex();
	}

	private void initAd() {
		StartAppSDK.init(this, "109866585", "201154763", false);
		this.registerReceiver(this.mConnReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));

	}

	private void initListener() {
		mBtnCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(new Intent(MainActivity.this,
						ListImagesActivity.class));
				startActivity(i);
			}
		});

		for (int i = 0; i < ITEM_SIZE; i++) {
			final int pos = i;
			mImgList[pos].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(new Intent(MainActivity.this,
							ViewPagerActivity.class));

					intent.putExtra("index", mImgListIdx[pos]);
					startActivity(intent);
				}
			});
		}

	}

	private void initTimer() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 99) {
					// Log.d("LINH", "Animate!");
					initAmination();
				}
			}
		};

		mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(60000);
						mHandler.sendEmptyMessage(99);
					}
					// stopThread(mThread);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		mThread.start();
	}

	private void initAmination() {
		final int[] newIndex = randomAllocate(SIZE);
		for (int i = 0; i < ITEM_SIZE; i++) {
			final int pos = i;
			final int idx = newIndex[pos];
			float xPos = mImgList[pos].getWidth();
			float yPos = mImgList[pos].getHeight();
			// Log.d("LINH", "x =" + xPos + " - y =" + yPos);
			AnimationSet am = new AnimationSet(true);
			ScaleAnimation s1 = new ScaleAnimation(0, 1.2f, 0, 1.2f, xPos / 2,
					yPos / 2);
			s1.setDuration(200);
			ScaleAnimation s2 = new ScaleAnimation(1.2f, 0.8f, 1.2f, 0.8f,
					xPos / 2, yPos / 2);
			s2.setDuration(200);
			s2.setStartOffset(200);
			ScaleAnimation s3 = new ScaleAnimation(0.8f, 1.04f, 0.8f, 1.04f,
					xPos / 2, yPos / 2);
			s3.setDuration(200);
			s3.setStartOffset(400);

			am.setFillAfter(true);
			am.addAnimation(s1);
			am.addAnimation(s2);
			am.addAnimation(s3);
			am.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					mImgList[pos].setImageUrl(mData.get(idx), imageLoader);
					mImgListIdx[pos] = idx;
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
				}
			});
			am.setStartOffset(i * 50);
			mImgList[pos].startAnimation(am);
		}

	}

	private void initRes() {
		Rabanera_shadow_font = Typeface.createFromAsset(getAssets(),
				"Rabanera-outline-shadow.ttf");
		mBtnCategory = (RelativeLayout) findViewById(R.id.btnCategory);
		mTextBanner = (TextView) findViewById(R.id.txtBanner);

		mImgListIdx = new int[ITEM_SIZE];
		mImgList = new MyNetworkImage[ITEM_SIZE];
		mImgList[0] = (MyNetworkImage) findViewById(R.id.layout_1_img1);
		mImgList[1] = (MyNetworkImage) findViewById(R.id.layout_1_img2);

		mImgList[2] = (MyNetworkImage) findViewById(R.id.layout_2_img1);
		mImgList[3] = (MyNetworkImage) findViewById(R.id.layout_2_img2);
		mImgList[4] = (MyNetworkImage) findViewById(R.id.layout_2_img3);
		mImgList[5] = (MyNetworkImage) findViewById(R.id.layout_2_img4);
		mImgList[6] = (MyNetworkImage) findViewById(R.id.layout_2_img5);

		mImgList[7] = (MyNetworkImage) findViewById(R.id.layout_3_img1);
		mImgList[8] = (MyNetworkImage) findViewById(R.id.layout_3_img2);
		mImgList[9] = (MyNetworkImage) findViewById(R.id.layout_3_img3);
		mImgList[10] = (MyNetworkImage) findViewById(R.id.layout_3_img4);

		mImgList[11] = (MyNetworkImage) findViewById(R.id.layout_4_img1);
		mImgList[12] = (MyNetworkImage) findViewById(R.id.layout_4_img2);
		mImgList[13] = (MyNetworkImage) findViewById(R.id.layout_4_img3);

		final int[] newIndex = randomAllocate(previewCacheItems.length);

		for (int i = 0; i < ITEM_SIZE; i++) {
			/*
			 * imageLoader.get(mData.get(newIndex[i]), ImageLoader
			 * .getImageListener(mImgList[i], R.drawable.icon_no_img,
			 * R.drawable.icon_no_img), 400, 300);
			 */
			//mImgList[i].setImageUrl(mData.get(newIndex[i]), imageLoader);
			Log.d("LINH", "id = " + previewCacheItems[newIndex[i]]);
			mImgList[i].setImageResource(R.drawable.f0);
			mImgListIdx[i] = newIndex[i];
		}

		mTextBanner.setTypeface(Rabanera_shadow_font);
	}

	private int[] randomAllocate(int size) {
		int result[] = new int[size];
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			tmp.add(i);
		}
		for (int i = 0; i < size; i++) {
			int rdNumber = random(tmp);
			result[i] = rdNumber;
		}
		return result;
	}

	private int random(ArrayList<Integer> arr) {
		int realSize = arr.size() - 1;
		int tmpValue = 0;
		if (realSize > 0) {
			int index = new Random().nextInt(realSize);
			tmpValue = arr.get(index);
			arr.remove(index);
		} else if (realSize == 0) {
			tmpValue = arr.get(0);
		}
		return tmpValue;
	}

	@Override
	protected void onDestroy() {
		// stopThread(thread);
		mThread.interrupt();
		unregisterReceiver(mConnReceiver);
		super.onDestroy();
	}

	private void initData() {
		mData = new ArrayList<String>();
		String[] links = getResources().getStringArray(R.array.flowers);
		for (String link : links) {
			mData.add(link);
		}
		SIZE = mData.size();
		Log.d("LINH", "SIZE = " + SIZE);
		
		previewCacheItems = new int[25];
		for (int i = 0; i < 25; i++) {
			previewCacheItems[i] = getResources().getIdentifier("f" + i, "drawable", getPackageName());
		}
		Log.d("LINH", "id = " + R.drawable.f0 + " ? " + previewCacheItems[0]);
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
			} else {
				Toast.makeText(mContext,
						"Please turn on network connection to download data!",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	private void initLoadingIndex() {
		final int[] newIndex = randomAllocate(10);
		mCtLoadingIndex = newIndex[5];
	}

}
