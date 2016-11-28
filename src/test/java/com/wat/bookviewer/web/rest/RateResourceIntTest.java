package com.wat.bookviewer.web.rest;

import com.wat.bookviewer.BookViewerApp;

import com.wat.bookviewer.domain.Rate;
import com.wat.bookviewer.repository.RateRepository;

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
 * Test class for the RateResource REST controller.
 *
 * @see RateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookViewerApp.class)
public class RateResourceIntTest {

    private static final String DEFAULT_USER_ID = "AAAAA";
    private static final String UPDATED_USER_ID = "BBBBB";

    private static final Long DEFAULT_BOOK_ID = 1L;
    private static final Long UPDATED_BOOK_ID = 2L;

    private static final Long DEFAULT_RATE = 1L;
    private static final Long UPDATED_RATE = 2L;

    @Inject
    private RateRepository rateRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRateMockMvc;

    private Rate rate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RateResource rateResource = new RateResource();
        ReflectionTestUtils.setField(rateResource, "rateRepository", rateRepository);
        this.restRateMockMvc = MockMvcBuilders.standaloneSetup(rateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rate createEntity() {
        Rate rate = new Rate()
                .userId(DEFAULT_USER_ID)
                .bookId(DEFAULT_BOOK_ID)
                .rate(DEFAULT_RATE);
        return rate;
    }

    @Before
    public void initTest() {
        rateRepository.deleteAll();
        rate = createEntity();
    }

    @Test
    public void createRate() throws Exception {
        int databaseSizeBeforeCreate = rateRepository.findAll().size();

        // Create the Rate

        restRateMockMvc.perform(post("/api/rates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rate)))
                .andExpect(status().isCreated());

        // Validate the Rate in the database
        List<Rate> rates = rateRepository.findAll();
        assertThat(rates).hasSize(databaseSizeBeforeCreate + 1);
        Rate testRate = rates.get(rates.size() - 1);
        assertThat(testRate.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRate.getBookId()).isEqualTo(DEFAULT_BOOK_ID);
        assertThat(testRate.getRate()).isEqualTo(DEFAULT_RATE);
    }

    @Test
    public void getAllRates() throws Exception {
        // Initialize the database
        rateRepository.save(rate);

        // Get all the rates
        restRateMockMvc.perform(get("/api/rates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(rate.getId())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
                .andExpect(jsonPath("$.[*].bookId").value(hasItem(DEFAULT_BOOK_ID.intValue())))
                .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())));
    }

    @Test
    public void getRate() throws Exception {
        // Initialize the database
        rateRepository.save(rate);

        // Get the rate
        restRateMockMvc.perform(get("/api/rates/{id}", rate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rate.getId()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.bookId").value(DEFAULT_BOOK_ID.intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.intValue()));
    }

    @Test
    public void getNonExistingRate() throws Exception {
        // Get the rate
        restRateMockMvc.perform(get("/api/rates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRate() throws Exception {
        // Initialize the database
        rateRepository.save(rate);
        int databaseSizeBeforeUpdate = rateRepository.findAll().size();

        // Update the rate
        Rate updatedRate = rateRepository.findOne(rate.getId());
        updatedRate
                .userId(UPDATED_USER_ID)
                .bookId(UPDATED_BOOK_ID)
                .rate(UPDATED_RATE);

        restRateMockMvc.perform(put("/api/rates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRate)))
                .andExpect(status().isOk());

        // Validate the Rate in the database
        List<Rate> rates = rateRepository.findAll();
        assertThat(rates).hasSize(databaseSizeBeforeUpdate);
        Rate testRate = rates.get(rates.size() - 1);
        assertThat(testRate.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRate.getBookId()).isEqualTo(UPDATED_BOOK_ID);
        assertThat(testRate.getRate()).isEqualTo(UPDATED_RATE);
    }

    @Test
    public void deleteRate() throws Exception {
        // Initialize the database
        rateRepository.save(rate);
        int databaseSizeBeforeDelete = rateRepository.findAll().size();

        // Get the rate
        restRateMockMvc.perform(delete("/api/rates/{id}", rate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Rate> rates = rateRepository.findAll();
        assertThat(rates).hasSize(databaseSizeBeforeDelete - 1);
    }
}
