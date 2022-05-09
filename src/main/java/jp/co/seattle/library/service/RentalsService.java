package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
		String sql = "INSERT INTO rentalbooks (book_id) VALUES (?) ";
		jdbcTemplate.update(sql,bookId);
	}
	
	/**
	 * 貸し出し書籍情報取得
	 * 
	 * @param bookId
	 * @return id
	 */
	public int getRentBook(int bookId) {
		String sql = "select book_id from rentalbooks where book_id = ?";
		try {
			int id = jdbcTemplate.queryForObject(sql,Integer.class,bookId);
			return id;
		} catch (Exception e) {
			return 0;
		}
	}
}
