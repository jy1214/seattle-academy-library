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
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class AddBooksController {
	final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private ThumbnailService thumbnailService;

	@RequestMapping(value = "/addBook", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String login(Model model) {
		return "addBook";
	}

	/**
	 * 書籍情報を登録する
	 * 
	 * @param locale    ロケール情報
	 * @param title     書籍名
	 * @param author    著者名
	 * @param publisher 出版社
	 * @param file      サムネイルファイル
	 * @param model     モデル
	 * @return 遷移先画面
	 */
	@Transactional
	@RequestMapping(value = "/insertBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String insertBook(Locale locale, @RequestParam("title") String title, @RequestParam("author") String author,
			@RequestParam("publisher") String publisher, @RequestParam("publish_date") String publishDate,
			@RequestParam("ISBN") String ISBN, @RequestParam("introduce") String introduce,
			@RequestParam("thumbnail") MultipartFile file, Model model) {
		logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

		// パラメータで受け取った書籍情報をDtoに格納する。
		BookDetailsInfo bookInfo = new BookDetailsInfo();
		bookInfo.setTitle(title);
		bookInfo.setAuthor(author);
		bookInfo.setPublisher(publisher);
		bookInfo.setPublishDate(publishDate);
		bookInfo.setISBN(ISBN);
		bookInfo.setIntroduce(introduce);

		// クライアントのファイルシステムにある元のファイル名を設定する
		String thumbnail = file.getOriginalFilename();

		if (!file.isEmpty()) {
			try {
				// サムネイル画像をアップロード
				String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
				// URLを取得
				String thumbnailUrl = thumbnailService.getURL(fileName);

				bookInfo.setThumbnailName(fileName);
				bookInfo.setThumbnailUrl(thumbnailUrl);

			} catch (Exception e) {

				// 異常終了時の処理
				logger.error("サムネイルアップロードでエラー発生", e);
				model.addAttribute("bookDetailsInfo", bookInfo);
				return "addBook";
			}
		}

		if (title.length() == 0 || author.length() == 0 || publisher.length() == 0) {
			if (ISBN.length() == 0 || ISBN.length() == 10 || ISBN.length() == 13 && ISBN.matches("^[0-9]+$")) {
				if (publishDate.matches("^[0-9]{8}+$")) {
					model.addAttribute("errorText", "必須項目を入力してください");
					model.addAttribute("bookInfo", bookInfo);
					return "addBook";
				} else {
					model.addAttribute("errorText", "必須項目を入力してください<br>出版日は半角数字のYYYYMMDD形式で入力してください");
					model.addAttribute("bookInfo", bookInfo);
					return "addBook";
				}
			} else if (publishDate.matches("^[0-9]{8}+$")) {
				model.addAttribute("errorText", "必須項目を入力してください<br>ISBNの桁数または半角数字が正しくありません");
				model.addAttribute("bookInfo", bookInfo);
				return "addBook";
			} else {
				model.addAttribute("errorText", "必須項目を入力してください<br>出版日は半角数字のYYYYMMDD形式で入力してください<br>ISBNの桁数または半角数字が正しくありません");
				model.addAttribute("bookInfo", bookInfo);
				return "addBook";
			}
		} else if (ISBN.length() == 0 || ISBN.length() == 10 || ISBN.length() == 13 && ISBN.matches("^[0-9]+$")) {
			if (publishDate.matches("^[0-9]{8}+$")) {
				// 書籍情報を新規登録する
				booksService.registBook(bookInfo);
				model.addAttribute("resultMessage", "登録完了");
				// TODO 登録した書籍の詳細情報を表示するように実装
				// 詳細画面に遷移する
				bookInfo.setBookId(booksService.newBook());
				model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookInfo.getBookId()));
				return "details";
			} else {
				model.addAttribute("errorText", "出版日は半角数字のYYYYMMDD形式で入力してください");
				model.addAttribute("bookInfo", bookInfo);
				return "addBook";
			}
		} else if (publishDate.matches("^[0-9]{8}+$")) {
			model.addAttribute("errorText", "ISBNの桁数または半角数字が正しくありません");
			model.addAttribute("bookInfo", bookInfo);
			return "addBook";

		} else {
			model.addAttribute("errorText", "ISBNの桁数または半角数字が正しくありません<br>出版日は半角数字のYYYYMMDD形式で入力してください");
			model.addAttribute("bookInfo", bookInfo);
			return "addBook";
		}
	}
}
