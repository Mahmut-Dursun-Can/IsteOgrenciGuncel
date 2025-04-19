    let selectedStars = 0;
    const stars = document.querySelectorAll(".star");
    stars.forEach(star => {
        star.addEventListener("click", () => {
            selectedStars = parseInt(star.getAttribute("data-value"));
            updateStarRating();
            document.getElementById("starValue").value = selectedStars;
        });
        star.addEventListener("mouseover", () => {
            highlightStars(parseInt(star.getAttribute("data-value")));
        });
        star.addEventListener("mouseout", () => {
            updateStarRating();
        });
    });

    function updateStarRating() {
        stars.forEach(star => {
            const starValue = parseInt(star.getAttribute("data-value"));
            if (starValue <= selectedStars) {
                star.style.color = "gold";
            } else {
                star.style.color = "gray";
            }
        });
    }

    function highlightStars(value) {
        stars.forEach(star => {
            const starValue = parseInt(star.getAttribute("data-value"));
            if (starValue <= value) {
                star.style.color = "gold";
            } else {
                star.style.color = "gray";
            }
        });
    }

    function submitComment() {
        const comment = document.getElementById("commentInput").value;
        if (comment && selectedStars > 0) {
            alert("Yorum Gönderildi: " + comment + "\nYıldız: " + selectedStars);
        } else {
            alert("Lütfen bir yorum yazın ve yıldız seçin.");
        }
    }