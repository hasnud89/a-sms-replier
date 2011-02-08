package cn.edu.hit.voidmain.asmsreplier.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class ContactInfoProvider extends ContentProvider {
	private static HashMap<String, String> contactInfoProjectionMap;
	static
	{
		contactInfoProjectionMap = new HashMap<String, String>();
		
		contactInfoProjectionMap.put(ContactInfoTableMetaData._ID, 
				ContactInfoTableMetaData._ID);
		contactInfoProjectionMap.put(ContactInfoTableMetaData.CONTACT_ID,
				ContactInfoTableMetaData.CONTACT_ID);
		contactInfoProjectionMap.put(ContactInfoTableMetaData.CONTACT_NAME,
				ContactInfoTableMetaData.CONTACT_NAME);
		contactInfoProjectionMap.put(ContactInfoTableMetaData.CONTACT_NUMBER,
				ContactInfoTableMetaData.CONTACT_NUMBER);
		contactInfoProjectionMap.put(ContactInfoTableMetaData.SHOULD_REPLY,
				ContactInfoTableMetaData.SHOULD_REPLY);
	}
	
	private static final UriMatcher uriMatcher;
	private static final int CONTACT_COLLECTION_URI_INDICATOR = 1;
	private static final int SINGLE_CONTACT_URI_INDICATOR = 2;
	static
	{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(ContactInfoProviderMetaData.AUTHORITY, 
				"contacts", 
			    CONTACT_COLLECTION_URI_INDICATOR);
		uriMatcher.addURI(ContactInfoProviderMetaData.AUTHORITY, 
				"contacts/#", 
				SINGLE_CONTACT_URI_INDICATOR);
	}
	
	private DatabaseHelper openHelper;
	
	@Override
	public boolean onCreate() {
		openHelper = new DatabaseHelper(getContext());
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
		switch(uriMatcher.match(uri))
		{
		case CONTACT_COLLECTION_URI_INDICATOR:
			return ContactInfoTableMetaData.CONTENT_TYPE;
		case SINGLE_CONTACT_URI_INDICATOR:
			return ContactInfoTableMetaData.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int count = 0;
		switch(uriMatcher.match(uri))
		{
		case CONTACT_COLLECTION_URI_INDICATOR:
			count = db.delete(ContactInfoTableMetaData.TABLE_NAME, where, whereArgs);
			break;
			
		case SINGLE_CONTACT_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);
			count = db.delete(ContactInfoTableMetaData.TABLE_NAME,
					ContactInfoTableMetaData._ID + "=" + rowId
					+ (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), 
					whereArgs);
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(uriMatcher.match(uri) != CONTACT_COLLECTION_URI_INDICATOR)
		{
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		if(!values.containsKey(ContactInfoTableMetaData.CONTACT_ID))
		{
			values.put(ContactInfoTableMetaData.CONTACT_ID, ContactInfoTableMetaData.NOT_IN_CONTACT_LIST_ID);
		}
		if(!values.containsKey(ContactInfoTableMetaData.CONTACT_NAME))
		{
			values.put(ContactInfoTableMetaData.CONTACT_NAME, ContactInfoTableMetaData.NOT_IN_CONTACT_LIST_NAME);
		}
		if(!values.containsKey(ContactInfoTableMetaData.CONTACT_NUMBER))
		{
			throw new IllegalArgumentException("Failed to insert row because phone number is needed " + uri);
		}
		if(!values.containsKey(ContactInfoTableMetaData.SHOULD_REPLY))
		{
			values.put(ContactInfoTableMetaData.SHOULD_REPLY, ContactInfoTableMetaData.DEFAULT_SHOULD_REPLY);
		}
		
		SQLiteDatabase db = openHelper.getWritableDatabase();
		long rowId = db.insert(ContactInfoTableMetaData.TABLE_NAME, 
				ContactInfoTableMetaData.CONTACT_NAME,
				values);
		
		if(rowId > 0)
		{
			Uri insertedBookUri = ContentUris.withAppendedId(ContactInfoTableMetaData.CONTENT_URI, rowId);
			
			getContext().getContentResolver().notifyChange(insertedBookUri, null);
			return insertedBookUri;
		}
		
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		switch(uriMatcher.match(uri))
		{
		case CONTACT_COLLECTION_URI_INDICATOR:
			qb.setTables(ContactInfoTableMetaData.TABLE_NAME);
			qb.setProjectionMap(contactInfoProjectionMap);
			break;
			
		case SINGLE_CONTACT_URI_INDICATOR:
			qb.setTables(ContactInfoTableMetaData.TABLE_NAME);
			qb.setProjectionMap(contactInfoProjectionMap);
			qb.appendWhere(ContactInfoTableMetaData._ID + "=" + uri.getPathSegments().get(1));
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		String orderBy;
		if(TextUtils.isEmpty(sortOrder))
		{
			orderBy = ContactInfoTableMetaData.DEFAULT_SORT_ORDER;
		}
		else
		{
			orderBy = sortOrder;
		}
		
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int count;
		switch(uriMatcher.match(uri))
		{
		case CONTACT_COLLECTION_URI_INDICATOR:
			count = db.update(ContactInfoTableMetaData.TABLE_NAME, values, where, whereArgs);
			break;
			
		case SINGLE_CONTACT_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);
			count = db.update(ContactInfoTableMetaData.TABLE_NAME, values, 
					ContactInfoTableMetaData._ID + "=" + rowId
					+ (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""),
					whereArgs);
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
