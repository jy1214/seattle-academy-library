package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class BulkBooksController {
	final static Logger logger = LoggerFactory.getLogger(BulkBooksController.class);

	@Autowired
	private BooksService booksService;

	@RequestMapping(value = "/bulkBook", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String bulkBook(Model model) {
		return "bulkBook";
	}

	@RequestMapping(value = "/bulkRegist", method = RequestMethod.POST)
	public String bulkRegist(@RequestParam("upload_file") MultipartFile uploadFile, Model model) {

		String line = null;
		List<String> errorLine = new ArrayList<String>();
		List<String[]> booksList = new ArrayList<String[]>();
		int count = 0;

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(uploadFile.getInputStream(), StandardCharsets.UTF_8))) {

			if (!br.ready()) {
				model.addAttribute("errorBulk", "csvに書籍情報がありません");
				return "bulkBook";
			} else {
				while ((line = br.readLine()) != null) {
					count ++ ;
					String data[] = line.split(",", -1);

					// ifでtrueの時、書籍情報をセット。falseの時、行目でエラーメッセージ表示
					if (data[0].length() != 0 && data[1].length() != 0 && data[2].length() != 0
							&& data[3].matches("^[0-9]{8}+$") && data[4].matches("^[0-9]{0}|[0-9]{10}|[0-9]{13}+$")) {
						booksList.add(data);
					} else {
						errorLine.add("<p>" + count + "行目の書籍登録でエラーが起きました。</p>");
					}
				}
			}
		} catch (IOException e) {
			model.addAttribute("errorBulk", "ファイルが読み込めません");
			return "bulkBook";
		}
		// ifでエラーリストに何か入っている時、エラーメッセージを表示。空の時、registBookメソッドを実行
		if (errorLine.size() > 0) {
			model.addAttribute("errorBulk", errorLine);
			return "bulkBook";
		} else {
			String[] bookList;
			BookDetailsInfo bookInfo = new BookDetailsInfo();

			for (int i = 0; i < booksList.size(); i++) {
				bookList = booksList.get(i);

				for (int j = 0; j < bookList.length; j++) {
					bookInfo.setTitle(bookList[0]);
					bookInfo.setAuthor(bookList[1]);
					bookInfo.setPublisher(bookList[2]);
					bookInfo.setPublishDate(bookList[3]);
					bookInfo.setISBN(bookList[4]);
					bookInfo.setIntroduce(bookList[5]);
				}
				booksService.registBook(bookInfo);
			}
			model.addAttribute("bookList", booksService.getBookList());
			return "home";
		}
	}
}
