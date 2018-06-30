package network.kotlin.flow9.net.networkbasic.network.request;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import network.kotlin.flow9.net.networkbasic.domain.Melon;

public class MelonRequest extends NetworkRequest<Melon> {

    private int page;
    private int count;

    public MelonRequest(int page, int count) {
        this.page = page;
        this.count = count;
    }

    @Override
    public URL getURL() throws MalformedURLException {
       // String urlText = MELON_CHART_URL + String.format(MELON_CHART_PARAMS, VERSION, page, count);
        return new URL("");
    }

    @Override
    public void setRequestHeader(HttpURLConnection conn) {
        super.setRequestHeader(conn);
        conn.setRequestProperty("Accept", "" /* HEADER_ACCEPT_JSON */);
        conn.setRequestProperty("appKey", "" /* HEADER_APPKEY */);
    }

    @Override
    public Melon parse(InputStream is) throws IOException {
        // Gson gson = new Gson();
        // MelonResult result = gson.fromJson(new InputStreamReader(is), MelonResult.class);
        return null;
    }


}
