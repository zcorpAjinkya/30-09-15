
package com.zcorp.yielloh;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UpdateProfile extends ActionBarActivity
{

	private ViewPager viewPager;
	private SectionsPagerAdapter myViewPagerAdapter;
	private ArrayList<Integer> listOfItems;

	private LinearLayout dotsLayout;
	private int dotsCount;
	private TextView[] dots;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_profile);

		initViews();
		setViewPagerItemsWithAdapter();
		setUiPageViewController();
	}

	public void setCurrentItem(int item, boolean smoothScroll)
	{
		viewPager.setCurrentItem(item, smoothScroll);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{

		public SectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			Fragment fragment = null;

			switch (position)
			{
			case 0:

				fragment = new ProfilePicture();
				
				break;
			case 1:
				fragment = new CoverPhoto();
				break;
			default:
				break;

			}

			return fragment;

		}

		@Override
		public int getCount()
		{

			return 2;
		}

		
	}

	

	private void setUiPageViewController()
	{
		dotsLayout = (LinearLayout) findViewById(R.id.viewPagerCountDots);
		dotsCount = myViewPagerAdapter.getCount();
		dots = new TextView[dotsCount];

		for (int i = 0; i < dotsCount; i++)
		{
			dots[i] = new TextView(this);
			dots[i].setText(Html.fromHtml("&#8226;"));
			dots[i].setTextSize(30);
			dots[i].setTextColor(getResources().getColor(
					android.R.color.darker_gray));
			dotsLayout.addView(dots[i]);
		}

		dots[0].setTextColor(getResources().getColor(R.color.white));
	}

	private void setViewPagerItemsWithAdapter()
	{
		myViewPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(myViewPagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(viewPagerPageChangeListener);
	}

	// page change listener
	OnPageChangeListener viewPagerPageChangeListener = new OnPageChangeListener()
	{

		@Override
		public void onPageSelected(int position)
		{
			for (int i = 0; i < dotsCount; i++)
			{
				dots[i].setTextColor(getResources().getColor(
						android.R.color.darker_gray));
			}
			dots[position].setTextColor(getResources().getColor(R.color.white));
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{

		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{

		}
	};

	private void initViews()
	{

		getSupportActionBar().hide();

		viewPager = (ViewPager) findViewById(R.id.viewPager);

		listOfItems = new ArrayList<Integer>();
		listOfItems.add(1);
		listOfItems.add(2);
		listOfItems.add(3);
		listOfItems.add(4);
		listOfItems.add(5);
	}

	
}