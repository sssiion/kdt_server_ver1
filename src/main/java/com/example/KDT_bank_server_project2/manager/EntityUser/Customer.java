package com.example.KDT_bank_server_project2.manager.EntityUser;


import com.example.KDT_bank_server_project2.manager.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter@Setter
@Entity
@Table(name = "customer")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(name = "resident_number", unique = true, nullable = false, length = 14)
    private String residentNumber;

    @Column(columnDefinition = "TEXT")
    private String address;



    public enum CustomerStatus {
        ACTIVE, INACTIVE, SUSPENDED;

        public static CustomerStatus fromString(String status) {
            if (status == null) return ACTIVE;
            try {
                return CustomerStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ACTIVE;
            }
        }
    }



}