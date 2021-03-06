package com.wat.bookviewer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.wat.bookviewer.domain.Publisher;

import com.wat.bookviewer.repository.PublisherRepository;
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
 * REST controller for managing Publisher.
 */
@RestController
@RequestMapping("/api")
public class PublisherResource {

    private final Logger log = LoggerFactory.getLogger(PublisherResource.class);

    @Inject
    private PublisherRepository publisherRepository;

    /**
     * POST  /publishers : Create a new publisher.
     *
     * @param publisher the publisher to create
     * @return the ResponseEntity with status 201 (Created) and with body the new publisher, or with status 400 (Bad Request) if the publisher has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/publishers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Publisher> createPublisher(@RequestBody Publisher publisher) throws URISyntaxException {
        log.debug("REST request to save Publisher : {}", publisher);
        if (publisher.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("publisher", "idexists", "A new publisher cannot already have an ID")).body(null);
        }
        Publisher result = publisherRepository.save(publisher);
        return ResponseEntity.created(new URI("/api/publishers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("publisher", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /publishers : Updates an existing publisher.
     *
     * @param publisher the publisher to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated publisher,
     * or with status 400 (Bad Request) if the publisher is not valid,
     * or with status 500 (Internal Server Error) if the publisher couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/publishers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Publisher> updatePublisher(@RequestBody Publisher publisher) throws URISyntaxException {
        log.debug("REST request to update Publisher : {}", publisher);
        if (publisher.getId() == null) {
            return createPublisher(publisher);
        }
        Publisher result = publisherRepository.save(publisher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("publisher", publisher.getId().toString()))
            .body(result);
    }

    /**
     * GET  /publishers : get all the publishers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of publishers in body
     */
    @RequestMapping(value = "/publishers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Publisher> getAllPublishers() {
        log.debug("REST request to get all Publishers");
        List<Publisher> publishers = publisherRepository.findAll();
        return publishers;
    }

    /**
     * GET  /publishers/:id : get the "id" publisher.
     *
     * @param id the id of the publisher to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the publisher, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/publishers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Publisher> getPublisher(@PathVariable Integer id) {
        log.debug("REST request to get Publisher : {}", id);
        Publisher publisher = publisherRepository.findOne(id);
        return Optional.ofNullable(publisher)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /publishers/:id : delete the "id" publisher.
     *
     * @param id the id of the publisher to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/publishers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePublisher(@PathVariable Integer id) {
        log.debug("REST request to delete Publisher : {}", id);
        publisherRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("publisher", id.toString())).build();
    }

}
