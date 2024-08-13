package com.ecommerce.project.payload;

public class TestimonialDTO {
    private Long id;
    private String text;
    private String author;

    // Constructors, getters, and setters
    public TestimonialDTO() {
    }

    public TestimonialDTO(Long id, String text, String author) {
        this.id = id;
        this.text = text;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}