package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RentalBooksController {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);

	@Autowired
	private BooksService booksService;
	@Autowired
	private RentalsService rentalsService;

	@RequestMapping(value = "/rentBook", method = RequestMethod.POST)
	public String rentBook(@RequestParam("bookId") int bookId, Model model) {

		if (rentalsService.getRentBook(bookId) == 0) {
			rentalsService.rentBook(bookId);
		} else {
			model.addAttribute("errorRent","貸し出し済みです。");
		}
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}

}
