package com.six.hats.brand.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.telephony.TelephonyManager;

import androidx.core.net.ConnectivityManagerCompat;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionManager {

    private final int STATE_FRESH = 0;
    private final int STATE_LAUNCHED = 1;
    private final int STATE_BACKGROUND = 2;

    private int currentLaunchState = STATE_FRESH;

    public static final String CLASS_4G = "4g";
    public static final String CLASS_3G = "3g";
    public static final String CLASS_2G = "2g";
    public static final String CLASS_WIFI = "wifi";

    private static ConnectionManager instance;

    private ConnectivityManager connectivityManager;
    private List<OnNetworkChangeListener> onNetworkChangeListeners;
    private TelephonyManager mTelephonyManager;
    private boolean connected;
    private String activityName;
    private Handler mHandler = new Handler();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ExecutorService externalExecutorService = null;
    private int networkType = -1;
    private String networkClass;

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, final Intent intent) {
            ExecutorService es = externalExecutorService == null ? executorService : externalExecutorService;
            es.submit(new Runnable() {
                @Override
                public void run() {
                    final NetworkInfo networkInfoFromBroadcast = ConnectivityManagerCompat
                            .getNetworkInfoFromBroadcast(connectivityManager, intent);
                    networkType = mTelephonyManager.getNetworkType();
                    final boolean isConnected = networkInfoFromBroadcast != null ? networkInfoFromBroadcast.isConnected() : false;
                    final String newNetworkClass = getNetworkClass(networkInfoFromBroadcast);
                    if (newNetworkClass != networkClass || connected != isConnected) {
                        networkClass = newNetworkClass;
                        connected = isConnected;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isConnected == connected && networkClass == newNetworkClass) {
                                    notifyNetworkChanged(networkInfoFromBroadcast);
                                }
                            }
                        });
                    }
                }
            });
        }
    };

    public void setExecutorService(ExecutorService executorService) {
        if (this.executorService != null && executorService != null) {
            this.executorService.shutdown();
            this.executorService = null;
            this.externalExecutorService = executorService;
        }
    }

    private ConnectionManager() {
        onNetworkChangeListeners = new LinkedList<>();
    }


    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }

    public void onCreate(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        context.registerReceiver(networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        //notifyNetworkChanged(connectivityManager.getActiveNetworkInfo());
        ExecutorService es = externalExecutorService == null ? executorService : externalExecutorService;
        es.submit(new Runnable() {
            @Override
            public void run() {
                forceNetworkNotification();
            }
        });
    }

    public void forceNetworkNotification() {
        notifyNetworkChanged(connectivityManager.getActiveNetworkInfo());
    }

    public void onDestroy(Context context) {
        context.unregisterReceiver(networkChangeReceiver);
    }

    public static interface OnNetworkChangeListener {
        public void onNetworkChanged(NetworkInfo networkInfo, String networkClass, boolean connected);
    }

    public boolean isConnected() {
        return this.connected;
    }

    private void notifyNetworkChanged(NetworkInfo networkInfo) {
        final String networkClass = getActiveNetworkInfo();
        final List<OnNetworkChangeListener> listeners = new LinkedList<>(this.onNetworkChangeListeners);
        for (OnNetworkChangeListener listener : listeners) {
            listener.onNetworkChanged(networkInfo, networkClass, connected);
        }
    }

    public void registerOnNetworkChangeListener(OnNetworkChangeListener listener) {
        onNetworkChangeListeners.add(listener);
    }

    public void unRegisterOnNetworkChangeListener(OnNetworkChangeListener listener) {
        onNetworkChangeListeners.remove(listener);
    }

    public String getActiveNetworkInfo() {
        try {
            String networkClass = this.networkClass;
            if (networkClass == null || !connected) {
                return "OFFLINE";
            } else {
                return networkClass;
            }
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }


    private String getNetworkClass(NetworkInfo networkInfo) {
        String networkName;
        switch (networkInfo.getType()) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
                networkName = CLASS_WIFI;
                break;
            default:
                networkName = getNetworkClassInternal();
        }
        return networkName;
    }

    private String getNetworkClassInternal() {
        switch (networkType) {
            case -1:
                return "UNKNOWN";
            case 0:
                return null;
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return CLASS_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return CLASS_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return CLASS_4G;
            default:
                return CLASS_WIFI;
        }
    }


    public void onActivityStart(final Activity activity) {
        switch (currentLaunchState) {
            case STATE_BACKGROUND:
                forceNetworkNotification();
        }
        currentLaunchState = STATE_LAUNCHED;
        this.activityName = activity.toString();
    }

    public void onActivityStop(Activity activity) {
        if (activity.toString().equalsIgnoreCase(this.activityName)) {
            currentLaunchState = STATE_BACKGROUND;
        }
    }

}
