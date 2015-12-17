package com.momsdhaba.user;

import java.io.InputStream;
import java.util.ArrayList;

import com.momsdhaba.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class UserHistoryAdapter extends ArrayAdapter<User_History_Data> {
	ArrayList<User_History_Data> historyList;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;

	public UserHistoryAdapter(Context context, int resource,
			ArrayList<User_History_Data> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		historyList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.tvfood = (TextView) v.findViewById(R.id.text_fname);
			holder.tvcount = (TextView) v.findViewById(R.id.text_fcount);
			holder.tvamount = (TextView) v.findViewById(R.id.text_amount);
			holder.tvdate = (TextView) v.findViewById(R.id.text_date);
			holder.tvOrderid = (TextView) v.findViewById(R.id.text_orderid);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		holder.tvfood.setText(historyList.get(position).getFoodName());
		holder.tvcount.setText(historyList.get(position).getFoodcount());
		holder.tvamount.setText(historyList.get(position).getTotal());
		holder.tvdate.setText(historyList.get(position).getDate());
		holder.tvOrderid.setText(historyList.get(position).getOrderid());
		//holder.tvOrderItemid.setText(historyList.get(position).getOrderItemid());
		return v;
	}

	static class ViewHolder {

		public TextView tvfood;
		public TextView tvcount;
		public TextView tvamount;
		public TextView tvdate;
		public TextView tvOrderid;
		public TextView tvOrderItemid;

	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
}
