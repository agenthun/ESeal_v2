package com.agenthun.eseal_v2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import in.srain.cube.mints.base.MenuItemFragment;
import in.srain.cube.mints.base.TitleBaseFragment;
import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class MainFragment extends MenuItemFragment {
	final String mTitle = "蓝牙锁 v1.0";
	private static final int REQUEST_ENABLE_BT = 1;
	private static final long SCAN_PERIOD = 10000;

	private PtrClassicFrameLayout frameLayout;
	private ListView listDevice;
	private LeDeviceListAdapter mLeDeviceListAdapter;
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = super.createView(inflater, container, savedInstanceState);
		listDevice = (ListView) view.findViewById(R.id.list_device);

		frameLayout = (PtrClassicFrameLayout) view
				.findViewById(R.id.list_devices_frame);
		final StoreHouseHeader header = new StoreHouseHeader(getContext());
		header.setPadding(0, LocalDisplay.dp2px(15), 0, 0);
		// header.initWithStringArray(R.array.bluetooth);
		header.initWithPointList(getPointList());

		frameLayout.setDurationToCloseHeader(1000);
		frameLayout.setHeaderView(header);
		frameLayout.addPtrUIHandler(header);

		frameLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				frameLayout.autoRefresh(false);
			}
		}, 100);

		frameLayout.setPtrHandler(new PtrHandler() {

			@Override
			public void onRefreshBegin(final PtrFrameLayout frame) {
				// TODO Auto-generated method stub
				updateDevice();
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				// TODO Auto-generated method stub
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						content, header);
			}
		});

		// Use this check to determine whether BLE is supported on the device.
		// Then
		// you can selectively disable BLE-related features.
		if (!getContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(getContext(), "该设备不支持BLE功能", Toast.LENGTH_SHORT)
					.show();
			getContext().finish();
		}

		// Initializes Bluetooth adapter.
		final BluetoothManager bluetoothManager = (BluetoothManager) getContext()
				.getSystemService(getContext().BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		// Ensures Bluetooth is available on the device and it is enabled. If
		// not,
		// displays a dialog requesting user permission to enable Bluetooth.
		if (mBluetoothAdapter == null) {
			Toast.makeText(getContext(), "该设备不支持蓝牙功能", Toast.LENGTH_SHORT)
					.show();
			getContext().finish();
		}

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (!mBluetoothAdapter.isEnabled()) {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
		// 加载BLE列表
		mLeDeviceListAdapter = new LeDeviceListAdapter(getContext());
		listDevice.setAdapter(mLeDeviceListAdapter);
		listDevice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position >= 0) {
					BluetoothDevice device = mLeDeviceListAdapter
							.getBluetoothDevice(position);
					if (mScanning) {
						mBluetoothAdapter.stopLeScan(mLeScanCallback);
						mScanning = false;
					}
					if (device == null)
						return;
					getContext().pushFragmentToBackStack(
							DeviceItemFragment.class, device.getAddress());
				}
			}
		});
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		mLeDeviceListAdapter.clear();
		mLeDeviceListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_ENABLE_BT
				&& resultCode == Activity.RESULT_CANCELED) {
			getContext().finish();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void updateDevice() {
		// TODO Auto-generated method stub
		frameLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mScanning = false;
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
				frameLayout.refreshComplete();
				// mLeDeviceListAdapter.notifyDataSetChanged();
			}
		}, SCAN_PERIOD);
		mLeDeviceListAdapter.clear();
		mLeDeviceListAdapter.notifyDataSetChanged();
		mScanning = true;
		mBluetoothAdapter.startLeScan(mLeScanCallback);
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		public void onLeScan(final BluetoothDevice device,
				final int rssi, final byte[] scanRecord) {
			getContext().runOnUiThread(new Runnable() {
				public void run() {
					mLeDeviceListAdapter.addDevice(device, rssi);
					mLeDeviceListAdapter.notifyDataSetChanged();
				}
			});
		};
	};

	@Override
	protected void addItemInfo(ArrayList<MenuItemInfo> itemInfos) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setupViews(View view) {
		// TODO Auto-generated method stub
		setHeaderTitle(mTitle);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.device_listview_content;
	}

	private ArrayList<float[]> getPointList() {
		// this point is taken from https://github.com/cloay/CRefreshLayout
		List<Point> startPoints = new ArrayList<Point>();
		startPoints.add(new Point(0, 27));
		startPoints.add(new Point(9, 18));
		startPoints.add(new Point(18, 9));
		startPoints.add(new Point(9, 0));
		startPoints.add(new Point(9, 18));
		startPoints.add(new Point(9, 36));
		startPoints.add(new Point(18, 27));
		startPoints.add(new Point(9, 18));

		startPoints.add(new Point(30, 12));
		startPoints.add(new Point(35, 6));
		startPoints.add(new Point(35, 28));
		startPoints.add(new Point(30, 20));
		startPoints.add(new Point(46, 7));
		startPoints.add(new Point(42, 9));
		startPoints.add(new Point(42, 12));
		startPoints.add(new Point(53, 8));
		startPoints.add(new Point(59, 8));
		startPoints.add(new Point(54, 12));
		startPoints.add(new Point(42, 17));
		startPoints.add(new Point(50, 6));
		startPoints.add(new Point(41, 21));
		startPoints.add(new Point(58, 21));
		startPoints.add(new Point(50, 26));
		startPoints.add(new Point(42, 22));
		startPoints.add(new Point(49, 26));

		startPoints.add(new Point(67, 7));
		startPoints.add(new Point(81, 5));
		startPoints.add(new Point(67, 11));
		startPoints.add(new Point(67, 11));
		startPoints.add(new Point(94, 11));
		startPoints.add(new Point(80, 12));
		startPoints.add(new Point(70, 16));
		startPoints.add(new Point(87, 14));
		startPoints.add(new Point(69, 21));
		startPoints.add(new Point(87, 18));
		startPoints.add(new Point(81, 21));
		startPoints.add(new Point(81, 28));
		startPoints.add(new Point(73, 24));
		startPoints.add(new Point(86, 24));

		startPoints.add(new Point(104, 11));
		startPoints.add(new Point(104, 11));
		startPoints.add(new Point(129, 11));
		startPoints.add(new Point(106, 18));
		startPoints.add(new Point(117, 6));

		List<Point> endPoints = new ArrayList<Point>();
		endPoints.add(new Point(9, 18));
		endPoints.add(new Point(18, 9));
		endPoints.add(new Point(9, 0));
		endPoints.add(new Point(9, 18));
		endPoints.add(new Point(9, 36));
		endPoints.add(new Point(18, 27));
		endPoints.add(new Point(9, 18));
		endPoints.add(new Point(0, 9));

		endPoints.add(new Point(39, 11));
		endPoints.add(new Point(35, 28));
		endPoints.add(new Point(31, 28));
		endPoints.add(new Point(39, 17));
		endPoints.add(new Point(41, 9));
		endPoints.add(new Point(42, 18));
		endPoints.add(new Point(48, 12));
		endPoints.add(new Point(59, 8));
		endPoints.add(new Point(59, 18));
		endPoints.add(new Point(59, 12));
		endPoints.add(new Point(59, 17));
		endPoints.add(new Point(50, 21));
		endPoints.add(new Point(58, 21));
		endPoints.add(new Point(50, 26));
		endPoints.add(new Point(40, 28));
		endPoints.add(new Point(49, 26));
		endPoints.add(new Point(60, 28));

		endPoints.add(new Point(94, 7));
		endPoints.add(new Point(81, 11));
		endPoints.add(new Point(67, 15));
		endPoints.add(new Point(94, 11));
		endPoints.add(new Point(94, 15));
		endPoints.add(new Point(70, 16));
		endPoints.add(new Point(83, 16));
		endPoints.add(new Point(69, 21));
		endPoints.add(new Point(91, 21));
		endPoints.add(new Point(93, 23));
		endPoints.add(new Point(81, 28));
		endPoints.add(new Point(74, 28));
		endPoints.add(new Point(66, 28));
		endPoints.add(new Point(95, 28));

		endPoints.add(new Point(106, 18));
		endPoints.add(new Point(129, 11));
		endPoints.add(new Point(127, 18));
		endPoints.add(new Point(127, 18));
		endPoints.add(new Point(117, 28));

		ArrayList<float[]> list = new ArrayList<float[]>();

		int offsetX = Integer.MAX_VALUE;
		int offsetY = Integer.MAX_VALUE;

		for (int i = 0; i < startPoints.size(); i++) {
			offsetX = Math.min(startPoints.get(i).x, offsetX);
			offsetY = Math.min(startPoints.get(i).y, offsetY);
		}
		for (int i = 0; i < endPoints.size(); i++) {
			float[] point = new float[4];
			point[0] = startPoints.get(i).x - offsetX;
			point[1] = startPoints.get(i).y - offsetY;
			point[2] = endPoints.get(i).x - offsetX;
			point[3] = endPoints.get(i).y - offsetY;
			list.add(point);
		}
		return list;
	}
}
