let authCookie = Cookies.get("Authorization");

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/travel-shooting-websocket',
    connectHeaders: {
        "Authorization": authCookie
    }
});

let chatRoomId = null;
let page = 0;

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/sub/chat/room/' + chatRoomId, (message) => {
        showMessage(JSON.parse(message.body).body);
    });
    getChatMessagesOnConnect(page);
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
    alert("웹소켓 연결 오류");
    window.location.reload();
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
    alert("STOMP 오류");
    window.location.reload();
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#chat-messages").html("");
    $(".button-wrapper").html('<button id="get-more-chat-btn">채팅 내역 더 불러오기</button>');
    page = 0;
}

function connect() {
    chatRoomId = $("#room-number").val();
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    let message = $("#message");
    stompClient.publish({
        destination: "/pub/message/" + chatRoomId,
        body: JSON.stringify({
            'content': message.val()
        })
    });
    message.val('');
}

function showMessage(message) {
    $("#chat-messages").append("<tr><td>"
        + "<div class='row'>"
        + "<div class='col-md-2 message-sender'>"
        + message.senderName
        + "</div>"
        + "<div class='col-md-8 message-content' data-chat-message-id='" + message.chatMessageId + "'>"
        + message.content
        + "</div>"
        + "<div class='col-md-2 message-time'>"
        + message.simpleTime
        + "</div>"
        + "</div>"
        + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendMessage());
    $(document).on("click", "#get-more-chat-btn", function () {
        getChatMessages(page);
    });
});

function showPastMessage(message) {
    $("#chat-messages").prepend("<tr><td>"
        + "<div class='row'>"
        + "<div class='col-md-2 message-sender'>"
        + message.senderName
        + "</div>"
        + "<div class='col-md-8 message-content' data-chat-message-id='" + message.chatMessageId + "'>"
        + message.content
        + "</div>"
        + "<div class='col-md-2 message-time'>"
        + message.simpleTime
        + "</div>"
        + "</div>"
        + "</td></tr>");
}

function getChatMessagesOnConnect(currentPage) {
    $.ajax({
        url: "/api/chat-room/" + chatRoomId + "/paging?page=" + currentPage + "&size=10",
        type: "GET",
        header: {"Authorization": authCookie}
    })
        .done(function (response) {
            response.forEach(function (message) {
                showPastMessage(message);
            })
            // page++;
        })
        .fail(function (response) {
            console.log(response);
            if (response.responseJSON.message === "조회할 데이터가 없습니다.") {
                $(".button-wrapper").html('<p>마지막 채팅입니다.</p>');
            }
        })
}

function getChatMessages() {
    let oldestId = $(".message-content").first().data("chat-message-id") || 0;
    let params = {
        chatMessageId: oldestId,
        pageSize: 10
    }
    let queryString = $.param(params);
    $.ajax({
        url: "/api/chat-room/" + chatRoomId + "?" + queryString,
        type: "GET",
        header: {"Authorization": authCookie}
    })
        .done(function (response) {
            response.forEach(function (message) {
                showPastMessage(message);
            })
        })
        .fail(function (response) {
            console.log(response);
            if (response.responseJSON.message === "조회할 데이터가 없습니다.") {
                $(".button-wrapper").html('<p>마지막 채팅입니다.</p>');
            }
        })
}