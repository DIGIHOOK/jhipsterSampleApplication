package com.poc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A AccountJEDI.
 */
@Entity
@Table(name = "account_jedi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccountJEDI implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_num")
    private Integer accountNum;

    @Column(name = "account_label")
    private String accountLabel;

    @Column(name = "old_balance")
    private Long oldBalance;

    @Column(name = "balance")
    private Long balance;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAccountNum() {
        return accountNum;
    }

    public AccountJEDI accountNum(Integer accountNum) {
        this.accountNum = accountNum;
        return this;
    }

    public void setAccountNum(Integer accountNum) {
        this.accountNum = accountNum;
    }

    public String getAccountLabel() {
        return accountLabel;
    }

    public AccountJEDI accountLabel(String accountLabel) {
        this.accountLabel = accountLabel;
        return this;
    }

    public void setAccountLabel(String accountLabel) {
        this.accountLabel = accountLabel;
    }

    public Long getOldBalance() {
        return oldBalance;
    }

    public AccountJEDI oldBalance(Long oldBalance) {
        this.oldBalance = oldBalance;
        return this;
    }

    public void setOldBalance(Long oldBalance) {
        this.oldBalance = oldBalance;
    }

    public Long getBalance() {
        return balance;
    }

    public AccountJEDI balance(Long balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountJEDI accountJEDI = (AccountJEDI) o;
        if (accountJEDI.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountJEDI.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountJEDI{" +
            "id=" + getId() +
            ", accountNum=" + getAccountNum() +
            ", accountLabel='" + getAccountLabel() + "'" +
            ", oldBalance=" + getOldBalance() +
            ", balance=" + getBalance() +
            "}";
    }
}
