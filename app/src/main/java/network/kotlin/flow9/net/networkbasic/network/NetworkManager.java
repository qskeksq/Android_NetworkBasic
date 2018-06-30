package network.kotlin.flow9.net.networkbasic.network;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import network.kotlin.flow9.net.networkbasic.network.request.NetworkRequest;

public class NetworkManager {

    private static NetworkManager instance;
    // ThreadPool
    private ThreadPoolExecutor mExecutor;
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 64;
    private static final int KEEP_ALIVE_TIME = 5000;
    private LinkedBlockingQueue<Runnable> mRequestQueue = new LinkedBlockingQueue<>();
    // Callback Message
    public static final int MESSAGE_SEND_SUCCESS = 1;
    public static final int MESSAGE_FAIL = 2;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    public NetworkManager() {
        mExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                mRequestQueue
        );
    }

    /**
     * 메인 스레드로 네트워크 처리 결과를 전달하기 위한 handler
     */
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            NetworkRequest request = (NetworkRequest) msg.obj;
            switch (msg.what) {
                case MESSAGE_SEND_SUCCESS:
                    request.sendSuccess();
                    break;
                case MESSAGE_FAIL:
                    request.sendFail();
                    break;
            }
        }
    };

    public void sendSuccess(NetworkRequest request) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SEND_SUCCESS, request));
    }

    public void sendFail(NetworkRequest request) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, request));
    }

    /**
     * 처리 결과를 전달하기 위한 리스너
     */
    public interface OnResultListener<T> {
        public void onSuccess(NetworkRequest<T> request, T result);
        public void onFail(NetworkRequest<T> request, int code);
    }

    /**
     * 별도의 스레드로 NetworkRequest 처리
     */
    public <T> void getNetworkData(NetworkRequest<T> request, OnResultListener<T> listener) {
        request.setOnResultListener(listener);
        getNetworkData(request);
    }

    public <T> void getNetworkData(NetworkRequest<T> request) {
        request.setNetworkManager(this);
        mExecutor.execute(request);
    }

    /**
     * 취소
     */
    public void processCancel(NetworkRequest request) {
        mRequestQueue.remove(request);
    }

}
