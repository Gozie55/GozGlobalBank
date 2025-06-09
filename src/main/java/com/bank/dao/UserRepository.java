/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.bank.dao;

import com.bank.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author CHIGOZIE IWUJI
 */
public interface UserRepository extends JpaRepository <Users, Integer> {
    Users findByUsername (String username);
}
