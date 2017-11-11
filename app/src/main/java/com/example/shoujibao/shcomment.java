package com.example.shoujibao;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class shcomment extends Activity {
	private ArrayAdapter<comment> adapter;
	private List<comment> mynews = new ArrayList<comment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shcomment);
		initView();
		update();
		Toast.makeText(shcomment.this, "刷新成功", Toast.LENGTH_LONG).show();
	}

	private void initView() {
		// TODO Auto-generated method stub
		Button bt2 = (Button) findViewById(R.id.button2);
		bt2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				update();
				Toast.makeText(shcomment.this, "刷新成功", Toast.LENGTH_LONG)
						.show();
			}
		});
		adapter = new ArrayAdapter<comment>(this, R.layout.itemview1, mynews) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				comment comment = getItem(position);

				View view = shcomment.this.getLayoutInflater().inflate(
						R.layout.itemview1, null);
				((TextView) view.findViewById(R.id.textView1))
						.setText(comment.comment);
				return view;
			}
		};
		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(adapter);
	}
	private void update() {
		// TODO Auto-generated method stub
		adapter.clear();
		new AsyncTask<String, Integer, Integer>() {
			@Override
			protected Integer doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				try {
					final AVQuery<AVObject> query = new AVQuery<AVObject>(
							"comment");
					query.include("comment");
					List<AVObject> comments = query.find();
					query.whereEqualTo("newsid", arg0[0]);

					for (int i = 0; i < comments.size(); i++) {
						comment comment1 = new comment(comments.get(i)
								.getObjectId(), comments.get(i).getString(
								"content"));
						mynews.add(comment1);
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
		}.execute(getIntent().getStringExtra("newsid"));
	}

}
