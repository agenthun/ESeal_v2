package com.agenthun.eseal_v2;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.SystemClock;
import android.provider.ContactsContract.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeviceItemListAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> datas;
	private LayoutInflater mInflater;
	
	public static String ITEM_Id = "ID";
	public static String ITEM_Forced_Open_Time = "Forced Open Time";
	public static String ITEM_Status = "Status";

	public DeviceItemListAdapter(Context c) {
		// TODO Auto-generated constructor stub
		super();
		datas = new ArrayList<HashMap<String, String>>();
		
		mInflater = LayoutInflater.from(c);
	}

	public void addData(String times, String detail) {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put(ITEM_Id, times);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
		Date curData = new Date(System.currentTimeMillis());
//		String time = dateFormat.format(curData);
		
		data.put(ITEM_Forced_Open_Time, detail);
		data.put(ITEM_Status, "非法打开");
		datas.add(data);
	}

	public void clear() {
		datas.clear();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_items, null);
			TextView id = (TextView) convertView.findViewById(R.id.item_id);
			TextView forcedOpenTime = (TextView) convertView.findViewById(R.id.item_time);
			TextView status = (TextView) convertView.findViewById(R.id.item_status);
			holder = new Holder(id, forcedOpenTime, status);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
/*		holder.dataId.setText("1");
		holder.dataForcedOpenTime.setText("20150210123456");
		holder.dataStatus.setText("非法打开");*/
		
		holder.dataId.setText(datas.get(position).get(ITEM_Id));
		holder.dataForcedOpenTime.setText(datas.get(position).get(ITEM_Forced_Open_Time));
		holder.dataStatus.setText(datas.get(position).get(ITEM_Status));
		
		return convertView;
	}

	private class Holder {
		TextView dataId;
		TextView dataForcedOpenTime;
		TextView dataStatus;

		public Holder(TextView id, TextView forcedOpenTime, TextView status) {
			// TODO Auto-generated constructor stub
			dataId = id;
			dataForcedOpenTime = forcedOpenTime;
			dataStatus = status;
		}
	}

}
