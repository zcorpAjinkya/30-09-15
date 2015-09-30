
package com.zcorp.yielloh.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{

	private static final int DB_VERSION = 5;

	private static final String CREATE_TABLE_PHOTO = "create table IF NOT EXISTS  "
			+ AppConstants.TABLE_NAME_PHOTO
			+ "(id INTEGER primary key AUTOINCREMENT, profile BLOB,cover_photo BLOB);";

	private static final String CREATE_TABLE_INTEREST = "create table IF NOT EXISTS  "
			+ AppConstants.TABLE_NAME_INTEREST
			+ "(id INTEGER primary key AUTOINCREMENT, interest_id TEXT,interest TEXT);";

	/*
	 * private static final String CREATE_TABLE_POSTS1 =
	 * "create table IF NOT EXISTS  " + AppConstants.TABLE_NAME +
	 * "(id INTEGER primary key AUTOINCREMENT, mobile_no TEXT,requirements TEXT,customer_ID TEXT,ordered_ID TEXT,description TEXT,emergency INTEGER,type_of_service TEXT, preferred_time TEXT,location TEXT,graphics BLOB);"
	 * ;
	 */
	public DBHelper(Context context)
	{
		super(context, AppConstants.DATABASE_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// Called when the database is created for the
		// first time. This is where the creation of
		// tables and the initial population of the tables should happen.

		db.execSQL(CREATE_TABLE_PHOTO);
		db.execSQL(CREATE_TABLE_INTEREST);
		/* db.execSQL(CREATE_TABLE_POSTS1); */
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Called when the database needs to be upgraded.
		// The implementation should use this method to
		// drop tables, add tables, or do anything else
		// it needs to upgrade to the new schema version.

		db.execSQL("DROP TABLE IF EXISTS " + AppConstants.TABLE_NAME_PHOTO);

		// Create after dropping
		onCreate(db);
	}

}
