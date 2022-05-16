package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

/**
 * 書籍検索コントローラー
 */
@Controller
public class SerchBooksController {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);

	@Autowired
	private BooksService booksService;

	/**
	 * 書籍を検索する
	 * 
	 * @param search 検索キーワード
	 */
	@Transactional
	@RequestMapping(value = "/searchBook", method = RequestMethod.POST)
	public String searchBook(Locale locale, @RequestParam("search") String searchBook,
			@RequestParam("radiobutton") int radio, Model model) {
		if (radio == 0) {
			if (booksService.matchBookList(searchBook).isEmpty()) {
				model.addAttribute("resultMessage", "「" + searchBook + "」に一致する書籍がありません。");
			} else {
				model.addAttribute("bookList", booksService.searchBookList(searchBook));
			}
		} else {
			if (booksService.searchBookList(searchBook).isEmpty()) {
				model.addAttribute("resultMessage", "「" + searchBook + "」を含む書籍がありません。");
			} else {
				model.addAttribute("bookList", booksService.searchBookList(searchBook));
			}
		}
			model.addAttribute("resultMessage", "「を含む」もしくは「に一致する」にチェックを入れてください。");
		return "home";
	}

}
