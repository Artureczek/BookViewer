package com.wat.bookviewer.web.rest;

import com.wat.bookviewer.BookViewerApp;

import com.wat.bookviewer.domain.Book;
import com.wat.bookviewer.repository.BookRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookResource REST controller.
 *
 * @see BookResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookViewerApp.class)
public class BookResourceIntTest {

    private static final String DEFAULT_ISBN = "AAAAA";
    private static final String UPDATED_ISBN = "BBBBB";

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAA";
    private static final String UPDATED_AUTHOR = "BBBBB";

    private static final Long DEFAULT_YEAR = 1L;
    private static final Long UPDATED_YEAR = 2L;

    private static final Long DEFAULT_PUBLISHER_ID = 1L;
    private static final Long UPDATED_PUBLISHER_ID = 2L;

    private static final String DEFAULT_IMG_SMALL = "AAAAA";
    private static final String UPDATED_IMG_SMALL = "BBBBB";

    private static final String DEFAULT_IMG_MEDIUM = "AAAAA";
    private static final String UPDATED_IMG_MEDIUM = "BBBBB";

    private static final String DEFAULT_IMG_LARGE = "AAAAA";
    private static final String UPDATED_IMG_LARGE = "BBBBB";

    @Inject
    private BookRepository bookRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBookMockMvc;

    private Book book;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookResource bookResource = new BookResource();
        ReflectionTestUtils.setField(bookResource, "bookRepository", bookRepository);
        this.restBookMockMvc = MockMvcBuilders.standaloneSetup(bookResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Book createEntity() {
        Book book = new Book()
                .isbn(DEFAULT_ISBN)
                .title(DEFAULT_TITLE)
                .author(DEFAULT_AUTHOR)
                .year(DEFAULT_YEAR)
                .publisherId(DEFAULT_PUBLISHER_ID)
                .imgSmall(DEFAULT_IMG_SMALL)
                .imgMedium(DEFAULT_IMG_MEDIUM)
                .imgLarge(DEFAULT_IMG_LARGE);
        return book;
    }

    @Before
    public void initTest() {
        bookRepository.deleteAll();
        book = createEntity();
    }

    @Test
    public void createBook() throws Exception {
        int databaseSizeBeforeCreate = bookRepository.findAll().size();

        // Create the Book

        restBookMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(book)))
                .andExpect(status().isCreated());

        // Validate the Book in the database
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeCreate + 1);
        Book testBook = books.get(books.size() - 1);
        assertThat(testBook.getIsbn()).isEqualTo(DEFAULT_ISBN);
        assertThat(testBook.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBook.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBook.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testBook.getPublisherId()).isEqualTo(DEFAULT_PUBLISHER_ID);
        assertThat(testBook.getImgSmall()).isEqualTo(DEFAULT_IMG_SMALL);
        assertThat(testBook.getImgMedium()).isEqualTo(DEFAULT_IMG_MEDIUM);
        assertThat(testBook.getImgLarge()).isEqualTo(DEFAULT_IMG_LARGE);
    }

    @Test
    public void getAllBooks() throws Exception {
        // Initialize the database
        bookRepository.save(book);

        // Get all the books
        restBookMockMvc.perform(get("/api/books?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId())))
                .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.intValue())))
                .andExpect(jsonPath("$.[*].publisherId").value(hasItem(DEFAULT_PUBLISHER_ID.intValue())))
                .andExpect(jsonPath("$.[*].imgSmall").value(hasItem(DEFAULT_IMG_SMALL.toString())))
                .andExpect(jsonPath("$.[*].imgMedium").value(hasItem(DEFAULT_IMG_MEDIUM.toString())))
                .andExpect(jsonPath("$.[*].imgLarge").value(hasItem(DEFAULT_IMG_LARGE.toString())));
    }

    @Test
    public void getBook() throws Exception {
        // Initialize the database
        bookRepository.save(book);

        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", book.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(book.getId()))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.intValue()))
            .andExpect(jsonPath("$.publisherId").value(DEFAULT_PUBLISHER_ID.intValue()))
            .andExpect(jsonPath("$.imgSmall").value(DEFAULT_IMG_SMALL.toString()))
            .andExpect(jsonPath("$.imgMedium").value(DEFAULT_IMG_MEDIUM.toString()))
            .andExpect(jsonPath("$.imgLarge").value(DEFAULT_IMG_LARGE.toString()));
    }

    @Test
    public void getNonExistingBook() throws Exception {
        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBook() throws Exception {
        // Initialize the database
        bookRepository.save(book);
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();

        // Update the book
        Book updatedBook = bookRepository.findOne(book.getId());
        updatedBook
                .isbn(UPDATED_ISBN)
                .title(UPDATED_TITLE)
                .author(UPDATED_AUTHOR)
                .year(UPDATED_YEAR)
                .publisherId(UPDATED_PUBLISHER_ID)
                .imgSmall(UPDATED_IMG_SMALL)
                .imgMedium(UPDATED_IMG_MEDIUM)
                .imgLarge(UPDATED_IMG_LARGE);

        restBookMockMvc.perform(put("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBook)))
                .andExpect(status().isOk());

        // Validate the Book in the database
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeUpdate);
        Book testBook = books.get(books.size() - 1);
        assertThat(testBook.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testBook.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBook.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBook.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testBook.getPublisherId()).isEqualTo(UPDATED_PUBLISHER_ID);
        assertThat(testBook.getImgSmall()).isEqualTo(UPDATED_IMG_SMALL);
        assertThat(testBook.getImgMedium()).isEqualTo(UPDATED_IMG_MEDIUM);
        assertThat(testBook.getImgLarge()).isEqualTo(UPDATED_IMG_LARGE);
    }

    @Test
    public void deleteBook() throws Exception {
        // Initialize the database
        bookRepository.save(book);
        int databaseSizeBeforeDelete = bookRepository.findAll().size();

        // Get the book
        restBookMockMvc.perform(delete("/api/books/{id}", book.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeDelete - 1);
    }
}
