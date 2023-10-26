package websocket;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/bed",
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class)
public class ChatServerEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected, sessionID = " + session.getId());
    }

    @OnMessage
    public void onMessage(Bed message, Session session) throws IOException, EncodeException {
        for (Session clientSession : session.getOpenSessions()) {
            if (clientSession.isOpen()) {
                try {
                    clientSession.getBasicRemote().sendObject(message);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Session " + session.getId() + " closed because " + closeReason);
    }
}
