package sop.controllers;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import sop.models.Images;
import sop.models.Services;
import sop.models.*;
import sop.repositories.AuthRepository;
import sop.repositories.ContractRepository;
import sop.repositories.EmployeeRepository;
import sop.repositories.ImageRepository;
import sop.repositories.ServiceRepository;
import sop.repositories.UserRepository;

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

	@GetMapping("/index")
	public String index() {
		return "Employee/index";
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
}
