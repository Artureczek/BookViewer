package com.wat.bookviewer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.wat.bookviewer.domain.Book;
import com.wat.bookviewer.domain.LastIndex;
import com.wat.bookviewer.domain.Purchase;

import com.wat.bookviewer.domain.User;
import com.wat.bookviewer.domain.reponse.BookResponse;
import com.wat.bookviewer.domain.reponse.SingleBookRespone;
import com.wat.bookviewer.repository.BookRepository;
import com.wat.bookviewer.repository.LastIndexRepository;
import com.wat.bookviewer.repository.PurchaseRepository;
import com.wat.bookviewer.repository.UserRepository;
import com.wat.bookviewer.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Purchase.
 */
@RestController
@RequestMapping("/api")
public class PurchaseResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseResource.class);

    @Inject
    private PurchaseRepository purchaseRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private BookRepository bookRepository;

    @Inject
    private LastIndexRepository lastIndexRepository;

    /**
     * POST  /purchases : Create a new purchase.
     *
     * @param purchase the purchase to create
     * @return the ResponseEntity with status 201 (Created) and with body the new purchase, or with status 400 (Bad Request) if the purchase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/purchases",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase) throws URISyntaxException {
        log.debug("REST request to save Purchase : {}", purchase);
        if (purchase.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("purchase", "idexists", "A new purchase cannot already have an ID")).body(null);
        }
        Purchase result = purchaseRepository.save(purchase);
        return ResponseEntity.created(new URI("/api/purchases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("purchase", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchases : Updates an existing purchase.
     *
     * @param purchase the purchase to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchase,
     * or with status 400 (Bad Request) if the purchase is not valid,
     * or with status 500 (Internal Server Error) if the purchase couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/purchases",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Purchase> updatePurchase(@RequestBody Purchase purchase) throws URISyntaxException {
        log.debug("REST request to update Purchase : {}", purchase);
        if (purchase.getId() == null) {
            return createPurchase(purchase);
        }
        Purchase result = purchaseRepository.save(purchase);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("purchase", purchase.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchases : get all the purchases.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of purchases in body
     */
    @RequestMapping(value = "/purchases",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Purchase> getAllPurchases() {
        log.debug("REST request to get all Purchases");
        List<Purchase> purchases = purchaseRepository.findAll();
        return purchases;
    }

    /**
     * GET  /purchases/:id : get the "id" purchase.
     *
     * @param id the id of the purchase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the purchase, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/purchases/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Purchase> getPurchase(@PathVariable Integer id) {
        log.debug("REST request to get Purchase : {}", id);
        Purchase purchase = purchaseRepository.findOne(id);
        return Optional.ofNullable(purchase)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /purchases/:id : delete the "id" purchase.
     *
     * @param id the id of the purchase to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/purchases/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePurchase(@PathVariable Integer id) {
        log.debug("REST request to delete Purchase : {}", id);
        purchaseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("purchase", id.toString())).build();
    }

    @Timed
    @Transactional
    @RequestMapping(value = "/purchase/{login}/{id}/{count}", method = RequestMethod.PUT)
    public void createPurchase2(@PathVariable String login, @PathVariable Integer id, @PathVariable Integer count) {

        Optional<User> user = userRepository.findOneByLogin(login);
        String userId = user.get().getId();
        Purchase purchase = new Purchase();
        LastIndex lastIndex = lastIndexRepository.findOneByTable("Purchase");
        purchase.setId(lastIndex.getValue());
        lastIndex.setValue(lastIndex.getValue() + 1);
        purchase.setBookId(id.longValue());
        purchase.setUserId(userId);
        purchase.setDate(LocalDate.now());
        purchase.setValue(Double.valueOf(count));
        lastIndexRepository.save(lastIndex);
        purchaseRepository.save(purchase);

    }

    @Timed
    @Transactional
    @RequestMapping(value = "/purchasedbooks/{login}", method = RequestMethod.POST)
    public BookResponse getUsersPurchases(@PathVariable String login) {

        Optional<User> maybeUser = userRepository.findOneByLogin(login);

        List<Purchase> purchaseList = purchaseRepository.findAllByUserId(maybeUser.get().getId());
        List<SingleBookRespone> bookList = new ArrayList<>();
        purchaseList.stream().forEach(e-> bookList.add(new SingleBookRespone(bookRepository.findById((int)(long)e.getBookId()), e.getDate().plusDays(e.getValue().longValue()))));

        return new BookResponse<>(bookList, bookList.size());
    }

}
