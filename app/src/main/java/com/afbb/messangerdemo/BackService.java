package com.afbb.messangerdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

public class BackService extends Service {

	public static final int ACTIVITY_MESSAGE_SEND = 1;
	public static final int SERVICE_MESSAGE_SEND = 2;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mMessenger.getBinder();
	}

	Messenger mMessenger = new Messenger(new InCommingHandler());

	class InCommingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case ACTIVITY_MESSAGE_SEND:

				Bundle bundle = new Bundle();
				bundle = msg.getData();
				String data = bundle.getString("data");
				
				 Toast.makeText(getApplicationContext(), data +
				 "  in Service", Toast.LENGTH_SHORT).show();
				
				Message message = Message.obtain(null,
						BackService.SERVICE_MESSAGE_SEND);
				Bundle bundle2 = new Bundle();
				bundle.putString("data1", data);
				message.setData(bundle2);
				try {
					msg.replyTo.send(message);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

}
