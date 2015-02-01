package fi.iki.elonen;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

public class WebSocketResponseHandler {
    public static final String HEADER_UPGRADE = "upgrade";
    public static final String HEADER_UPGRADE_VALUE = "websocket";
    public static final String HEADER_CONNECTION = "connection";
    public static final String HEADER_CONNECTION_VALUE = "Upgrade";
    public static final String HEADER_WEBSOCKET_VERSION = "sec-websocket-version";
    public static final String HEADER_WEBSOCKET_VERSION_VALUE = "13";
    public static final String HEADER_WEBSOCKET_KEY = "sec-websocket-key";
    public static final String HEADER_WEBSOCKET_ACCEPT = "sec-websocket-accept";
    public static final String HEADER_WEBSOCKET_PROTOCOL = "sec-websocket-protocol";

    public final static String WEBSOCKET_KEY_MAGIC = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    private final IWebSocketFactory webSocketFactory;

    public WebSocketResponseHandler(IWebSocketFactory webSocketFactory) {
        this.webSocketFactory = webSocketFactory;
    }

    public Response serve(final IHTTPSession session) {
        Map<String, String> headers = session.getHeaders();
        if (isWebsocketRequested(session)) {
            if (!HEADER_WEBSOCKET_VERSION_VALUE.equalsIgnoreCase(headers.get(HEADER_WEBSOCKET_VERSION))) {
                return new Response(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT,
                        "Invalid Websocket-Version " + headers.get(HEADER_WEBSOCKET_VERSION));
            }

            if (!headers.containsKey(HEADER_WEBSOCKET_KEY)) {
                return new Response(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT,
                        "Missing Websocket-Key");
            }

            WebSocket webSocket = webSocketFactory.openWebSocket(session);
            Response handshakeResponse = webSocket.getHandshakeResponse();
            try {
                handshakeResponse.addHeader(HEADER_WEBSOCKET_ACCEPT, makeAcceptKey(headers.get(HEADER_WEBSOCKET_KEY)));
            } catch (NoSuchAlgorithmException e) {
                return new Response(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT,
                        "The SHA-1 Algorithm required for websockets is not available on the server.");
            }

            if (headers.containsKey(HEADER_WEBSOCKET_PROTOCOL)) {
                handshakeResponse.addHeader(HEADER_WEBSOCKET_PROTOCOL, headers.get(HEADER_WEBSOCKET_PROTOCOL).split(",")[0]);
            }

            return handshakeResponse;
        } else {
            return null;
        }
    }

    protected boolean isWebsocketRequested(IHTTPSession session) {
        Map<String, String> headers = session.getHeaders();
        String upgrade = headers.get(HEADER_UPGRADE);
        boolean isCorrectConnection = isWebSocketConnectionHeader(headers);
        boolean isUpgrade = HEADER_UPGRADE_VALUE.equalsIgnoreCase(upgrade);
        return (isUpgrade && isCorrectConnection);
    }

    private boolean isWebSocketConnectionHeader(Map<String, String> headers) {
        String connection = headers.get(HEADER_CONNECTION);
        return (connection != null && connection.toLowerCase().contains(HEADER_CONNECTION_VALUE.toLowerCase()));
    }

    public static String makeAcceptKey(String key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        String text = key + WEBSOCKET_KEY_MAGIC;
        md.update(text.getBytes(), 0, text.length());
        byte[] sha1hash = md.digest();
        return Base64.getEncoder().encodeToString(sha1hash);
    }

}
