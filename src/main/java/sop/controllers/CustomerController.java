package sop.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import sop.models.Images;
import sop.models.Services;
import sop.models.Users;
import sop.repositories.*;
import sop.utils.FileUtility;
import sop.utils.SecurityUtility;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	AuthRepository repAuth;
	@Autowired
	EmployeeRepository repEmp;
	@Autowired
	ImageRepository repIm;
	@Autowired
	ServiceRepository repSer;

	@GetMapping("/index")
	public String index() {
		return "Common/Index";
	}

	@GetMapping("/login")
	public String login() {
		return "Clients/LoginClient";
	}

	@PostMapping("/chklogins")
	public String chklogins(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpServletRequest request) {
		Logger log = Logger.getGlobal();
		log.info("Attempted login by user: " + username);

		String encryptedPassword = repAuth.findPasswordByUsername(username);
		
		Integer Role = repAuth.findUserTypeByUsername(username);

		if (encryptedPassword != null && SecurityUtility.compareBcrypt(encryptedPassword, password)) {

			request.getSession().setAttribute("username", username);
	

			if (Role == 0) {
				return "redirect:/admin/index";
			} else if (Role == 1) {
				return "redirect:/Employee/index";
			} else {
				return "redirect:/client/login";
			}
		} else {
			request.setAttribute("error", "Invalid username or password");
			return "redirect:/admin/login";
		}
	}

	@GetMapping("/addemployee")
	public String Addemployee() {
		return "Admin/Addemployee";
	}

	@PostMapping("/add")
	public String addUser(@RequestParam String username, @RequestParam String passwordHash,
			@RequestParam String fullName) {
		Users user = new Users();
		user.setUsername(username);
		user.setPasswordHash(passwordHash); // Hash the password if necessary
		String hashedPassword = SecurityUtility.encryptBcrypt(passwordHash);
		user.setPasswordHash(hashedPassword);
		user.setFullName(fullName);

		user.setCreatedAt(LocalDateTime.now());
		user.setEmail(null);
		user.setPhoneNumber("000000000000");
		user.setStatus(0);
		user.setRole(1);
		user.setToken(null);

		repEmp.addUser(user); // Call the service method to add the user
		return "redirect:/admin/index";
	}

	@GetMapping("/Service")
	public String index(HttpServletRequest request,Model model) {
		List<Services> services = repSer.findAll();
		List<Images> lsImage = repIm.findAll();
		String username = (String) request.getSession().getAttribute("username");
		for (Services service : services) {
			boolean hasMainImage = false;
			for (Images image : lsImage) {
				if (image.getServiceID() == service.getServiceId() && image.getMainStatus() == 1) {
					hasMainImage = true;
					break;
				}
			}
			if (!hasMainImage) {
				Images noImage = new Images();
				noImage.setServiceID(service.getServiceId());
				noImage.setImageName("noimage.jpg");
				noImage.setMainStatus(1);
				lsImage.add(noImage);
			}
		}

		model.addAttribute("service", services);
		model.addAttribute("lsImage", lsImage);
		model.addAttribute("username", username);    
		return "Admin/Service";
	}

	@GetMapping("/Servicedetail/{id}")
	public String getProductDetails(@PathVariable("id") int id, Model model) {
		// Find the product by ID
		Services services = repSer.findImageByServiceId(id);
		if (services != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String formattedDate = services.getCreatedAt().format(formatter);

			model.addAttribute("services", services);
			model.addAttribute("formattedCreatedAt", formattedDate);

			// Retrieve images related to the product
			List<Images> lsImage = repIm.findAll(); // Assuming a method to filter images by productId
			model.addAttribute("lsImage", lsImage);

			return "Admin/ServiceDetail";// Assuming Views.PRODUCT_DETAIL is a constant that refers to your Thymeleaf
											// template
		} else {
			model.addAttribute("error", "Product not found");
			return "error"; // Assuming you have an error view to display when the product is not found
		}
	}

	@GetMapping("/images/newImgService/{id}")
	public String showImageForm(@PathVariable("id") int id,Model model) {
		model.addAttribute("image", new Images());
		
		Services product = repSer.findImageByServiceId(id);
		model.addAttribute("product", product);
		return "Admin/addImgService"; // Trả về view chứa form thêm hình ảnh
	}

	@PostMapping("/new")
	public String submitProductImage(@RequestParam("mainStatus") int mainStatus,
			@RequestParam("ServiceId") int ServiceId, @RequestParam("fileName") MultipartFile image) {

		List<Images> ServiceImg = repIm.findByServiceId(ServiceId);
		for (Images existingImage : ServiceImg) {
			if (existingImage.getMainStatus() == mainStatus) {
				// Cập nhật main status của hình ảnh chính hiện tại
				repIm.updateMainStatus(ServiceId, mainStatus);
				break;
			}
		}
		Images productImage = new Images();
		productImage.setImageName(FileUtility.uploadFileImage(image, "Uploads"));
		productImage.setMainStatus(mainStatus);
		productImage.setServiceID(ServiceId);
		repIm.save(productImage);

		return "redirect:/admin/Service";
	}

	@GetMapping("/rmitem")
	public String removeProductImage(@RequestParam("id") int id) {
		Images image = repIm.findById(id);
		if (image != null) {
			repIm.deleteProImg(image.getImageID());
			FileUtility.deleteFile("Uploads", image.getImageName());
		}
		return "redirect:admin/ServiceDetail/" + image.getServiceID();
	}
}
