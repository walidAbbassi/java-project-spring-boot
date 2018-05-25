/**
* @author  Walid Abbassi
*/
package com.opendevup.scolarite.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.opendevup.scolarite.dao.EtudiantRepository;
import com.opendevup.scolarite.entities.Etudiant;

//This means that this class is a Controller
@Controller
//@RequestMapping(value="/Etudiant") // This means URL's start with /Etudiant (after Application path)
public class EtudiantController {
	//constructor injection
	@Autowired
	private EtudiantRepository etudiantRepository;
	//inject via application.properties
	@Value("${image.path}")
    private String imagePath;		//Save the uploaded file to this folder
	
	/**
	 * Find model etudiant from database
	 * @param model
	 * @param page
	 * @param motCles
	 * @return
	 */
	//url:http://localhost:8080/Index
	//Annotation for mapping web requests onto methods in request-handling classes with flexible method signatures.
	@RequestMapping(value="/user/Index")
	public String Index(Model model, 
			@RequestParam(name = "page", defaultValue = "0")int page, 
			@RequestParam(name = "motCles", defaultValue = "")String motCles){
		Page<Etudiant> PageEtudiant = etudiantRepository.chercherEtudiant("%"+motCles+"%",new PageRequest(page, 5));
		int pageCounter = PageEtudiant.getTotalPages();
		long totalElements = PageEtudiant.getTotalElements();
		int[] pages = new int[pageCounter];
		for (int i = 0; i < pageCounter; i++) pages[i]=i;
		
		//model for Html
		model.addAttribute("pageEtudiants",PageEtudiant);
		model.addAttribute("pages",pages);
		model.addAttribute("pageCourante",page);
		model.addAttribute("totalPages",pageCounter);
		model.addAttribute("motCles",motCles);
		model.addAttribute("totalElements",totalElements);
		return "/etudiants";
	}
	
	/**
	 * add image to table
	 * @param id
	 * @param filename
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	//url:http://localhost:8080/getPhoto
	//Annotation for mapping web requests onto methods in request-handling classes with flexible method signatures.
	@RequestMapping(value="/getPhoto", produces=MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] getPhoto(long id, String filename) throws Exception, IOException 
	{
		File file = new File(imagePath+id+filename);
		return IOUtils.toByteArray(new FileInputStream(file));
	}
	
	/**
	 * delete model etudiant in database
	 * @param id
	 * @return
	 */
	//url:http://localhost:8080/suprimer
	//Annotation for mapping web requests onto methods in request-handling classes with flexible method signatures.
	@RequestMapping(value="/admin/suprimer")
	public String suprimer(long id)
	{
		etudiantRepository.deleteById(id);
		return "redirect:/user/Index";
	}
    
	/**
	 * Edit new model etudiant in database and image 
	 * @param model
	 * @return
	 */
	//url:http://localhost:8080/form
	//Annotation for mapping HTTP GET requests onto specific handler methods
	//Specifically, @GetMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.GET).
	//new annotation since 4.3
	@GetMapping("/admin/form")
	public String formEtudiant(Model model)
	{
		model.addAttribute("etudiant",new Etudiant());
		return "/formEtudiant";
	}
	
	/**
	 * Edit exist model etudiant in database and image
	 * @param id
	 * @param model
	 * @return
	 */
	//url:http://localhost:8080/editer
	//Annotation for mapping HTTP GET requests onto specific handler methods
	//Specifically, @GetMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.GET).
	//new annotation since 4.3
	@GetMapping("/admin/editer")
	public String editer(long id, Model model)
	{
		Etudiant etudiant = etudiantRepository.getOne(id);
		//model for Html
		model.addAttribute("etudiant",etudiant);
		return "/editerFormEtudiant";
	}
	
	/**
	 * save model etudiant in database and image
	 * @param etudiant
	 * @param bindingResult
	 * @param file
	 * @param redirectAttributes
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	//Annotation for mapping HTTP POST requests onto specific handler methods.
	//Specifically, @PostMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.POST).
	//new annotation since 4.3
	@PostMapping("/admin/save")
	public String save(@Valid Etudiant etudiant,BindingResult bindingResult,
			@RequestParam("file") MultipartFile file, 
			RedirectAttributes redirectAttributes) throws IllegalStateException, IOException
	{
		if ((file.isEmpty())||(bindingResult.hasErrors())) {
            return "/formEtudiant";
        }
		else {
			etudiant.setPhoto(file.getOriginalFilename());
			//insert
			etudiantRepository.save(etudiant);
			// save image
			file.transferTo(new File(imagePath + etudiant.getId() + file.getOriginalFilename()));
		}
		return "redirect:/user/Index";
	}

	/**
	 * update model etudiant in database and image
	 * @param etudiant
	 * @param bindingResult
	 * @param file
	 * @param redirectAttributes
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	//Annotation for mapping HTTP POST requests onto specific handler methods.
	//Specifically, @PostMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.POST).
	//new annotation since 4.3
	@PostMapping("/admin/update")
	public String update(@Valid Etudiant etudiant,BindingResult bindingResult,
			@RequestParam("file") MultipartFile file, 
			RedirectAttributes redirectAttributes) throws IllegalStateException, IOException
	{
		if ((file.isEmpty())||(bindingResult.hasErrors())) {
			System.out.println("11111111111111111111111");
            return "/editerFormEtudiant";
        }
		else {
			etudiant.setPhoto(file.getOriginalFilename());
			//update
			etudiantRepository.save(etudiant);
			// save image
			file.transferTo(new File(imagePath + etudiant.getId() + file.getOriginalFilename()));
		}
		System.out.println("2222222222222222222222222222");
		return "redirect:/user/Index";
	}
	
}