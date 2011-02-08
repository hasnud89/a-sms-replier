package cn.edu.hit.voidmain.asmsreplier.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cn.edu.hit.voidmain.asmsreplier.R;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private Context context = null;
	
	public DatabaseHelper(Context context) {
		super(context, ContactInfoProviderMetaData.DATABASE_NAME,
				null, 
				ContactInfoProviderMetaData.DATABASE_VERSION);
		
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(context.getResources().getString(R.string.create_contacts));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(context.getResources().getString(R.string.drop_contacts));
		onCreate(db);
	}
	
}