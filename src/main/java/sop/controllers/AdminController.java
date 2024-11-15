package sop.controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import sop.models.*;
import sop.modelviews.*;
import sop.repositories.*;
import sop.utils.FileUtility;
import sop.utils.SecurityUtility;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	AuthRepository repAuth;
	@Autowired
	EmployeeRepository repEmp;
	@Autowired
	ImageRepository repIm;
	@Autowired
	ServiceRepository repSer;
	@Autowired
	PostRepository repPost;
	@Autowired
	BannerRepository RepBan;
	@Autowired
	QuotesRepository repQuo;
	@Autowired
	UserRepository repUser;

	@GetMapping("")
	public String index() {
		return "Admin/Home";
	}

	@GetMapping("/login")
	public String login() {
		return "Admin/LoginAdmin";
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
				return "redirect:/Employee/contract";
			} else {
				return "redirect:/client/login";
			}
		} else {
			request.setAttribute("error", "Invalid username or password");
			return "admin/login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate(); // Xóa session
		return "redirect:/admin/login"; // Chuyển hướng về trang đăng nhập
	}

	@GetMapping("/addemployee")
	public String Addemployee() {
		return "Admin/Addemployee";
	}

	@PostMapping("/add")
	public String addUser(@RequestParam String username, @RequestParam String passwordHash,
			@RequestParam String fullName, @RequestParam String email, @RequestParam String phoneNumber) {
		Users user = new Users();
		user.setUsername(username);
		user.setPasswordHash(passwordHash); // Hash the password if necessary
		String hashedPassword = SecurityUtility.encryptBcrypt(passwordHash);
		user.setPasswordHash(hashedPassword);
		user.setFullName(fullName);

		user.setCreatedAt(LocalDateTime.now());
		user.setEmail(email);
		user.setPhoneNumber(phoneNumber);
		user.setStatus(0);
		user.setRole(1);
		user.setToken(null);

		repEmp.addUser(user); // Call the service method to add the user
		return "redirect:/admin/employeeList";
	}

	@GetMapping("/Service")
	public String services(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			HttpServletRequest request, Model model) {
		List<Services> services = repSer.findAllPaginated(page, size);
		List<Images> lsImage = repIm.findAll();
		String username = (String) request.getSession().getAttribute("username");
		if (username != null) {
			model.addAttribute("username", username);
			model.addAttribute("isLoggedIn", true);
		} else {
			model.addAttribute("isLoggedIn", false);
		}
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
		// Get total number of services for pagination calculation
		int totalServices = repSer.getTotalServices();
		int totalPages = (int) Math.ceil((double) totalServices / size);

		// Add attributes to the model
		model.addAttribute("service", services);
		model.addAttribute("lsImage", lsImage);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);

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
	public String showImageForm(@PathVariable("id") int id, Model model) {
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

	@GetMapping("/employeeList")
	public String getEmployee(HttpServletRequest request, Model model) {
		List<Users> users = repEmp.findAll();
		String username = (String) request.getSession().getAttribute("username");
		model.addAttribute("users", users);
		model.addAttribute("username", username);
		return "Admin/Employee";
	}

	// Hiển thị danh sách bài viết
	@GetMapping("/posts")
	public String getAllPosts(Model model) {
		List<Posts> posts = repPost.getAllPosts();
		List<Images> lsImage = repIm.findAll();
		for (Posts post : posts) {
			boolean hasMainImage = false;
			for (Images image : lsImage) {
				if (image.getPostID() == post.getPostId() && image.getMainStatus() == 1) {
					hasMainImage = true;
					break;
				}
			}
			if (!hasMainImage) {
				Images noImage = new Images();
				noImage.setPostID(post.getPostId());
				noImage.setImageName("noimage.jpg");
				noImage.setMainStatus(1);
				lsImage.add(noImage);
			}
		}
		model.addAttribute("lsImage", lsImage);
		model.addAttribute("posts", posts);
		return "Admin/ListPost"; // Trả về trang list.html cho posts
	}

	// Hiển thị form tạo mới bài viết
	@GetMapping("/createposts")
	public String createPostForm(Model model) {
		model.addAttribute("post", new Posts());
		return "posts/create"; // Trả về trang create.html cho posts
	}

	// Lưu bài viết mới
	@PostMapping("/createposts")
	public String createPost(@ModelAttribute("post") Posts post) {
		repPost.createPost(post);
		return "redirect:/web/posts"; // Điều hướng về trang danh sách bài viết sau khi tạo
	}

	// Hiển thị form chỉnh sửa bài viết
	@GetMapping("/editpost/{id}")
	public String editPostForm(@PathVariable int id, Model model) {
		Posts post = repPost.getPostById(id);
		model.addAttribute("post", post);
		return "Admin/EditPost"; // Trả về trang edit.html cho posts
	}

	// Cập nhật bài viết
	@PostMapping("/editpost/{id}")
	public String updatePost(@PathVariable int id, @ModelAttribute("post") Posts post) {
		post.setPostId(id);
		repPost.updatePost(post);
		return "redirect:/admin/posts"; // Điều hướng về trang danh sách bài viết sau khi cập nhật
	}

	// Xóa bài viết
	@GetMapping("/deleteposts/{id}")
	public String deletePost(@PathVariable int id) {
		repPost.deletePost(id);
		return "redirect:/admin/posts"; // Điều hướng về trang danh sách bài viết sau khi xóa
	}

	@GetMapping("/listQuotes")
	public String listQuotes(Model model) {
		List<Users> users = repUser.findAll();
		List<Quotes> quotes = repQuo.findPending();

		// Create a map of userId to fullName for quick lookup

		Map<Integer, String> userIdToFullNameMap1 = users.stream()
				.collect(Collectors.toMap(Users::getUserId, Users::getFullName));

		model.addAttribute("quotes", quotes);

		model.addAttribute("userIdToFullNameMap1", userIdToFullNameMap1);
		return "Admin/ListQuotes";
	}

	@GetMapping("/QuoteDetail/{id}")
	public String getQuoteDetails(@PathVariable("id") int id, Model model) {
		// Find the quote by ID
		List<Users> users = repEmp.findAll();
		Quotes quote = repQuo.findById(id);

		Users user = repUser.findById(quote.getCustomerId());
		if (quote != null) {
			// Format the quote date
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			String formattedDate = quote.getQuoteDate().format(formatter);
			model.addAttribute("user", user);
			model.addAttribute("quote", quote);
			model.addAttribute("users", users);
			model.addAttribute("formattedQuoteDate", formattedDate);

			return "Admin/QuoteDetail"; // Update with the actual path to your Thymeleaf template for quote details
		} else {
			model.addAttribute("error", "Quote not found");
			return "error"; // Update with the path to your error page
		}
	}

	@PostMapping("/assignjobs")
	public String updateQuote(@RequestParam("employeeId") int employeeId, @RequestParam("quoteId") int quoteId,
			Model model) {
		// Tạo đối tượng Quotes và thiết lập các giá trị
		Quotes quote = new Quotes();
		quote.setQuoteId(quoteId); // Thiết lập quoteId
		quote.setEmployeeId(employeeId);
		quote.setStatus("confirm");

		// Gọi phương thức update từ repository
		int rowsAffected = repQuo.update(quote);

		if (rowsAffected > 0) {
			model.addAttribute("message", "Quote updated successfully!");
		} else {
			model.addAttribute("error", "Failed to update the quote.");
		}

		// Redirect hoặc trả về view
		return "redirect:/admin/listQuotes";
	}

	@GetMapping("/quotes")
	public String showQuotes(Model model) {
		// Fetch all quotes
		List<Users> users = repUser.findAll();
		List<Quotes> quotesList = repQuo.findAll();

		Map<Integer, String> userIdToFullNameMap1 = users.stream()
				.collect(Collectors.toMap(Users::getUserId, Users::getFullName));

		model.addAttribute("quotes", quotesList);
		model.addAttribute("userIdToFullNameMap1", userIdToFullNameMap1);
		// Return the name of the HTML template
		return "Admin/quoteslistAll";
	}
	@GetMapping("/QuoteItem/{id}")
	public String getQuoteItem(@PathVariable("id") int id, Model model) {
		// Find the quote by ID
		
		Quotes quote = repQuo.findById(id);

		Users user = repUser.findById(quote.getCustomerId());
		Users users = repUser.findById(quote.getEmployeeId());
		if (quote != null) {
			// Format the quote date
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			String formattedDate = quote.getQuoteDate().format(formatter);
			model.addAttribute("user", user);
			model.addAttribute("quote", quote);
			model.addAttribute("users", users);
			model.addAttribute("formattedQuoteDate", formattedDate);

			return "Admin/QuotesItem"; // Update with the actual path to your Thymeleaf template for quote details
		} else {
			model.addAttribute("error", "Quote not found");
			return "error"; // Update with the path to your error page
		}
	}


}
