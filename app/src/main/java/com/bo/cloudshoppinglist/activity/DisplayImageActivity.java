package com.bo.cloudshoppinglist.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bo.cloudshoppinglist.R;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayInputStream;

/**
 * Created by bowang on 28/03/15.
 */
public class DisplayImageActivity extends Activity {
    private Button btnDelete;
    ImageView imageDetail;

    private TextView addImage;
    private Button backButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.display);
        btnDelete = (Button) findViewById(R.id.deleteBtn);
        btnDelete.setVisibility(View.VISIBLE);
        imageDetail = (ImageView) findViewById(R.id.imageView1);
        backButton = (Button) findViewById(R.id.imageButtonBack);
        backButton.setOnClickListener(getOnClickListener());
        addImage = (TextView) findViewById(R.id.imageButtonAdd);
        addImage.setVisibility(View.GONE);
        /**
         * getting intent data from search and previous screen
         */

        try {
            ParseFile parseFile = Constant.shopListItemPhoto.getParseFile(Constant.ShoppingItemPhoto.COLUMN_IMAGE_FILE);

            byte[] outImage = parseFile.getData();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(
                    outImage);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            imageDetail.setImageBitmap(theImage);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), CloudShoppingListApplication.ERROR_NETWORK_ERROR, Toast.LENGTH_LONG).show();
            return;
        }
        btnDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    ParseObject parseObject = Constant.shopListItemPhoto;
                    parseObject.deleteInBackground();
                    Constant.shopListItemPhoto = null;

                } catch (Exception ex) {
                    Log.e("DisplayImageActivity", ex.getMessage());
                }
                finish();

            }
        });

    }

    private OnClickListener getOnClickListener() {
        return new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                finish();

            }
        };
    }
}