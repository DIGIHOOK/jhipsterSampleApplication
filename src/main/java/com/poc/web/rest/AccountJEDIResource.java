package com.poc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.poc.domain.AccountJEDI;
import com.poc.repository.AccountJEDIRepository;
import com.poc.web.rest.errors.BadRequestAlertException;
import com.poc.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AccountJEDI.
 */
@RestController
@RequestMapping("/api")
public class AccountJEDIResource {

    private final Logger log = LoggerFactory.getLogger(AccountJEDIResource.class);

    private static final String ENTITY_NAME = "accountJEDI";

    private final AccountJEDIRepository accountJEDIRepository;

    public AccountJEDIResource(AccountJEDIRepository accountJEDIRepository) {
        this.accountJEDIRepository = accountJEDIRepository;
    }

    /**
     * POST  /account-jedis : Create a new accountJEDI.
     *
     * @param accountJEDI the accountJEDI to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountJEDI, or with status 400 (Bad Request) if the accountJEDI has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-jedis")
    @Timed
    public ResponseEntity<AccountJEDI> createAccountJEDI(@RequestBody AccountJEDI accountJEDI) throws URISyntaxException {
        log.debug("REST request to save AccountJEDI : {}", accountJEDI);
        if (accountJEDI.getId() != null) {
            throw new BadRequestAlertException("A new accountJEDI cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountJEDI result = accountJEDIRepository.save(accountJEDI);
        return ResponseEntity.created(new URI("/api/account-jedis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-jedis : Updates an existing accountJEDI.
     *
     * @param accountJEDI the accountJEDI to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountJEDI,
     * or with status 400 (Bad Request) if the accountJEDI is not valid,
     * or with status 500 (Internal Server Error) if the accountJEDI couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-jedis")
    @Timed
    public ResponseEntity<AccountJEDI> updateAccountJEDI(@RequestBody AccountJEDI accountJEDI) throws URISyntaxException {
        log.debug("REST request to update AccountJEDI : {}", accountJEDI);
        if (accountJEDI.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccountJEDI result = accountJEDIRepository.save(accountJEDI);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountJEDI.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-jedis : get all the accountJEDIS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accountJEDIS in body
     */
    @GetMapping("/account-jedis")
    @Timed
    public List<AccountJEDI> getAllAccountJEDIS() {
        log.debug("REST request to get all AccountJEDIS");
        return accountJEDIRepository.findAll();
    }

    /**
     * GET  /account-jedis/:id : get the "id" accountJEDI.
     *
     * @param id the id of the accountJEDI to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountJEDI, or with status 404 (Not Found)
     */
    @GetMapping("/account-jedis/{id}")
    @Timed
    public ResponseEntity<AccountJEDI> getAccountJEDI(@PathVariable Long id) {
        log.debug("REST request to get AccountJEDI : {}", id);
        Optional<AccountJEDI> accountJEDI = accountJEDIRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(accountJEDI);
    }

    /**
     * DELETE  /account-jedis/:id : delete the "id" accountJEDI.
     *
     * @param id the id of the accountJEDI to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-jedis/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountJEDI(@PathVariable Long id) {
        log.debug("REST request to delete AccountJEDI : {}", id);

        accountJEDIRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
