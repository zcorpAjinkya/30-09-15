
package com.zcorp.yielloh;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcorp.yielloh.model.AccountManager;
import com.zcorp.yielloh.model.AppConstants;
import com.zcorp.yielloh.model.DBHelper;
import com.zcorp.yielloh.model.TocDataManager;
import com.zcorp.yielloh.model.TocList;
import com.zcorp.yielloh.model.TocListDataManager;

public class AddInterests extends ActionBarActivity implements
		OnItemClickListener
{

	boolean isFirstViewClick = false;
	boolean isSecondViewClick = false;

	private boolean isTocExpanded;

	private ImageView tocExpand;
	private TocDataManager tocDataManager;
	private TocListDataManager toclistDataManager;
	private TocList toclistAdapter;
	private ListView listToc;
	private ArrayList<String> chapters;
	private ArrayList<Integer> level_indicators;
	private ArrayList<Integer> co_status;
	private ArrayList<Integer> realPositions;
	private ArrayList<Integer> ggparents;
	private ArrayList<Integer> gparents;
	private ArrayList<Integer> parents, child;
	private ArrayList<Boolean> final_clicked;
	private ArrayList<Integer> part_indicator;
	private String[] chapters_clone;
	private int list_len, list_len_1, list_len_2, list_len_3;
	private LoadToc loadToc;
	private int loadat;
	public boolean isnt;
	public int gg_parent, g_parent, parent, level, sumit, sumit1, sumit2,
			sumit3;
	int lasttocpos = 0;
	int currpos = 0;
	int gparentPos, ggparentPos, parentPos, childPos;
	Button btnAdd;
	private AccountManager objectAccount;
	boolean register = false;
	boolean flag = false;
	private Uri fileUri;
	private ProgressDialog mProgressDialog;
	private String strUserName, strEmail, strPassword, filename, image,
			txtProfile, strGender, strDOB, Address, txtGeneralSign,
			access_token, refresh_token, email, ObjectID, txtprofile,
			txtGeneral, avatar_link, cover_photo_link, username, gender,
			date_of_birth, serverEmailId, id, interest;
	private SQLiteDatabase db;
	int interestCount;
	private DBHelper database;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interest_layout);

		tocDataManager = new TocDataManager(AddInterests.this);
		toclistDataManager = new TocListDataManager(AddInterests.this);

		listToc = (ListView) findViewById(R.id.listToc);
		listToc.setOnItemClickListener(this);

		btnAdd = (Button) findViewById(R.id.add);

		loadToc = new LoadToc();
		loadToc.execute(0);

		// Initialise Database
		database = new DBHelper(AddInterests.this);

		db = database.getWritableDatabase();

		Cursor cursor = db.query(AppConstants.TABLE_NAME_INTEREST, null, null,
				null, null, null, null);
		interestCount = cursor.getCount();

		objectAccount = new AccountManager(this);

		btnAdd.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (interestCount == 0)
				{
					if (toclistAdapter.chapter.size() >= 5)
					{
						new AddInterest()
								.execute("http://staging.yielloh.com/add_interests");
					}
					else
					{
						Toast.makeText(AddInterests.this,
								"Please Select minimum 5 topics",
								Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					if (toclistAdapter.chapter.size() >= 0)
					{
						new AddInterest()
								.execute("http://staging.yielloh.com/add_interests");
					}
					else
					{
						Toast.makeText(AddInterests.this,
								"Please Select At least 1 topics",
								Toast.LENGTH_LONG).show();
					}

				}

			}
		});
	}

	private class LoadToc extends AsyncTask<Integer, Void, TocList>
	{
		@Override
		protected TocList doInBackground(Integer... arg0)
		{
			loadat = arg0[0];
			chapters = new ArrayList<String>();
			co_status = new ArrayList<Integer>();
			level_indicators = new ArrayList<Integer>();
			realPositions = new ArrayList<Integer>();
			ggparents = new ArrayList<Integer>();
			gparents = new ArrayList<Integer>();
			parents = new ArrayList<Integer>();
			child = new ArrayList<Integer>();
			final_clicked = new ArrayList<Boolean>();
			part_indicator = new ArrayList<Integer>();
			list_len = tocDataManager.getGreatGrandpaLevels(0);
			chapters_clone = tocDataManager.getFirstLevelList(0);
			for (int i = 0; i < list_len; i++)
			{
				chapters.add(chapters_clone[i]);
				if (tocDataManager.getGrandpaLevels(i, 0) == 0)
				{
					co_status.add(1);
					final_clicked.add(true);
					level_indicators.add(111);

				}
				else
				{
					co_status.add(0); // 0 - expand 1 -collapse
					final_clicked.add(false);
					level_indicators.add(1);
				}

				// 1-first 2=second 3-third 4-forth level indicators
				realPositions.add(i);
				ggparents.add(i);
				gparents.add(-1);
				parents.add(-1);
				child.add(-1);
				part_indicator.add(1);
			}

			toclistAdapter = new TocList(AddInterests.this,
					R.layout.first_list_item, chapters, level_indicators,
					co_status, final_clicked, part_indicator, 0);

			return toclistAdapter;
		}

		@Override
		protected void onPostExecute(TocList result)
		{
			listToc.setAdapter(result);
			try
			{
				switch (loadat)
				{
				case 1:
					switch (level)
					{
					case 1:

						getAllSSubchapters(gg_parent, gg_parent);
						listToc.setSelection(gg_parent);
						break;
					case 2:
						getAllSSubchapters(gg_parent, gg_parent);
						getAllTSubchapters(gg_parent, g_parent, gg_parent
								+ g_parent + 1);
						listToc.setSelection(gg_parent);
						break;
					case 3:
						getAllSSubchapters(gg_parent, gg_parent);
						getAllTSubchapters(gg_parent, g_parent, gg_parent
								+ g_parent + 1);
						getAllFSubchapters(gg_parent, g_parent, parent,
								gg_parent + g_parent + parent + 2);
						listToc.setSelection(gg_parent);
						break;
					}
					listToc.setSelection(gg_parent);
					break;

				case 2:
					getAllSSubchapters(gg_parent, gg_parent);
					list_len_1 = toclistDataManager.getGrandpaLevels(gg_parent,
							0);
					for (int i = 0; i < list_len_1; i++)
					{
						sumit1 = getAllTSubchapters(gg_parent, i, gg_parent + i
								+ sumit + 1);
						sumit += sumit1;
					}
					sumit = 0;
					sumit1 = 0;
					for (int i = 0; i < list_len_1; i++)
					{
						if (i == 4)
						{
							// System.out.println("gachaknar");
						}
						sumit1 = getAllTLength(gg_parent, i, gg_parent + i
								+ sumit + 1);

						isnt = false;
						for (int j = 0; j < sumit1; j++)
						{
							isnt = isAllTwithF(gg_parent, i, gg_parent + i + j
									+ 1 + sumit2);
							if (isnt == false)
							{
								sumit3 = getAllFSubchapters(gg_parent, i, j,
										gg_parent + i + j + 2 + sumit2);
								sumit2 += sumit3;
							}
							else
							{
								sumit3 = getAllFLength(gg_parent, i, j,
										gg_parent + i + j + 2 + sumit2);
								sumit2 += sumit3;
								break;
							}
						}
						if (!isnt)
							sumit2 += sumit1;
						sumit += sumit1;
					}
					listToc.setSelection(gg_parent);
					break;
				case 3: // t part indexed list
					switch (level)
					{
					case 1:
						getAllSTSubchapters(gg_parent, gg_parent);
						listToc.setSelection(gg_parent);
						break;
					case 2:
						getAllSTSubchapters(gg_parent, gg_parent);
						getAllTTSubchapters(gg_parent, g_parent, gg_parent
								+ g_parent + 1);
						listToc.setSelection(gg_parent);
						break;
					case 3:
						getAllSTSubchapters(gg_parent, gg_parent);
						getAllTTSubchapters(gg_parent, g_parent, gg_parent
								+ g_parent + 1);
						getAllFTSubchapters(gg_parent, g_parent, gg_parent
								+ g_parent, gg_parent + g_parent + 1);
						listToc.setSelection(gg_parent);
						break;
					}
					listToc.setSelection(gg_parent);
					break;

				case 4: // t part expand list
					try
					{
						int listLength1, thirdLevelChapters = 0, addAt = 0, tocPosition = 0;
						int fourthLevelChapters = 0;
						int addAtFourth = 0;

						getAllSTSubchapters(gg_parent, gg_parent);
						listLength1 = toclistDataManager.getGrandpaLevels(
								gg_parent, 0);
						// Toast.makeText(getApplicationContext(),
						// "List Lenght"+listLength1,
						// Toast.LENGTH_SHORT).show();
						tocPosition = gg_parent + 1;

						for (int iIndex = 0; iIndex < listLength1; iIndex++)
						{
							thirdLevelChapters = toclistDataManager
									.getParentLevels(gg_parent, iIndex, 0);
							if (thirdLevelChapters == 0)
							{
							}
							else
							{
								getAllTTSubchapters(gg_parent, iIndex, g_parent
										+ iIndex + tocPosition + addAt);
								fourthLevelChapters = 0;

								for (int jIndex = 0; jIndex < thirdLevelChapters; jIndex++)
								{
									fourthLevelChapters = toclistDataManager
											.getChildLevels(gg_parent, iIndex,
													jIndex, 0);
									System.out.println(fourthLevelChapters
											+ "  :: Foruthf");
									if (fourthLevelChapters == 0)
									{
									}
									else
									{
										getAllFTSubchapters(gg_parent, iIndex,
												jIndex, g_parent + iIndex
														+ tocPosition + addAt
														+ jIndex + 1);

										addAt += fourthLevelChapters;
									}

								}
								addAt += thirdLevelChapters;
							}

						}
						listToc.setSelection(gg_parent);
					}
					catch (Exception e)
					{
						listToc.setSelection(gg_parent);
						// TODO: handle exception
					}
					break;

				}
			}
			catch (Exception ex)
			{
				listToc.setSelection(gg_parent);
			}
		}
	}

	public boolean checkChapterParts(String chapter, String chapter_1)
	{
		chapter = giveChapterParts(chapter)[1];
		chapter_1 = giveChapterParts(chapter_1)[1];
		if (chapter.equals(chapter_1))
			return true;
		return false;
	}

	private String opchapter;
	private String[] chap_parts = new String[2];

	public String[] giveChapterParts(String chapter)
	{
		opchapter = chapter;
		chap_parts[0] = opchapter.substring(0, opchapter.indexOf(" ") + 1);
		chap_parts[1] = opchapter.substring(opchapter.indexOf(" ") + 1,
				opchapter.length());
		return chap_parts;
	}

	public int getAllSSubchapters(int real_position, int addat_position)
	{
		int addat = addat_position;
		chapters_clone = tocDataManager.getSecondLevelList(real_position, 0);
		list_len_1 = tocDataManager.getGrandpaLevels(real_position, 0);
		co_status.set(addat, 1);
		for (int i = 0; i < list_len_1; i++)
		{
			addat++;
			chapters.add(addat, chapters_clone[i]);
			if (tocDataManager.getParentLevels(real_position, i, 0) == 0)
			{
				co_status.add(addat, 1);
				final_clicked.add(addat, true);
				if (real_position == 1)
					level_indicators.add(addat, 4); // 1-first 2=second 3-third
													// 4-forth level indicators
				else
					level_indicators.add(addat, 2); // 1-first 2=second 3-third
													// 4-forth level indicators
				realPositions.add(addat, 0);
				ggparents.add(addat, real_position);
				gparents.add(addat, i);
				parents.add(addat, 0);
				part_indicator.add(addat, 0);
			}
			else
			{
				co_status.add(addat, 0); // 0 - closed 1 - open
				final_clicked.add(addat, false);
				if (real_position == 1)
					level_indicators.add(addat, 4); // 1-first 2=second 3-third
													// 4-forth level indicators
				else
					level_indicators.add(addat, 2);
				realPositions.add(addat, i);
				ggparents.add(addat, real_position);
				gparents.add(addat, -1);
				parents.add(addat, -1);
				part_indicator.add(addat, 0);
			}
		}
		return list_len_1;
	}

	public int getAllTSubchapters(int gg_parent_position, int real_position,
			int addat_position)
	{

		int addat = addat_position;
		chapters_clone = tocDataManager.getThirdLevelList(gg_parent_position,
				real_position, 0);
		list_len_2 = tocDataManager.getParentLevels(gg_parent_position,
				real_position, 0);
		if (list_len_2 == 1)
		{
			if (checkChapterParts(chapters.get(addat), chapters_clone[0]))
			{
				list_len_2 = getAllFSubchapters(gg_parent_position,
						real_position, 0, addat);
			}
			else
			{
				co_status.set(addat, 1);
				for (int i = 0; i < list_len_2; i++)
				{
					addat++;
					chapters.add(addat, chapters_clone[i]);
					if (tocDataManager.getChildLevels(gg_parent_position,
							real_position, i, 0) == 0)
					{
						co_status.add(addat, 1);
						final_clicked.add(addat, true);
					}
					else
					{
						co_status.add(addat, 0); // 0 - closed 1 - open
						final_clicked.add(addat, false);
					}
					level_indicators.add(addat, 3); // 1-first 2=second 3-third
													// 4-forth level indicators
					realPositions.add(addat, i);
					ggparents.add(addat, gg_parent_position);
					gparents.add(addat, real_position);
					parents.add(addat, -1);
					final_clicked.add(addat, false);
					part_indicator.add(addat, 0);
				}
			}
		}
		else
		{
			co_status.set(addat, 1);
			for (int i = 0; i < list_len_2; i++)
			{
				addat++;
				chapters.add(addat, chapters_clone[i]);
				if (tocDataManager.getChildLevels(gg_parent_position,
						real_position, i, 0) == 0)
				{
					co_status.add(addat, 1);
					final_clicked.add(addat, true);
				}
				else
				{
					co_status.add(addat, 0); // 0 - closed 1 - open
					final_clicked.add(addat, false);
				}
				level_indicators.add(addat, 3); // 1-first 2=second 3-third
												// 4-forth level indicators
				realPositions.add(addat, i);
				ggparents.add(addat, gg_parent_position);
				gparents.add(addat, real_position);
				parents.add(addat, -1);
				part_indicator.add(addat, 0);
			}
		}
		return list_len_2;
	}

	public int getAllFSubchapters(int gg_parent_position,
			int g_parent_position, int real_position, int addat_position)
	{
		int addat = addat_position;
		chapters_clone = tocDataManager.getFourthLevelList(gg_parent_position,
				g_parent_position, real_position, 0);
		list_len_3 = tocDataManager.getChildLevels(gg_parent_position,
				g_parent_position, real_position, 0);
		co_status.set(addat, 1);
		for (int i = 0; i < list_len_3; i++)
		{
			addat++;
			chapters.add(addat, chapters_clone[i]);
			co_status.add(addat, 0); // 0 - closed 1 - open
			if (is4G(gg_parent_position, g_parent_position, real_position))
				level_indicators.add(addat, 41);
			else
				level_indicators.add(addat, 4); // 1-first 2=second 3-third
												// 4-forth level indicators
			realPositions.add(addat, i);
			ggparents.add(addat, gg_parent_position);
			gparents.add(addat, g_parent_position);
			parents.add(addat, real_position);
			final_clicked.add(addat, true);
			part_indicator.add(addat, 0);
		}
		return list_len_3;
	}

	public int getAllTLength(int gg_parent_position, int real_position,
			int addat_position)
	{
		int addat = addat_position;
		chapters_clone = tocDataManager.getThirdLevelList(gg_parent_position,
				real_position, 0);
		list_len_2 = tocDataManager.getParentLevels(gg_parent_position,
				real_position, 0);
		if (list_len_2 == 1)
		{
			if (checkChapterParts(chapters.get(addat), chapters_clone[0]))
			{
				list_len_2 = getAllFLength(gg_parent_position, real_position,
						0, addat);
			}
		}
		return list_len_2;
	}

	public int getAllFLength(int gg_parent_position, int g_parent_position,
			int real_position, int addat_position)
	{
		chapters_clone = tocDataManager.getFourthLevelList(gg_parent_position,
				g_parent_position, real_position, 0);
		list_len_3 = tocDataManager.getChildLevels(gg_parent_position,
				g_parent_position, real_position, 0);
		return list_len_3;
	}

	public boolean isAllTwithF(int gg_parent_position, int real_position,
			int addat_position)
	{
		int addat = addat_position;
		chapters_clone = tocDataManager.getThirdLevelList(gg_parent_position,
				real_position, 0);
		list_len_2 = tocDataManager.getParentLevels(gg_parent_position,
				real_position, 0);
		if (list_len_2 == 1)
		{
			System.out.println("checking->" + chapters.get(addat) + "=="
					+ chapters_clone[0]);
			if (checkChapterParts(chapters.get(addat), chapters_clone[0]))
			{
				list_len_2 = getAllFLength(gg_parent_position, real_position,
						0, addat);
				System.out.println("returning true");
				return true;
			}
		}
		return false;
	}

	public int getAllSTSubchapters(int real_position, int addat_position)
	{
		int addat = addat_position;
		chapters_clone = tocDataManager.getSecondLevelList(real_position, 0);
		list_len_1 = tocDataManager.getGrandpaLevels(real_position, 0);
		co_status.set(addat, 1);
		for (int i = 0; i < list_len_1; i++)
		{
			addat++;
			chapters.add(addat, chapters_clone[i]);
			if (tocDataManager.getParentLevels(real_position, i, 0) == 0)
			{
				co_status.add(addat, 1);
				final_clicked.add(addat, true);

				level_indicators.add(addat, 2); // 1-first 2=second 3-third
												// 4-forth level indicators
				realPositions.add(addat, i);
				ggparents.add(addat, real_position);
				gparents.add(addat, i);
				parents.add(addat, 0);
				child.add(addat, 0);
				part_indicator.add(addat, 1);
			}
			else
			{
				co_status.add(addat, 0); // 0 - closed 1 - open
				final_clicked.add(addat, false);
				level_indicators.add(addat, 2);
				realPositions.add(addat, i);
				ggparents.add(addat, real_position);
				gparents.add(addat, i);
				parents.add(addat, i);
				child.add(addat, 0);
				part_indicator.add(addat, 1);
			}
		}
		return list_len_1;
	}

	public int getAllTTSubchapters(int gg_parent_position, int real_position,
			int addat_position)
	{
		int addat = addat_position;
		chapters_clone = tocDataManager.getThirdLevelList(gg_parent_position,
				real_position, 0);
		list_len_2 = tocDataManager.getParentLevels(gg_parent_position,
				real_position, 0);
		co_status.set(addat, 1);
		for (int i = 0; i < list_len_2; i++)
		{
			addat++;
			chapters.add(addat, chapters_clone[i]);
			if (tocDataManager.getChildLevels(gg_parent_position,
					real_position, i, 0) == 0)
			{
				co_status.add(addat, 1); // 0 - closed 1 - open
				final_clicked.add(addat, true);

				level_indicators.add(addat, 3);
				realPositions.add(addat, i);
				ggparents.add(addat, gg_parent_position);
				gparents.add(addat, real_position);
				parents.add(addat, i);
				child.add(addat, 0);
				part_indicator.add(addat, 1);
			}
			else
			{
				co_status.add(addat, 0); // 0 - closed 1 - open
				final_clicked.add(addat, false);
				level_indicators.add(addat, 3);
				realPositions.add(addat, i);
				ggparents.add(addat, gg_parent_position);
				gparents.add(addat, real_position);
				parents.add(addat, i);
				child.add(addat, 0);
				part_indicator.add(addat, 1);
			}
		}
		return list_len_2;
	}

	public int getAllFTSubchapters(int gg_parent_position,
			int g_parent_position, int real_position, int addat_position)
	{
		int addat = addat_position;
		chapters_clone = tocDataManager.getFourthLevelList(gg_parent_position,
				g_parent_position, real_position, 0);
		list_len_3 = tocDataManager.getChildLevels(gg_parent_position,
				g_parent_position, real_position, 0);
		co_status.set(addat, 1);
		for (int i = 0; i < list_len_3; i++)
		{
			addat++;
			chapters.add(addat, chapters_clone[i]);
			co_status.add(addat, 1); // 0 - closed 1 - open
			final_clicked.add(addat, true);
			level_indicators.add(addat, 4);
			realPositions.add(addat, i);
			ggparents.add(addat, gg_parent_position);
			gparents.add(addat, g_parent_position);
			parents.add(addat, real_position);
			child.add(addat, i);
			part_indicator.add(addat, 1);
		}
		return list_len_3;
	}

	public void removeAllSTSubchapters(int real_position,
			int removefrom_position)
	{
		int remove_at = removefrom_position;
		list_len_1 = tocDataManager.getGrandpaLevels(real_position, 0);
		co_status.set(remove_at, 0);

		for (int i = 0; i < list_len_1; i++)
		{
			System.out.println("Removing at->" + i);
			remove_at++;
			switch (co_status.get(remove_at))
			{
			case 1:
				removeAllTTSubchapters(ggparents.get(remove_at),
						realPositions.get(remove_at), remove_at);
				chapters.remove(remove_at);
				co_status.remove(remove_at);
				level_indicators.remove(remove_at);
				realPositions.remove(remove_at);
				ggparents.remove(remove_at);
				gparents.remove(remove_at);
				parents.remove(remove_at);
				child.remove(remove_at);
				final_clicked.remove(remove_at);
				part_indicator.remove(remove_at);
				break;
			case 0:
				chapters.remove(remove_at);
				co_status.remove(remove_at);
				level_indicators.remove(remove_at);
				realPositions.remove(remove_at);
				ggparents.remove(remove_at);
				gparents.remove(remove_at);
				parents.remove(remove_at);
				child.remove(remove_at);
				final_clicked.remove(remove_at);
				part_indicator.remove(remove_at);
				break;
			}
			remove_at--;
		}
	}

	public void removeAllTTSubchapters(int gg_parent_position,
			int real_position, int removefrom_position)
	{
		int remove_at = removefrom_position;
		list_len_2 = tocDataManager.getParentLevels(gg_parent_position,
				real_position, 0);
		System.out.println("parent levels:" + list_len);
		co_status.set(remove_at, 0);
		for (int i = 0; i < list_len_2; i++)
		{
			remove_at++;
			switch (co_status.get(remove_at))
			{
			case 0:
				// System.out.println("thirdlevel->removed - c ->"+chapters.get(remove_at));
				chapters.remove(remove_at);
				co_status.remove(remove_at);
				level_indicators.remove(remove_at);
				realPositions.remove(remove_at);
				ggparents.remove(remove_at);
				gparents.remove(remove_at);
				parents.remove(remove_at);
				child.add(remove_at);
				final_clicked.remove(remove_at);
				part_indicator.remove(remove_at);
				break;
			case 1:
				removeAllFTSubchapters(ggparents.get(remove_at),
						gparents.get(remove_at), realPositions.get(remove_at),
						remove_at);
				chapters.remove(remove_at);
				co_status.remove(remove_at);
				level_indicators.remove(remove_at);
				realPositions.remove(remove_at);
				ggparents.remove(remove_at);
				gparents.remove(remove_at);
				parents.remove(remove_at);
				child.add(remove_at);
				final_clicked.remove(remove_at);
				part_indicator.remove(remove_at);
				break;
			}
			remove_at--;
		}
	}

	public void removeAllFTSubchapters(int gg_parent_position,
			int g_parent_position, int real_position, int removefrom_position)
	{
		int remove_at = removefrom_position;
		list_len_3 = tocDataManager.getChildLevels(gg_parent_position,
				g_parent_position, real_position, 0);
		System.out.println("parent levels:" + list_len);
		co_status.set(remove_at, 0);
		for (int i = 0; i < list_len_3; i++)
		{
			remove_at++;
			switch (co_status.get(remove_at))
			{
			case 0:
				chapters.remove(remove_at);
				co_status.remove(remove_at);
				level_indicators.remove(remove_at);
				realPositions.remove(remove_at);
				ggparents.remove(remove_at);
				gparents.remove(remove_at);
				parents.remove(remove_at);
				child.remove(remove_at);
				final_clicked.remove(remove_at);
				part_indicator.remove(remove_at);
				break;
			case 1:
				chapters.remove(remove_at);
				co_status.remove(remove_at);
				level_indicators.remove(remove_at);
				realPositions.remove(remove_at);
				ggparents.remove(remove_at);
				gparents.remove(remove_at);
				parents.remove(remove_at);
				child.remove(remove_at);
				final_clicked.remove(remove_at);
				part_indicator.remove(remove_at);
				break;
			}
			remove_at--;
		}
	}

	public void loadListAt(int gg_parent, int g_parent, int parent, int level,
			int loadat)
	{
		this.gg_parent = gg_parent;
		this.g_parent = g_parent;
		this.parent = parent;
		this.level = level;
		loadToc = new LoadToc();
		loadToc.execute(loadat);
	}

	int co_id, level_id, part;

	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3)
	{

		TextView label = (TextView) v.getTag(R.id.txtChapter);
		CheckBox checkbox = (CheckBox) v.getTag(R.id.imgNavigator1);

		part = part_indicator.get(position);
		System.out.println(part);
		switch (arg0.getId())
		{
		case R.id.listToc:
			switch (part)
			{
			case 1:
				if (!final_clicked.get(position))
				{
					level_id = level_indicators.get(position);

					switch (level_id)
					{
					case 1:
						co_id = co_status.get(position);
						if (co_id == 0)
						{
							getAllSTSubchapters(realPositions.get(position),
									position);
							toclistAdapter.notifyDataSetChanged();
							toclistAdapter.notifyDataSetInvalidated();

						}
						else
						{
							removeAllSTSubchapters(realPositions.get(position),
									position);
							toclistAdapter.notifyDataSetChanged();
							toclistAdapter.notifyDataSetInvalidated();
							if (isTocExpanded)
							{
								tocExpand.setImageResource(R.drawable.xclose);
								isTocExpanded = false;
							}

						}
						listToc.setSelection(position);
						break;
					case 2:
						co_id = co_status.get(position);
						if (co_id == 0)
						{
							getAllTTSubchapters(ggparents.get(position),
									realPositions.get(position), position);
							toclistAdapter.notifyDataSetChanged();
							toclistAdapter.notifyDataSetInvalidated();

						}
						else
						{
							removeAllTTSubchapters(ggparents.get(position),
									realPositions.get(position), position);
							toclistAdapter.notifyDataSetChanged();
							toclistAdapter.notifyDataSetInvalidated();

						}

						break;
					case 3:
						co_id = co_status.get(position);
						if (co_id == 0)
						{
							getAllFTSubchapters(ggparents.get(position),
									gparents.get(position),
									realPositions.get(position), position);
							toclistAdapter.notifyDataSetChanged();
							toclistAdapter.notifyDataSetInvalidated();

						}
						else
						{
							removeAllFTSubchapters(ggparents.get(position),
									gparents.get(position),
									realPositions.get(position), position);
							toclistAdapter.notifyDataSetChanged();
							toclistAdapter.notifyDataSetInvalidated();
						}

						break;
					}
				}
				else
				{

					ggparentPos = ggparents.get(position);
					if (ggparentPos != 0)
						ggparentPos -= 1;

					/*
					 * if(ggparentPos==2) ggparentPos-=1;
					 */
					if (gparents.get(position) == -1)
						gparentPos = 0;
					else
						gparentPos = gparents.get(position);
					if (parents.get(position) == -1)
						parentPos = 0;
					else
						parentPos = parents.get(position);
					if (child.get(position) == -1)
						childPos = 0;
					else
						childPos = child.get(position);

				}
				break;
			}
			break;
		}
	}

	private boolean is4g;

	public boolean is4G(int gg_parent, int g_parent, int real_position)
	{
		is4g = false;
		return is4g;
	}

	private class AddInterest extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			mProgressDialog = ProgressDialog
					.show(AddInterests.this, null, null);
			if (!mProgressDialog.isShowing())
			{

				// Set progressBar to preogress dialog
				mProgressDialog.setContentView(R.layout.progress_bar_layout);
				mProgressDialog.setCancelable(false);
				// Show progressdialog
				mProgressDialog.show();
			}

		}

		@Override
		protected String doInBackground(String... urls)
		{

			return INTEREST(urls[0]);
		}

		@Override
		protected void onPostExecute(String result)
		{

			if ((email != null) && !email.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(AddInterests.this, "Email Id is already taken",
						Toast.LENGTH_LONG).show();
				email = "";
			}
			if ((txtprofile != null) && !txtprofile.matches(""))
			{
				mProgressDialog.dismiss();
				Toast.makeText(AddInterests.this, "User Name is already taken",
						Toast.LENGTH_LONG).show();
				txtprofile = "";
			}
			if ((txtGeneral != null) && !txtGeneral.matches(""))
			{
				mProgressDialog.dismiss();

				if (txtGeneral.matches("invalid_token"))
				{
					new HttpAsyncTask()
							.execute("http://staging.yielloh.com/oauth/token/");
				}
				else
				{
					Toast.makeText(AddInterests.this, txtGeneral,
							Toast.LENGTH_LONG).show();
				}
				txtGeneral = "";
			}
			if (register)
			{
				mProgressDialog.dismiss();
				register = false;
				Log.e("Result", result);
				finish();

			}

		}
	}

	public String INTEREST(String url)
	{

		String result = "";
		try
		{

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);

			String user1 = "";

			// 3. build jsonObject
			JSONArray arrayList = new JSONArray();

			for (int i = 0; i < toclistAdapter.chapter.size(); i++)
			{
				arrayList.put(toclistAdapter.chapter.get(i));
			}

			JSONObject jsonObject = new JSONObject();

			jsonObject.put("interests", arrayList);

			// 4. convert JSONObject to JSON to String
			user1 = jsonObject.toString();

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(user1);

			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			httpPost.setHeader("Accept", "application/vnd.yielloh.v1");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Authorization",
					"Bearer " + objectAccount.getAccessToken());
			httpPost.setHeader("Content-type", "application/json");

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			String res = EntityUtils.toString(httpResponse.getEntity());

			Log.e("Response", res);

			Log.e("Register", res);

			if (res.toLowerCase().contains("errors"))
			{
				JSONObject myObject = new JSONObject(res);

				JSONObject object = myObject.getJSONObject("errors");

				register = false;
				if (object.has("profile.username") && (object.has("email")))
				{
					txtGeneral = "Email and User Name has already taken";
				}
				else if (object.has("email"))
				{
					email = object.getString("email");
				}
				else if (object.has("profile.username"))
				{
					txtprofile = object.getString("profile.username");
				}

			}
			else if (res.toLowerCase().contains("error"))
			{
				JSONObject myObject = new JSONObject(res);

				txtGeneral = myObject.getString("error");
			}
			else
			{
				register = true;

				JSONObject myObject = new JSONObject(res);

				JSONArray array = myObject.getJSONArray("interest_list");
				if (array != null)
				{
					db.delete(AppConstants.TABLE_NAME_INTEREST, null, null);
					for (int i = 0; i < array.length(); i++)
					{

						interest = array.getString(i);

						ContentValues values = new ContentValues();

						values.put("interest", interest);

						db.insert(AppConstants.TABLE_NAME_INTEREST, null,
								values);

					}
				}

			}
		}
		catch (Exception e)
		{

			Log.e("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			mProgressDialog = ProgressDialog
					.show(AddInterests.this, null, null);

			if (!mProgressDialog.isShowing())
			{
				// Set progressBar to preogress dialog
				mProgressDialog.setContentView(R.layout.progress_bar_layout);
				mProgressDialog.setCancelable(false);
				// Show progressdialog
				mProgressDialog.show();
			}

		}

		@Override
		protected String doInBackground(String... urls)
		{

			return POST(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result)
		{

			if ((txtGeneral != null) && !txtGeneral.matches(""))
			{
				mProgressDialog.dismiss();

				txtGeneral = "";
			}
			if (flag)

			{
				mProgressDialog.dismiss();
				objectAccount.setAccessToken(access_token);
				objectAccount.setRefreshToken(refresh_token);

				flag = false;
				new AddInterest()
						.execute("http://staging.yielloh.com/add_interests");

			}

		}
	}

	public String POST(String url)
	{
		InputStream inputStream = null;
		String result = "";
		try
		{

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);

			String user1 = "";

			// 3. build jsonObject

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("grant_type", "refresh_token");
			jsonObject.put("refresh_token", objectAccount.getRefreshToken());

			jsonObject.put("client_id", AppConstants.client_Id);
			jsonObject.put("client_secret", AppConstants.client_secrete);

			// 4. convert JSONObject to JSON to String
			user1 = jsonObject.toString();

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(user1);

			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			httpPost.setHeader("Accept", "application/vnd.yielloh.v1");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			String res = EntityUtils.toString(httpResponse.getEntity());

			Log.e("Response", res);

			if (res.toLowerCase().contains("error"))
			{
				JSONObject myObject = new JSONObject(res);

				flag = false;
				if (myObject.has("error"))
				{
					txtGeneral = myObject.getString("error");

					Log.e("Erro", txtGeneral);

				}

			}
			else
			{
				flag = true;
				JSONObject myObject = new JSONObject(res);
				if (myObject.has("access_token"))
				{
					access_token = myObject.getString("access_token");
					refresh_token = myObject.getString("refresh_token");

				}
			}

		}
		catch (Exception e)
		{
			Log.e("InputStream", "" + e);
		}

		// 11. return result
		return result;
	}

}
