package com.wat.bookviewer.web.rest;

import com.wat.bookviewer.BookViewerApp;

import com.wat.bookviewer.domain.JhiUser;
import com.wat.bookviewer.repository.JhiUserRepository;

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
 * Test class for the JhiUserResource REST controller.
 *
 * @see JhiUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookViewerApp.class)
public class JhiUserResourceIntTest {

    private static final String DEFAULT_LOGIN = "AAAAA";
    private static final String UPDATED_LOGIN = "BBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAA";
    private static final String UPDATED_PASSWORD = "BBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final String DEFAULT_ACTIVATED = "AAAAA";
    private static final String UPDATED_ACTIVATED = "BBBBB";

    private static final String DEFAULT_LANG_KEY = "AAAAA";
    private static final String UPDATED_LANG_KEY = "BBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBB";

    private static final LocalDate DEFAULT_CREATED_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DAY = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_COUNTRY_ID = 1L;
    private static final Long UPDATED_COUNTRY_ID = 2L;

    @Inject
    private JhiUserRepository jhiUserRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restJhiUserMockMvc;

    private JhiUser jhiUser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JhiUserResource jhiUserResource = new JhiUserResource();
        ReflectionTestUtils.setField(jhiUserResource, "jhiUserRepository", jhiUserRepository);
        this.restJhiUserMockMvc = MockMvcBuilders.standaloneSetup(jhiUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JhiUser createEntity() {
        JhiUser jhiUser = new JhiUser()
                .login(DEFAULT_LOGIN)
                .password(DEFAULT_PASSWORD)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .activated(DEFAULT_ACTIVATED)
                .langKey(DEFAULT_LANG_KEY)
                .createdBy(DEFAULT_CREATED_BY)
                .createdDay(DEFAULT_CREATED_DAY);
        return jhiUser;
    }

    @Before
    public void initTest() {
        jhiUserRepository.deleteAll();
        jhiUser = createEntity();
    }

    @Test
    public void createJhiUser() throws Exception {
        int databaseSizeBeforeCreate = jhiUserRepository.findAll().size();

        // Create the JhiUser

        restJhiUserMockMvc.perform(post("/api/jhi-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jhiUser)))
                .andExpect(status().isCreated());

        // Validate the JhiUser in the database
        List<JhiUser> jhiUsers = jhiUserRepository.findAll();
        assertThat(jhiUsers).hasSize(databaseSizeBeforeCreate + 1);
        JhiUser testJhiUser = jhiUsers.get(jhiUsers.size() - 1);
        assertThat(testJhiUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testJhiUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testJhiUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testJhiUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testJhiUser.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testJhiUser.getLangKey()).isEqualTo(DEFAULT_LANG_KEY);
        assertThat(testJhiUser.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testJhiUser.getCreatedDay()).isEqualTo(DEFAULT_CREATED_DAY);
    }

    @Test
    public void getAllJhiUsers() throws Exception {
        // Initialize the database
        jhiUserRepository.save(jhiUser);

        // Get all the jhiUsers
        restJhiUserMockMvc.perform(get("/api/jhi-users?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jhiUser.getId())))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.toString())))
                .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANG_KEY.toString())))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
                .andExpect(jsonPath("$.[*].createdDay").value(hasItem(DEFAULT_CREATED_DAY.toString())))
                .andExpect(jsonPath("$.[*].countryId").value(hasItem(DEFAULT_COUNTRY_ID.intValue())));
    }

    @Test
    public void getJhiUser() throws Exception {
        // Initialize the database
        jhiUserRepository.save(jhiUser);

        // Get the jhiUser
        restJhiUserMockMvc.perform(get("/api/jhi-users/{id}", jhiUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jhiUser.getId()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.toString()))
            .andExpect(jsonPath("$.langKey").value(DEFAULT_LANG_KEY.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDay").value(DEFAULT_CREATED_DAY.toString()))
            .andExpect(jsonPath("$.countryId").value(DEFAULT_COUNTRY_ID.intValue()));
    }

    @Test
    public void getNonExistingJhiUser() throws Exception {
        // Get the jhiUser
        restJhiUserMockMvc.perform(get("/api/jhi-users/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateJhiUser() throws Exception {
        // Initialize the database
        jhiUserRepository.save(jhiUser);
        int databaseSizeBeforeUpdate = jhiUserRepository.findAll().size();

        // Update the jhiUser
        JhiUser updatedJhiUser = jhiUserRepository.findOne(jhiUser.getId());
        updatedJhiUser
                .login(UPDATED_LOGIN)
                .password(UPDATED_PASSWORD)
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .activated(UPDATED_ACTIVATED)
                .langKey(UPDATED_LANG_KEY)
                .createdBy(UPDATED_CREATED_BY)
                .createdDay(UPDATED_CREATED_DAY);

        restJhiUserMockMvc.perform(put("/api/jhi-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedJhiUser)))
                .andExpect(status().isOk());

        // Validate the JhiUser in the database
        List<JhiUser> jhiUsers = jhiUserRepository.findAll();
        assertThat(jhiUsers).hasSize(databaseSizeBeforeUpdate);
        JhiUser testJhiUser = jhiUsers.get(jhiUsers.size() - 1);
        assertThat(testJhiUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testJhiUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testJhiUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testJhiUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testJhiUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testJhiUser.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testJhiUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testJhiUser.getCreatedDay()).isEqualTo(UPDATED_CREATED_DAY);
    }

    @Test
    public void deleteJhiUser() throws Exception {
        // Initialize the database
        jhiUserRepository.save(jhiUser);
        int databaseSizeBeforeDelete = jhiUserRepository.findAll().size();

        // Get the jhiUser
        restJhiUserMockMvc.perform(delete("/api/jhi-users/{id}", jhiUser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<JhiUser> jhiUsers = jhiUserRepository.findAll();
        assertThat(jhiUsers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
