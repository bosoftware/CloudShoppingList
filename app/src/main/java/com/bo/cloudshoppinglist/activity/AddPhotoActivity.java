package com.bo.cloudshoppinglist.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
/**
 * Created by bowang on 28/03/15.
 */
public abstract class AddPhotoActivity extends Activity {

	private static final int CAMERA_REQUEST = 1;



	private Activity activity;

	protected void onCreate(Bundle savedInstanceState, Activity activity) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.activity = activity;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		Bitmap yourImage = null;

		Bundle extras = data.getExtras();
		if (extras != null) {
			yourImage = extras.getParcelable("data");
		}
		if (yourImage == null) {
			if (data.getData() == null) {
				yourImage = (Bitmap) data.getExtras().get("data");
			} else {
				try {
					yourImage = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), data.getData());
				} catch (Exception ex) {
					Log.e("Take photo error", ex.getMessage(), ex);
				}
			}
		}

		if (yourImage != null) {

			// convert bitmap to byte
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
			//yourImage = ScalingUtilities.performResize(yourImage, 400, 400);
			byte imageInByte[] = stream.toByteArray();

			// Inserting Contacts
			Log.d("Insert: ", "Inserting ..");
            ParseFile parseFile = new ParseFile(Constant.ShoppingItemPhoto.COLUMN_IMAGE_FILE_NAME,imageInByte);
            ParseObject parseObject = new ParseObject(Constant.ShoppingItemPhoto.TABLE_NAME);
            parseObject.put(Constant.ShoppingItemPhoto.COLUMN_IMAGE_FILE,parseFile);
            parseObject.put(Constant.ShoppingItemPhoto.COLUMN_SHOP_LIST,Constant.shopList);
            parseObject.put(Constant.ShoppingItemPhoto.COLUMN_SHOP_LIST_ITEM,Constant.shopListItem);

            parseObject.saveInBackground();
			Intent i = new Intent(this, activity.getClass());
			startActivity(i);
			finish();

		}

	}

	/**
	 * open camera method
	 */
	public void callCamera() {

		Intent cameraIntent = new Intent(
				MediaStore.ACTION_IMAGE_CAPTURE);

		startActivityForResult(cameraIntent, CAMERA_REQUEST);

	}

}
