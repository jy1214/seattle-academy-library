package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.RentalBooksInfo;
import jp.co.seattle.library.rowMapper.RentalBooksInfoRowMapper;

@Service
public class RentalsService {
	final static Logger logger = LoggerFactory.getLogger(RentalsService.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍IDをrentalbooksテーブルに追加
	 * 
	 * @param bookId 書籍ID
	 */
	public void rentBook(int bookId) {
		String sql = "INSERT INTO rentalbooks (book_id,rent_date) VALUES (" + bookId + ", now()) ";
		jdbcTemplate.update(sql);
	}

	/**
	 * 貸し出し書籍情報取得
	 * 
	 * @param bookId
	 * @return rentals
	 */
	public RentalBooksInfo getRentBook(int bookId) {
		String sql = "select book_id, title, rent_date, return_date from books RIGHT OUTER JOIN rentalbooks on books.id = rentalbooks.book_id where book_id = "
				+ bookId;
		try {
			RentalBooksInfo rentals = jdbcTemplate.queryForObject(sql, new RentalBooksInfoRowMapper());
			return rentals;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 貸出日をrentalbooksテーブルから削除 返却日を追加
	 * 
	 * @param bookId 書籍ID
	 */
	public void returnBook(int bookId) {
		String sql = "UPDATE rentalbooks SET rent_date=null,return_date=now() where book_id = ? ";
		jdbcTemplate.update(sql, bookId);
	}

	/**
	 * 貸出日をrentalbooksテーブルに追加 返却日を削除
	 * 
	 * @param bookId 書籍ID
	 */
	public void rentAgainBook(int bookId) {
		String sql = "UPDATE rentalbooks SET rent_date=now(),return_date=null where book_id = ? ";
		jdbcTemplate.update(sql, bookId);
	}

	/**
	 * 貸し出し日取得
	 * 
	 * @param bookId
	 */
	public String getRentDate(int bookId) {
		String sql = "select rent_date from rentalbooks where book_id = " + bookId + " and return_date is null";
		try {
			String rentDate = jdbcTemplate.queryForObject(sql, String.class);
			return rentDate;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 貸し出し書籍データを取得する
	 *
	 * @param bookId
	 * 
	 * @return 貸し出し書籍リスト
	 */
	public List<RentalBooksInfo> rentalBookList() {

		List<RentalBooksInfo> rentalBookList = jdbcTemplate.query(
				"select book_id, title, rent_date, return_date from books RIGHT OUTER JOIN rentalbooks on books.id = rentalbooks.book_id",
				new RentalBooksInfoRowMapper());

		return rentalBookList;
	}
}
