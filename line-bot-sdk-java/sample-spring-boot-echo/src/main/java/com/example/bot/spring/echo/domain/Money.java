package com.example.bot.spring.echo.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Money")
@Data
public class Money {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_")
    private String name;

    @Column(name = "number_")
    private Integer number;
}
