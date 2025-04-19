    function kapatPanel() {
        document.getElementById("qrPanel").style.display = "none";
    }

        function showContent(sectionId) {
            const sections = document.querySelectorAll('.content');
            sections.forEach(section => section.classList.add('hidden'));
            if (sectionId === 'qrcode') {
                document.getElementById('olustur').style.display = 'block';
            } else {
                document.getElementById('olustur').style.display = 'none';
            }
            document.getElementById(sectionId).classList.remove('hidden');
        }

document.getElementById('olustur').addEventListener('click', async () => {
    document.getElementById("qrPanel").style.display = "block";

    const response = await fetch("/api/qr/generate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: currentUsername
            }) // sadece username gönderiyoruz
        });
    if (response.ok) {
        const qrBase64 = await response.text();
        document.getElementById('qrcode-box').innerHTML = `<img src="${qrBase64}" />`;
    } else {
        const errorMsg = await response.text();
        document.getElementById('qrcode-box').innerHTML = `<p style="color:red;">${errorMsg}</p>`;
    }
});