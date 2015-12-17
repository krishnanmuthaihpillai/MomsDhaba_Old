package com.momsdhaba;

import java.io.InputStream;
import java.util.ArrayList;

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

public class ChefListAdapter extends ArrayAdapter<Chefdata> {
	ArrayList<Chefdata> actorList;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;

	public ChefListAdapter(Context context, int resource,
			ArrayList<Chefdata> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		actorList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);

			holder.imageview = (ImageView) v.findViewById(R.id.foodimage);
		
			holder.tvCname = (TextView) v.findViewById(R.id.chefsname);
			holder.tvDescription = (TextView) v.findViewById(R.id.description);
			holder.tvDate = (TextView) v.findViewById(R.id.date);
			holder.tvFtype = (TextView) v.findViewById(R.id.foodtype);
			holder.tvPrice = (TextView) v.findViewById(R.id.price);
			holder.tvFname = (TextView) v.findViewById(R.id.foodname);
			holder.tvFquantity = (TextView) v.findViewById(R.id.foodquantity);
			
			
			// holder.tvOrder = (TextView) v.findViewById(R.id.foodquantity);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		holder.imageview.setImageResource(R.drawable.ic_launcher);
		new DownloadImageTask(holder.imageview).execute(actorList.get(position).getImage());
	
		holder.tvCname.setText(actorList.get(position).getName());
		holder.tvDescription.setText(actorList.get(position).getDescription());
		holder.tvDate.setText(actorList.get(position).getDate());
		holder.tvFtype.setText(actorList.get(position).getFoodtype()); // foodtype
		holder.tvFname.setText(actorList.get(position).getFoodName());
		holder.tvFquantity.setText("Food Quantity :"
				+ actorList.get(position).getFoodQuantity());
		holder.tvPrice.setText("₹ :" + actorList.get(position).getPrice()); // food
		// holder.tvId.setText(actorList.get(position).getFoodId()); //foodorder
		// holder.tvId.setText(actorList.get(position).getOrder());

		return v;

	}

	static class ViewHolder {
		public ImageView imageview;
	
		public TextView tvCname;
		public TextView tvDescription;
		public TextView tvDate;
		public TextView tvFtype;
		public TextView tvPrice;
		public TextView tvFname;
		public TextView tvFquantity;
		public TextView tvId;
		// public TextView tvOrder;

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