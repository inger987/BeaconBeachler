package com.example.inger.beaconbeachler;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;


public class FileContentProvider extends ContentProvider {

    public static final Uri PATH = Uri.parse("content://com.example.inger.beaconbeachler.bilde/");
    private static final HashMap<String, String> FILE_TYPES = new HashMap<String, String>();

    static {
        FILE_TYPES.put(".jpg", "image/jpeg");
        FILE_TYPES.put(".jpeg", "image/jpeg");
    }

    @Override
    public boolean onCreate() {

        try {

            File file = new File(getContext().getFilesDir(), "Image.jpg");

            if(!file.exists()) {
                file.createNewFile();
            }

            getContext().getContentResolver().notifyChange(PATH, null);
            return (true);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public String getType(Uri uri) {

        String path = uri.toString();
        for (String extension : FILE_TYPES.keySet()) {
            if (path.endsWith(extension)) {
                return (FILE_TYPES.get(extension));
            }
        }
        return (null);

    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {

        File file = new File(getContext().getFilesDir(), "Image.jpg");

        if (file.exists()) {
            return (ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE));
        }

        throw new FileNotFoundException(uri.getPath());

    }

    @Override
    public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sort) {

        throw new RuntimeException("Denne funksjonen støttes ikke");

    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {

        throw new RuntimeException("Denne funksjonen støttes ikke");

    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {

        throw new RuntimeException("Denne funksjonen støttes ikke");

    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {

        throw new RuntimeException("Operation not supported");

    }

}