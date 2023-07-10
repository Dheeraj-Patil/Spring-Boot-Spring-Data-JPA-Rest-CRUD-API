package com.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.Tutorial;
import com.demo.repository.TutorialRepository;

@RestController
public class TutorialController {
	
	@Autowired
	TutorialRepository tutorialRepository;
	
	@RequestMapping(value="/tutorials/{id}", method = RequestMethod.GET)
	public Tutorial getTutorial(@PathVariable("id") long id) {
		System.out.println("Finding tutorial by ID:"+id);
		return tutorialRepository.findById(id).get();
	}
	
	//@RequestMapping(value="/tutorials", method=RequestMethod.GET)
	//public List<Tutorial> getAllTutorials(){
	//	return tutorialRepository.findAll();
	//}
	
	 @GetMapping("/tutorials")
	  public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
	    try {
	      List<Tutorial> tutorials = new ArrayList<Tutorial>();

	      if (title == null)
	        tutorialRepository.findAll().forEach(tutorials::add);
	      else
	        tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

	      if (tutorials.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }

	      return new ResponseEntity<>(tutorials, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	
	
	
	@RequestMapping(value = "/tutorials", method = RequestMethod.POST)
	public List<Tutorial> insertMultipleTutorials(@RequestBody List<Tutorial> tutorials) {
		tutorialRepository.saveAll(tutorials);
		return tutorials;
	}
	
	@RequestMapping(value="/tutorials", method=RequestMethod.PUT)
	public Tutorial updateTutorial(@RequestBody Tutorial tutorial) {
		return tutorialRepository.save(tutorial);
	}
	
	@RequestMapping(value="/tutorials/{id}", method=RequestMethod.PUT)
	public Tutorial updateTutorialById(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
		Tutorial existingTutorial = tutorialRepository.findById(id).get();
		if(existingTutorial != null) {
			existingTutorial.setTitle(tutorial.getTitle());
			existingTutorial.setDescription(tutorial.getDescription());
			existingTutorial.setPublished(tutorial.isPublished());
			return tutorialRepository.save(existingTutorial);
		}else {
			throw new IllegalArgumentException("Tutorial not found with ID: " + id);
		}
		
	}
	
	@RequestMapping(value="/tutorials/{id}", method = RequestMethod.DELETE)
	public void deleteTutorial(@PathVariable("id") long id) {
		System.out.println("Deleted the record with id: " +id);
		tutorialRepository.deleteById(id);
	}
	
	@RequestMapping(value = "/tutorials/published", method= RequestMethod.GET )
	  public ResponseEntity<List<Tutorial>> findByPublished() {
	    try {
	      List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

	      if (tutorials.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }
	      return new ResponseEntity<>(tutorials, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	
	
	
	
}
		