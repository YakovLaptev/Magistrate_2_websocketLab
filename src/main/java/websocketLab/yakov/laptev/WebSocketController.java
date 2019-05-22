package websocketLab.yakov.laptev;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class WebSocketController {

    private TXTSocketHandler txtSocketHandler;

    public WebSocketController(TXTSocketHandler txtSocketHandler) {
        this.txtSocketHandler = txtSocketHandler;
    }

    @GetMapping("/ws/users")
    public List<String> connectedEquipments() {
        return this.txtSocketHandler.sessions
                .stream()
                .map(WebSocketSession::getId)
                .collect(Collectors.toList());
    }
}