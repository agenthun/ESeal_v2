package com.agenthun.eseal_v2;

import java.io.Closeable;
import java.security.Timestamp;
import java.util.List;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BluetoothLeService extends Service {
	private final static String TAG = BluetoothLeService.class.getSimpleName();

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGatt;

	private String mBluetoothDeviceAddress;
	private int mConnectionState = STATE_DISCONNECTED;
	private String times;

	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;

	public final static String ACTION_GATT_CONNECTED = "com.example.ble.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.ble.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.ble.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_GATT_SERVICES_ELECTRONICSEAL_DISCOVERED = "com.example.ble.ACTION_GATT_SERVICES_ELECTRONICSEAL_DISCOVERED";
	public final static String ACTION_DATA_WAITING = "com.example.ble.ACTION_DATA_WAITING";
	public final static String ACTION_DATA_AVAILABLE = "com.example.ble.ACTION_DATA_AVAILABLE";
	public final static String ACTION_WRITE_DATA = "com.example.ble.ACTION_WRITE_DATA";

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

	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			String intentAction;
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				intentAction = ACTION_GATT_CONNECTED;
				mConnectionState = STATE_CONNECTED;
				broadcastUpdate(intentAction);
				Log.i(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.i(TAG, "Attempting to start service discovery:"
						+ mBluetoothGatt.discoverServices());
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				intentAction = ACTION_GATT_DISCONNECTED;
				mConnectionState = STATE_DISCONNECTED;
				Log.i(TAG, "Disconnected from GATT server.");
				broadcastUpdate(intentAction);
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			// TODO Auto-generated method stub
			if (status == BluetoothGatt.GATT_SUCCESS) {
				if (gatt.getService(UUID_ELECTRONICSEAL_SERVICES) != null) {
					broadcastUpdate(ACTION_GATT_SERVICES_ELECTRONICSEAL_DISCOVERED);
				} else {
					broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
				}
			} else {
				Log.w(TAG, "onServicesDiscovered received: " + status);
			}
		}

		// @Override
		// public void onDescriptorRead(BluetoothGatt gatt,
		// BluetoothGattDescriptor descriptor, int status) {
		// // TODO Auto-generated method stub
		// super.onDescriptorRead(gatt, descriptor, status);
		// }

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicWrite(gatt, characteristic, status);
			if (UUID_ELECTRONICSEAL_PASSCODE.equals(characteristic
					.getUuid())) {
				broadcastUpdate(ACTION_WRITE_DATA);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicRead(gatt, characteristic, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				if (UUID_ELECTRONICSEAL_FORCED_OPEN_TIMES.equals(characteristic
						.getUuid())) {
					broadcastUpdate(ACTION_DATA_WAITING,
							characteristic.getValue());
				} else if (UUID_ELECTRONICSEAL_DETAILS.equals(characteristic
						.getUuid())) {
					broadcastUpdate(ACTION_DATA_AVAILABLE,
							characteristic.getValue());
				}
				// broadcastUpdate(ACTION_DATA_AVAILABLE,
				// characteristic.getValue());
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			// TODO Auto-generated method stub
			super.onCharacteristicChanged(gatt, characteristic);
			// broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			if (UUID_ELECTRONICSEAL_FORCED_OPEN_TIMES.equals(characteristic
					.getUuid())) {
				broadcastUpdate(ACTION_DATA_WAITING, characteristic.getValue());
			} else if (UUID_ELECTRONICSEAL_DETAILS.equals(characteristic
					.getUuid())) {
				broadcastUpdate(ACTION_DATA_AVAILABLE,
						characteristic.getValue());
			}
		};

	};

	public class LocalBinder extends Binder {
		BluetoothLeService getService() {
			return BluetoothLeService.this;
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

	protected void broadcastUpdate(final String action) {
		// TODO Auto-generated method stub
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}

	// private void broadcastUpdate(final String action,
	// final BluetoothGattCharacteristic characteristic) {
	// // For all other profiles, writes the data formatted in HEX.
	// /*
	// * final byte[] data = characteristic.getValue(); if (data != null &&
	// * data.length > 0) { final StringBuilder stringBuilder = new
	// * StringBuilder(data.length); for (byte byteChar : data)
	// * stringBuilder.append(String.format("%02X ", byteChar));
	// * intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
	// * stringBuilder.toString()); }
	// */
	//
	// // final byte[] data = characteristic.getValue();
	// // if (data != null && data.length > 0) {
	// // final Intent intent = new Intent(action);
	// // if (UUID_ELECTRONICSEAL_FORCED_OPEN_TIMES.equals(characteristic
	// // .getUuid())) {
	// // intent.putExtra(DATA_FORCED_OPEN_TIMES,
	// // String.format("%d", data[0]));
	// // } else if (UUID_ELECTRONICSEAL_DETAILS.equals(characteristic
	// // .getUuid())) {
	// // intent.putExtra(DATA_DETAILS, new String(data));
	// // }
	// // sendBroadcast(intent);
	// // }
	//
	// final byte[] data = characteristic.getValue();
	// String times = null;
	// final Intent intent = new Intent(action);
	// if (data != null && data.length > 0) {
	// if (action == ACTION_DATA_WAITING) {
	// times = String.format("%d", data[0]);
	// } else if (action == ACTION_DATA_AVAILABLE) {
	// intent.putExtra(DATA_FORCED_OPEN_TIMES, times);
	// intent.putExtra(DATA_DETAILS, new String(data));
	// }
	// }
	// sendBroadcast(intent);
	// }

	private void broadcastUpdate(final String action, byte value[]) {
		final Intent intent = new Intent(action);
		if (value != null) {
			times = null;
			if (action == ACTION_DATA_WAITING)
				times = String.format("%d", value[0]);
			else if (action == ACTION_DATA_AVAILABLE) {
				intent.putExtra(DATA_FORCED_OPEN_TIMES, times);
				intent.putExtra(DATA_DETAILS, new String(value));
			}
		}
		sendBroadcast(intent);
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

	public boolean readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return false;
		}
		return mBluetoothGatt.readCharacteristic(characteristic);
	}
	
	public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return false;
		}
		return mBluetoothGatt.writeCharacteristic(characteristic);
	}
	
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}

		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
	}

	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null) {
			return null;
		}
		return mBluetoothGatt.getServices();
	}

	public BluetoothGattService getElectronicSealGattService() {
		if (mBluetoothGatt == null) {
			return null;
		}
		return mBluetoothGatt.getService(UUID_ELECTRONICSEAL_SERVICES);
	}
}
