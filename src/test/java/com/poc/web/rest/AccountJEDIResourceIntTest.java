package com.poc.web.rest;

import com.poc.JhipsterSampleApplicationApp;

import com.poc.domain.AccountJEDI;
import com.poc.repository.AccountJEDIRepository;
import com.poc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.poc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AccountJEDIResource REST controller.
 *
 * @see AccountJEDIResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class AccountJEDIResourceIntTest {

    private static final Integer DEFAULT_ACCOUNT_NUM = 1;
    private static final Integer UPDATED_ACCOUNT_NUM = 2;

    private static final String DEFAULT_ACCOUNT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_LABEL = "BBBBBBBBBB";

    private static final Long DEFAULT_OLD_BALANCE = 1L;
    private static final Long UPDATED_OLD_BALANCE = 2L;

    private static final Long DEFAULT_BALANCE = 1L;
    private static final Long UPDATED_BALANCE = 2L;

    @Autowired
    private AccountJEDIRepository accountJEDIRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountJEDIMockMvc;

    private AccountJEDI accountJEDI;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccountJEDIResource accountJEDIResource = new AccountJEDIResource(accountJEDIRepository);
        this.restAccountJEDIMockMvc = MockMvcBuilders.standaloneSetup(accountJEDIResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountJEDI createEntity(EntityManager em) {
        AccountJEDI accountJEDI = new AccountJEDI()
            .accountNum(DEFAULT_ACCOUNT_NUM)
            .accountLabel(DEFAULT_ACCOUNT_LABEL)
            .oldBalance(DEFAULT_OLD_BALANCE)
            .balance(DEFAULT_BALANCE);
        return accountJEDI;
    }

    @Before
    public void initTest() {
        accountJEDI = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountJEDI() throws Exception {
        int databaseSizeBeforeCreate = accountJEDIRepository.findAll().size();

        // Create the AccountJEDI
        restAccountJEDIMockMvc.perform(post("/api/account-jedis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountJEDI)))
            .andExpect(status().isCreated());

        // Validate the AccountJEDI in the database
        List<AccountJEDI> accountJEDIList = accountJEDIRepository.findAll();
        assertThat(accountJEDIList).hasSize(databaseSizeBeforeCreate + 1);
        AccountJEDI testAccountJEDI = accountJEDIList.get(accountJEDIList.size() - 1);
        assertThat(testAccountJEDI.getAccountNum()).isEqualTo(DEFAULT_ACCOUNT_NUM);
        assertThat(testAccountJEDI.getAccountLabel()).isEqualTo(DEFAULT_ACCOUNT_LABEL);
        assertThat(testAccountJEDI.getOldBalance()).isEqualTo(DEFAULT_OLD_BALANCE);
        assertThat(testAccountJEDI.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createAccountJEDIWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountJEDIRepository.findAll().size();

        // Create the AccountJEDI with an existing ID
        accountJEDI.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountJEDIMockMvc.perform(post("/api/account-jedis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountJEDI)))
            .andExpect(status().isBadRequest());

        // Validate the AccountJEDI in the database
        List<AccountJEDI> accountJEDIList = accountJEDIRepository.findAll();
        assertThat(accountJEDIList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAccountJEDIS() throws Exception {
        // Initialize the database
        accountJEDIRepository.saveAndFlush(accountJEDI);

        // Get all the accountJEDIList
        restAccountJEDIMockMvc.perform(get("/api/account-jedis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountJEDI.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountNum").value(hasItem(DEFAULT_ACCOUNT_NUM)))
            .andExpect(jsonPath("$.[*].accountLabel").value(hasItem(DEFAULT_ACCOUNT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].oldBalance").value(hasItem(DEFAULT_OLD_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }
    

    @Test
    @Transactional
    public void getAccountJEDI() throws Exception {
        // Initialize the database
        accountJEDIRepository.saveAndFlush(accountJEDI);

        // Get the accountJEDI
        restAccountJEDIMockMvc.perform(get("/api/account-jedis/{id}", accountJEDI.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountJEDI.getId().intValue()))
            .andExpect(jsonPath("$.accountNum").value(DEFAULT_ACCOUNT_NUM))
            .andExpect(jsonPath("$.accountLabel").value(DEFAULT_ACCOUNT_LABEL.toString()))
            .andExpect(jsonPath("$.oldBalance").value(DEFAULT_OLD_BALANCE.intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingAccountJEDI() throws Exception {
        // Get the accountJEDI
        restAccountJEDIMockMvc.perform(get("/api/account-jedis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountJEDI() throws Exception {
        // Initialize the database
        accountJEDIRepository.saveAndFlush(accountJEDI);

        int databaseSizeBeforeUpdate = accountJEDIRepository.findAll().size();

        // Update the accountJEDI
        AccountJEDI updatedAccountJEDI = accountJEDIRepository.findById(accountJEDI.getId()).get();
        // Disconnect from session so that the updates on updatedAccountJEDI are not directly saved in db
        em.detach(updatedAccountJEDI);
        updatedAccountJEDI
            .accountNum(UPDATED_ACCOUNT_NUM)
            .accountLabel(UPDATED_ACCOUNT_LABEL)
            .oldBalance(UPDATED_OLD_BALANCE)
            .balance(UPDATED_BALANCE);

        restAccountJEDIMockMvc.perform(put("/api/account-jedis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccountJEDI)))
            .andExpect(status().isOk());

        // Validate the AccountJEDI in the database
        List<AccountJEDI> accountJEDIList = accountJEDIRepository.findAll();
        assertThat(accountJEDIList).hasSize(databaseSizeBeforeUpdate);
        AccountJEDI testAccountJEDI = accountJEDIList.get(accountJEDIList.size() - 1);
        assertThat(testAccountJEDI.getAccountNum()).isEqualTo(UPDATED_ACCOUNT_NUM);
        assertThat(testAccountJEDI.getAccountLabel()).isEqualTo(UPDATED_ACCOUNT_LABEL);
        assertThat(testAccountJEDI.getOldBalance()).isEqualTo(UPDATED_OLD_BALANCE);
        assertThat(testAccountJEDI.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountJEDI() throws Exception {
        int databaseSizeBeforeUpdate = accountJEDIRepository.findAll().size();

        // Create the AccountJEDI

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountJEDIMockMvc.perform(put("/api/account-jedis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountJEDI)))
            .andExpect(status().isBadRequest());

        // Validate the AccountJEDI in the database
        List<AccountJEDI> accountJEDIList = accountJEDIRepository.findAll();
        assertThat(accountJEDIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAccountJEDI() throws Exception {
        // Initialize the database
        accountJEDIRepository.saveAndFlush(accountJEDI);

        int databaseSizeBeforeDelete = accountJEDIRepository.findAll().size();

        // Get the accountJEDI
        restAccountJEDIMockMvc.perform(delete("/api/account-jedis/{id}", accountJEDI.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountJEDI> accountJEDIList = accountJEDIRepository.findAll();
        assertThat(accountJEDIList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountJEDI.class);
        AccountJEDI accountJEDI1 = new AccountJEDI();
        accountJEDI1.setId(1L);
        AccountJEDI accountJEDI2 = new AccountJEDI();
        accountJEDI2.setId(accountJEDI1.getId());
        assertThat(accountJEDI1).isEqualTo(accountJEDI2);
        accountJEDI2.setId(2L);
        assertThat(accountJEDI1).isNotEqualTo(accountJEDI2);
        accountJEDI1.setId(null);
        assertThat(accountJEDI1).isNotEqualTo(accountJEDI2);
    }
}
