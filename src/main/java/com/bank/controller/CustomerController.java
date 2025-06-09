/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bank.controller;

import com.bank.entity.Customer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bank.dao.CustomerRepository;

/**
 *
 * @author CHIGOZIE IWUJI
 */

@Controller

public class CustomerController {
    
    @Autowired
    CustomerRepository repo;
    
    @GetMapping("/customer")
    @ResponseBody
    public List<Customer> getAllPerson(){
        return (List<Customer>) repo.findAll();
    }
    
    @GetMapping("/getFirstname")
    @ResponseBody
    public List<Customer> getPersonByFirstname(String fname){
        return repo.findByFirstname(fname);
    }
    
    @GetMapping("/getLasttname")
    @ResponseBody
    public List<Customer> getPersonByLastname(String lname){
        return repo.findByLastname(lname);
    }
    
    @GetMapping("/personId")
    @ResponseBody
    
    public Customer getPersonById(int id){
        return repo.findById(id).orElse(new Customer());
    }
    
    @DeleteMapping("/delete")
    @ResponseBody
    public String deletePerson(int id){
        if(repo.existsById(id)){
            repo.deleteById(id);
            return "Deleted Successfully";
        }
        else{
            return "User of Id"+ id+" does not exists";
        }
    }
}
