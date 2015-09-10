package com.smapps.sgbg.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.smapps.p1_hdwpp.R;
import com.smapps.sgbg.model.CategoryData;
import com.smapps.sgbg.model.CategoryInfo;
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

	private ImageView[] mImgList;
	private int[] mImgListIdx;
	private RelativeLayout mBtnCategory;
	private TextView mTextBanner;

	private Context mContext;
	private int[] mLoadingImgId;
	private String[] mLoadingImgLink;
	private int SIZE = 0;
	private int ITEM_SIZE = 14;

	private Thread mThread;
	private Handler mHandler;
	public static int mCtLoadingIndex = 0;
	
	private StartAppAd startAppAd = new StartAppAd(this);

	public static final String mAllCtName[] = { "Anime", "Beach", "Christmas",
			"Creative_Graphic", "Cute_Baby", "Flower", "Love", "Nature",
			"Abstract", "Animal_Bird", "Brand_Logo", "Celebration",
			"Celebrities", "Cute_Animal", "Digital_Universe", "Dreamy_Fantasy",
			"Fantasy_Girl", "Game", "Inspirational", "Motorcycle", "Plane",
			"Sport", "Tiger", "Travel_World" };

	public static HashMap<String, ArrayList<CategoryData>> mAllData;
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
						CategoryActivity.class));
				startActivity(i);
			}
		});

		for (int i = 0; i < ITEM_SIZE; i++) {
			final int tmp = i;
			mImgList[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(new Intent(MainActivity.this,
							ViewPagerActivity.class));
					int idx = mImgListIdx[tmp];
					String title = findTitle(idx);
					i.putExtra("title", title);
					startActivity(i);
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
						Thread.sleep(5000);
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

	private void initLoadingImage() {
		SIZE = (mAllCtName.length) * 3;
		Log.d("LINH", "SIZE =" + SIZE);
		mLoadingImgId = new int[SIZE];
		mLoadingImgId[0] = R.drawable.abstract_aqua_clear;
		mLoadingImgId[1] = R.drawable.abstract_pink_abstract_designs;
		mLoadingImgId[2] = R.drawable.abstract_sparkling_design;
		mLoadingImgId[3] = R.drawable.animals_birds_dolphins_in_sea;
		mLoadingImgId[4] = R.drawable.animals_birds_frog;
		mLoadingImgId[5] = R.drawable.animals_birds_pelican_water_bird;
		mLoadingImgId[6] = R.drawable.anime_anime_girl_128;
		mLoadingImgId[7] = R.drawable.anime_anime_girls_28;
		mLoadingImgId[8] = R.drawable.anime_anime_purity;
		mLoadingImgId[9] = R.drawable.beach_mayan_ruins_mexico_beach;
		mLoadingImgId[10] = R.drawable.beach_renesse_beach;
		mLoadingImgId[11] = R.drawable.beach_small_sea_wave_hdtv_1080p;
		mLoadingImgId[12] = R.drawable.brands_logos_blue_windows_7;
		mLoadingImgId[13] = R.drawable.brands_logos_iphone_background;
		mLoadingImgId[14] = R.drawable.brands_logos_windows_glass_feel;
		mLoadingImgId[15] = R.drawable.celebration_2015_christmas_new_year;
		mLoadingImgId[16] = R.drawable.celebration_2015_happy_new_year;
		mLoadingImgId[17] = R.drawable.celebration_halloween_2013;
		mLoadingImgId[18] = R.drawable.celebrities_katrina_kaif_photoshoot;
		mLoadingImgId[19] = R.drawable.celebrities_samantha_song_dance;
		mLoadingImgId[20] = R.drawable.celebrities_victoria_justice_2014;
		mLoadingImgId[21] = R.drawable.christmas_christmas_moon;
		mLoadingImgId[22] = R.drawable.christmas_simple_merry_christmas;
		mLoadingImgId[23] = R.drawable.christmas_xmas_roller_coaster;
		mLoadingImgId[24] = R.drawable.creative_graphic_dragon_wings;
		mLoadingImgId[25] = R.drawable.creative_graphic_nature_sail;
		mLoadingImgId[26] = R.drawable.creative_graphic_warrior_cat;
		mLoadingImgId[27] = R.drawable.cute_animals_blue_eyes_and_blossoms;
		mLoadingImgId[28] = R.drawable.cute_animals_british_shorthair_kitten;
		mLoadingImgId[29] = R.drawable.cute_animals_cute_puppies;
		mLoadingImgId[30] = R.drawable.cute_baby_babies_playing_together;
		mLoadingImgId[31] = R.drawable.cute_baby_cute_little_babies_hq_7;
		mLoadingImgId[32] = R.drawable.cute_baby_cutest_baby_girl;
		mLoadingImgId[33] = R.drawable.digital_universe_planet_fight;
		mLoadingImgId[34] = R.drawable.digital_universe_serenity;
		mLoadingImgId[35] = R.drawable.digital_universe_space_horizon;
		mLoadingImgId[36] = R.drawable.dreamy_fantasy_balloon_flight;
		mLoadingImgId[37] = R.drawable.dreamy_fantasy_lonely_mountains;
		mLoadingImgId[38] = R.drawable.dreamy_fantasy_moscow_dreamland;
		mLoadingImgId[39] = R.drawable.fantasy_girls_fantasy_archer_girl;
		mLoadingImgId[40] = R.drawable.fantasy_girls_fantasy_girl_10;
		mLoadingImgId[41] = R.drawable.fantasy_girls_half_devil_lancer;
		mLoadingImgId[42] = R.drawable.flower_lilac_flowers;
		mLoadingImgId[43] = R.drawable.flower_pink_gerbera_flower;
		mLoadingImgId[44] = R.drawable.flower_purple_flames;
		mLoadingImgId[45] = R.drawable.games_2014_watch_dogs_game;
		mLoadingImgId[46] = R.drawable.games_aiden_pearce;
		mLoadingImgId[47] = R.drawable.games_ciri_in_the_witcher_3_wild_hunt;
		mLoadingImgId[48] = R.drawable.inspirational_blurry_abstract;
		mLoadingImgId[49] = R.drawable.inspirational_sci_fi_twilight;
		mLoadingImgId[50] = R.drawable.inspirational_the_legend_of_zelda_ocarina_of_time;
		mLoadingImgId[51] = R.drawable.love_celebrating_valentines_day;
		mLoadingImgId[52] = R.drawable.love_i_love_you_teddy_bear;
		mLoadingImgId[53] = R.drawable.love_life_nothing_without_love;
		mLoadingImgId[54] = R.drawable.motorcycle_ducati_new_pearl_white_livery;
		mLoadingImgId[55] = R.drawable.motorcycle_honda_cbr_1000rr_2009_yellow;
		mLoadingImgId[56] = R.drawable.motorcycle_yamaha_r6s;
		mLoadingImgId[57] = R.drawable.nature_solar_eclipse;
		mLoadingImgId[58] = R.drawable.nature_sunrise_mountains;
		mLoadingImgId[59] = R.drawable.nature_uphill_road;
		mLoadingImgId[60] = R.drawable.planes_f22_raptor_fly_over;
		mLoadingImgId[61] = R.drawable.planes_f_15e_strike_eagles__b_2_spirit_bomber;
		mLoadingImgId[62] = R.drawable.planes_us_airforce_x_35c;
		mLoadingImgId[63] = R.drawable.sport_2012_london_olympic_games;
		mLoadingImgId[64] = R.drawable.sport_billiards;
		mLoadingImgId[65] = R.drawable.sport_skiing_in_france;
		mLoadingImgId[66] = R.drawable.tigers_sleeping_leopard;
		mLoadingImgId[67] = R.drawable.tigers_snow_leopard;
		mLoadingImgId[68] = R.drawable.tigers_widescreen_tiger;
		mLoadingImgId[69] = R.drawable.travel_world_amazing_canyons;
		mLoadingImgId[70] = R.drawable.travel_world_paris_eiffel_tower;
		mLoadingImgId[71] = R.drawable.travel_world_sunset_in_taipei;
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
					mImgList[pos].setImageResource(mLoadingImgId[idx]);
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
		mImgList = new ImageView[ITEM_SIZE];
		mImgList[0] = (ImageView) findViewById(R.id.layout_1_img1);
		mImgList[1] = (ImageView) findViewById(R.id.layout_1_img2);

		mImgList[2] = (ImageView) findViewById(R.id.layout_2_img1);
		mImgList[3] = (ImageView) findViewById(R.id.layout_2_img2);
		mImgList[4] = (ImageView) findViewById(R.id.layout_2_img3);
		mImgList[5] = (ImageView) findViewById(R.id.layout_2_img4);
		mImgList[6] = (ImageView) findViewById(R.id.layout_2_img5);

		mImgList[7] = (ImageView) findViewById(R.id.layout_3_img1);
		mImgList[8] = (ImageView) findViewById(R.id.layout_3_img2);
		mImgList[9] = (ImageView) findViewById(R.id.layout_3_img3);
		mImgList[10] = (ImageView) findViewById(R.id.layout_3_img4);

		mImgList[11] = (ImageView) findViewById(R.id.layout_4_img1);
		mImgList[12] = (ImageView) findViewById(R.id.layout_4_img2);
		mImgList[13] = (ImageView) findViewById(R.id.layout_4_img3);

		initLoadingImage();

		final int[] newIndex = randomAllocate(SIZE);

		for (int i = 0; i < ITEM_SIZE; i++) {
			int tmp = newIndex[i];
			mImgListIdx[i] = tmp;
			mImgList[i].setImageResource(mLoadingImgId[tmp]);
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
		/*
		 * <string-array name="Beach"></string-array> <string-array
		 * name="Anime"></string-array> <string-array
		 * name="Christmas"></string-array> <string-array
		 * name="Creative_Graphic"></string-array> <string-array
		 * name="Cute Baby"></string-array> <string-array
		 * name="Flower"></string-array> <string-array
		 * name="Love"></string-array> <string-array
		 * name="Nature"></string-array> "Abstract", "Animal_Bird",
		 * "Brand_Logo", "Celebration", "Celebrities", "Cute_Animal",
		 * "Digital_Universe", "Dreamy_Fantasy", "Fantasy_Girl", "Game",
		 * "Inspirational", "Motorcycle", "Plane", "Sport", "Tiger",
		 * "Travel_World"
		 */
		mAllData = new HashMap<String, ArrayList<CategoryData>>();
		String[] Anime, Beach, Christmas, Creative_Graphic, Cute_Baby, Flower, Love, Nature, Abstract, Animal_Bird, Brand_Logo, Celebration, Celebrities, Cute_Animal, Digital_Universe, Dreamy_Fantasy, Fantasy_Girl, Game, Inspirational, Motorcycle, Plane, Sport, Tiger, Travel_World;
		String[] Anime_og, Beach_og, Christmas_og, Creative_Graphic_og, Cute_Baby_og, Flower_og, Love_og, Nature_og, Abstract_og, Animal_Bird_og, Brand_Logo_og, Celebration_og, Celebrities_og, Cute_Animal_og, Digital_Universe_og, Dreamy_Fantasy_og, Fantasy_Girl_og, Game_og, Inspirational_og, Motorcycle_og, Plane_og, Sport_og, Tiger_og, Travel_World_og;
		Anime = getResources().getStringArray(R.array.Anime);
		Anime_og = getResources().getStringArray(R.array.Anime_original);
		Beach = getResources().getStringArray(R.array.Beach);
		Beach_og = getResources().getStringArray(R.array.Beach_original);
		Christmas = getResources().getStringArray(R.array.Christmas);
		Christmas_og = getResources()
				.getStringArray(R.array.Christmas_original);
		Creative_Graphic = getResources().getStringArray(
				R.array.Creative_Graphic);
		Creative_Graphic_og = getResources().getStringArray(
				R.array.Creative_Graphic_original);
		Cute_Baby = getResources().getStringArray(R.array.Cute_Baby);
		Cute_Baby_og = getResources()
				.getStringArray(R.array.Cute_Baby_original);
		Flower = getResources().getStringArray(R.array.Flower);
		Flower_og = getResources().getStringArray(R.array.Flower_original);
		Love = getResources().getStringArray(R.array.Love);
		Love_og = getResources().getStringArray(R.array.Love_original);
		Nature = getResources().getStringArray(R.array.Nature);
		Nature_og = getResources().getStringArray(R.array.Nature_original);

		/*
		 * "Abstract", "Animal_Bird", "Brand_Logo", "Celebration",
		 * "Celebrities", "Cute_Animal", "Digital_Universe", "Dreamy_Fantasy",
		 * "Fantasy_Girl", "Game", "Inspirational", "Motorcycle", "Plane",
		 * "Sport", "Tiger", "Travel_World"
		 */

		Abstract = getResources().getStringArray(R.array.Abstract);
		Abstract_og = getResources().getStringArray(R.array.Abstract_original);
		Animal_Bird = getResources().getStringArray(R.array.Animal_Bird);
		Animal_Bird_og = getResources().getStringArray(
				R.array.Animal_Bird_original);
		Brand_Logo = getResources().getStringArray(R.array.Brand_Logo);
		Brand_Logo_og = getResources().getStringArray(
				R.array.Brand_Logo_original);
		Celebration = getResources().getStringArray(R.array.Celebration);
		Celebration_og = getResources().getStringArray(
				R.array.Celebration_original);
		Celebrities = getResources().getStringArray(R.array.Celebrities);
		Celebrities_og = getResources().getStringArray(
				R.array.Celebrities_original);
		Cute_Animal = getResources().getStringArray(R.array.Cute_Animal);
		Cute_Animal_og = getResources().getStringArray(
				R.array.Cute_Animal_original);
		Digital_Universe = getResources().getStringArray(
				R.array.Digital_Universe);
		Digital_Universe_og = getResources().getStringArray(
				R.array.Digital_Universe_original);
		Dreamy_Fantasy = getResources().getStringArray(R.array.Dreamy_Fantasy);
		Dreamy_Fantasy_og = getResources().getStringArray(
				R.array.Dreamy_Fantasy_original);
		Fantasy_Girl = getResources().getStringArray(R.array.Fantasy_Girl);
		Fantasy_Girl_og = getResources().getStringArray(
				R.array.Fantasy_Girl_original);
		Game = getResources().getStringArray(R.array.Game);
		Game_og = getResources().getStringArray(R.array.Game_original);
		Inspirational = getResources().getStringArray(R.array.Inspirational);
		Inspirational_og = getResources().getStringArray(
				R.array.Inspirational_original);
		Motorcycle = getResources().getStringArray(R.array.Motorcycle);
		Motorcycle_og = getResources().getStringArray(
				R.array.Motorcycle_original);
		Plane = getResources().getStringArray(R.array.Plane);
		Plane_og = getResources().getStringArray(R.array.Plane_original);

		Sport = getResources().getStringArray(R.array.Sport);
		Sport_og = getResources().getStringArray(R.array.Sport_original);
		Tiger = getResources().getStringArray(R.array.Tiger);
		Tiger_og = getResources().getStringArray(R.array.Tiger_original);
		Travel_World = getResources().getStringArray(R.array.Travel_World);
		Travel_World_og = getResources().getStringArray(
				R.array.Travel_World_original);

		// ******************************************************
		ArrayList<CategoryData> tmpArrayList;
		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Anime.length; i++) {
			CategoryData tmp = new CategoryData(Anime_og[i], Anime[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Anime", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Beach.length; i++) {
			CategoryData tmp = new CategoryData(Beach_og[i], Beach[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Beach", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Christmas.length; i++) {
			CategoryData tmp = new CategoryData(Christmas_og[i], Christmas[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Christmas", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Creative_Graphic.length; i++) {
			CategoryData tmp = new CategoryData(Creative_Graphic_og[i],
					Creative_Graphic[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Creative_Graphic", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Cute_Baby.length; i++) {
			CategoryData tmp = new CategoryData(Cute_Baby_og[i], Cute_Baby[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Cute_Baby", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Flower.length; i++) {
			CategoryData tmp = new CategoryData(Flower_og[i], Flower[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Flower", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Love.length; i++) {
			CategoryData tmp = new CategoryData(Love_og[i], Love[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Love", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Nature.length; i++) {
			CategoryData tmp = new CategoryData(Nature_og[i], Nature[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Nature", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Abstract.length; i++) {
			CategoryData tmp = new CategoryData(Abstract_og[i], Abstract[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Abstract", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Animal_Bird.length; i++) {
			CategoryData tmp = new CategoryData(Animal_Bird_og[i],
					Animal_Bird[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Animal_Bird", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Brand_Logo.length; i++) {
			CategoryData tmp = new CategoryData(Brand_Logo_og[i], Brand_Logo[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Brand_Logo", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Celebration.length; i++) {
			CategoryData tmp = new CategoryData(Celebration_og[i],
					Celebration[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Celebration", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Celebrities.length; i++) {
			CategoryData tmp = new CategoryData(Celebrities_og[i],
					Celebrities[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Celebrities", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Cute_Animal.length; i++) {
			CategoryData tmp = new CategoryData(Cute_Animal_og[i],
					Cute_Animal[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Cute_Animal", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Digital_Universe.length; i++) {
			CategoryData tmp = new CategoryData(Digital_Universe_og[i],
					Digital_Universe[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Digital_Universe", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Dreamy_Fantasy.length; i++) {
			CategoryData tmp = new CategoryData(Dreamy_Fantasy_og[i],
					Dreamy_Fantasy[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Dreamy_Fantasy", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Fantasy_Girl.length; i++) {
			CategoryData tmp = new CategoryData(Fantasy_Girl_og[i],
					Fantasy_Girl[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Fantasy_Girl", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Game.length; i++) {
			CategoryData tmp = new CategoryData(Game_og[i], Game[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Game", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Inspirational.length; i++) {
			CategoryData tmp = new CategoryData(Inspirational_og[i],
					Inspirational[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Inspirational", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Motorcycle.length; i++) {
			CategoryData tmp = new CategoryData(Motorcycle_og[i], Motorcycle[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Motorcycle", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Plane.length; i++) {
			CategoryData tmp = new CategoryData(Plane_og[i], Plane[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Plane", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Sport.length; i++) {
			CategoryData tmp = new CategoryData(Sport_og[i], Sport[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Sport", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Tiger.length; i++) {
			CategoryData tmp = new CategoryData(Tiger_og[i], Tiger[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Tiger", tmpArrayList);

		tmpArrayList = new ArrayList<CategoryData>();
		for (int i = 0; i < Travel_World.length; i++) {
			CategoryData tmp = new CategoryData(Travel_World_og[i],
					Travel_World[i]);
			tmpArrayList.add(tmp);
		}
		mAllData.put("Travel_World", tmpArrayList);

		Log.d("LINH", "All data: " + mAllData.size());
	}

	private String findTitle(int idx) {

		/*
		 * "Anime", "Beach", "Christmas", "Creative_Graphic", "Cute_Baby",
		 * "Flower", "Love", "Nature", "Abstract", "Animal_Bird", "Brand_Logo",
		 * "Celebration", "Celebrities", "Cute_Animal", "Digital_Universe",
		 * "Dreamy_Fantasy", "Fantasy_Girl", "Game", "Inspirational",
		 * "Motorcycle", "Plane", "Sport", "Tiger", "Travel_World"
		 */
		String title = "";
		if (idx < 3) {
			title = "Abstract";
		} else if (idx < 6) {
			title = "Animal_Bird";
		} else if (idx < 9) {
			title = "Anime";
		} else if (idx < 12) {
			title = "Beach";
		} else if (idx < 15) {
			title = "Brand_Logo";
		} else if (idx < 18) {
			title = "Celebration";
		} else if (idx < 21) {
			title = "Celebrities";
		} else if (idx < 24) {
			title = "Christmas";
		} else if (idx < 27) {
			title = "Creative_Graphic";
		} else if (idx < 30) {
			title = "Cute_Animal";
		} else if (idx < 33) {
			title = "Cute_Baby";
		} else if (idx < 36) {
			title = "Digital_Universe";
		} else if (idx < 39) {
			title = "Dreamy_Fantasy";
		} else if (idx < 42) {
			title = "Fantasy_Girl";
		} else if (idx < 45) {
			title = "Flower";
		} else if (idx < 48) {
			title = "Game";
		} else if (idx < 51) {
			title = "Inspirational";
		} else if (idx < 54) {
			title = "Love";
		} else if (idx < 57) {
			title = "Motorcycle";
		} else if (idx < 60) {
			title = "Nature";
		} else if (idx < 63) {
			title = "Plane";
		} else if (idx < 66) {
			title = "Sport";
		} else if (idx < 69) {
			title = "Tiger";
		} else if (idx < 72) {
			title = "Travel_World";
		}
		return title;
	}

	// private synchronized void stopThread(Thread thread) {
	// if (thread != null) {
	// thread = null;
	// }
	// }

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
