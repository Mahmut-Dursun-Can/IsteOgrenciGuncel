// qr-admin.js

let html5QrCode;


/**
 * İçerik bölümlerini gizler/gösterir.
 * QR bölümüne geçince taramayı başlatır, diğer bölümlere geçince durdurur.
 */
function showContent(id) {
  document.querySelectorAll('.content').forEach(c => c.classList.add('hidden'));
  document.getElementById(id).classList.remove('hidden');
  if (id === 'qr') startScanner();
  else stopScanner();
}

/**
 * Arka kamerayı zorlayarak QR taramayı başlatır.
 * Eğer environment kamerası yoksa fallback ile 'back'/'rear' etiketli kameraya geçer.
 */
function startScanner() {
  const reader = document.getElementById('reader');
  reader.style.display = 'block';
  html5QrCode = new Html5Qrcode('reader');

  const scannerConfig = {
    fps: 10,
    qrbox: { width: 250, height: 400 },
    videoConstraints: {
      facingMode: { exact: 'environment' },
      width: { ideal: 480 },
      height: { ideal: 640 }
    }
  };

  // 1) environment (arka kamera) ile başlat
  html5QrCode.start(
    scannerConfig.videoConstraints,
    scannerConfig,
    onScanSuccess,
    onScanError
  ).catch(err => {
    console.warn('Environment kamera erişilemedi:', err);
    // 2) Fallback: label'da 'back' veya 'rear' içeren kamerayı seç
    Html5QrCode.getCameras()
      .then(cameras => {
        const backCam = cameras.find(cam =>
          /back|rear/i.test(cam.label)
        );
        const camId = backCam ? backCam.id : cameras[0].id;
        html5QrCode.start(
          camId,
          { fps: 10, qrbox: { width: 250, height: 400 } },
          onScanSuccess,
          err => console.warn('Fallback tarama hatası:', err)
        );
      })
      .catch(fbErr => console.error('Kamera listesi alınamadı:', fbErr));
  });
}

/** QR başarıyla okunduğunda çalışır */
function onScanSuccess(qrCodeMessage) {
  alert('QR Okundu: ' + qrCodeMessage);
  sendScanToServer(qrCodeMessage);
  stopScanner();
}

/** Tarama sırasında oluşan hataları konsola basar */
function onScanError(errorMessage) {
  console.warn('Tarama hatası:', errorMessage);
}

/** QR taramayı durdurur ve temizler */
function stopScanner() {
  if (!html5QrCode) return;
  html5QrCode.stop()
    .then(() => {
      html5QrCode.clear();
      document.getElementById('reader').style.display = 'none';
    })
    .catch(err => console.error('Durdurma hatası:', err));
}

function sendScanToServer(token) {
  fetch('/api/qr-scan', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: token
  })
  .then(res => {
    if (!res.ok) throw new Error('QR kayıt hatası');
    return fetch(`/api/qr-scan/status`);
  })
  .then(res => {
    if (!res.ok) throw new Error('Durum kontrol hatası');
    return res.json();
  })
  .then(isValid => {
    if (isValid) showSuccess();
    else showError();
  })
  .catch(err => {
    console.error(err);
    showError('Sunucu hatası');
  });
}

function showSuccess(msg = 'QR kodu geçerli ve başarıyla okundu.') {
  const el = document.getElementById('status');
  el.textContent = msg;
  el.classList.remove('text-red-600');
  el.classList.add('text-green-600');
  el.classList.remove('hidden');
  el.classList.add('visible');
}

function showError(msg = 'QR kod geçersiz veya süresi dolmuş.') {
  const el = document.getElementById('status');
  el.textContent = msg;
  el.classList.remove('text-green-600');
  el.classList.add('text-red-600');
  el.classList.remove('hidden');
  el.classList.add('visible');
}







