package com.wat.bookviewer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.wat.bookviewer.domain.JhiUser;

import com.wat.bookviewer.repository.JhiUserRepository;
import com.wat.bookviewer.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing JhiUser.
 */
@RestController
@RequestMapping("/api")
public class JhiUserResource {

    private final Logger log = LoggerFactory.getLogger(JhiUserResource.class);
        
    @Inject
    private JhiUserRepository jhiUserRepository;

    /**
     * POST  /jhi-users : Create a new jhiUser.
     *
     * @param jhiUser the jhiUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jhiUser, or with status 400 (Bad Request) if the jhiUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/jhi-users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JhiUser> createJhiUser(@RequestBody JhiUser jhiUser) throws URISyntaxException {
        log.debug("REST request to save JhiUser : {}", jhiUser);
        if (jhiUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("jhiUser", "idexists", "A new jhiUser cannot already have an ID")).body(null);
        }
        JhiUser result = jhiUserRepository.save(jhiUser);
        return ResponseEntity.created(new URI("/api/jhi-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("jhiUser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jhi-users : Updates an existing jhiUser.
     *
     * @param jhiUser the jhiUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jhiUser,
     * or with status 400 (Bad Request) if the jhiUser is not valid,
     * or with status 500 (Internal Server Error) if the jhiUser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/jhi-users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JhiUser> updateJhiUser(@RequestBody JhiUser jhiUser) throws URISyntaxException {
        log.debug("REST request to update JhiUser : {}", jhiUser);
        if (jhiUser.getId() == null) {
            return createJhiUser(jhiUser);
        }
        JhiUser result = jhiUserRepository.save(jhiUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("jhiUser", jhiUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jhi-users : get all the jhiUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jhiUsers in body
     */
    @RequestMapping(value = "/jhi-users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<JhiUser> getAllJhiUsers() {
        log.debug("REST request to get all JhiUsers");
        List<JhiUser> jhiUsers = jhiUserRepository.findAll();
        return jhiUsers;
    }

    /**
     * GET  /jhi-users/:id : get the "id" jhiUser.
     *
     * @param id the id of the jhiUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jhiUser, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/jhi-users/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JhiUser> getJhiUser(@PathVariable String id) {
        log.debug("REST request to get JhiUser : {}", id);
        JhiUser jhiUser = jhiUserRepository.findOne(id);
        return Optional.ofNullable(jhiUser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /jhi-users/:id : delete the "id" jhiUser.
     *
     * @param id the id of the jhiUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/jhi-users/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteJhiUser(@PathVariable String id) {
        log.debug("REST request to delete JhiUser : {}", id);
        jhiUserRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("jhiUser", id.toString())).build();
    }

}
