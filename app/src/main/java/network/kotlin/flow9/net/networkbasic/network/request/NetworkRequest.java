package network.kotlin.flow9.net.networkbasic.network.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import network.kotlin.flow9.net.networkbasic.network.NetworkManager;

public abstract class NetworkRequest<T> implements Runnable {

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final int DEFAULT_TIMEOUT = 10000;
    private NetworkManager manager;
    private NetworkManager.OnResultListener mListener;
    private int connectionTimeout = DEFAULT_TIMEOUT;
    private int readTimeout = DEFAULT_TIMEOUT;
    private HttpURLConnection mConn;

    private T result;
    private int errorCode = -1;
    private int retry = 3;

    private boolean isCanceled = false;

    /**
     * 데이터를 얻어오기 위한 URL
     */
    abstract public URL getURL() throws MalformedURLException;

    /**
     * 얻어온 데이터를 처리하는 메소드
     */
    abstract public T parse(InputStream is) throws IOException;

    public void setOnResultListener(NetworkManager.OnResultListener<T> listener) {
        mListener = listener;
    }

    public void setNetworkManager(NetworkManager manager) {
        this.manager = manager;
    }

    public String getRequestMethod() {
        return METHOD_GET;
    }

    /**
     * 네트워크 요청시 서버로 전달할 값을 정의하기 위한 메소드
     */
    public void setRequestHeader(HttpURLConnection conn) {
    }

    public void setConfiguration(HttpURLConnection conn) {
    }

    public void writeOutput(OutputStream conn) {
    }


    /**
     * 처리 결과를 요청한 객체에 전달하기 위한 처리를 해 주는 메소드
     */
    public void sendSuccess() {
        if (!isCanceled) {
            if (mListener != null) {
                mListener.onSuccess(this, result);
            }
        } else {
            manager.processCancel(this);
        }

    }

    public void sendFail() {
        if (!isCanceled) {
            if (mListener != null) {
                mListener.onFail(this, errorCode);
            }
        } else {
            manager.processCancel(this);
        }

    }

    /**
     * 취소
     */
    public void setCancel() {
        isCanceled = true;
        if (mConn != null) {
            mConn.disconnect();
        }
        manager.processCancel(this);
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    @Override
    public void run() {
        int retryCount = retry;
        while (retryCount > 0 && !isCanceled) {
            try {
                URL url = getURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(connectionTimeout);
                conn.setReadTimeout(readTimeout);
                String requestMethod = getRequestMethod();
                if (requestMethod.equals(METHOD_POST)) {
                    conn.setDoOutput(true);
                }
                conn.setRequestMethod(requestMethod);
                setConfiguration(conn);
                setRequestHeader(conn);
                if (conn.getDoOutput()) {
                    writeOutput(conn.getOutputStream());
                }
                if (isCanceled) continue;
                int responseCode = conn.getResponseCode();
                if (isCanceled) continue;
                mConn = conn;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    result = parse(is);
                    manager.sendSuccess(this);
                    return;
                } else {
                    retryCount = 0;
                }
            } catch (MalformedURLException e) {
                retryCount = 0;
            } catch (IOException e) {
                e.printStackTrace();
                retryCount--;
            }
            manager.sendFail(this);
        }
    }
}
