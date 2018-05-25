/**
* @author  Walid Abbassi
*/
package com.opendevup.scolarite.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.opendevup.scolarite.entities.User;
import com.opendevup.scolarite.service.UserService;

//This means that this class is a Controller
@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	//inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Welcome";
	
	/**
	 * home page with message from file : application.properties
	 * @param model
	 * @return
	 */
	//url:http://localhost:8080/home
	//Annotation for mapping HTTP GET requests onto specific handler methods
	//Specifically, @GetMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.GET).
	//@RequestMapping(value="/home", method = RequestMethod.GET)
	//new annotation since 4.3
	@GetMapping(value = "/home")
	public String home(Map<String, Object> model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			User user = userService.findUserByUsername(auth.getName());
			model.put("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		} catch (Exception e) {
			// TODO: handle exception
		      String userName = auth.getName(); //get logged in username
		      model.put("userName", "Welcome " + userName + "you are static user ( not registred in database )");
		}
		
		model.put("adminMessage","Content Available Only for Users with Admin Role");
		model.put("message", this.message);
		return "/home";
	}
	
	/**
	 * login with username and password 
	 * @return
	 */
	//url:http://localhost:8080/login
	//Annotation for mapping HTTP GET requests onto specific handler methods
	//Specifically, @GetMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.GET).
	//@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	//new annotation since 4.3
	@GetMapping(value={"/", "/login"})
	public String login(){
		Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails && validLogin((UserDetails) principal)){ // Go to admin home if already logged in
			return "redirect:/home";
		}
		return "/login";
	}
	
	/**
	 * registration user form
	 * @param model
	 * @return
	 */
	//url:http://localhost:8080/registration
	//Annotation for mapping HTTP GET requests onto specific handler methods
	//Specifically, @GetMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.GET).
	//@RequestMapping(value="/registration", method = RequestMethod.GET)
	//new annotation since 4.3
	@GetMapping(value = "/registration")
	public String registration(Model model){
		User user = new User();
		model.addAttribute("user", user);
		return "/registration";
	}
	
	/**
	 * registration user in database
	 * @param user
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	//url:http://localhost:8080/registration
	//Annotation for mapping HTTP POST requests onto specific handler methods.
	//Specifically, @PostMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.POST).
	//@RequestMapping(value = "/registration", method = RequestMethod.POST)
	//new annotation since 4.3
	@PostMapping(value = "/registration")
	public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
		User userExists = userService.findUserByUsername(user.getUsername());
		if (userExists != null) {
			bindingResult
					.rejectValue("email", "error.user",
							"There is already a user registered with the username provided");
		}
		if (bindingResult.hasErrors()) {
			return "/registration";
		} else {
			userService.saveUser(user, "USER");
			model.addAttribute("successMessage", "User has been registered successfully");
			model.addAttribute("user", new User());
		}
		return "/registration";
	}
	
	/**
	 * registration user form
	 * @param model
	 * @return
	 */
	//url:http://localhost:8080/registration
	//Annotation for mapping HTTP GET requests onto specific handler methods
	//Specifically, @GetMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.GET).
	//@RequestMapping(value="/registration", method = RequestMethod.GET)
	//new annotation since 4.3
	@GetMapping(value = "/admin/registrationAdmin")
	public String registrationAdmin(Model model){
		User user = new User();
		model.addAttribute("user", user);
		return "/registrationAdmin";
	}
	
	/**
	 * registration user in database
	 * @param user
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	//url:http://localhost:8080/registration
	//Annotation for mapping HTTP POST requests onto specific handler methods.
	//Specifically, @PostMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.POST).
	//@RequestMapping(value = "/registration", method = RequestMethod.POST)
	//new annotation since 4.3
	@PostMapping(value = "/registrationAdmin")
	public String createNewAdmin(@Valid User user, BindingResult bindingResult, Model model) {
		User userExists = userService.findUserByUsername(user.getUsername());
		if (userExists != null) {
			bindingResult
					.rejectValue("email", "error.user",
							"There is already a user registered with the username provided");
		}
		if (bindingResult.hasErrors()) {
			return "/registrationAdmin";
		} else {
			userService.saveUser(user, "ADMIN");
			model.addAttribute("successMessage", "User has been registered successfully");
			model.addAttribute("user", new User());
		}
		return "/registrationAdmin";
	}
	
	/**
	 * evaluating user details
	 * This function does a check to ascertain the validity of the logged in user
	 * You may also consider evaluating userDetails.getAuthorities()
	 * @param userDetails
	 * @return
	 */
	private boolean validLogin(UserDetails userDetails) {
	    return userDetails.isAccountNonExpired() &&
	            userDetails.isAccountNonLocked() &&
	            userDetails.isCredentialsNonExpired() &&
	            userDetails.isEnabled();
	}
}