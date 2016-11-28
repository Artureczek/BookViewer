package com.wat.bookviewer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.wat.bookviewer.domain.LastIndex;

import com.wat.bookviewer.repository.LastIndexRepository;
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
 * REST controller for managing LastIndex.
 */
@RestController
@RequestMapping("/api")
public class LastIndexResource {

    private final Logger log = LoggerFactory.getLogger(LastIndexResource.class);
        
    @Inject
    private LastIndexRepository lastIndexRepository;

    /**
     * POST  /last-indices : Create a new lastIndex.
     *
     * @param lastIndex the lastIndex to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lastIndex, or with status 400 (Bad Request) if the lastIndex has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/last-indices",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LastIndex> createLastIndex(@RequestBody LastIndex lastIndex) throws URISyntaxException {
        log.debug("REST request to save LastIndex : {}", lastIndex);
        if (lastIndex.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lastIndex", "idexists", "A new lastIndex cannot already have an ID")).body(null);
        }
        LastIndex result = lastIndexRepository.save(lastIndex);
        return ResponseEntity.created(new URI("/api/last-indices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lastIndex", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /last-indices : Updates an existing lastIndex.
     *
     * @param lastIndex the lastIndex to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lastIndex,
     * or with status 400 (Bad Request) if the lastIndex is not valid,
     * or with status 500 (Internal Server Error) if the lastIndex couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/last-indices",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LastIndex> updateLastIndex(@RequestBody LastIndex lastIndex) throws URISyntaxException {
        log.debug("REST request to update LastIndex : {}", lastIndex);
        if (lastIndex.getId() == null) {
            return createLastIndex(lastIndex);
        }
        LastIndex result = lastIndexRepository.save(lastIndex);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lastIndex", lastIndex.getId().toString()))
            .body(result);
    }

    /**
     * GET  /last-indices : get all the lastIndices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of lastIndices in body
     */
    @RequestMapping(value = "/last-indices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LastIndex> getAllLastIndices() {
        log.debug("REST request to get all LastIndices");
        List<LastIndex> lastIndices = lastIndexRepository.findAll();
        return lastIndices;
    }

    /**
     * GET  /last-indices/:id : get the "id" lastIndex.
     *
     * @param id the id of the lastIndex to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lastIndex, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/last-indices/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LastIndex> getLastIndex(@PathVariable String id) {
        log.debug("REST request to get LastIndex : {}", id);
        LastIndex lastIndex = lastIndexRepository.findOne(id);
        return Optional.ofNullable(lastIndex)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /last-indices/:id : delete the "id" lastIndex.
     *
     * @param id the id of the lastIndex to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/last-indices/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLastIndex(@PathVariable String id) {
        log.debug("REST request to delete LastIndex : {}", id);
        lastIndexRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lastIndex", id.toString())).build();
    }

}
