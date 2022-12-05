package com.alliance.leadbooster.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Entity
@EqualsAndHashCode
public class Products {

    @Id
    @Column(name = "name")
    private String name = UUID.randomUUID().toString();

}
