    let randomWord = ''; // Kelimeyi burada saklayacağız

    const users = [];
    let currentUserIndex = 0;


    // Join butonuna tıklanınca
    document.getElementById('joinButton').addEventListener('click', () => {
    const userName = currentUsername;



      // Kullanıcıyı diziye ekle
      users.push(userName);

      // Join butonunu gizle
      document.getElementById('joinButton').style.display = 'none';

      // Oyunla ilgili elemanları göster
      document.getElementById('drawingCanvas').style.display = 'block';
      document.getElementById('guessInput').style.display = 'block';
      document.getElementById('guessButton').style.display = 'block';
      document.getElementById('randomWord').style.display = 'block';
      document.getElementById('resultMessage').style.display = 'block';

      // 5 saniyede bir API'den rastgele kelime çek
      setInterval(fetchRandomWord, 5000);
      setInterval(showNext,5000);
      // İlk kullanıcıyı göster


      // WebSocket bağlantısını başlat (initWebSocket fonksiyonunu kendi ihtiyaçlarına göre doldur)
      initWebSocket();
    });

    // Sıradaki kullanıcıyı gösteren fonksiyon
    function showNext() {
      if (users.length === 0) return;
      const currentUser = users[currentUserIndex];
        console.log(`Sıradaki kullanıcı: ${currentUser}`);

      currentUserIndex = (currentUserIndex + 1) % users.length;
    }

    // API'den rastgele kelime çeken fonksiyon
    function fetchRandomWord() {
      console.log('Yeni kelime alınıyor...');
      fetch('/api/random-word')
        .then(response => {
          if (!response.ok) throw new Error('Ağ hatası');
          return response.text();
        })
        .then(word => {
          randomWord = word.trim().toLowerCase();
          document.getElementById('randomWordDisplay').innerText = randomWord;
          // Yeni kelimeyle birlikte sıradaki kullanıcıyı göster
          showNext();
        })
        .catch(error => {
          console.error('Kelime alınamadı:', error);
        });
    }

    // Tahmin butonuna basıldığında
    document.getElementById('guessButton').addEventListener('click', () => {
        const guess = document.getElementById('guessInput').value.trim().toLowerCase();

        // Eğer kelime alınmadıysa bir hata mesajı göster
        if (!randomWord) {
            document.getElementById('resultMessage').innerText = '❌ Kelime henüz alınmadı!';
            document.getElementById('resultMessage').style.color = 'red';
            return;
        }

        // Tahmin doğru mu kontrol et
        const messageElement = document.getElementById('resultMessage');

        if (guess === randomWord) {
            messageElement.innerText = "✅ Success! Tahmin doğru.";
            messageElement.style.color = "green";
            console.log("✅ Success! Tahmin doğru."); // Console'a yazdır

        } else {
            messageElement.innerText = `❌ Yanlış tahmin. Doğru kelime: ${randomWord}`;
            messageElement.style.color = "red";
            console.log(`❌ Yanlış tahmin.`); // Console'a yazdır

        }
    });

    // WebSocket bağlantısını başlatan fonksiyon
 function initWebSocket() {
     const socket = new SockJS('/ws'); // WebSocket endpoint
     const stompClient = Stomp.over(socket);

     stompClient.connect({}, function () {
         // Sunucudan gelen çizim verilerini dinle
         stompClient.subscribe('/topic/draw', function (message) {
             const data = JSON.parse(message.body);

             if (data.type === 'start') {
                 ctx.beginPath();
                 ctx.moveTo(data.x, data.y);
             } else if (data.type === 'draw') {
                 ctx.lineTo(data.x, data.y);
                 ctx.stroke();
                 ctx.beginPath();
                 ctx.moveTo(data.x, data.y);
             }
         });
     });

     const canvas = document.getElementById('drawingCanvas');
     const ctx = canvas.getContext('2d');

     let drawing = false;

     // Yeni çizime başlama
     canvas.addEventListener('mousedown', (e) => {
         drawing = true;
         const x = e.offsetX;
         const y = e.offsetY;
         ctx.beginPath();
         ctx.moveTo(x, y);
         sendStartDraw(x, y, stompClient);
     });

     // Çizim devam ediyor
     canvas.addEventListener('mousemove', (e) => {
         if (!drawing) return;
         const x = e.offsetX;
         const y = e.offsetY;
         ctx.lineTo(x, y);
         ctx.stroke();
         ctx.beginPath();
         ctx.moveTo(x, y);
         sendDraw(x, y, stompClient);
     });

     // Çizim bitti
     canvas.addEventListener('mouseup', () => {
         drawing = false;
         ctx.beginPath(); // Yeni yolu başlat
     });

     canvas.addEventListener('mouseout', () => {
         drawing = false;
         ctx.beginPath();
     });

     // Yeni çizim başlangıcını gönder
     function sendStartDraw(x, y, client) {
         const data = { type: 'start', x, y };
         client.send("/app/draw", {}, JSON.stringify(data));
     }

     // Çizim noktasını gönder
     function sendDraw(x, y, client) {
         const data = { type: 'draw', x, y };
         client.send("/app/draw", {}, JSON.stringify(data));
     }
 }
