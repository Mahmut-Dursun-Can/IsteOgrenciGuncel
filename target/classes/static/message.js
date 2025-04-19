    function goBack() {
        document.getElementById("chat-box").style.display = "none";
    }


    let stompClients = {};
    let messageHistories = {};

    function openMessagingPanel(targetUserId) {
        document.getElementById("chat-box").style.display = "block";
        const messageArea = document.getElementById("chat-messages");
        messageArea.innerHTML = '';
        if (messageHistories[targetUserId]) {
            messageHistories[targetUserId].forEach(msg => {
                showMessage(msg.content, msg.senderUsername);
            });
        }
        connect(targetUserId);
    }

    function connect(targetUserId) {
        if (!stompClients[targetUserId]) {
            const socket = new SockJS('/ws');
            stompClients[targetUserId] = Stomp.over(socket);
            stompClients[targetUserId].connect({}, function () {
                console.log(`STOMP bağlantısı kuruldu, ${targetUserId} için abone olunuyor...`);
                stompClients[targetUserId].subscribe('/topic/messages', function (messageOutput) {
                    console.log("Gelen mesaj:", messageOutput.body);
                    const message = JSON.parse(messageOutput.body);
                    if ((message.senderId == currentUserId && message.recipientId == targetUserId)
                        || (message.senderId == targetUserId && message.recipientId == currentUserId)) {
                        showMessage(message.content, message.senderUsername);
                        if (!messageHistories[targetUserId]) {
                            messageHistories[targetUserId] = [];
                        }
                        messageHistories[targetUserId].push({
                            content: message.content,
                            senderUsername: message.senderUsername
                        });
                    }
                });
            });
        }
        document.getElementById("sendBtn").onclick = function () {
            const content = document.getElementById("messageInput").value;
            console.log("Gönderilen mesaj:", content);
            stompClients[targetUserId].send("/app/chat.sendMessage", {}, JSON.stringify({
                senderId: currentUserId,
                senderUsername: currentUsername,
                recipientId: targetUserId,
                content: content,
                timestamp: new Date().toISOString()
            }));
            document.getElementById("messageInput").value = '';
        };
    }

    function showMessage(content, senderUsername) {
        const messageArea = document.getElementById("chat-messages");
        const msg = document.createElement("div");
        let senderSpan = document.createElement("strong");
        senderSpan.textContent = senderUsername + ": ";
        let contentSpan = document.createElement("span");
        contentSpan.textContent = content;
        msg.appendChild(senderSpan);
        msg.appendChild(contentSpan);
        messageArea.appendChild(msg);
        console.log("Mesaj ekleniyor:", senderUsername, content);
    }
