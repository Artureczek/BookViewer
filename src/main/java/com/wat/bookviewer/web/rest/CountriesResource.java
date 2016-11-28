package com.wat.bookviewer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.wat.bookviewer.domain.Countries;

import com.wat.bookviewer.repository.CountriesRepository;
import com.wat.bookviewer.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Countries.
 */
@RestController
@RequestMapping("/api")
public class CountriesResource {

    private final Logger log = LoggerFactory.getLogger(CountriesResource.class);

    @Inject
    private CountriesRepository countriesRepository;

    /**
     * POST  /countries : Create a new countries.
     *
     * @param countries the countries to create
     * @return the ResponseEntity with status 201 (Created) and with body the new countries, or with status 400 (Bad Request) if the countries has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/countries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Countries> createCountries(@RequestBody Countries countries) throws URISyntaxException {
        log.debug("REST request to save Countries : {}", countries);
        if (countries.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("countries", "idexists", "A new countries cannot already have an ID")).body(null);
        }
        Countries result = countriesRepository.save(countries);
        return ResponseEntity.created(new URI("/api/countries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("countries", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /countries : Updates an existing countries.
     *
     * @param countries the countries to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated countries,
     * or with status 400 (Bad Request) if the countries is not valid,
     * or with status 500 (Internal Server Error) if the countries couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/countries",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Countries> updateCountries(@RequestBody Countries countries) throws URISyntaxException {
        log.debug("REST request to update Countries : {}", countries);
        if (countries.getId() == null) {
            return createCountries(countries);
        }
        Countries result = countriesRepository.save(countries);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("countries", countries.getId().toString()))
            .body(result);
    }

    /**
     * GET  /countries : get all the countries.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of countries in body
     */
    @RequestMapping(value = "/countries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Countries> getAllCountries() {
        log.debug("REST request to get all Countries");
        List<Countries> countries = countriesRepository.findAll();
        return countries;
    }

    /**
     * GET  /countries/:id : get the "id" countries.
     *
     * @param id the id of the countries to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the countries, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/countries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Countries> getCountries(@PathVariable Integer id) {
        log.debug("REST request to get Countries : {}", id);
        Countries countries = countriesRepository.findOne(id);
        return Optional.ofNullable(countries)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /countries/:id : delete the "id" countries.
     *
     * @param id the id of the countries to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/countries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCountries(@PathVariable Integer id) {
        log.debug("REST request to delete Countries : {}", id);
        countriesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("countries", id.toString())).build();
    }

}
