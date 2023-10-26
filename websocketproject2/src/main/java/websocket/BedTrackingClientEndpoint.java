package websocket;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint(
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class)
public class BedTrackingClientEndpoint {
    private static CountDownLatch latch;
    private BedMessageListener listener;

    @OnOpen
    public void onOpen(Session session) throws EncodeException {
        System.out.println("--- Connected " + session.getId());
        try {
            session.getBasicRemote().sendText("start");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @OnMessage
    public void onMessage(Bed bed, Session session) throws IOException, EncodeException {
        if (this.listener != null) {
            this.listener.actionPerformed(bed);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Session " + session.getId() + " closed because " + closeReason);
        latch.countDown();
    }

    public void addMessageListener(BedMessageListener listener) {
        this.listener = listener;
    }

    public static void main(String[] args) throws EncodeException {
        new HomeFrame();
    }
}
