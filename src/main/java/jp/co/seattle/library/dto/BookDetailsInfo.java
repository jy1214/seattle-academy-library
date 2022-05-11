package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍詳細情報格納DTO
 *
 */
@Configuration
@Data
public class BookDetailsInfo {

	private int bookId;

	private String title;

	private String author;

	private String publisher;

	private String publishDate;

	private String ISBN;

	private String introduce;

	private String thumbnailUrl;

	private String thumbnailName;

	private int rentBookId;

	public BookDetailsInfo() {

	}

	public BookDetailsInfo(int bookId, String title, String author, String publisher, String publishDate, String ISBN,
			String introduce, String thumbnailUrl, String thumbnailName, int rentBookId) {
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.publishDate = publishDate;
		this.ISBN = ISBN;
		this.introduce = introduce;
		this.thumbnailUrl = thumbnailUrl;
		this.thumbnailName = thumbnailName;
		this.rentBookId = rentBookId;
	}

}