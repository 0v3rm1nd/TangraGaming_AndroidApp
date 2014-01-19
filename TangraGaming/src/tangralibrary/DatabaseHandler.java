package tangralibrary;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "tangra";

	// User table name
	private static final String TABLE_LOGIN = "user";
	private static final String TABLE_MAINROOM = "mainroom";

	// User Table Columns names
	private static final String KEY_EMAIL = "email";
	private static final String KEY_NICKNAME = "nickname";
	private static final String KEY_NAME = "name";
	private static final String KEY_SIGNATURE = "signature";
	private static final String KEY_HOBBY = "hobby";
	private static final String KEY_BIRTHDAY = "birthday";
	private static final String KEY_GENDER = "gender";
	private static final String KEY_DATECREATED = "datecreated";
	private static final String KEY_DATEUPDATED = "dateupdated";
	private static final String KEY_LASTLOGIN = "lastlogin";
	private static final String KEY_LOCATION = "location";

	// MainRoom Table Columns names
	private static final String KEY_MAINROOM_NAME = "name";
	private static final String KEY_MAINROOM_DATECREATED = "datecreated";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_EMAIL + " TEXT PRIMARY KEY," + KEY_NICKNAME
				+ " TEXT UNIQUE," + KEY_NAME + " TEXT," + KEY_SIGNATURE
				+ " TEXT," + KEY_HOBBY + " TEXT," + KEY_BIRTHDAY + " TEXT,"
				+ KEY_GENDER + " TEXT," + KEY_DATECREATED + " TEXT,"
				+ KEY_DATEUPDATED + " TEXT," + KEY_LASTLOGIN + " TEXT,"
				+ KEY_LOCATION + " TEXT" + ")";
		String CREATE_MAINROOM_TABLE = "CREATE TABLE " + TABLE_MAINROOM + "("
				+ KEY_MAINROOM_NAME + " TEXT PRIMARY KEY,"
				+ KEY_MAINROOM_DATECREATED + " TEXT" + ")";

		db.execSQL(CREATE_LOGIN_TABLE);
		db.execSQL(CREATE_MAINROOM_TABLE);

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAINROOM);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String email, String nickname, String name,
			String location, String gender, String birthday, String hobby,
			String datecreated, String dateupdated, String lastlogin) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_NICKNAME, nickname); // Nickname
		values.put(KEY_NAME, name); // Name
		values.put(KEY_LOCATION, location); // Location
		values.put(KEY_GENDER, gender); // Gender
		values.put(KEY_BIRTHDAY, birthday); // Birthday
		values.put(KEY_HOBBY, hobby); // Hobby
		values.put(KEY_DATECREATED, datecreated);// Datecreated
		values.put(KEY_DATEUPDATED, dateupdated);// Dateupdated
		values.put(KEY_LASTLOGIN, lastlogin);// Lastlogin

		// Inserting Row
		db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection
	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			// start from column 1 which in our case is email
			user.put(KEY_EMAIL, cursor.getString(0));
			user.put(KEY_NICKNAME, cursor.getString(1));
			user.put(KEY_NAME, cursor.getString(2));
			user.put(KEY_LOCATION, cursor.getString(10));
			user.put(KEY_GENDER, cursor.getString(6));
			user.put(KEY_BIRTHDAY, cursor.getString(5));
			user.put(KEY_HOBBY, cursor.getString(4));
			user.put(KEY_DATECREATED, cursor.getString(7));
			user.put(KEY_DATEUPDATED, cursor.getString(8));
			user.put(KEY_LASTLOGIN, cursor.getString(9));

		}
		cursor.close();
		db.close();
		// return user
		return user;
	}

	/**
	 * Update user data
	 * */
	public void updateUser(String email, String name, String location,
			String gender, String birthday, String hobby, String dateupdated) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // name
		values.put(KEY_LOCATION, location); // location
		values.put(KEY_GENDER, gender);// gender
		values.put(KEY_BIRTHDAY, birthday);// birthday
		values.put(KEY_HOBBY, hobby);// hobby
		values.put(KEY_DATEUPDATED, dateupdated);// dateupdated

		// Updating Row
		db.update(TABLE_LOGIN, values, "email= '" + email + "'", null);
		db.close(); // Closing database connection

	}

	/**
	 * Insert Main Room Data
	 * */
	public void addMainRooms(String name, String datecreated) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MAINROOM_NAME, name); // main room name
		values.put(KEY_MAINROOM_DATECREATED, datecreated); // datecreated

		// Inserting Row
		db.insert(TABLE_MAINROOM, null, values);
		db.close(); // Closing database connection

	}

	/**
	 * Get Main Room Details
	 * */
	public Cursor getMainRoomDetails() {

		String selectQuery = "SELECT name FROM " + TABLE_MAINROOM;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		return cursor;
	}

	// Why do I need that ?
	public String getUID() {
		String uid = null;

		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			uid = cursor.getString(3);
		}
		cursor.close();
		db.close();
		return uid;
	}

	// Check if there are records in the db if there is then the user is already
	// logged in
	/**
	 * Getting user login status return true if rows are there in table
	 * */
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}

	// When the user logs out the database is cleared
	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void resetTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.delete(TABLE_MAINROOM, null, null);
		db.close();
	}

}