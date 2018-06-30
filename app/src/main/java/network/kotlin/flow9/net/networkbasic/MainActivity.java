package network.kotlin.flow9.net.networkbasic;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import network.kotlin.flow9.net.networkbasic.domain.Melon;
import network.kotlin.flow9.net.networkbasic.domain.Song;
import network.kotlin.flow9.net.networkbasic.network.NetworkManager;
import network.kotlin.flow9.net.networkbasic.network.RequestFactory;
import network.kotlin.flow9.net.networkbasic.network.request.NetworkRequest;
import network.kotlin.flow9.net.networkbasic.util.NetworkUtil;

public class MainActivity extends AppCompatActivity {

    private String htmlStr;
    private TextView htmlText;
    private long downloadId;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            htmlText.setText(htmlStr);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        htmlText = findViewById(R.id.htmltext);
    }

    private void downloadHtml() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                htmlStr = NetworkUtil.downloadHTML(NetworkUtil.ADDRESS);
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (downloadId != 0) {
            unregisterReceiver(NetworkUtil.downloadComplete);
        }
        downloadId = 0;
    }

    public void getMelonChart() {
        NetworkRequest request = RequestFactory.getMelonRequest();
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<Melon>() {
            @Override
            public void onSuccess(NetworkRequest<Melon> request, Melon result) {

            }

            @Override
            public void onFail(NetworkRequest<Melon> request, int code) {

            }
        });
    }

}
