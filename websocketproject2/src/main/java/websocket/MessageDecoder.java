package websocket;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Bed> {

    @Override
    public Bed decode(String jsonMessage) throws DecodeException {
        Bed bed = new Bed();

        try {
            JsonObject jsonObject = Json.createReader(new StringReader(jsonMessage)).readObject();

            bed.setBedNumber(getIntOrNull(jsonObject, "bedNumber"));
            bed.setPatientName(jsonObject.getString("patientName"));
            bed.setStatus(jsonObject.getString("status"));
            bed.setStatusImagePath(jsonObject.getString("statusImagePath"));

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DecodeException(jsonMessage, "Error decoding JSON!", ex);
        }

        return bed;
    }

    private Integer getIntOrNull(JsonObject jsonObject, String key) {
        return jsonObject.containsKey(key) ? jsonObject.getInt(key) : null;
    }

    // Add similar methods for other data types if necessary

    @Override
    public boolean willDecode(String jsonMessage) {
        try {
            Json.createReader(new StringReader(jsonMessage)).readObject();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void init(EndpointConfig ec) {
        System.out.println("Initializing message decoder");
    }

    @Override
    public void destroy() {
        System.out.println("Destroyed message decoder");
    }
}
