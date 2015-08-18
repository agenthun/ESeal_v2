package com.agenthun.eseal_v2;

import java.util.ArrayList;
import java.util.HashMap;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeDeviceListAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, Object>> mLeDevices;
	private LayoutInflater mInflater;

	private ImageView signalRssi;

	public static String BLUETOOTH_DEVICE = "Device";
	public static String BLUETOOTH_RSSI = "Rssi";

	public LeDeviceListAdapter(Context c) {
		// TODO Auto-generated constructor stub
		super();
		mLeDevices = new ArrayList<HashMap<String, Object>>();
		new ArrayList<BluetoothDevice>();
		// mInflater = (LayoutInflater)
		// c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater = LayoutInflater.from(c);
	}

	public void addDevice(BluetoothDevice device, int rssi) {
		for (int i = 0; i < mLeDevices.size(); i++) {
			HashMap<String, Object> data = mLeDevices.get(i);
			BluetoothDevice bluetoothDevice = (BluetoothDevice) data
					.get(BLUETOOTH_DEVICE);
			if (bluetoothDevice.getAddress().compareTo(device.getAddress()) == 0) {
				data.put(BLUETOOTH_RSSI, rssi);
				mLeDevices.set(i, data); // 更新信息
				return;
			}
		}

		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(BLUETOOTH_DEVICE, device);
		data.put(BLUETOOTH_RSSI, rssi);
		mLeDevices.add(data);
	}

	// public void removeDevice(BluetoothDevice device) {
	// mLeDevices.remove(device);
	// }

	public BluetoothDevice getBluetoothDevice(int position) {
		HashMap<String, Object> data = mLeDevices.get(position);
		return (BluetoothDevice) data.get(BLUETOOTH_DEVICE);
	}

	public void clear() {
		mLeDevices.clear();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mLeDevices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mLeDevices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_devices, null);
			signalRssi = (ImageView) convertView
					.findViewById(R.id.device_signal);
			TextView name = (TextView) convertView
					.findViewById(R.id.ble_device_name);
			TextView address = (TextView) convertView
					.findViewById(R.id.ble_device_address);
			viewHolder = new ViewHolder(name, address, signalRssi);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, Object> bluetoothDevice = mLeDevices.get(position);
		BluetoothDevice device = (BluetoothDevice) bluetoothDevice
				.get(BLUETOOTH_DEVICE);
		int rssi = (Integer) bluetoothDevice.get(BLUETOOTH_RSSI);
		final String deviceName = device.getName();
		if (deviceName != null && deviceName.length() > 0) {
			viewHolder.deviceName.setText(deviceName);
		} else {
			viewHolder.deviceName.setText(R.string.unknown_device);
		}
		viewHolder.deviceAddress.setText(device.getAddress());
		viewHolder.deviceRssi.setImageLevel(Math.abs(rssi));

		return convertView;
	}

	private class ViewHolder {
		TextView deviceName;
		TextView deviceAddress;
		ImageView deviceRssi;

		public ViewHolder(TextView name, TextView address, ImageView rssi) {
			// TODO Auto-generated constructor stub
			deviceName = name;
			deviceAddress = address;
			deviceRssi = rssi;
		}
	}
}
