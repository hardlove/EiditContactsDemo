package com.globalroam.editcontactsdemo;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts.Data;

import java.util.ArrayList;

//import com.globalroam.editcontactsdemo.Contact.Phone;


public class ContactUtils {


	//AsyncQueryHandler

	//Mobile Home Work Other


	/**
	 *
	 * @param context
	 * @param numbers
	 * @param types  eg:ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
	 * @return
	 */
	public static void addContact(Context context,String name, String[] numbers,int[] types) {
		addContact(context, null, name, numbers, types);

	}

	private static void addContact(Context context, String contactId, String name, String[] numbers, int[] types) {

		long rawContactId;
		ContentValues values = new ContentValues();
		if (contactId == null) {
			ContentResolver cr = context.getContentResolver();
			Uri rawContactUri = cr.insert(ContactsContract.RawContacts.CONTENT_URI, values);
			rawContactId = ContentUris.parseId(rawContactUri);
		} else {
			rawContactId = Long.parseLong(contactId);
		}



		//往data表插入姓名数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);//内容类型
		values.put(StructuredName.GIVEN_NAME, name);
		context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

		// 往data表插入电话数据

		for (int i = 0; i < numbers.length; i++) {
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);// 内容类型
			values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, numbers[i]);
			values.put(ContactsContract.CommonDataKinds.Phone.TYPE, types[i]);
			context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
		}


	}
	public static void updateContact(Context context,String contactId,String name,String[] numbers,int[] types) {


		deleteContact(context,contactId);
		addContact(context, name, numbers, types);

//		ContentValues values = new ContentValues();
//		values.clear();
//		values.put(ContactsContract.Contacts.DISPLAY_NAME, name);
//		int rc1 = context.getContentResolver().update(ContactsContract.RawContacts.CONTENT_URI, values,
//				ContactsContract.Contacts._ID + "=?", new String[]{contactId});
//
//		ContentResolver resolver = context.getContentResolver();
////		int rc1 = 0, rc2 = 0;
//		//删除data表中数据
//		String where = ContactsContract.Data.CONTACT_ID + " =?";
//		String[] whereparams = new String[]{contactId};
//		rc1 = resolver.delete(ContactsContract.Data.CONTENT_URI, where, whereparams);
//
//		addContact(context,contactId, name, numbers, types);
//
//		/*//删除rawContact表中数据
//		where = ContactsContract.RawContacts.CONTACT_ID + " =?";
//		whereparams = new String[]{contactId};
//
//		rc2 = resolver.delete(ContactsContract.RawContacts.CONTENT_URI, where, whereparams);
//*/

	}



	public static void deleteContact(Context context, String contactId) {

		/*Uri uri = Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, contactId);
		int rows = context.getContentResolver().delete(uri, null, null);
        if (rows > -1) {
            return true;
        }

		return false;*/


		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		//delete contact
		ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
				.withSelection(ContactsContract.RawContacts.CONTACT_ID+"="+contactId, null)
				.build());
		//delete contact information such as phone number,email
		ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
				.withSelection(ContactsContract.Data.CONTACT_ID+"=" + contactId, null)
				.build());


		try {
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (Exception e) {
		}
	}

	public  static String getContactId(Context context) {
		Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				cursor.close();
				return  contactId;
			}
		}

		return null;
	}

	public static String getContactNameById(Context context, String contactID) {
		Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,contactID);
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		if (cursor!=null && cursor.moveToFirst()) {
			String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			cursor.close();
			return name;
		}

		return null;
	}

	public static Contact.Phone[] getPhonesByContacaId(Context context, String contactId) {
		ContentResolver cr = context.getContentResolver();
		String selection = ContactsContract.Contacts._ID+"="+contactId;
		Cursor cursor  = cr.query(ContactsContract.Contacts.CONTENT_URI, null, selection , null, null);
		if(cursor == null){
			return null;
		}
		Contact.Phone[] phones = null;
		if(cursor.moveToFirst()){
			int nameColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
			String contactName = cursor.getString(nameColumnIndex);
			//取得电话号码
			String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);

			if(phoneCursor == null){
				return null;
			}
			Contact.Phone p = null;
			int count = phoneCursor.getCount();
			phones = new Contact.Phone[count];
			int i = 0;
			while(phoneCursor.moveToNext())
			{
				String PhoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				String type = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
				//格式化手机号
				PhoneNumber = PhoneNumber.replace("-","");
				PhoneNumber = PhoneNumber.replace(" ","");

				p = new Contact.Phone(contactName,PhoneNumber, Integer.valueOf(type));
				phones[i++] = p;


			}
			phoneCursor.close();
		}
		cursor.close();
		return phones;
	}


	
	

}
