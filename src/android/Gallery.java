package org.apache.cordova.gallery;

import java.util.ArrayList;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;

public class Gallery extends CordovaPlugin {

  private static final String ACTION_GET_ALL_PHOTOS = "getAllPhotos";

  @Override
  public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
    try {
      // if action is getAllPhotos
      if (ACTION_GET_ALL_PHOTOS.equals(action)) {

        // DO operation in thread pool to avoid cordova thread blocking
        cordova.getThreadPool().equals(new Runnable() {
          public void run() {
            // Return list of images in JSON Array
            // callbackContext.success(new JSONArray(getAllPhotos(cordova.getActivity())));
            callbackContext.success('gallery');
          }
        });

      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return super.execute(action, args, callbackContext);
  }

  /**
   * Get All Photos
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
}