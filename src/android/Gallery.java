package org.apache.cordova.gallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;

public class Gallery extends CordovaPlugin {

  private static final String ACTION_GET_ALL_PHOTOS = "getAllPhotos";

  private static final String ACTION_GET_ALL_ALBUMS = "getAllAlbums";

  public static HashMap<String, ArrayList<String>> albumMap = new HashMap<String, ArrayList<String>>();

  @Override
  public boolean execute(String action, JSONArray args,  final CallbackContext callbackContext) throws JSONException {
    try {
      if (ACTION_GET_ALL_PHOTOS.equals(action)) {
        // DO operation in thread pool to avoid cordova thread blocking
        cordova.getThreadPool().execute(new Runnable() {
          public void run() {
            // Return list of images in JSON Array
            callbackContext.success(new JSONArray(getAllPhotos(cordova.getActivity())));
          }
        });
        return true;
      }

      if (ACTION_GET_ALL_ALBUMS.equals(action)) {
        // DO operation in thread pool to avoid cordova thread blocking
        cordova.getThreadPool().execute(new Runnable() {
          public void run() {
            // Return list of images in JSON Array
            callbackContext.success(new JSONObject(getAllAlbums(cordova.getActivity())));
          }
        });
        return true;
      }

      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Get All Photos.
   * @param activity the activity
   * @return ArrayList with images Path
   */
  private ArrayList<String> getAllPhotos(Activity activity) {
    Uri uri;
    Cursor cursor;
    int column_index_data, column_index_folder_name;

    ArrayList<String> listOfAllPhotos = new ArrayList<String>();

    String absolutePathOfPhoto = null;

    uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    String[] projection = { MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

    cursor = activity.getContentResolver().query(uri, projection, null, null, null);

    column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);

    column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

    while (cursor.moveToNext()) {
      absolutePathOfPhoto = cursor.getString(column_index_data);
      
      listOfAllPhotos.add(absolutePathOfPhoto);
    }
    return listOfAllPhotos;
  }

  /**
   * Get All Albums.
   * @param activity the activity
   * @return Map of folder to files
   */
  private Map<String, ArrayList<String>> getAllAlbums(Activity activity) {

    albumMap.clear();

    Uri uri;
    Cursor cursor;
    int column_index_data, column_index_folder_name;

    String absolutePathOfPhoto = null;

    uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    String[] projection = { MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

    cursor = activity.getContentResolver().query(uri, projection, null, null, null);

    column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);

    column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

    while (cursor.moveToNext()) {

      absolutePathOfPhoto = cursor.getString(column_index_data);

      folderName = cursor.getString(column_index_folder_name);

      if (albumMap.containsKey(folderName)) {

        albumMap.get(folderName).add(absolutePathOfPhoto);

      } else {
        ArrayList<String> listOfAllPhotos = new ArrayList<String>();
        listOfAllPhotos.add(absolutePathOfPhoto);
        albumMap.put(folderName, listOfAllPhotos);
      }
    }

    return albumMap;
  }
}