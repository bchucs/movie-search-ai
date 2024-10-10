package com.bchu.movie_rec_ai.java;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class MovieSearchController {
    private MovieService movieService;
    private MovieSearchAI ai;

    public MovieSearchController() {
        movieService = new MovieService();
        ai = new MovieSearchAI();
    }

    @GetMapping("/home")
    public String showSearchForm() {
        return "index";
    }

    @PostMapping("/search")
    public String searchMovies(@RequestParam String query, Model model) throws TranslateException, ModelNotFoundException, MalformedModelException, IOException {
        float[] queryEmbedding = ai.getEmbeddings(query);
        List<Map.Entry<Movie, Double>> rec = movieService.returnRecMovies(queryEmbedding);

        model.addAttribute("movieRecs", rec);

        return "results";
    }

}