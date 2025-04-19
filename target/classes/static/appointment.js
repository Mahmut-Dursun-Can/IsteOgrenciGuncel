function selectTime(time) {
        var slots = document.querySelectorAll('.time-slot');
        slots.forEach(function(slot) {
            slot.classList.remove('selected');
        });
        document.getElementById('selectedTime').value = time;
        var selectedSlot = document.querySelector('.time-slot[onclick="selectTime(\'' + time + '\')"]');
        selectedSlot.classList.add('selected');
    }