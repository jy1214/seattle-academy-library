package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 * booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍リストを取得する
	 *
	 * @return 書籍リスト
	 */
	public List<BookInfo> getBookList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"select id, title,author, publisher, publish_date, ISBN, introduce, thumbnail_url from books order by title",
				new BookInfoRowMapper());

		return getedBookList;
	}

	/**
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param bookId 書籍ID
	 * @return 書籍情報
	 */
	public BookDetailsInfo getBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "select books.id, title, author, publisher, publish_date, ISBN, introduce, thumbnail_name, thumbnail_url, reg_date, upd_date, rentalbooks.book_id, rent_date, return_date, case when rentalbooks.rent_date is not null then '貸し出し中' else '貸し出し可' end as status from books LEFT OUTER JOIN rentalbooks on books.id = rentalbooks.book_id where books.id ="
				+ bookId;

		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

		return bookDetailsInfo;
	}

	/**
	 * 書籍を登録する
	 *
	 * @param bookInfo 書籍情報
	 */
	public void registBook(BookDetailsInfo bookInfo) {

		String sql = "INSERT INTO books (title, author,publisher,publish_date,ISBN,introduce,thumbnail_name,thumbnail_url,reg_date,upd_date) VALUES ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getPublishDate() + "','" + bookInfo.getISBN() + "','" + bookInfo.getIntroduce() + "','"
				+ bookInfo.getThumbnailName() + "','" + bookInfo.getThumbnailUrl() + "'," + "now()," + "now())";

		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍を削除する
	 * 
	 * @param bookId 書籍ID
	 */
	public void deleteBook(int bookId) {
		String sql = "WITH t AS (DELETE FROM books WHERE id = " + bookId + ") DELETE FROM rentalbooks WHERE book_id = " + bookId;
		jdbcTemplate.update(sql);
	}

	/**
	 * 登録した本の書籍IDを取得する
	 *
	 * @return bookId
	 */
	public int newBook() {

		// JSPに渡すデータを設定する
		String sql = "SELECT id FROM books order by id desc limit 1";

		int bookId = jdbcTemplate.queryForObject(sql, Integer.class);

		return bookId;
	}

	/**
	 * 書籍データを更新する
	 *
	 * @param bookInfo 書籍情報
	 */
	public void upBook(BookDetailsInfo bookInfo) {

		String sql = "update books set(title, author,publisher,publish_date,ISBN,introduce,thumbnail_name,thumbnail_url,upd_date) = ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getPublishDate() + "','" + bookInfo.getISBN() + "','" + bookInfo.getIntroduce() + "','"
				+ bookInfo.getThumbnailName() + "','" + bookInfo.getThumbnailUrl() + "'," + "now())where id = "
				+ bookInfo.getBookId();

		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍データを検索する(部分一致)
	 *
	 * @param search1 検索内容
	 * 
	 * @return 書籍リスト
	 */
	public List<BookInfo> searchBookList(String searchBook) {

		List<BookInfo> searchedBookList = jdbcTemplate.query(
				"select id, title,author, publisher, publish_date, ISBN, introduce, thumbnail_url from books where title like '%"
						+ searchBook + "%' order by title",
				new BookInfoRowMapper());

		return searchedBookList;
	}

	/**
	 * 書籍データを検索する(完全一致)
	 *
	 * @param search1 検索内容
	 * 
	 * @return 書籍リスト
	 */
	public List<BookInfo> matchBookList(String searchBook) {

		List<BookInfo> matchedBookList = jdbcTemplate.query(
				"select id, title,author, publisher, publish_date, ISBN, introduce, thumbnail_url from books where title like '"
						+ searchBook + "' order by title",
				new BookInfoRowMapper());
		return matchedBookList;
	}
}
