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
import java.util.List;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;
import sop.models.Contracts;
import sop.models.Images;
import sop.models.QuoteItems;
import sop.models.Quotes;
import sop.models.Services;
import sop.models.Users;
import sop.repositories.*;

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

	@GetMapping("/login")
	public String loginclient() {
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
	    //String username = (String) request.getSession().getAttribute("username");
	    //Users user = repAuth.findByUsername(username);
	    List<QuoteItems> listQuoteItem = repQuo.getQuoteItem(1);

	    if (listQuoteItem != null && listQuoteItem.size() > 0) {
			QuoteItemsWrapper quoteItemsWrapper = new QuoteItemsWrapper();
        	quoteItemsWrapper.setQuotes(listQuoteItem);

	        model.addAttribute("quotes", listQuoteItem);
			model.addAttribute("quotesWrapper", quoteItemsWrapper);
	        return "Clients/GetQuote"; // The name of the HTML template
	    } else {
	        model.addAttribute("errorMessage", "User not found!");
	        return "Clients/GetQuote"; // Redirect to an error or info page
	    }
	}

	String s ="";

	@PostMapping("/selectcategory")
	public String selectcategory(@ModelAttribute("quotesWrapper") QuoteItemsWrapper quotesWrapper, Model model)
	{
		List<QuoteItems> selectedQuotes = quotesWrapper.getQuotes().stream()
											.filter(QuoteItems::isSelected)
											.collect(Collectors.toList());
        selectedQuotes.forEach(quoteItem -> {
            quoteItem.setNotes("Đã chọn");
			// s += "/Q_" + quoteItem.getQuoteId() +"_" +quoteItem.getServiceId() +"_" +quoteItem.getUnitPrice() +"_";
			quoteItemRepository.saveQuoteItem(quoteItem);
		});
		
		model.addAttribute("message", "Update Quote Success");
		model.addAttribute("redirectUrl", "/client/getquotes");
		return "Common/success";
	}

	
	
	@GetMapping("/getcontracts")
	public String getinfoquotes(Model model, HttpServletRequest request) {
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
			return "Clients/getinfoquote/" + employeeId;
	}
	
	@PostMapping("/signcontract")
	public String signcontract(@ModelAttribute("contractId") int contractId,
									 Model model) {

		Contracts contract = contractRepository.getContracts(contractId).get(0);
		contract.setStatus("Signed");
		contract.setSignDate(Timestamp.valueOf(LocalDateTime.now()));
		contract.setPaymentStages("SIGNED");
		contractRepository.updateContract(contract);
		return "Clients/getcontract";
	}


}
