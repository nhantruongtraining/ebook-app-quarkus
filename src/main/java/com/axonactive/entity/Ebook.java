package com.axonactive.entity;

import javax.persistence.*;

@Entity(name = "ebook")
public class Ebook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String title;

    @Column(length = 120)
    private String author;

    @Column(name = "published_year")
    private Integer publishedYear;

}
