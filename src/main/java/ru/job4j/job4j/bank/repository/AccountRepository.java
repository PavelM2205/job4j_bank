package ru.job4j.job4j.bank.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.job4j.bank.model.Account;

import java.util.Optional;

@Repository
public class AccountRepository extends Store<Account> {
    public Optional<Account> findByRequisite(String passport, String requisite) {
        return store.values().stream()
                .filter(acc -> acc.getRequisite().equals(requisite)
                        && acc.getUser().getPassport().equals(passport))
                .findFirst();
    }
}
