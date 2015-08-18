package com.agenthun.eseal_v2;

import in.srain.cube.mints.base.TitleBaseFragment;
import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanRecord;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class DeviceItemFragment extends TitleBaseFragment {
	
	private final static String TAG = DeviceItemFragment.class.getSimpleName();
	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	protected static final int REFRESH_GET_ID = 0;
	protected static final int REFRESH_COMPLETE = 1;

	private String mDeviceName;
	private String mDeviceAddress;
	private String forcedOpenTimes;
	private String details;

	private BluetoothLeService mBluetoothLeService;
	private BluetoothGattCharacteristic mBluetoothGattCharacteristic;
	private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics;
	private ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

	int times = 0;

	final String mString = "Refresh";
	final String mTitle = "蓝牙锁 v1.0 > 开锁记录";

	private PtrClassicFrameLayout frameLayout;
	private ListView listHistory;
	private DeviceItemListAdapter mDeviceItemListAdapter;

	private boolean reFreshing = false;
	private boolean mConnected = false;

	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";

	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				getContext().finish();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			mBluetoothLeService.connect(mDeviceAddress);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				mConnected = true;
				// updateConnectionState(R.string.connected);
				// invalidateOptionsMenu();
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				mConnected = false;
				// updateConnectionState(R.string.disconnected);
				// invalidateOptionsMenu();
				clearUI();
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				// Show all the supported services and characteristics on the
				// user interface.
				// displayGattServices(mBluetoothLeService.getSupportedGattServices());
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_ELECTRONICSEAL_DISCOVERED
					.equals(action)) {
				displayElectronicSealGattService(mBluetoothLeService
						.getElectronicSealGattService());
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				forcedOpenTimes = intent
						.getStringExtra(BluetoothLeService.DATA_FORCED_OPEN_TIMES);
				details = intent
						.getStringExtra(BluetoothLeService.DATA_DETAILS);
			} else if (BluetoothLeService.ACTION_WRITE_DATA.equals(action)) {
//				byte value[] = {00,00,10,10};
//				charas.get(0).setValue(value);
//				mBluetoothLeService.writeCharacteristic(charas.get(0));
			}
		}
	};

	protected void clearUI() {
		// TODO Auto-generated method stub
		listHistory.setAdapter((DeviceItemListAdapter) null);
	}

	protected void displayElectronicSealGattService(
			BluetoothGattService electronicSealGattService) {
		// TODO Auto-generated method stub
		String uuid = null;
		String unknownCharaString = getResources().getString(
				R.string.unknown_characteristic);

		ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
		mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

		ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
		List<BluetoothGattCharacteristic> gattCharacteristics = electronicSealGattService
				.getCharacteristics();

		for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
			charas.add(gattCharacteristic);
			uuid = gattCharacteristic.getUuid().toString();
			HashMap<String, String> currentCharaData = new HashMap<String, String>();
			currentCharaData.put(LIST_NAME,
					SampleGattAttributes.lookup(uuid, unknownCharaString));
			currentCharaData.put(LIST_UUID, uuid);
			gattCharacteristicGroupData.add(currentCharaData);
			mBluetoothLeService.readCharacteristic(gattCharacteristic);
		}

		mGattCharacteristics.add(charas);
		gattCharacteristicData.add(gattCharacteristicGroupData);
	}

	protected void displayGattServices(
			List<BluetoothGattService> supportedGattServices) {
		// TODO Auto-generated method stub
		String uuid = null;
	}

	protected void displayData(String data) {
		// TODO Auto-generated method stub
		if (data != null) {

		}
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		// TODO Auto-generated method stub
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_ELECTRONICSEAL_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_WAITING);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothLeService.ACTION_WRITE_DATA);
		return intentFilter;
	}

	@Override
	public void onEnter(Object data) {
		// TODO Auto-generated method stub
		if (data != null && data instanceof String) {
			mDeviceAddress = (String) data;
		}
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.gatt_services, null);	
		listHistory = (ListView) view.findViewById(R.id.list_history);

		mDeviceItemListAdapter = new DeviceItemListAdapter(getContext());
		listHistory.setAdapter(mDeviceItemListAdapter);

		frameLayout = (PtrClassicFrameLayout) view
				.findViewById(R.id.list_items_frame);
		final StoreHouseHeader header = new StoreHouseHeader(getContext());
		header.setPadding(0, LocalDisplay.dp2px(15), 0, 0);
		header.initWithString(mString);
		setHeaderTitle(mDeviceAddress + " 历史记录");

		Intent gattServiceIntent = new Intent(getContext(),
				BluetoothLeService.class);
		getContext().bindService(gattServiceIntent, mServiceConnection,
				getContext().BIND_AUTO_CREATE);

		frameLayout.setDurationToCloseHeader(1000);
		frameLayout.setHeaderView(header);
		frameLayout.addPtrUIHandler(header);

		// frameLayout.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// frameLayout.autoRefresh(false);
		// }
		// }, 100);
		frameLayout.setPtrHandler(new PtrHandler() {

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				// TODO Auto-generated method stub
				updateData();
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				// TODO Auto-generated method stub
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						content, header);
			}
		});

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getContext().registerReceiver(mGattUpdateReceiver,
				makeGattUpdateIntentFilter());
		if (mBluetoothLeService != null) {
			final boolean result = mBluetoothLeService.connect(mDeviceAddress);
			Log.d(TAG, "Connect request result=" + result);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getContext().unregisterReceiver(mGattUpdateReceiver);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getContext().unbindService(mServiceConnection);
		mBluetoothLeService = null;
	}

	protected void updateData() {
		// TODO Auto-generated method stub

		// Handler handler = new Handler();
		// handler.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// frameLayout.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// times++;
		// mDeviceItemListAdapter
		// .addData(times+"", details);
		// frameLayout.refreshComplete();
		// mDeviceItemListAdapter.notifyDataSetChanged();
		// }
		// }, 1000);
		// mBluetoothLeService.readCharacteristic(charas.get(2));
		// }
		// }, 1000);
		// mBluetoothLeService.readCharacteristic(charas.get(1));

		final Handler handler = new Handler();

		frameLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mDeviceItemListAdapter
								.addData(forcedOpenTimes, details);
						frameLayout.refreshComplete();
						mDeviceItemListAdapter.notifyDataSetChanged();
					}
				}, 2000);
				mBluetoothLeService.readCharacteristic(charas.get(2));
				times++;
				// mDeviceItemListAdapter.addData(times + "", details);

			}
		}, 500);

		mBluetoothLeService.readCharacteristic(charas.get(1));
	}
}
