package com.wat.bookviewer.web.rest;

import com.wat.bookviewer.BookViewerApp;

import com.wat.bookviewer.domain.LastIndex;
import com.wat.bookviewer.repository.LastIndexRepository;

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
 * Test class for the LastIndexResource REST controller.
 *
 * @see LastIndexResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookViewerApp.class)
public class LastIndexResourceIntTest {

    private static final String DEFAULT_TABLE = "AAAAA";
    private static final String UPDATED_TABLE = "BBBBB";

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    @Inject
    private LastIndexRepository lastIndexRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLastIndexMockMvc;

    private LastIndex lastIndex;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LastIndexResource lastIndexResource = new LastIndexResource();
        ReflectionTestUtils.setField(lastIndexResource, "lastIndexRepository", lastIndexRepository);
        this.restLastIndexMockMvc = MockMvcBuilders.standaloneSetup(lastIndexResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LastIndex createEntity() {
        LastIndex lastIndex = new LastIndex()
                .table(DEFAULT_TABLE)
                .value(DEFAULT_VALUE);
        return lastIndex;
    }

    @Before
    public void initTest() {
        lastIndexRepository.deleteAll();
        lastIndex = createEntity();
    }

    @Test
    public void createLastIndex() throws Exception {
        int databaseSizeBeforeCreate = lastIndexRepository.findAll().size();

        // Create the LastIndex

        restLastIndexMockMvc.perform(post("/api/last-indices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lastIndex)))
                .andExpect(status().isCreated());

        // Validate the LastIndex in the database
        List<LastIndex> lastIndices = lastIndexRepository.findAll();
        assertThat(lastIndices).hasSize(databaseSizeBeforeCreate + 1);
        LastIndex testLastIndex = lastIndices.get(lastIndices.size() - 1);
        assertThat(testLastIndex.getTable()).isEqualTo(DEFAULT_TABLE);
        assertThat(testLastIndex.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    public void getAllLastIndices() throws Exception {
        // Initialize the database
        lastIndexRepository.save(lastIndex);

        // Get all the lastIndices
        restLastIndexMockMvc.perform(get("/api/last-indices?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lastIndex.getId())))
                .andExpect(jsonPath("$.[*].table").value(hasItem(DEFAULT_TABLE.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    public void getLastIndex() throws Exception {
        // Initialize the database
        lastIndexRepository.save(lastIndex);

        // Get the lastIndex
        restLastIndexMockMvc.perform(get("/api/last-indices/{id}", lastIndex.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lastIndex.getId()))
            .andExpect(jsonPath("$.table").value(DEFAULT_TABLE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    public void getNonExistingLastIndex() throws Exception {
        // Get the lastIndex
        restLastIndexMockMvc.perform(get("/api/last-indices/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateLastIndex() throws Exception {
        // Initialize the database
        lastIndexRepository.save(lastIndex);
        int databaseSizeBeforeUpdate = lastIndexRepository.findAll().size();

        // Update the lastIndex
        LastIndex updatedLastIndex = lastIndexRepository.findOne(lastIndex.getId());
        updatedLastIndex
                .table(UPDATED_TABLE)
                .value(UPDATED_VALUE);

        restLastIndexMockMvc.perform(put("/api/last-indices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLastIndex)))
                .andExpect(status().isOk());

        // Validate the LastIndex in the database
        List<LastIndex> lastIndices = lastIndexRepository.findAll();
        assertThat(lastIndices).hasSize(databaseSizeBeforeUpdate);
        LastIndex testLastIndex = lastIndices.get(lastIndices.size() - 1);
        assertThat(testLastIndex.getTable()).isEqualTo(UPDATED_TABLE);
        assertThat(testLastIndex.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    public void deleteLastIndex() throws Exception {
        // Initialize the database
        lastIndexRepository.save(lastIndex);
        int databaseSizeBeforeDelete = lastIndexRepository.findAll().size();

        // Get the lastIndex
        restLastIndexMockMvc.perform(delete("/api/last-indices/{id}", lastIndex.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LastIndex> lastIndices = lastIndexRepository.findAll();
        assertThat(lastIndices).hasSize(databaseSizeBeforeDelete - 1);
    }
}
