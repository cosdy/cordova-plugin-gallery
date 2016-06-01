package org.apache.cordova.gallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.List;

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

import android.content.Context;
import android.content.CursorLoader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;


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

      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Get All Photos.
   * @param activity the activity
   * @return ArrayList with photos
   */
    /**
   * Get All Photos.
   * @param activity the activity
   * @return ArrayList with images Path
   */
  private ArrayList<String> getAllPhotos(Activity activity) {

    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    ArrayList<String> Photos = new ArrayList<String>();

    String[] mediaColumns = new String[] {
      MediaStore.Images.Media._ID, 
      MediaStore.Images.Media.DATA,
      "(SELECT _data FROM thumbnails WHERE thumbnails.image_id =images._id) AS thumbnail"
    };

    Cursor cursor = activity.getContentResolver().query(uri, mediaColumns, null, null, MediaStore.Images.Media.DATE_ADDED + " desc");

    assert cursor != null;

    String fullPath = null;
    String thumbnailPath = null;

    while (cursor.moveToNext()) {
      String fullPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
      if (fullPath.endsWith(".gif")) {
        continue;
      }
      String thumbnailPath = cursor.getString(cursor.getColumnIndex("thumbnail"));
      Photos.add(thumbnailPath + "?" + fullPath);
    }
    
    cursor.close();
    return Photos;
  }

  /**
   * Get the path to the full image for a given thumbnail.
   */
  // private String toFullPath(Cursor thumbnailsCursor, Activity activity){
  //   String imageId = thumbnailsCursor.getString(thumbnailsCursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));

  //   // Request image related to this thumbnail
  //   String[] filePathColumn = { MediaStore.Images.Media.DATA };
  //   Cursor imagesCursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, MediaStore.Images.Media._ID + "=?", new String[]{imageId}, null);

  //   if (imagesCursor != null && imagesCursor.moveToFirst()) {
  //     int columnIndex = imagesCursor.getColumnIndex(filePathColumn[0]);
  //     String filePath = imagesCursor.getString(columnIndex);
  //     imagesCursor.close();
  //     return filePath;
  //   } else {
  //     imagesCursor.close();
  //     return new String("");
  //   }
  // }

  /**
   * Get All Albums.
   * @param activity the activity
   * @return Map of folder to files
   */
  // private Map<String, ArrayList<String>> getAllAlbums(Activity activity) {

  //   albumMap.clear();

  //   Uri uri;
  //   Cursor cursor;
  //   int column_index_data, column_index_folder_name;

  //   String absolutePathOfPhoto = null;
  //   String folderName = null;

  //   uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

  //   String[] projection = { MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

  //   cursor = activity.getContentResolver().query(uri, projection, null, null, null);

  //   column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);

  //   column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

  //   while (cursor.moveToNext()) {

  //     absolutePathOfPhoto = cursor.getString(column_index_data);

  //     folderName = cursor.getString(column_index_folder_name);

  //     if (albumMap.containsKey(folderName)) {

  //       albumMap.get(folderName).add(absolutePathOfPhoto);

  //     } else {
  //       ArrayList<String> listOfAllPhotos = new ArrayList<String>();
  //       listOfAllPhotos.add(absolutePathOfPhoto);
  //       albumMap.put(folderName, listOfAllPhotos);
  //     }
  //   }

  //   return albumMap;
  // }
}