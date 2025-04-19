package com.example.iste.service;

import com.example.iste.entity.Appointment;
import com.example.iste.repository.AppointmentRepository;
import com.example.iste.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    public Appointment findByUsername(String username) {
        return appointmentRepository.findByUsername(username);  // username ile randevu sorgulama
    }
}
