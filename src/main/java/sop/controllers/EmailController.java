package sop.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import sop.repositories.AuthRepository;
import sop.repositories.EmployeeRepository;
import sop.repositories.ImageRepository;
import sop.repositories.ServiceRepository;
import sop.repositories.UserRepository;
import sop.service.EmailService;

@Controller
public class EmailController {

	@Autowired
	EmailService emailService;
	@Autowired
	AuthRepository repAuth;
	@Autowired
	EmployeeRepository repEmp;
	@Autowired
	ImageRepository repIm;
	@Autowired
	ServiceRepository repSer;
	@Autowired
	UserRepository repUser;

	@GetMapping("/sendmail")
	public String showEmailForm() {
		return "Email/verificationForm"; // render the send-email.html form
	}

	
	 @PostMapping("/sendVerificationEmail")
	    public String sendVerificationEmail(String email, HttpServletRequest request, Model model) {
	        // Tìm kiếm user theo email
	        Integer userId = repUser.findUserIdByEmail(email);
	        if (userId != null) {
	            // Tạo mã xác minh và lưu vào cột Token
	            String token = UUID.randomUUID().toString();
	            repUser.saveVerificationToken(userId, token);

	            // Tạo URL xác minh
	            String verificationUrl = request.getRequestURL().toString().replace("/sendVerificationEmail", "/verify?token=" + token);

	            // Gửi email xác minh
	            emailService.sendVerificationEmail(email, verificationUrl);

	            model.addAttribute("message", "Verification email sent! Please check your inbox.");
	        } else {
	            model.addAttribute("message", "Email not found.");
	        }
	        return "/Email/verificationResult"; // Trả về tên trang hiển thị kết quả
	    }

	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("token") String token, Model model) {
		Integer userId = repUser.findUserIdByToken(token);
		if (userId != null) {
			repUser.verifyUser(userId);
			model.addAttribute("message", "Account verified successfully!");
		} else {
			model.addAttribute("message", "Invalid verification link!");
		}
		return "/Email/verificationResult";
	}
}
