/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bank.dao;

import com.bank.entity.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author CHIGOZIE IWUJI
 */
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    List<Customer> findByFirstname(String fname);

    List<Customer> findByLastname(String lname);

    boolean existsByEmail(String email);

    boolean existsByPhone(long phone);

    boolean existsByUsername(String username);

    Customer findByUsername(String username);

    @Query("SELECT c FROM Customer c WHERE c.accnumber = :accNumber")
    Optional<Customer> findByAccnumber(@Param("accNumber") String accNumber);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhone(Long phone);

}
