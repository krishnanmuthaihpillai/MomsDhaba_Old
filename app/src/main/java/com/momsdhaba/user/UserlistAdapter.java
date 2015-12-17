package com.momsdhaba.user;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.momsdhaba.R;

public class UserlistAdapter extends ArrayAdapter<Userdata> {
	private final static String TAG = UserlistAdapter.class.getSimpleName();
	ArrayList<Userdata> usersList;
	LayoutInflater vi;
	int Resource;
	public static HashMap<String, Float> hashMaptotal = new HashMap<String, Float>();
	public static HashMap<String, Float> hashMap = new HashMap<String, Float>();
	public static HashMap<String, Integer> hashMapcount = new HashMap<String, Integer>();
	public static HashMap<String, String> hashMapFood = new HashMap<String, String>();
	public static HashMap<String, String> hashMapordertype = new HashMap<String, String>();
	// ArrayList<String> foodname = new ArrayList<String>();
	String quantity, c_id, price, fname, Arrayfood, ordertype;
	float total;
	public static float Amounttotal = 0.0f;
	public static String keyamount;
	public String keycount, orderkey;
	int key_value, key_values = 0;
	private Context ctx;

	// float t = 0.0f;
	// String strcount;

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
		public ImageView imageprofile;
		public ImageView foodtypeimage;
		// public Button plus;
		// public Button minus;
		ImageView plus, minus, delivery, takeaway, dining;

	}

	public UserlistAdapter(Context context, int resource,
			ArrayList<Userdata> objects) {
		super(context, resource, objects);
		ctx = context;
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		usersList = objects;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// convert view = design

		final ViewHolder holder;
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.foodtypeimage = (ImageView) v
					.findViewById(R.id.foodtypeimage);
			holder.imageprofile = (ImageView) v
					.findViewById(R.id.image_profile);
			holder.imageview = (ImageView) v.findViewById(R.id.ufoodimage);
			holder.tvCname = (TextView) v.findViewById(R.id.uchefsname);
			holder.tvDescription = (TextView) v.findViewById(R.id.udescription);
			holder.tvDate = (TextView) v.findViewById(R.id.udate);
			// holder.tvFtype = (TextView) v.findViewById(R.id.ufoodtype);
			holder.tvPrice = (TextView) v.findViewById(R.id.uprice);
			holder.tvFname = (TextView) v.findViewById(R.id.ufoodname);
			holder.tvFquantity = (TextView) v.findViewById(R.id.ufoodquantity);
			holder.plus = (ImageView) v.findViewById(R.id.btn_plus);
			holder.minus = (ImageView) v.findViewById(R.id.btn_minus);
			holder.delivery = (ImageView) v.findViewById(R.id.delivery);
			holder.takeaway = (ImageView) v.findViewById(R.id.takeaway);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		Userdata rowItem = (Userdata) getItem(position);

		holder.imageview.setImageResource(R.drawable.ic_launcher);
		new DownloadImageTask(holder.imageview).execute(usersList.get(position)
				.getImage());
		new DownloadImageTask(holder.imageprofile).execute(usersList.get(
				position).getProfile());
		new DownloadImageTask(holder.foodtypeimage).execute(usersList.get(
				position).getFoodtype());
		holder.tvCname.setText(usersList.get(position).getName());
		holder.tvDescription.setText(usersList.get(position).getDescription());
		holder.tvDate.setText(usersList.get(position).getDate());
		// holder.tvFtype.setText(usersList.get(position).getFoodtype());
		holder.tvFname.setText(usersList.get(position).getFoodName());
		holder.tvFquantity.setText(String.valueOf(rowItem.getNum()));
		holder.tvPrice.setText("₹ :" + usersList.get(position).getPrice());

		// holder.tvFquantity.setText("Food Quantity :");
		// + usersList.get(position).getFoodQuantity());order ,takeaway ,dining;

		holder.plus.setTag(rowItem);
		holder.minus.setTag(rowItem);
		holder.delivery.setTag(rowItem);
		// holder.takeaway.setTag(rowItem);
		// holder.dining.setTag(rowItem);

		// quantity = usersList.get(position).getFoodQuantity();
		// price = usersList.get(position).getPrice();
		// c_id = usersList.get(position).getFoodId();
		// holder.delivery.setOnClickListener(this);
		// holder.delivery.setBackgroundResource(R.drawable.deleveryselecter);

		holder.plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				quantity = usersList.get(position).getFoodQuantity();
				price = usersList.get(position).getPrice();
				c_id = usersList.get(position).getFoodId();
				fname = usersList.get(position).getFoodName();

				ImageView ib = (ImageView) v;
				Userdata state = (Userdata) ib.getTag();
				state.setUpSelected(ib.isClickable());

				int cnt = 0;
				try {
					cnt = Integer.valueOf(holder.tvFquantity.getText()
							.toString());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cnt++;
				state.setNum(cnt);
				holder.tvFquantity.setText(String.valueOf(state.getNum()));
				Log.d(TAG, "cnt " + cnt);
				// strcount = Integer.toString(cnt);
				if (cnt >= Integer.parseInt(quantity)) {
					holder.plus.setEnabled(false);
				} else {
					holder.plus.setEnabled(true);
				}

				float int_price = Float.parseFloat(price);
				// int int_price = Integer.parseInt(price);
				// / Log.d("int_priceint_price", "" + int_price);
				total = int_price * state.getNum();
				// Log.d(TAG, "total " + total);
				hashMap.put(c_id, total);
				hashMapcount.put(c_id, cnt);
				hashMapFood.put(c_id, fname);
				hashMaptotal.put(c_id, total);
				hashMapordertype.put(c_id, ordertype);
				for (String key : hashMaptotal.keySet()) {
					// Log.i(TAG, "hashMap  " + hashMaptotal);
					Float value = hashMaptotal.get(key);

				}

				for (String key : hashMap.keySet()) {
					// Log.i(TAG, "hashMap  " + hashMap);
					Float value = hashMap.get(key);

				}
				for (String key : hashMapordertype.keySet()) {
					String value = hashMapordertype.get(key);
					orderkey = key;
					Log.i(TAG, "hashMapordertype  " + key);
				}

				try {
					Iterator<String> it1 = hashMap.keySet().iterator();
					while (it1.hasNext()) {
						String key = it1.next();
						Float val = hashMap.get(key);
						// System.out.println("Map Value:" + hashMap.get(key));
						try {
							if (val == 0.0) {
								hashMap.remove(key);
							}
						} catch (Exception e) {

							e.printStackTrace();
						}
						// Log.i(TAG, "hashMap  " + hashMap);
					}

					// System.out.println("Map Size:" + hashMap.size());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (String k : hashMapcount.keySet()) {
					Log.i(TAG, "hashMapcount - " + k);
					Log.i(TAG, "keycount - " + keycount);
					keycount = k;
				}
				for (String key : hashMapFood.keySet()) {
					Log.i(TAG, "hashMapFood + " + hashMapFood);
					String value = hashMapFood.get(key);

				}

				try {
					Iterator<String> it1 = hashMapcount.keySet().iterator();
					while (it1.hasNext()) {
						String key = it1.next();
						int val = hashMapcount.get(key);
						key_value = hashMapcount.get(key);
						// System.out.println("Map Value:"+
						// hashMapcount.get(key));
						try {
							if (val == 0.0) {
								hashMapcount.remove(key);
								hashMapFood.remove(key);
								hashMapordertype.remove(key);

							}
							if (!orderkey.contains(keycount)) {
								hashMapordertype.remove(key);
							}

						} catch (Exception e) {

							e.printStackTrace();
						}
						// Log.i(TAG, "hashMapcount  " + hashMapcount);
					}

					// System.out.println("Map Size:" + hashMapcount.size());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					Iterator<String> it1 = hashMapFood.keySet().iterator();
					while (it1.hasNext()) {
						String key = it1.next();
						// / Log.i(TAG, "key_value key_value " + key_value);
						System.out.println("Map Value:" + hashMapFood.get(key));

						try {
							// if (key_value == 0.0||key_value==0) {
							// hashMapFood.remove(key);
							// }
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					System.out.println("Map Size:" + hashMapFood.size());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				float t = 0.0f;
				for (float f : hashMaptotal.values()) {
					t += f;
					Amounttotal = t;
					Intent in = new Intent("my.action.string");
					in.putExtra("TOTAL", Amounttotal);
					ctx.sendBroadcast(in);

					// Log.d(TAG, "TOTAL " + t);
					// Log.i(TAG, "Amounttotal " + Amounttotal);
				}

				// editor.putString("COUNT", keycount);
				// Log.i(TAG, "AMOUNT " + keyamount);
				// Log.i(TAG, "COUNT " + keycount);

			}
		});

		holder.minus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				price = usersList.get(position).getPrice();
				quantity = usersList.get(position).getFoodQuantity();
				c_id = usersList.get(position).getFoodId();
				fname = usersList.get(position).getFoodName();
				// Log.d(TAG, "price " + price);
				// Log.d(TAG, "quantity " + quantity);
				// Log.d(TAG, "c_id " + c_id);
				ImageView ib = (ImageView) v;
				Userdata state = (Userdata) ib.getTag();

				state.setDownSelected(v.isClickable());
				int cnt = Integer.valueOf(holder.tvFquantity.getText()
						.toString());

				cnt--;

				if (cnt > 0) {
					state.setNum(cnt);
					holder.tvFquantity.setText(String.valueOf((state.getNum())));
					Log.d(TAG, "cnt " + state.getNum());

				} else {
					state.setNum(0);
					holder.tvFquantity.setText("0");
				}

				if (cnt >= Integer.parseInt(quantity)) {
					holder.plus.setEnabled(false);
				} else {
					holder.plus.setEnabled(true);
				}
				// strcount = Integer.toString(cnt);
				float int_price = Float.parseFloat(price);
				// int int_price = Integer.parseInt(price);
				total = int_price * state.getNum();
				// Log.d(TAG, "total " + total);
				hashMap.put(c_id, total);
				hashMapcount.put(c_id, state.getNum());
				hashMapFood.put(c_id, fname);
				hashMaptotal.put(c_id, total);
				hashMapordertype.put(c_id, ordertype);

				for (String key : hashMaptotal.keySet()) {
					// / Log.i(TAG, "hashMap " + hashMaptotal);
					// Float value = hashMaptotal.get(key);

				}
				for (String key : hashMap.keySet()) {
					// Log.i(TAG, "hashMap  " + hashMap);
					Float value = hashMap.get(key);

				}
				for (String key : hashMapordertype.keySet()) {
					String value = hashMapordertype.get(key);
					orderkey = key;
					Log.i(TAG, "hashMapordertype  " + key);
				}

				try {
					Iterator<String> it1 = hashMap.keySet().iterator();
					while (it1.hasNext()) {
						String key = it1.next();
						Float val = hashMap.get(key);
						// System.out.println("Map Value:" + hashMap.get(key));
						try {
							if (val == 0.0) {
								hashMap.remove(key);
							}
						} catch (Exception e) {

							e.printStackTrace();
						}
						// Log.i(TAG, "hashMap " + hashMap);
					}

					System.out.println("Map Size:" + hashMap.size());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (String k : hashMapcount.keySet()) {
					Log.i(TAG, "hashMapcount - " + k);
					Log.i(TAG, "keycount - " + keycount);
					keycount = k;
				}

				for (String key : hashMapFood.keySet()) {
					// / Log.i(TAG, "hashMapFood + " + hashMapFood);
					String value = hashMapFood.get(key);

				}
				

				try {
					Iterator<String> it1 = hashMapcount.keySet().iterator();
					while (it1.hasNext()) {
						String key = it1.next();
						int val = hashMapcount.get(key);
						key_values = hashMapcount.get(key);
						// System.out.println("Map Value:"+hashMapcount.get(key));
						try {
							if (val == 0.0) {
								hashMapcount.remove(key);
								hashMapFood.remove(key);
								hashMapordertype.remove(key);

							}

						} catch (Exception e) {

							e.printStackTrace();
						}
						// Log.i(TAG, "hashMapcount " + hashMapcount);
					}

					// System.out.println("Map Size:" + hashMapcount.size());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					Iterator<String> it1 = hashMapFood.keySet().iterator();
					while (it1.hasNext()) {
						String key = it1.next();

						Log.i(TAG, "key_value  key_value " + key_values);
						try {
							// if (key_values == 0.0||key_values==0) {
							// if (state.getNum()==0) {
							// hashMapFood.remove(key);
							// }
						} catch (Exception e) {

							e.printStackTrace();
						}
						Log.i(TAG, "hashMapFood " + hashMapFood);
					}

				} catch (Exception e) {

					e.printStackTrace();
				}

				float t = 0.0f;
				for (float f : hashMaptotal.values()) {
					t += f;
					Amounttotal = t;
					// String s = Float.toString(25.0f);
					Intent in = new Intent("my.action.string");
					in.putExtra("TOTAL", Amounttotal);
					ctx.sendBroadcast(in);
					Amounttotal = 0.0f;
					// Log.d(TAG, "TOTAL ??????" + t);
					// Log.i(TAG, "Amounttotal " + Amounttotal);
				}

			}
		});
		holder.delivery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.isClickable()) {
					int cnt = 0;
					try {
						cnt = Integer.valueOf(holder.tvFquantity.getText()
								.toString());
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					holder.delivery
							.setBackgroundResource(R.drawable.deleveryselecter);

					hashMapordertype.put(c_id, ordertype);
					
					try {
						
						ordertype = "DELIVERY";						
					} catch (Exception e) {

						e.printStackTrace();
					}
//					try {
//						Iterator<String> it1 = hashMapordertype.keySet()
//								.iterator();
//						while (it1.hasNext()) {
//							String key = it1.next();
//							String val = hashMapordertype.get(key);
//							Log.e("key_values", "" + key_values);				
//							try {
//								if (key_values == 0) {
//									hashMapordertype.remove(key);
//									//hashMapordertype.put(val, null);
//									Log.e("::::::::", "" + hashMapordertype.remove(key));
//								}
//							} catch (Exception e) {
//
//								e.printStackTrace();
//							}
//							
//						}
//					
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
			}

			

		});

		holder.takeaway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.isClickable()) {

					holder.takeaway.setBackgroundResource(R.drawable.takeawayselecter);
					int cnt = 0;
					try {
						cnt = Integer.valueOf(holder.tvFquantity.getText()
								.toString());
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					hashMapordertype.put(c_id, ordertype);
				
					try {
						ordertype = "TAKE_AWAY";
						
					} catch (Exception e) {

						e.printStackTrace();
					}
//					try {
//						Iterator<String> it1 = hashMapordertype.keySet()
//								.iterator();
//						while (it1.hasNext()) {
//							String key = it1.next();
//							String val = hashMapordertype.get(key);
//							Log.e("key_values", "" + key_values);
//							try {
//								if (key_values == 0) {
//									hashMapordertype.remove(key);
//									//hashMapordertype.put(val, null);
//									Log.e("::::::::", "" + hashMapordertype.remove(key));
//								}
//							} catch (Exception e) {
//
//								e.printStackTrace();
//							}
//							
//						}
//						
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}

			}

		});
		return v;

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

	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	//
	// }

}// ('TAKE_AWAY','TAKE_AWAY'),('DELIVERY', 'DELIVERY'),('DINING', 'DINING'),