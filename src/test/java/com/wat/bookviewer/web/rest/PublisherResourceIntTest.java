package com.wat.bookviewer.web.rest;

import com.wat.bookviewer.BookViewerApp;

import com.wat.bookviewer.domain.Publisher;
import com.wat.bookviewer.repository.PublisherRepository;

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
 * Test class for the PublisherResource REST controller.
 *
 * @see PublisherResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookViewerApp.class)
public class PublisherResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private PublisherRepository publisherRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPublisherMockMvc;

    private Publisher publisher;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PublisherResource publisherResource = new PublisherResource();
        ReflectionTestUtils.setField(publisherResource, "publisherRepository", publisherRepository);
        this.restPublisherMockMvc = MockMvcBuilders.standaloneSetup(publisherResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Publisher createEntity() {
        Publisher publisher = new Publisher()
                .name(DEFAULT_NAME);
        return publisher;
    }

    @Before
    public void initTest() {
        publisherRepository.deleteAll();
        publisher = createEntity();
    }

    @Test
    public void createPublisher() throws Exception {
        int databaseSizeBeforeCreate = publisherRepository.findAll().size();

        // Create the Publisher

        restPublisherMockMvc.perform(post("/api/publishers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publisher)))
                .andExpect(status().isCreated());

        // Validate the Publisher in the database
        List<Publisher> publishers = publisherRepository.findAll();
        assertThat(publishers).hasSize(databaseSizeBeforeCreate + 1);
        Publisher testPublisher = publishers.get(publishers.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getAllPublishers() throws Exception {
        // Initialize the database
        publisherRepository.save(publisher);

        // Get all the publishers
        restPublisherMockMvc.perform(get("/api/publishers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(publisher.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    public void getPublisher() throws Exception {
        // Initialize the database
        publisherRepository.save(publisher);

        // Get the publisher
        restPublisherMockMvc.perform(get("/api/publishers/{id}", publisher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(publisher.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingPublisher() throws Exception {
        // Get the publisher
        restPublisherMockMvc.perform(get("/api/publishers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePublisher() throws Exception {
        // Initialize the database
        publisherRepository.save(publisher);
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();

        // Update the publisher
        Publisher updatedPublisher = publisherRepository.findOne(publisher.getId());
        updatedPublisher
                .name(UPDATED_NAME);

        restPublisherMockMvc.perform(put("/api/publishers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPublisher)))
                .andExpect(status().isOk());

        // Validate the Publisher in the database
        List<Publisher> publishers = publisherRepository.findAll();
        assertThat(publishers).hasSize(databaseSizeBeforeUpdate);
        Publisher testPublisher = publishers.get(publishers.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void deletePublisher() throws Exception {
        // Initialize the database
        publisherRepository.save(publisher);
        int databaseSizeBeforeDelete = publisherRepository.findAll().size();

        // Get the publisher
        restPublisherMockMvc.perform(delete("/api/publishers/{id}", publisher.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Publisher> publishers = publisherRepository.findAll();
        assertThat(publishers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
