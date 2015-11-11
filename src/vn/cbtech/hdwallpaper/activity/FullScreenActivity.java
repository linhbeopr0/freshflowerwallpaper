package vn.cbtech.hdwallpaper.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import vn.cbtech.hdwallpaper.volley.utils.AppController;
import vn.cbtech.hdwallpaper.volley.utils.MyNetworkImage;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.ImageLoader.ImageContainer;

import vn.cbtech.hdwallpaper.R;
//import com.startapp.android.publish.Ad;
//import com.startapp.android.publish.AdEventListener;
//import com.startapp.android.publish.StartAppAd;
//import com.startapp.android.publish.StartAppSDK;
//import com.startapp.android.publish.StartAppAd.AdMode;

import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FullScreenActivity extends Activity {

	private LinearLayout mBtnSetWpp;
	private LinearLayout mBtnSave;
	private ProgressBar mPbLoader;
	private MyNetworkImage mImgFullScreenView;
	private ImageLoader mImgLoader;
	private Context mContext;

	private String mTitle;
	private int mIndex;
	private String mLink;
	private static int count = 0;
	private StartAppAd startAppAd = new StartAppAd(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen);
		Intent i = getIntent();
		mTitle = i.getStringExtra("title");
		if (mTitle == null) {
			mLink = i.getStringExtra("link");
		} else {
			mIndex = i.getIntExtra("index", 0);
			mLink = MainActivity.mAllData.get(mTitle).get(mIndex).getlink();
		}
		mLink = refineTo1280(mLink);
		Log.d("LINH", "link = " + mLink);
		mContext = this;
		mBtnSetWpp = (LinearLayout) findViewById(R.id.btnSet);
		mBtnSave = (LinearLayout) findViewById(R.id.btnSave);
		mPbLoader = (ProgressBar) findViewById(R.id.pbLoader);
		mImgFullScreenView = (MyNetworkImage) findViewById(R.id.imgFullscreen);
		mImgLoader = AppController.getInstance().getImageLoader();

		mBtnSetWpp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bitmap bitmap = ((BitmapDrawable) mImgFullScreenView
						.getDrawable()).getBitmap();
				if (bitmap != null) {
					setWpp(bitmap);
				} else {
					Log.d("LINH", "bitmap null");
				}
			}
		});

		mBtnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bitmap bitmap = ((BitmapDrawable) mImgFullScreenView
						.getDrawable()).getBitmap();
				if (bitmap != null) {
					saveToSdCard(bitmap);
				} else {
					// Toast.makeText(mContext, "Saving file failed",
					// Toast.LENGTH_LONG).show();
					Log.d("LINH", "bitmap null!");
				}
			}
		});

		displayFullScreenImage(mLink);

		// StartAppSDK.init(this, "109866585", "209847183", false);
		// StartAppAd.showSlider(this);
		// startAppAd.loadAd(AdMode.AUTOMATIC);
		//
		// if (checkInternetConnection() && (count == 0 || count % 3 == 0)) {
		// StartAppAd.showSplash(this, savedInstanceState);
		// startAppAd.showAd();
		// startAppAd.loadAd();
		// } else {
		// }
		//
		// count++;
		initAd();
		Log.d("LINH", "Full count = " + count);
	}

	private void displayFullScreenImage(String link) {
		// mImgLoader.get(link, new ImageListener() {
		//
		// @Override
		// public void onErrorResponse(VolleyError arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onResponse(ImageContainer response, boolean arg1) {
		// Bitmap bm = response.getBitmap();
		// if (bm != null) {
		// // resize bitmap to full screen
		// float height = bm.getHeight();
		// final float screenWidth = FeedActivity.width;
		// final float screenHeight = FeedActivity.height;
		//
		// float tmpHeight = (float) (0.65 * screenHeight);
		// Log.d("LINH", "height = " + screenHeight + "|maxHeight = "
		// + tmpHeight);
		// if (height > tmpHeight) {
		// height = screenHeight;
		// } else {
		// float factor = (float) height / (float) bm.getWidth();
		// height = factor * (float) screenWidth;
		// }
		// Log.d("LINH", "Final: h = " + height + "|w = "
		// + screenWidth);
		// Bitmap resizedBm = Bitmap.createScaledBitmap(bm,
		// (int) screenWidth, (int) 1920, true);
		// mImgFullScreenView.setImageBitmap(resizedBm);
		// resizedBm = null;
		// }
		//
		// }
		// });

		mImgFullScreenView.setImageUrl(link, mImgLoader);
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	private void setWpp(Bitmap bitmap) {
		// // String imagePath = Environment.getExternalStorageDirectory()
		// // + savingFolderPath + savingfilePath + pos + ".jpg";
		// // File outFile = new File(imagePath);
		Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
		intent.setDataAndType(getImageUri(mContext, bitmap), "image/*");
		intent.putExtra("mimeType", "image/jpg");
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra("theme", 2);
		startActivity(Intent.createChooser(intent, "Select"));
		// try {
		// WallpaperManager wm = WallpaperManager.getInstance(mContext);
		// wm.setBitmap(bitmap);
		// Toast.makeText(mContext, "ok", Toast.LENGTH_SHORT).show();
		// } catch (Exception e) {
		// e.printStackTrace();
		// Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
		// }

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mConnReceiver);
		count++;
		super.onDestroy();
	}

	private void saveToSdCard(Bitmap bitmap) {
		Log.d("LINH", "Saving");
		File myDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"hdwallpaper");

		myDir.mkdirs();
		Random generator = new Random();
		int n = 10000;
		n = generator.nextInt(n);
		String fname = "Wallpaper-" + n + ".jpg";
		File file = new File(myDir, fname);
		Log.d("LINH", "fileName = " + file.getAbsolutePath());
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mContext, "Saving file failed", Toast.LENGTH_LONG)
					.show();
		}

		if (file.exists()) {
			Toast.makeText(mContext, "Saved at " + file.getAbsolutePath(),
					Toast.LENGTH_LONG).show();
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
				if (count == 0 || (count % 4) == 0) {
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

	private String refineTo1280(String link) {
		String tmp = link.replace("wide", "1280x720");
		String tmp2 = tmp.replace("walls", "download");
		return tmp2;
	}
}
