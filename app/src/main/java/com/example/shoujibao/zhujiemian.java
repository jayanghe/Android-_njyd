package com.example.shoujibao;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

public class zhujiemian extends Activity implements OnClickListener {

	private ArrayAdapter<News> adapter;
	private List<News> mynews = new ArrayList<News>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhujiemian);

		initView();
		update();
		Toast.makeText(zhujiemian.this, "Ë¢ÐÂ³É¹¦", Toast.LENGTH_LONG).show();

	}

	private void initView() {
		// TODO Auto-generated method stub
		Button bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(this);


		adapter = new ArrayAdapter<News>(this, R.layout.itemview, mynews) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				News news = getItem(position);

				View view = zhujiemian.this.getLayoutInflater().inflate(
						R.layout.itemview, null);
				((TextView) view.findViewById(R.id.textView1))
						.setText(news.title);
				((TextView) view.findViewById(R.id.textView2))
						.setText(news.content);
				((ImageView) view.findViewById(R.id.imageView1))
						.setImageBitmap(news.image);

				return view;
			}
		};

		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(adapter);

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				News news = adapter.getItem(arg2);
				AVObject obj = new AVObject("news");
				obj.setObjectId(news.id);

				return true;
			}

		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				News news = adapter.getItem(arg2);
				Intent intent = new Intent(zhujiemian.this,
						CommentActivity.class);
				intent.putExtra("newsid", news.id);
				startActivity(intent);

				return true;
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {

		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.button1) {
			// save();
			update();
		} else {
			update();
		}

	}

	private void update() {
		// TODO Auto-generated method stub
		final AVQuery<AVObject> query = new AVQuery<AVObject>("news");
		query.include("owner");

		adapter.clear();

		new AsyncTask<String, Integer, Integer>() {

			@Override
			protected Integer doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				try {

					List<AVObject> newss = query.find();

					for (int i = 0; i < newss.size(); i++) {

						AVFile file = (AVFile) newss.get(i).get("pic");
						if (file != null) {
							byte[] bytes = file.getData();
							Bitmap bt = BitmapFactory.decodeByteArray(bytes, 0,
									bytes.length);
							News news = new News(newss.get(i).getObjectId(),
									newss.get(i).getString("title"), newss.get(
											i).getString("content"), bt, newss
											.get(i).getString("mark"));
							mynews.add(news);
						} else {
							News news = new News(newss.get(i).getObjectId(),
									newss.get(i).getString("title"), newss.get(
											i).getString("content"), null,
									newss.get(i).getString("mark"));
							mynews.add(news);

						}
					}

				} catch (AVException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				adapter.notifyDataSetChanged();
			}
		}.execute("");

	}



}
