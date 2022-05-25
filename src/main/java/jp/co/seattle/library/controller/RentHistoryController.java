package jp.co.seattle.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.seattle.library.service.RentalsService;

@Controller
public class RentHistoryController {
	
	@Autowired
    private RentalsService rentalsService;
	
	 @RequestMapping(value = "/rentHistory", method = RequestMethod.POST)
	    public String rentHistory(Model model) {
		 model.addAttribute("rentBookList",rentalsService.rentalBookList());
		 return "rentHistory";
	 }

}
