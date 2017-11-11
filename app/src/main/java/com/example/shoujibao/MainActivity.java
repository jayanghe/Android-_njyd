package com.example.shoujibao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

public class MainActivity extends Activity implements OnClickListener {

	private ArrayAdapter<News> adapter;
	private List<News> mynews = new ArrayList<News>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		update();

	}

	private void initView() {
		// TODO Auto-generated method stub
		Button bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(this);

		Button bt1 = (Button) findViewById(R.id.button2);
		bt1.setOnClickListener(this);

		adapter = new ArrayAdapter<News>(this, R.layout.itemview, mynews) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				News news = getItem(position);

				View view = MainActivity.this.getLayoutInflater().inflate(
						R.layout.itemview, null);
				((TextView) view.findViewById(R.id.textView1))
						.setText(news.title);
				((TextView) view.findViewById(R.id.textView2))
						.setText(news.content);
				((TextView) view.findViewById(R.id.textView3))
						.setText(news.mark);
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
				Intent intent = new Intent(MainActivity.this,
						shcomment.class);
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
			save();

		} else {
			update();
			Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_LONG).show();
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

	private void save() {
		// TODO Auto-generated method stub
		String title = ((EditText) findViewById(R.id.editText1)).getText()
				.toString();
		String content = ((EditText) findViewById(R.id.editText2)).getText()
				.toString();
		if (title.length() == 0 || content.length() == 0) {
			Toast.makeText(getApplicationContext(), "标题或内容不能为空", 1).show();
		} else {

			AVObject news = new AVObject("news");
			news.put("title", ((EditText) findViewById(R.id.editText1))
					.getText().toString());
			news.put("content", ((EditText) findViewById(R.id.editText2))
					.getText().toString());
			news.put("mark", ((EditText) findViewById(R.id.editText3))
					.getText().toString());
			// news.put("price",
			// Integer.parseInt(mPriceEdit.getText().toString()));

			news.put("owner", AVUser.getCurrentUser());
			try {
				news.put(
						"pic",
						AVFile.withAbsoluteLocalPath("11.png",
								Environment.getExternalStorageDirectory()
										+ ".0/11.png"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			news.saveInBackground(new SaveCallback() {
				@Override
				public void done(AVException arg0) {

					if (arg0 == null) {
						update();

						Toast.makeText(MainActivity.this, "发布成功",
								Toast.LENGTH_LONG).show();
						// Toast.makeText(MainActivity.this, "  ",
						// Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(MainActivity.this, arg0.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}
}
