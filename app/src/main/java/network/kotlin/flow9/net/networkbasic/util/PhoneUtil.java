package network.kotlin.flow9.net.networkbasic.util;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class PhoneUtil {

    public static TelephonyManager sTelManager;
    public static String TAG = "PhoneUtil";

    public static void setPhoneStateListener(Context context) {
        if(sTelManager == null) {
            sTelManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        sTelManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public static void getNetworkInfo(Context context) {
        if(sTelManager == null) {
            sTelManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        Log.d(TAG, sTelManager.getNetworkCountryIso());
        Log.d(TAG, sTelManager.getNetworkOperatorName());
        if (sTelManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
            Log.d(TAG, "LTE");
        }
        if (sTelManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA) {
            Log.d(TAG, "3G");
        }
    }

    public static String getLocale(Context context) {
        // Locale myLocale = context.getResources().getConfiguration().locale.getDefault();
        // Locale myLocale2 = Locale.getDefault();
        return null;
    }

    public static String getPhoneNumber(Context context) {
        if(sTelManager == null) {
            sTelManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        return sTelManager.getLine1Number();
    }

    public static String getSubscriberId(Context context) {
        if(sTelManager == null) {
            sTelManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        return sTelManager.getSubscriberId();
    }

    public static String getDeviceId(Context context) {
        if(sTelManager == null) {
            sTelManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        return sTelManager.getDeviceId();
    }

    static PhoneStateListener phoneStateListener = new PhoneStateListener() {

        // 서비스 상태 변경 감지
        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            switch (serviceState.getState()) {
                case ServiceState.STATE_EMERGENCY_ONLY:
                    break;
                case ServiceState.STATE_IN_SERVICE:
                    break;
                case ServiceState.STATE_OUT_OF_SERVICE:
                    break;
                case ServiceState.STATE_POWER_OFF:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onMessageWaitingIndicatorChanged(boolean mwi) {
            super.onMessageWaitingIndicatorChanged(mwi);
        }

        @Override
        public void onCallForwardingIndicatorChanged(boolean cfi) {
            super.onCallForwardingIndicatorChanged(cfi);
        }

        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
        }

        // 전화가 걸려온 상태 감지
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
            }
        }

        @Override
        public void onDataConnectionStateChanged(int state) {
            super.onDataConnectionStateChanged(state);
        }

        @Override
        public void onDataConnectionStateChanged(int state, int networkType) {
            super.onDataConnectionStateChanged(state, networkType);
        }

        @Override
        public void onDataActivity(int direction) {
            super.onDataActivity(direction);
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
        }

        @Override
        public void onCellInfoChanged(List<CellInfo> cellInfo) {
            super.onCellInfoChanged(cellInfo);
        }

        @Override
        public void onUserMobileDataStateChanged(boolean enabled) {
            super.onUserMobileDataStateChanged(enabled);
        }
    };

}
