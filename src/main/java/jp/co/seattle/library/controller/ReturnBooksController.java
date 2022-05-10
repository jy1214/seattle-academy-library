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
public class ReturnBooksController {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);

	@Autowired
	private BooksService booksService;
	@Autowired
	private RentalsService rentalsService;

	@RequestMapping(value = "/returnBook", method = RequestMethod.POST)
	public String returnBook(@RequestParam("bookId") int bookId, Model model) {

		if (rentalsService.getRentBook(bookId) == 0) {
			model.addAttribute("errorRent", "貸し出しされていません。");
		} else {
			rentalsService.returnBook(bookId);
		}
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}
}
