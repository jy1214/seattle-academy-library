package jp.co.seattle.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentalsService;

@Controller
public class RentalBooksController {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);

	@Autowired
	private BooksService booksService;
	@Autowired
	private RentalsService rentalsService;

	@RequestMapping(value = "/rentBook", method = RequestMethod.POST)
	public String rentBook(@RequestParam("bookId") int bookId, Model model) {

		if (rentalsService.getRentBook(bookId) == null) {
			rentalsService.rentBook(bookId);
		} else {
			if (rentalsService.getRentDate(bookId) == null) {
				rentalsService.rentAgainBook(bookId);
			} else {
				model.addAttribute("errorRent", "貸し出し済みです。");
			}
		}
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		System.out.println(booksService.getBookInfo(bookId));
		return "details";
	}
}
