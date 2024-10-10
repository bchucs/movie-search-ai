package com.bchu.movie_rec_ai.java;

public class Movie {

    private String title;
    private String genre;
    private String description;
    private String actors;
    private String link;

    public Movie(String title, String genre, String description, String actors, String link) {
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.actors = actors;
        this.link = link;
    }

    public String getActors() {
        return actors;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getLink() {
        return link;
    }

}
