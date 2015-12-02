package com.ozsaat.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ozsaat.criminalintent.database.CrimeBaseHelper;
import com.ozsaat.criminalintent.database.CrimeCursorWrapper;
import com.ozsaat.criminalintent.database.CrimeDbSchema;
import com.ozsaat.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab crimeLab;
    private Context context;
    private SQLiteDatabase database;

    public CrimeLab(Context context) {
        context = context.getApplicationContext();
        database = new CrimeBaseHelper(context).getWritableDatabase();

        }


    public static CrimeLab get(Context context) {
        if (crimeLab == null) {
            crimeLab = new CrimeLab(context);
        }

        return crimeLab;
    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        database.insert(CrimeTable.NAME, null, values);
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {

        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[] { id.toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        database.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }

    private CrimeCursorWrapper queryCrimes (String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(CrimeTable.NAME,
                null, //Column - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new CrimeCursorWrapper(cursor);
    }
}
