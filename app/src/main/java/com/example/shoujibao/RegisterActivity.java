package com.example.shoujibao;

import java.text.BreakIterator;

import android.app.Activity;
import android.app.backup.RestoreObserver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

public class RegisterActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		Button bt1 = (Button) findViewById(R.id.button1);
		bt1.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.button1) {
			register();
		} else {
			startActivity(new Intent(this, geren_login.class));
		}
	}

	private void register() {
		// TODO Auto-generated method stub
		EditText et1 = (EditText) findViewById(R.id.editText1);
		EditText et2 = (EditText) findViewById(R.id.editText2);
		EditText et3 = (EditText) findViewById(R.id.editText3);
		EditText et4 = (EditText) findViewById(R.id.editText4);
		String name = et1.getText().toString();
		String pwd = et2.getText().toString();
		String pwd_sure = et4.getText().toString();
		String email = et3.getText().toString();
		if (pwd_sure.equals(pwd)) {

			Toast.makeText(getApplicationContext(), "��������һ��", 1).show();
			AVUser user = new AVUser();// �½� AVUser ����ʵ��
			user.setUsername(name);// �����û���
			user.setPassword(pwd);// ��������
			user.setEmail(email);// ��������
			user.signUpInBackground(new SignUpCallback() {
				@Override
				public void done(AVException e) {
					if (e == null) {
						// ע��ɹ�
						Toast.makeText(getApplicationContext(), "ע��ɹ�", 1)
								.show();
						startActivity(new Intent(RegisterActivity.this,
								geren_login.class));
					} else {
						// ʧ�ܵ�ԭ������ж��֣����������û����Ѿ����ڡ�
						Toast.makeText(getApplicationContext(), e.getMessage(),
								1).show();
					}
				}
			});

		} else {
			Toast.makeText(getApplicationContext(), "�������벻һ�£�����������", 1).show();

		}

	}
}
