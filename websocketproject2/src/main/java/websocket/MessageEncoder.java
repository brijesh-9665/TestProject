package websocket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Bed> {

    @Override
    public String encode(Bed bed) throws EncodeException {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("bedNumber", bed.getBedNumber())
                .add("patientName", bed.getPatientName())
                .add("status", bed.getStatus())
                .add("availability", bed.isAvailability())
                .add("statusImagePath", bed.getStatusImagePath())
                .build();

        return jsonObject.toString();
    }

    @Override
    public void init(EndpointConfig config) {
        System.out.println("Initializing message encoder");
    }

    @Override
    public void destroy() {
        System.out.println("Destroying encoder...");
    }
}
