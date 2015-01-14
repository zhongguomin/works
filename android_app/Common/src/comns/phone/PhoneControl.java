package comns.phone;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;

/**
 * @ClassName: PhoneControl
 * 
 * @author: chellychi
 * 
 * @version: V1.0
 * 
 * @Date: 2013-7-17 下午7:10:39
 * 
 * @Description: 类<code>PhoneControl</code>是手机功能相关的类</p>
 * 
 *               Copyright 2013。 All rights reserved.
 * 
 *               You have the permissions to modify.
 * 
 */
public class PhoneControl {

	public static final String ACTION_SMS_SEND = ".sms.action.SMS_SEND";
	public static final String ACTION_SMS_DELIVERED = ".sms.action.SMS_DELIVERED";

	/**
	 * 发送短信
	 * 
	 * @param context
	 *            上下文
	 * @param telNum
	 *            发送目的号码
	 * @param text
	 *            发送内容
	 * @param sendBundle
	 *            发送的intent携带的数据
	 * @param deliverBundle
	 *            对方接受到的intent携带的数据
	 */
	public static void sendSms(Context context, String telNum, String text,
			Bundle sendBundle, Bundle deliverBundle) {

		if (sendBundle == null) {
			sendBundle = new Bundle();
		}

		if (deliverBundle == null) {
			deliverBundle = new Bundle();
		}

		SmsManager smsManager = SmsManager.getDefault();

		Intent sendIntent = new Intent(context.getPackageName()
				+ ACTION_SMS_SEND);
		sendIntent.putExtras(sendBundle);
		PendingIntent sendPi = PendingIntent.getBroadcast(context, 1,
				sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		ArrayList<PendingIntent> sendPiList = new ArrayList<PendingIntent>();
		sendPiList.add(sendPi);
		Intent deliverIntent = new Intent(context.getPackageName()
				+ ACTION_SMS_DELIVERED);
		deliverIntent.putExtras(deliverBundle);
		PendingIntent deliverPi = PendingIntent.getBroadcast(context, 1,
				deliverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		ArrayList<PendingIntent> deliverPiList = new ArrayList<PendingIntent>();
		deliverPiList.add(deliverPi);

		ArrayList<String> divideContents = smsManager.divideMessage(text);
		smsManager.sendMultipartTextMessage(telNum, null, divideContents,
				sendPiList, deliverPiList);
	}

	/**
	 * 发送短信
	 * 
	 * @param context
	 *            上下文
	 * @param telNum
	 *            发送目的号码
	 * @param text
	 *            发送内容
	 */
	public static void sendSms(Context context, String telNum, String text) {

		sendSms(context, telNum, text, null, null);
	}

}
