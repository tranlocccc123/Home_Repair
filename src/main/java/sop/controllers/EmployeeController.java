package sop.controllers;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import sop.models.*;
import sop.repositories.AuthRepository;
import sop.repositories.ContractRepository;
import sop.repositories.EmployeeRepository;
import sop.repositories.ImageRepository;
import sop.repositories.QuoteItemRepository;
import sop.repositories.QuotesRepository;
import sop.repositories.ServiceRepository;
import sop.repositories.UserRepository;
import sop.utils.SecurityUtility;

@Controller
@RequestMapping("Employee")
public class EmployeeController {
	@Autowired
	AuthRepository repAuth;
	@Autowired
	EmployeeRepository repEmp;
	@Autowired
	ImageRepository repIm;
	@Autowired
	ServiceRepository repSer;
	@Autowired
	ContractRepository repCon;
	@Autowired
	UserRepository repUser;
	@Autowired
	QuotesRepository repQuo;
	@Autowired
	QuoteItemRepository repQuoItem;

	public static String generateRandomString(int length) {
		String randomUUID = UUID.randomUUID().toString().replaceAll("-", "");
		return randomUUID.substring(0, length); // Lấy 6 ký tự đầu tiên
	}

	@GetMapping("/index")
	public String index() {
		return "Employee/index";
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

	@GetMapping("/contract")
	public String getAllContractbyId(HttpServletRequest request, Model model) {
		String username = (String) request.getSession().getAttribute("username");
		int id = repUser.findIdByUsername(username);
		List<Contracts> lsContract = repCon.findByIdUser(id);
		model.addAttribute("lsContract", lsContract);
		model.addAttribute("username", username);
		return "Employee/Contract";
	}

	@GetMapping("/ContractItem/{id}")
	public String getProductDetails(@PathVariable("id") int id, Model model) {
		// Find the product by ID
		ContracItems ContracItems = repCon.findbyContractID(id);
		if (ContracItems != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String formattedDate = ContracItems.getCreatedAt().format(formatter);

			model.addAttribute("ContracItems", ContracItems);
			model.addAttribute("formattedCreatedAt", formattedDate);

			// Retrieve images related to the product
			List<Images> lsImage = repIm.findAll(); // Assuming a method to filter images by productId
			model.addAttribute("lsImage", lsImage);

			return "Employee/ContractItem";// Assuming Views.PRODUCT_DETAIL is a constant that refers to your Thymeleaf
											// template
		} else {
			model.addAttribute("error", "Product not found");
			return "error"; // Assuming you have an error view to display when the product is not found
		}
	}

	@GetMapping("/createquote")
	public String createquote(Model model) {
		model.addAttribute("quote", new Quotes());
		return "Employee/create-quote";
	}

	@PostMapping("/save")
	public String saveQuote(@ModelAttribute("quote") Quotes quote, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "Employee/create-quote";
		}
		repQuo.addQuote(quote);
		model.addAttribute("message", "Create Quote Success");
		return "Common/success";
	}

	@GetMapping("/getinfoquotes/{quoteid}")
	public String getinfoquotes(@PathVariable("quoteid") int quoteid, Model model, HttpServletRequest request) {

		String username = (String) request.getSession().getAttribute("username");
		int id = repUser.findIdByUsername(username);

		Quotes quote = repQuo.getQuote(quoteid);
		model.addAttribute("quote", quote);
		
		QuoteItems quoteItem = repQuoItem.getQuoteI(quoteid);
		model.addAttribute("quoteItems", quoteItem);
		
		Users customer = repUser.findById(id);
		model.addAttribute("customer",
				customer != null ? customer.getFullName() : "NULL(" + quote.getEmployeeId() + ")");

		return "Employee/getinfoquote";
	}

	@PostMapping("/createcontract")
	public String createcontract(@ModelAttribute("quoteId") int quoteId, @ModelAttribute("customerId") int customerId,
			@ModelAttribute("totalAmount") BigDecimal totalAmount, Model model) {
		 Quotes quote = repQuo.getQuote(quoteId);
		// model.addAttribute("quote", quote);
		String randomString = generateRandomString(6);
		Contracts contract = new Contracts();
		contract.setContractCode("HD" + randomString);
		contract.setQuoteId(quoteId);
		contract.setUserId(customerId);
		contract.setContractDate(Timestamp.valueOf(LocalDateTime.now()));
		contract.setPaymentStages("NULL");
		contract.setStatus("Draft");
		repCon.saveContract(contract);
		quote.setStatus("Contracted");
		repQuo.updatestatus(quote);
		return "Employee/getinfoquotes/" + customerId;
	}

	@GetMapping("/quoteslist")
	public String getQuoteByEmployee(HttpServletRequest request, Model model) {
		List<Users> users = repUser.findAll();
		String username = (String) request.getSession().getAttribute("username");
		int id = repUser.findIdByUsername(username);
		List<Quotes> quotes = repQuo.findByEmp(id);
		
		Map<Integer, String> userIdToFullNameMap1 = users.stream()
				.collect(Collectors.toMap(Users::getUserId, Users::getFullName));
	
		
		model.addAttribute("userIdToFullNameMap1",userIdToFullNameMap1);
		model.addAttribute("username", username);
		model.addAttribute("quote", quotes);
		return "Employee/QuotesEmp";

	}

	
}
