package com.agenthun.eseal_v2;

import java.util.UUID;

import org.json.JSONArray;

import com.agenthun.eseal_v2.BluetoothLeService.LocalBinder;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BluetoothLeServicePlus extends Service {
	private final static String TAG = BluetoothLeServicePlus.class
			.getSimpleName();

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGatt;

	private String mBluetoothDeviceAddress;
	private int mConnectionState = STATE_DISCONNECTED;

	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;

	public final static String ACTION_GATT_CONNECTED = "com.example.ble.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.ble.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.ble.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_GATT_SERVICES_ELECTRONICSEAL_DISCOVERED = "com.example.ble.ACTION_GATT_SERVICES_ELECTRONICSEAL_DISCOVERED";
	public final static String ACTION_DATA_WAITING = "com.example.ble.ACTION_DATA_WAITING";
	public final static String ACTION_DATA_AVAILABLE = "com.example.ble.ACTION_DATA_AVAILABLE";

	public final static String DATA_PASSCODE = "com.example.ble.DATA_PASSCODE";
	public final static String DATA_FORCED_OPEN_TIMES = "com.example.ble.DATA_FORCED_OPEN_TIMES";
	public final static String DATA_DETAILS = "com.example.ble.DATA_DETAILS";

	public final static UUID UUID_ELECTRONICSEAL_SERVICES = UUID
			.fromString(SampleGattAttributes.ELECTRONICSEAL_SERVICES);
	public final static UUID UUID_ELECTRONICSEAL_PASSCODE = UUID
			.fromString(SampleGattAttributes.ELECTRONICSEAL_PASSCODE);
	public final static UUID UUID_ELECTRONICSEAL_FORCED_OPEN_TIMES = UUID
			.fromString(SampleGattAttributes.ELECTRONICSEAL_FORCED_OPEN_TIMES);
	public final static UUID UUID_ELECTRONICSEAL_DETAILS = UUID
			.fromString(SampleGattAttributes.ELECTRONICSEAL_DETAILS);

	public class LocalBinder extends Binder {
		BluetoothLeServicePlus getService() {
			return BluetoothLeServicePlus.this;
		}
	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Close();
		return super.onUnbind(intent);
	}

	public boolean initialize() {
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}
		return true;
	}

	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		if (mBluetoothDeviceAddress != null
				&& address.equals(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
			Log.d(TAG,
					"Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect()) {
				mConnectionState = STATE_CONNECTING;
				return true;
			} else {
				return false;
			}
		}

		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}

		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
		return true;
	}

	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	private void Close() {
		// TODO Auto-generated method stub
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	public BluetoothGattService getElectronicSealGattService() {
		if (mBluetoothGatt == null) {
			return null;
		}
		return mBluetoothGatt.getService(UUID_ELECTRONICSEAL_SERVICES);
	}

	private BluetoothGattCharacteristic getCharacteristic(String deviceAddress,
			String serviceIndex, String characteristicIndex) {
		return (BluetoothGattCharacteristic) getElectronicSealGattService()
				.getCharacteristics()
				.get(Integer.parseInt(characteristicIndex));
	}
	
	public void getCharacteristics(JSONArray json) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mBluetoothDeviceAddress);
		
	}

	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			// TODO Auto-generated method stub
			super.onConnectionStateChange(gatt, status, newState);
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			// TODO Auto-generated method stub
			super.onServicesDiscovered(gatt, status);
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicRead(gatt, characteristic, status);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicWrite(gatt, characteristic, status);
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			// TODO Auto-generated method stub
			super.onCharacteristicChanged(gatt, characteristic);
		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			// TODO Auto-generated method stub
			super.onDescriptorRead(gatt, descriptor, status);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			// TODO Auto-generated method stub
			super.onDescriptorWrite(gatt, descriptor, status);
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			// TODO Auto-generated method stub
			super.onReadRemoteRssi(gatt, rssi, status);
		}

	};
	

}
