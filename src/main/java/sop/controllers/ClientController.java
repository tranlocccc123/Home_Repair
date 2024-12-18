package sop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import sop.models.Contracts;
import sop.models.Images;
import sop.models.Payment;
import sop.models.QuoteItems;
import sop.models.Quotes;
import sop.models.Services;
import sop.models.Users;
import sop.modelviews.QuoteItemsWrapper;
import sop.repositories.*;

import java.sql.Timestamp;

import sop.utils.SecurityUtility;

@Controller
@RequestMapping("/client")

public class ClientController {
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
	@Autowired
	QuotesRepository repQuo;
	@Autowired 
	ContractRepository repCon;
	@Autowired
	QuoteItemRepository quoteItemRepository;
	@Autowired
	ContractRepository contractRepository;
	@Autowired
	PaymentRepository paymentRepository;

	@GetMapping("/login")
	public String loginclient() {
		return "Clients/LoginClient";
	}

	@PostMapping("/chklogins")
	public String chklogins(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpServletRequest request) {
		Logger log = Logger.getGlobal();
		log.info("Attempted login by user: " + username);
		Users user = repAuth.findByUsername(username);

		String encryptedPassword = repAuth.findPasswordByUsername(username);
		Integer Role = repAuth.findUserTypeByUsername(username);

		if (encryptedPassword != null && SecurityUtility.compareBcrypt(encryptedPassword, password)) {

			request.getSession().setAttribute("username", username);
			request.getSession().setAttribute("userid", user.getUserId());

			if (Role == 0) {
				return "redirect:/admin/index";
			} else if (Role == 1) {
				return "redirect:/Employee/contract";
			} else {
				return "redirect:/client/index";
			}
		} else {
			request.setAttribute("error", "Invalid username or password");
			return "admin/login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate(); // Xóa session
		return "redirect:/client/login"; // Chuyển hướng về trang đăng nhập
	}

	@GetMapping("/index")
	public String index(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size,
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

		return "Clients/index";
	}

	@GetMapping("/Service")
	public String services(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size,
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

		return "Clients/index";
	}

	@GetMapping("/Servicedetail/{id}")
	public String getProductDetails(HttpServletRequest request, @PathVariable("id") int id, Model model) {
		String username = (String) request.getSession().getAttribute("username");
		if (username != null) {
			model.addAttribute("username", username);
			model.addAttribute("isLoggedIn", true);
		} else {
			model.addAttribute("isLoggedIn", false);
		}
		Services services = repSer.findImageByServiceId(id);
		if (services != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String formattedDate = services.getCreatedAt().format(formatter);

			model.addAttribute("services", services);
			model.addAttribute("formattedCreatedAt", formattedDate);

			// Retrieve images related to the product
			List<Images> lsImage = repIm.findAll(); // Assuming a method to filter images by productId
			model.addAttribute("lsImage", lsImage);
			return "Clients/ServiceDetail";// Assuming Views.PRODUCT_DETAIL is a constant that refers to your Thymeleaf
											// template
		} else {
			model.addAttribute("error", "Product not found");
			return "error"; // Assuming you have an error view to display when the product is not found
		}
	}

	// Render the form with the existing user data
	@GetMapping("/info")
	public String showEditForm(HttpServletRequest request, Model model) {
		String username = (String) request.getSession().getAttribute("username");
		Users user = repAuth.findByUsername(username);
		if (user != null) {
			model.addAttribute("user", user);
			return "Clients/Info"; // This should point to the correct Thymeleaf template.
		} else {
			model.addAttribute("errorMessage", "User not found!");
			return "Clients/info"; // Ensure the error view is correctly defined as well.
		}
	}

	@PostMapping("/edit")
	public String editUserInfo(@ModelAttribute Users updatedUser, Model model) {
		try {
			int userId = updatedUser.getUserId();
			Users existingUser = repUser.findById(userId);
			if (existingUser != null) {
				Users userWithSameUsername = repUser.findByUsername(updatedUser.getUsername(), userId);
				if (userWithSameUsername != null && userWithSameUsername.getUserId() != userId) {
					model.addAttribute("errorMessage", "Username already exists. Please choose another username.");
					model.addAttribute("user", existingUser); // Refill form with existing user info
					return "editUserForm";
				}

				// Update user data if username is unique or remains unchanged
				existingUser.setUsername(updatedUser.getUsername());
				existingUser.setFullName(updatedUser.getFullName());
				existingUser.setEmail(updatedUser.getEmail());
				existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

				repUser.updateUser(existingUser);
				model.addAttribute("successMessage", "User information updated successfully!");
				return "Clients/Info"; // Show the form again with success message
			} else {
				model.addAttribute("errorMessage", "User not found!");
				return "Clients/Info";
			}
		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "Cliens/info";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error updating user info: " + e.getMessage());
			return "Clients/Info";
		}
	}

	@GetMapping("/editProfiles")
	public String showEditProfileForm(HttpServletRequest request, Model model) {
		String username = (String) request.getSession().getAttribute("username");
		Users user = repAuth.findByUsername(username);
		if (user != null) {
			model.addAttribute("user", user);
			return "Clients/Info"; // The name of the HTML template
		} else {
			model.addAttribute("errorMessage", "User not found!");
			return "Clients/Info"; // Redirect to an error or info page
		}
	}

	@PostMapping("/editProfile")
	public String editProfile(@ModelAttribute Users user, Model model) {
		String result = repUser.editProfile(user);

		if ("success".equals(result)) {
			model.addAttribute("successMessage", "Profile updated successfully!");
			return "Clients/Info"; // Redirect to the profile page or success view
		} else {
			model.addAttribute("errorMessage", result); // Use the error message from the service
			model.addAttribute("user", user); // Refill the form with existing user data
			return "Clients/Info"; // Show the form again with error
		}
	}

	@GetMapping("/sendrequest")
	public String createquote(Model model) {
		model.addAttribute("quote", new Quotes());

		return "Clients/sendrequest";
	}

	@PostMapping("/sendrequest")
	public String saveQuote(@ModelAttribute("quote") Quotes quote, BindingResult result, Model model,
			HttpServletRequest request) {
		if (result.hasErrors()) {
			return "Clients/sendrequest";
		}
		String username = (String) request.getSession().getAttribute("username");
		Users user = repAuth.findByUsername(username);
		int customerId = user.getUserId();
		quote.setCustomerId(customerId);
		quote.setEmployeeId(0);
		quote.setStatus("Pending");
		quote.setQuoteDate(LocalDateTime.now());
		repQuo.addQuote(quote);
		model.addAttribute("message", "Send Service Request Success");
		model.addAttribute("redirectUrl", "/client/Service");
		return "Clients/success";
	}
	@GetMapping("/getquotes")
	public String getquotes(HttpServletRequest request, Model model) {

		int customerId = (int) request.getSession().getAttribute("userid");
		List<Quotes> quotes = repQuo.getQuoteItemWithCustomer(customerId);
		if (quotes != null && !quotes.isEmpty())
		{
			Quotes quote = quotes.get(0);
			List<QuoteItems> listQuoteItem = quoteItemRepository.getQuoteItem(quote.getQuoteId());
			if (listQuoteItem != null && listQuoteItem.size() > 0) {
				for (QuoteItems item : listQuoteItem) {
					item.selected = item.getNotes() != null && !item.getNotes().equals("NULL");
					item.fixedPrice = item.getNotes() != null && !item.getNotes().equals("NULL") ? Integer.parseInt(item.getNotes()) : item.getUnitPrice().intValue();
				}
				QuoteItemsWrapper quoteItemsWrapper = new QuoteItemsWrapper();
				quoteItemsWrapper.setQuotes(listQuoteItem);

				model.addAttribute("quotes", listQuoteItem);
				model.addAttribute("quotesWrapper", quoteItemsWrapper);
				return "Clients/GetQuote";
			} else {
				model.addAttribute("errorMessage", "User not found!");
				return "Clients/GetQuote";
			}
		}
		model.addAttribute("errorMessage", "User not found!");
		return "Clients/GetQuote";
	}

	String mess = "";

	@PostMapping("/selectcategory")
	public String selectcategory(@ModelAttribute("quotesWrapper") QuoteItemsWrapper quotesWrapper, Model model)
	{
        quotesWrapper.getQuotes().forEach(quoteItem -> {
            quoteItem.setNotes(quoteItem.selected && quoteItem.fixedPrice > 0? quoteItem.fixedPrice + "" : "NULL");
			quoteItemRepository.saveQuoteItem(quoteItem);
		});
		
		model.addAttribute("message", "Update Quote Success");
		model.addAttribute("redirectUrl", "/client/getquotes");
		return "Common/success";
	}

	
	
	@GetMapping("/getcontracts")
	public String getinfoquotes(Model model, HttpServletRequest request) {
		if (request.getSession().getAttribute("userid") == null)
		{
			return "Clients/login";
		}
		int employeeId = (int) request.getSession().getAttribute("userid");
		String username = (String) request.getSession().getAttribute("username");

		List<Contracts> listContract = repCon.getContracts(employeeId);
		if (listContract != null && listContract.size() > 0)
		{
			Contracts contract = listContract.get(0);
			List<QuoteItems> quoteItems = quoteItemRepository.getQuoteItem(contract.getQuoteId());
			model.addAttribute("contract", contract);
			model.addAttribute("quoteItems", quoteItems);
			model.addAttribute("username", username);


			return "Clients/getcontract";
		}
		else
			return "Clients/getcontract";
	}
	
	@PostMapping("/signcontract")
	public String signcontract(@ModelAttribute("contractId") int contractId, Model model)
	{
		List<Contracts> listContract =contractRepository.getContractsById(contractId);
		if (listContract.size() > 0)
		{
			Contracts contract = listContract.get(0);
			contract.setStatus("SIGNED");
			contract.setSignDate(Timestamp.valueOf(LocalDateTime.now()));
			contract.setPaymentStages("0");
			contractRepository.updateContract(contract);
			
			model.addAttribute("message", "Update Contract Success");
			model.addAttribute("redirectUrl", "/client/getcontracts");
			return "Common/success";
			// return "Clients/getcontract";
		}
		return "Clients/getcontracts/" + contractId;
	}

	
	@GetMapping("/search")
	public String search(Model model, HttpServletRequest request,
						@RequestParam(name = "keyword", required = false) String keyword) {
		// int employeeId = (int) request.getSession().getAttribute("userid");
		// String username = (String) request.getSession().getAttribute("username");

		List<Services> listService = repSer.findAll();
		List<Services> listKeywordService = listService.stream()
				.filter(ser -> ser.getServiceName().contains(keyword))
				.collect(Collectors.toList());
		
		// List<Services> services = repSer.findAllPaginated(page, size);
		List<Images> lsImage = repIm.findAll();
		String username = (String) request.getSession().getAttribute("username");
		if (username != null) {
			model.addAttribute("username", username);
			model.addAttribute("isLoggedIn", true);
		} else {
			model.addAttribute("isLoggedIn", false);
		}
		for (Services service : listKeywordService) {
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
		int totalServices = listKeywordService.size();//services.size();
		int totalPages = (int) Math.ceil((double) totalServices / 6);

		// Add attributes to the model
		model.addAttribute("service", listKeywordService);
		model.addAttribute("lsImage", lsImage);
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalPages", totalPages);
				
		return "Clients/index";
	}
	
	
	@PostMapping("/payment")
	public String payment(@ModelAttribute("contractId") int contractId,
						@ModelAttribute("deposit") double deposit, Model model) {

		List<Payment> listpayment = paymentRepository.getPaymentById(contractId);
		if (listpayment.size() == 5)
		{
			model.addAttribute("message", "Contract have already payment done");
			model.addAttribute("redirectUrl", "/client/getcontracts");
			return "Common/success";
		}
		else if (listpayment.size() == 0)
		{
			Payment payment = new Payment(contractId, "Lan1", "1", "1");
			paymentRepository.addPayment(payment);
			
			model.addAttribute("message", "Payment Success");
			model.addAttribute("redirectUrl", "/client/getcontracts");
			return "Common/success";
		}
		else
		{
			Optional<Payment> maxStagePay = listpayment.stream()
							.max(Comparator.comparingInt(payment -> Integer.parseInt(payment.getPaymentStage())));
			if (maxStagePay.isPresent())
			{
				int newStage = Integer.parseInt(maxStagePay.get().getPaymentStage());
				Payment payment = maxStagePay.get().copy();
				payment.setPaymentStage(newStage + 1 + "");
				paymentRepository.addPayment(payment);
				
				model.addAttribute("message", "Payment Success");
				model.addAttribute("redirectUrl", "/client/getcontracts");
				return "Common/success";

			}
		}
		return "Common/success";

	}


	@PostMapping("/comfirmcomplete")
	public String comfirmcomplete(@ModelAttribute("contractId") int contractId,Model model) {

		List<Contracts> listcontract = contractRepository.getContractsById(contractId);
		if (listcontract.size() > 0)
		{
			Contracts contract = listcontract.get(0);
			contract.setStatus("COMPLETE");
			contractRepository.updateContract(contract);
			model.addAttribute("message", "Confirm Complete Success");
			model.addAttribute("redirectUrl", "/client/getcontracts");
			return "Common/success";
		}
		return "Common/success/";
	}
}
