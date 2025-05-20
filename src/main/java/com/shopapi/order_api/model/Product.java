/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.shopapi.order_api.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
//import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 *
 * @author kevin
 */
@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "products")
public class Product implements Serializable{

    static final long serialVersionUID = 1L;

    @Id
    //@Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    @NotNull
    @Column(length = 120, nullable = false)
    String name;
    
    @Column()
    String description;
    
    @NotNull
    @Column(nullable = false)
    BigDecimal price;
    
    @Column()
    Integer stock;
    
    @Column(name = "create_at", updatable = false)
    LocalDateTime createAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    
    @OneToMany(mappedBy = "product")
    List<OrderDetail> details;
    
    @PrePersist
    public void prePersist() {
        if (createAt == null) {
            this.createAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
}
