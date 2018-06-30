package network.kotlin.flow9.net.networkbasic.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Build;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;

import network.kotlin.flow9.net.networkbasic.MainActivity;

public class NetworkUtil {

    public static final String ADDRESS = "http://www.googlg.com";

    public static ConnectivityManager cm;

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static NetworkInfo getNetworkInfo(Context context, Network network) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(network);
    }

    public static NetworkInfo[] getAllNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getAllNetworkInfo();
    }

    public static void permitMainThreadNetwork() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitNetwork()
                .build();
        StrictMode.setThreadPolicy(policy);
    }

    /**
     * get
     */
    public static void get() {
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * post
     */
    public static void post(String address) {
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(0);

            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            // os.write();
            // os.flush();

            InputStream is = new BufferedInputStream(conn.getInputStream());
            // read
            os.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * simple HTTP communication
     */
    public static String downloadHTML(String address) {
        String html = "";
        try {
            URL url = new URL(address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection != null) {
                urlConnection.setConnectTimeout(50000);
                urlConnection.setUseCaches(false);
                urlConnection.setDoInput(true);
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    String line = br.readLine();
                    while (line != null) {
                        html += line;
                        html += "\n";
                        line = br.readLine();
                    }
                    isr.close();
                    br.close();
                }
                urlConnection.disconnect();
            }
        } catch (NetworkOnMainThreadException e) {
            Log.e("네트워크 메인스레드 오류", e.getMessage());
        } catch (MalformedURLException e) {
            Log.e("네트워크 URL 오류", e.getMessage());
        } catch (IOException e) {
            Log.e("네트워크 스트림 오류", e.getMessage());
        }
        return html;
    }

    /**
     * download image by HTTP communication
     */
    public static boolean downloadImageOne(Context context, String address, String filename) {
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(50000);
            int length = conn.getContentLength();
            byte[] buffer = new byte[length];
            InputStream is = conn.getInputStream();
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            int data = is.read(buffer);
            while (data > 0) {
                fos.write(buffer, 0, data);
                data = is.read(buffer);
            }
            is.close();
            fos.close();
            conn.disconnect();
        } catch (NetworkOnMainThreadException e) {
            Log.e("네트워크 메인스레드 오류", e.getMessage());
            return false;
        } catch (MalformedURLException e) {
            Log.e("네트워크 URL 오류", e.getMessage());
            return false;
        } catch (IOException e) {
            Log.e("네트워크 스트림 오류", e.getMessage());
            return false;
        }
        return new File(context.getFilesDir().getAbsolutePath(), filename).exists();
    }

    /**
     * download image by download manager
     */
    public static long downloadImageTwo(Context context, String address, BroadcastReceiver br) {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(address);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("다운로드");
        request.setDescription("이미지 다운로드");
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_MOBILE |
                        DownloadManager.Request.NETWORK_WIFI);
        long networkId = dm.enqueue(request);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(br, filter);
        return networkId;
    }

    /**
     * receives when download is complete
     */
    public static BroadcastReceiver downloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "다운로드 완료", Toast.LENGTH_LONG).show();
        }
    };


    /**
     * monitor network state around all API Lewvel
     */
    public static void registerNetworkStateListener(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkStateReceiver, intentFilter);
    }

    static BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "네트워크 상태 변화 감지", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * monitor network state API Level over 21(Android 5.0)
     */
    public static void registerNetworkStateCallback(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            cm.registerNetworkCallback(builder.build(), networkCallback);
        }
    }

    /**
     *
     */
    static ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {

        @Override
        public void onAvailable(Network network) {
            NetworkInfo info = cm.getNetworkInfo(network);
            String networkTypeName = info.getTypeName();
        }

        @Override
        public void onLost(Network network) {

        }
    };

    /**
     * Connection Type
     */
    public static String getConnectionType(Context context) {
        String type = "";
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                type = "wifi";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                type = "mobile";
            }
        } else {

        }
        return type;
    }


}
