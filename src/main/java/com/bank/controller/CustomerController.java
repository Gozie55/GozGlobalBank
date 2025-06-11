package com.bank.controller;

import com.bank.entity.Customer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bank.dao.CustomerRepository;

@RestController
public class CustomerController {

    @Autowired
    CustomerRepository repo;

    @GetMapping("/customer")
    public List<Customer> getAllPerson() {
        return (List<Customer>) repo.findAll();
    }

    @GetMapping("/getFirstname")
    public List<Customer> getPersonByFirstname(@RequestParam String fname) {
        return repo.findByFirstname(fname);
    }

    @GetMapping("/getLasttname")
    public List<Customer> getPersonByLastname(@RequestParam String lname) {
        return repo.findByLastname(lname);
    }

    @GetMapping("/personId")
    public Customer getPersonById(@RequestParam int id) {
        return repo.findById(id).orElse(new Customer());
    }

    @DeleteMapping("/delete")
    public String deletePerson(@RequestParam int id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return "Deleted Successfully";
        } else {
            return "User of Id " + id + " does not exist";
        }
    }
}
