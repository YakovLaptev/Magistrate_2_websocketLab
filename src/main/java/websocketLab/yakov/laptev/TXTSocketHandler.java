package websocketLab.yakov.laptev;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TXTSocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(message.getPayload());
        String text = node.get("msg").asText();
        String id = node.get("sessionUserId").asText();
        String json = mapper.writeValueAsString(new Message(text, id));
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.getId().equals(session.getId()) || webSocketSession.getId().equals(id) || Objects.equals(id, "-1"))
                webSocketSession.sendMessage(new TextMessage(json));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        ObjectMapper mapper = new ObjectMapper();
        User user = new User(session.getId());
        String json = mapper.writeValueAsString(user);
        String newuser = mapper.writeValueAsString(new Message("NEW USER: "+session.getId(), session.getId()));
        for (WebSocketSession webSocketSession : sessions) {
            if (!webSocketSession.getId().equals(session.getId())) {
                webSocketSession.sendMessage(new TextMessage(newuser));
                webSocketSession.sendMessage(new TextMessage(json));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}