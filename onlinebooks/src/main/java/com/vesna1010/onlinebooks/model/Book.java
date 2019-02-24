package com.vesna1010.onlinebooks.model;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.vesna1010.onlinebooks.enums.Language;

@Entity
@Table(name = "BOOKS")
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;
	private String isbn;
	private String title;
	private Language language;
	private Category category;
	private byte[] contents;
	private LocalDate date = LocalDate.now();
	private Set<Author> authors = new HashSet<>();

	public Book() {
	}

	public Book(String isbn, String title, Language language, byte[] contents, LocalDate date) {
		this(isbn, title, language, null, contents, date);
	}

	public Book(String isbn, String title, Language language, Category category, byte[] contents, LocalDate date) {
		this.isbn = isbn;
		this.title = title;
		this.language = language;
		this.category = category;
		this.contents = contents;
		this.date = date;
	}

	@Id
	@Column(name = "ISBN", nullable = false)
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	@Column(name = "TITLE", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "BOOKS_AUTHORS", 
	           joinColumns = @JoinColumn(name = "BOOK_ID", referencedColumnName = "ISBN"), 
	           inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "ID"))
	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	public void addAuthor(Author author) {
		author.getBooks().add(this);
		this.getAuthors().add(author);
	}

	@Transient
	public byte[] getImage() {
		PDDocument document;
		PDFRenderer renderer;
		ByteArrayOutputStream output;

		try {
			document = PDDocument.load(this.getContents());
			renderer = new PDFRenderer(document);
			output = new ByteArrayOutputStream();

			ImageIO.write(renderer.renderImage(0), "jpeg", output);

			return output.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}

	@JsonIgnore
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENTS", nullable = false)
	public byte[] getContents() {
		return contents;
	}

	@JsonProperty
	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	@Column(name = "LANGUAGE", nullable = false)
	@Enumerated(EnumType.STRING)
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@ManyToOne
	@JoinColumn(name = "CATEGORY_ID", nullable = false)
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@Column(name = "DATE_OF_SAVING", nullable = false)
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", title=" + title + ", language=" + language + ", category=" + category
				+ ", date=" + date + ", authors=" + authors + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((isbn == null) ? 0 : isbn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (isbn == null) {
			if (other.isbn != null)
				return false;
		} else if (!isbn.equals(other.isbn))
			return false;
		return true;
	}

}