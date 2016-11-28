package com.wat.bookviewer.web.rest;

import com.wat.bookviewer.BookViewerApp;

import com.wat.bookviewer.domain.Countries;
import com.wat.bookviewer.repository.CountriesRepository;

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
 * Test class for the CountriesResource REST controller.
 *
 * @see CountriesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookViewerApp.class)
public class CountriesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAA";
    private static final String UPDATED_CURRENCY = "BBBBB";

    @Inject
    private CountriesRepository countriesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCountriesMockMvc;

    private Countries countries;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CountriesResource countriesResource = new CountriesResource();
        ReflectionTestUtils.setField(countriesResource, "countriesRepository", countriesRepository);
        this.restCountriesMockMvc = MockMvcBuilders.standaloneSetup(countriesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Countries createEntity() {
        Countries countries = new Countries()
                .name(DEFAULT_NAME)
                .currency(DEFAULT_CURRENCY);
        return countries;
    }

    @Before
    public void initTest() {
        countriesRepository.deleteAll();
        countries = createEntity();
    }

    @Test
    public void createCountries() throws Exception {
        int databaseSizeBeforeCreate = countriesRepository.findAll().size();

        // Create the Countries

        restCountriesMockMvc.perform(post("/api/countries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(countries)))
                .andExpect(status().isCreated());

        // Validate the Countries in the database
        List<Countries> countries = countriesRepository.findAll();
        assertThat(countries).hasSize(databaseSizeBeforeCreate + 1);
        Countries testCountries = countries.get(countries.size() - 1);
        assertThat(testCountries.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCountries.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
    }

    @Test
    public void getAllCountries() throws Exception {
        // Initialize the database
        countriesRepository.save(countries);

        // Get all the countries
        restCountriesMockMvc.perform(get("/api/countries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(countries.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }

    @Test
    public void getCountries() throws Exception {
        // Initialize the database
        countriesRepository.save(countries);

        // Get the countries
        restCountriesMockMvc.perform(get("/api/countries/{id}", countries.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(countries.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()));
    }

    @Test
    public void getNonExistingCountries() throws Exception {
        // Get the countries
        restCountriesMockMvc.perform(get("/api/countries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCountries() throws Exception {
        // Initialize the database
        countriesRepository.save(countries);
        int databaseSizeBeforeUpdate = countriesRepository.findAll().size();

        // Update the countries
        Countries updatedCountries = countriesRepository.findOne(countries.getId());
        updatedCountries
                .name(UPDATED_NAME)
                .currency(UPDATED_CURRENCY);

        restCountriesMockMvc.perform(put("/api/countries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCountries)))
                .andExpect(status().isOk());

        // Validate the Countries in the database
        List<Countries> countries = countriesRepository.findAll();
        assertThat(countries).hasSize(databaseSizeBeforeUpdate);
        Countries testCountries = countries.get(countries.size() - 1);
        assertThat(testCountries.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCountries.getCurrency()).isEqualTo(UPDATED_CURRENCY);
    }

    @Test
    public void deleteCountries() throws Exception {
        // Initialize the database
        countriesRepository.save(countries);
        int databaseSizeBeforeDelete = countriesRepository.findAll().size();

        // Get the countries
        restCountriesMockMvc.perform(delete("/api/countries/{id}", countries.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Countries> countries = countriesRepository.findAll();
        assertThat(countries).hasSize(databaseSizeBeforeDelete - 1);
    }
}
