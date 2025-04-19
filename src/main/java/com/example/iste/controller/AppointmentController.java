package com.example.iste.controller;

import com.example.iste.entity.Appointment;
import com.example.iste.entity.User;
import com.example.iste.repository.UserRepository;
import com.example.iste.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("username")  // Oturumda username'i tutmak
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/appointment/create")
    public String createAppointment(@ModelAttribute Appointment appointment, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        // Kullanıcının oturum bilgisini almak
        String username = (String) session.getAttribute("username");  // HttpSession kullanarak oturum bilgisini alıyoruz

        if (username == null) {
            model.addAttribute("error", "Kullanıcı oturum bilgisi bulunamadı.");
            return "login-user";  // Kullanıcı oturumu yoksa login sayfasına yönlendir
        }

        // Kullanıcının mevcut randevusunu kontrol et
        Appointment existingAppointment = appointmentService.findByUsername(username);

        if (existingAppointment != null) {
            // Randevu mevcutsa, hata mesajı ekleyerek yönlendirme yap
            redirectAttributes.addFlashAttribute("error", "Zaten bir randevunuz var.");
            return "redirect:/user-dashboard";  // Eğer randevu varsa, kullanıcıyı yönlendirin
        }

        // username'i appointment nesnesine ekliyoruz
        appointment.setUsername(username);

        // Randevuyu kaydediyoruz
        appointmentService.saveAppointment(appointment);

        // Başarılı bir şekilde randevu oluşturulduktan sonra success mesajı ile yönlendir
        redirectAttributes.addFlashAttribute("success", "Randevunuz başarıyla oluşturuldu!");
        return "redirect:/user-dashboard";
    }


    @ModelAttribute("username")  // Appointment nesnesini modele eklemek için
    public Appointment setupAppointment() {
        return new Appointment();
    }
}
