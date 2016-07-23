package com.bo.cloudshoppinglist.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bo.cloudshoppinglist.R;
import com.bo.cloudshoppinglist.activity.adapter.PhotoListAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bowang on 28/03/15.
 */
public class PhotoListActivity extends AddPhotoActivity {

    private List<ParseObject> photoList = new ArrayList<ParseObject>();
    private ListView photoListView;

    private static int inspectEventId;
    private static int itemId;
    private static int propertyId;
    private byte[] imageData;
    private int imageId;
    private Bitmap theImage;
    private PhotoListAdapter photoListAdapter;
    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private TextView addImage;
    private Handler handler;
    private Button backBtn;
    private TextView titleView;
    private TextView deleteButton;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photo_list);
        photoListView = (ListView) findViewById(R.id.photolist);
        titleView = (TextView) findViewById(R.id.title);
        titleView.setText(getString(R.string.photo_list));
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                refresh();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        backBtn = (Button) findViewById(R.id.imageButtonBack);
        backBtn.setOnClickListener(getOnClickListener());
        photoListAdapter = new PhotoListAdapter(this, R.layout.photo_listitem,
                photoList);
        photoListView.setAdapter(photoListAdapter);
        photoListView.setOnItemClickListener(getOnItemClickListener());

        addImage = (TextView) findViewById(R.id.imageButtonAdd);

        addImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                callCamera();
            }
        });
        handler = getHandler(photoListAdapter);

        refresh();
    }


    private OnClickListener getOnClickListener() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();

            }
        };
    }

    private void refresh() {
        swipeContainer.setRefreshing(true);
        progressBar.setVisibility(View.VISIBLE);
        new LoadPhotoListTask().execute();
    }

    private OnItemClickListener getOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                try {
                    ParseObject parseObject = photoList.get(position);
                    imageData = parseObject.getParseFile(Constant.ShoppingItemPhoto.COLUMN_IMAGE_FILE).getData();

                    // convert byte to bitmap
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(
                            imageData);
                    theImage = BitmapFactory.decodeStream(imageStream);
                    Intent intent = new Intent(PhotoListActivity.this,
                            DisplayImageActivity.class);
                    Constant.shopListItemPhoto = parseObject;
                    startActivity(intent);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        };
    }

    public class LoadPhotoListTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                List<ParseObject> list = getAllPhotos();
                photoList.clear();
                photoList.addAll(list);
                Message msg = new Message();
                msg.arg1 = 0;
                handler.sendMessage(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
                return "ERROR";
            }
            return null;
        }

    }

    private List<ParseObject> getAllPhotos() throws Exception {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constant.ShoppingItemPhoto.TABLE_NAME);
        query.whereEqualTo(Constant.ShoppingItemPhoto.COLUMN_SHOP_LIST_ITEM, Constant.shopListItem);
        try {
            return query.find();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), CloudShoppingListApplication.ERROR_NETWORK_ERROR, Toast.LENGTH_LONG).show();
            throw ex;
        }
    }

    private Handler getHandler(final PhotoListAdapter adapter) {
        return new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.arg1 == 0) {
                    adapter.notifyDataSetChanged();

                }
                progressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
            }

        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}
