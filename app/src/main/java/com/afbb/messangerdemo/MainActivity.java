package com.afbb.messangerdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	Boolean flag;
	protected Messenger mService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = new Intent(this, BackService.class);
		bindService(intent, conn, BIND_AUTO_CREATE);
	}

	ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
			flag = false;

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			mService = new Messenger(service);
			flag = true;
		}
	};
	private Messenger mMessanger = new Messenger(new InCommingHanler());

	class InCommingHanler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case BackService.SERVICE_MESSAGE_SEND:

				System.out
						.println("MainActivity.InCommingHanler.handleMessage()");
				Bundle bundle = new Bundle();
				bundle = msg.getData();
				String data = bundle.getString("data1");
				Toast.makeText(getApplicationContext(),
						"From Activity:  " + data,
						Toast.LENGTH_SHORT).show();

				break;

			default:
				break;
			}

			super.handleMessage(msg);
		}
	}

	public void sendData(View v) {
		if (flag) {
			Bundle bundle = new Bundle();
			bundle.putString("data", "Hai SIs how Are You ??");
			Message message = Message.obtain(null,
					BackService.ACTIVITY_MESSAGE_SEND);
			message.setData(bundle);
			message.replyTo = mMessanger;
			try {
				mService.send(message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
