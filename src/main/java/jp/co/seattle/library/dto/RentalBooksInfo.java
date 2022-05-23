package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍詳細情報格納DTO
 *
 */
@Configuration
@Data
public class RentalBooksInfo {

	private int rentBookId;

	private String title;

	private String rentDate;

	private String returnDate;

	public RentalBooksInfo() {

	}

	public RentalBooksInfo(int rentBookId, String title, String rentDate, String returnDate) {
		this.rentBookId = rentBookId;
		this.title = title;
		this.rentDate = rentDate;
		this.returnDate = returnDate;
	}
}
