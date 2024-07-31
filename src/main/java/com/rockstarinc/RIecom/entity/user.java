package com.rockstarinc.RIecom.entity;

import com.rockstarinc.RIecom.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Datos de los usuarios
    private String email;
    private String password;
    private String name;
    private UserRole role;

    // Define una columna de tipo Large Object (BLOB) para almacenar imágenes u otros datos binarios
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] img;
}
