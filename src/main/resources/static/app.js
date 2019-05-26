sessionUserIdconst = socketConn = new WebSocket('ws://localhost:8080/txtSocketHandler');

const updateUserList = function (userID) {
    $("#sessionUserId").empty();
    $.getJSON("/ws/users", function (data) {
        $("#sessionUserId").append("<option value =\"-1\">send to all users</option>");
        for (var i = 0, len = data.length; i < len; i++) {
            $("#sessionUserId").append('<option value = \"' + data[i] + '\">' + data[i] + "</option>");
            if($(".messagelist." + data[i]).length == 0) {
                $("#main-content").append("<div class='row'>\n" +
                    "        <div class='col-md-12'>\n" +
                    "            <table class='table table-striped'>\n" +
                    "                <thead>\n" +
                    "                <tr>\n" +
                    "                    <th>Dialog with " + data[i] + "</th>\n" +
                    "                </tr>\n" +
                    "                </thead>\n" +
                    "                <tbody class='messagelist " + data[i] + "'>\n" +
                    "                </tbody>\n" +
                    "            </table>\n" +
                    "        </div>\n" +
                    "    </div>");
            }
        }
    });
};

socketConn.onmessage = (e) => {
    console.log(e);
    if (JSON.parse(e.data).sessionId) {
        console.log("UPDATE LIST");
        updateUserList(JSON.parse(e.data).sessionId);
    }else {
        showMessage(JSON.parse(e.data).sessionUserId, JSON.parse(e.data).msg);
    }
};

socketConn.onopen = function () {
    alert("Connection established.");
};

socketConn.onclose = function (event) {
    if (event.wasClean) {
        alert('Connection closed clean');
    } else {
        alert('Disconnection');
    }
     alert('Code: ' + event.code + ' reason: ' + event.reason);
};

socketConn.onerror = function (error) {
    alert("error " + error.message);
};

$(document).ready(updateUserList());

function sendMessage() {
    const json = JSON.stringify({
        msg: $("#msg").val(),
        sessionUserId: $("#sessionUserId").val()
    });
    socketConn.send(json);
    updateUserList();
}

function showMessage(id, message) {
    console.log(id);
    $(".messagelist." + id).append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#send").click(function () {
        sendMessage();
    });
});
