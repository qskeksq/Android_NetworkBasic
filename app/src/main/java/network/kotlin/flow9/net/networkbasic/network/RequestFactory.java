package network.kotlin.flow9.net.networkbasic.network;

import network.kotlin.flow9.net.networkbasic.domain.Melon;
import network.kotlin.flow9.net.networkbasic.network.request.MelonRequest;
import network.kotlin.flow9.net.networkbasic.network.request.NetworkRequest;

public class RequestFactory {

    public static NetworkRequest getMelonRequest() {
        return new MelonRequest(1, 10);
    }

    public static NetworkRequest getMelonRequest(NetworkManager.OnResultListener<Melon> listener) {
        MelonRequest request = new MelonRequest(1, 10);
        request.setOnResultListener(listener);
        return request;
    }

}
