package com.wat.bookviewer.web.rest;

import com.wat.bookviewer.BookViewerApp;

import com.wat.bookviewer.domain.Purchase;
import com.wat.bookviewer.repository.PurchaseRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PurchaseResource REST controller.
 *
 * @see PurchaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookViewerApp.class)
public class PurchaseResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final Long DEFAULT_BOOK_ID = 1L;
    private static final Long UPDATED_BOOK_ID = 2L;

    private static final String DEFAULT_USER_ID = "AAAAA";
    private static final String UPDATED_USER_ID = "BBBBB";

    @Inject
    private PurchaseRepository purchaseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPurchaseMockMvc;

    private Purchase purchase;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PurchaseResource purchaseResource = new PurchaseResource();
        ReflectionTestUtils.setField(purchaseResource, "purchaseRepository", purchaseRepository);
        this.restPurchaseMockMvc = MockMvcBuilders.standaloneSetup(purchaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Purchase createEntity() {
        Purchase purchase = new Purchase()
                .date(DEFAULT_DATE)
                .value(DEFAULT_VALUE)
                .bookId(DEFAULT_BOOK_ID)
                .userId(DEFAULT_USER_ID);
        return purchase;
    }

    @Before
    public void initTest() {
        purchaseRepository.deleteAll();
        purchase = createEntity();
    }

    @Test
    public void createPurchase() throws Exception {
        int databaseSizeBeforeCreate = purchaseRepository.findAll().size();

        // Create the Purchase

        restPurchaseMockMvc.perform(post("/api/purchases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(purchase)))
                .andExpect(status().isCreated());

        // Validate the Purchase in the database
        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeCreate + 1);
        Purchase testPurchase = purchases.get(purchases.size() - 1);
        assertThat(testPurchase.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPurchase.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testPurchase.getBookId()).isEqualTo(DEFAULT_BOOK_ID);
        assertThat(testPurchase.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    public void getAllPurchases() throws Exception {
        // Initialize the database
        purchaseRepository.save(purchase);

        // Get all the purchases
        restPurchaseMockMvc.perform(get("/api/purchases?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(purchase.getId())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].bookId").value(hasItem(DEFAULT_BOOK_ID.intValue())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())));
    }

    @Test
    public void getPurchase() throws Exception {
        // Initialize the database
        purchaseRepository.save(purchase);

        // Get the purchase
        restPurchaseMockMvc.perform(get("/api/purchases/{id}", purchase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchase.getId()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.bookId").value(DEFAULT_BOOK_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()));
    }

    @Test
    public void getNonExistingPurchase() throws Exception {
        // Get the purchase
        restPurchaseMockMvc.perform(get("/api/purchases/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePurchase() throws Exception {
        // Initialize the database
        purchaseRepository.save(purchase);
        int databaseSizeBeforeUpdate = purchaseRepository.findAll().size();

        // Update the purchase
        Purchase updatedPurchase = purchaseRepository.findOne(purchase.getId());
        updatedPurchase
                .date(UPDATED_DATE)
                .value(UPDATED_VALUE)
                .bookId(UPDATED_BOOK_ID)
                .userId(UPDATED_USER_ID);

        restPurchaseMockMvc.perform(put("/api/purchases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPurchase)))
                .andExpect(status().isOk());

        // Validate the Purchase in the database
        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeUpdate);
        Purchase testPurchase = purchases.get(purchases.size() - 1);
        assertThat(testPurchase.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPurchase.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testPurchase.getBookId()).isEqualTo(UPDATED_BOOK_ID);
        assertThat(testPurchase.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    public void deletePurchase() throws Exception {
        // Initialize the database
        purchaseRepository.save(purchase);
        int databaseSizeBeforeDelete = purchaseRepository.findAll().size();

        // Get the purchase
        restPurchaseMockMvc.perform(delete("/api/purchases/{id}", purchase.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Purchase> purchases = purchaseRepository.findAll();
        assertThat(purchases).hasSize(databaseSizeBeforeDelete - 1);
    }
}
