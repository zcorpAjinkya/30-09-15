
package com.zcorp.yielloh;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.zcorp.yielloh.model.AccountManager;

public class ProfilePicture extends Fragment
{

	WebView mWebView;
	private ImageView addImage;
	private File file;
	private byte[] imageBytes, data1;
	private Button btnNext, btnSkip;
	private Bitmap bitmap;
	View rootView;
	private int serverResponseCode = 0;
	private ProgressDialog mProgressDialog;
	private String txtGeneral, filename, image, txtProfile, Address,
			txtGeneralSign, access_token, refresh_token, email, ObjectID;
	private AccountManager objectAccount;
	boolean register = false;
	private Uri fileUri;

	public ProfilePicture()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{

		rootView = inflater.inflate(R.layout.fragment_profile_photo, container,
				false);

		addImage = (ImageView) rootView.findViewById(R.id.imagePlus);

		objectAccount = new AccountManager(getActivity());

		btnSkip = (Button) rootView.findViewById(R.id.skip);
		btnNext = (Button) rootView.findViewById(R.id.next);

		btnNext.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				objectAccount.setAvatarLink("");
				((UpdateProfile) getActivity()).setCurrentItem(1, true);

			}
		});

		addImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 96);
				intent.putExtra("outputY", 96);

				intent.putExtra("return-data", true);
				intent.putExtra("scale", true);
				intent.putExtra("outputFormat",
						Bitmap.CompressFormat.JPEG.toString());

				startActivityForResult(
						Intent.createChooser(intent, "Select File"), 1);

			}
		});

		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == getActivity().RESULT_OK
				&& data != null && data.getData() != null)
		{

			Uri uri = data.getData();

			try
			{
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
						Locale.getDefault()).format(new Date());

				filename = "profilePicture_" + timeStamp + ".jpg";

				if (data != null)
				{
					Bundle extras = data.getExtras();
					bitmap = extras.getParcelable("data");

				}
				// Log.d(TAG, String.valueOf(bitmap));

				File mediaStorageDir = new File(
						Environment.getExternalStorageDirectory(),
						"Yielloh/Profile");

				if (!mediaStorageDir.exists())
				{
					if (!mediaStorageDir.mkdirs())
					{
						Log.e("Error", "File Directory");
					}

				}

				fileUri = Uri.fromFile(new File(mediaStorageDir.getPath()
						+ File.separator + "IMG_" + timeStamp + ".jpg"));

				OutputStream stream = new FileOutputStream(fileUri.getPath());
				bitmap.compress(CompressFormat.JPEG, 100, stream);

				new uploadProfilePicture()
						.execute("http://staging.yielloh.com/update_profile");

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private class uploadProfilePicture extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(getActivity(), null, null);
			// Set progressBar to preogress dialog
			mProgressDialog.setContentView(R.layout.progress_bar_layout);
			mProgressDialog.setCancelable(false);
			// Show progressdialog
			mProgressDialog.show();

		}

		@Override
		protected String doInBackground(String... urls)
		{
			Log.e("File Path", fileUri.getPath());

			return uploadFile(urls[0], fileUri.getPath());
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result)
		{
			mProgressDialog.dismiss();
			if (register)
			{
				((UpdateProfile) getActivity()).setCurrentItem(1, true);
			}
		}
	}

	public String uploadFile(String url, String filepath)
	{

		String result = "";
		try
		{
			image = convertFileToString(filepath);
			Log.e("file", image);

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPut httpPut = new HttpPut(url);

			String user1 = "";

			// 3. build jsonObject

			JSONObject profile = new JSONObject();

			JSONObject avatar = new JSONObject();
			JSONObject objectJSON = new JSONObject();

			objectJSON.put("data", image);
			objectJSON.put("filename", filename);
			objectJSON.put("content_type", "image/jpg");

			avatar.put("avatar", objectJSON);
			profile.put("profile", avatar);

			// 4. convert JSONObject to JSON to String
			user1 = profile.toString();

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(user1);

			// 6. set httpPost Entity
			httpPut.setEntity(se);

			Log.e("Token", objectAccount.getAccessToken());
			// 7. Set some headers to inform server about the type of the
			// content
			httpPut.setHeader("Accept", "application/vnd.yielloh.v1");
			httpPut.setHeader("Accept", "application/json");
			httpPut.setHeader("Authorization",
					"Bearer " + objectAccount.getAccessToken());
			httpPut.setHeader("Content-type", "application/json");

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPut);

			// 9. receive response as inputStream
			String res = EntityUtils.toString(httpResponse.getEntity());

			Log.e("Register", res);

			if (res.toLowerCase().contains("errors"))
			{
				JSONObject myObject = new JSONObject(res);

				JSONObject object = myObject.getJSONObject("errors");

				register = false;
				if (object.has("profile.username") && (object.has("email")))
				{
					txtGeneral = "Email and User Name has already taken";
				}
				else if (object.has("email"))
				{
					email = object.getString("email");
				}
				else if (object.has("profile.username"))
				{
					txtProfile = object.getString("profile.username");
				}

			}
			else if (res.toLowerCase().contains("error"))
			{
				JSONObject myObject = new JSONObject(res);

				txtGeneral = myObject.getString("error");
			}
			else
			{
				register = true;
				JSONObject myObject = new JSONObject(res);
				AccountManager account = new AccountManager(getActivity());
				account.setAvatarLink(myObject.getString("avatar"));
				account.setCoverPhotoLink("");
			}
		}
		catch (Exception e)
		{
			Log.e("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	public String convertFileToString(String pathOnSdCard)
	{
		String strFile = null;
		try
		{

			File file = new File(pathOnSdCard);

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 256;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bit = BitmapFactory.decodeStream(new FileInputStream(file),
					null, o2);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			bit.compress(Bitmap.CompressFormat.JPEG, 90, baos);
			data1 = baos.toByteArray();

			strFile = Base64.encodeToString(data1, Base64.NO_WRAP);

			Log.e("File 64", strFile);

		}
		catch (IOException e)
		{
			Log.e("File 64", "" + e);
			e.printStackTrace();
		}
		return strFile;
	}
}
