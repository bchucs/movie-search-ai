package com.bchu.movie_rec_ai.java;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MovieGenDB {

    private Map<String, Movie> movieMap = new HashMap<>();

    public MovieGenDB() throws IOException, RuntimeException {
        loadCSV();
    }

    public void saveMovieEmbedding() throws TranslateException, ModelNotFoundException, MalformedModelException, IOException {
        MovieSearchAI ai = new MovieSearchAI();

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("moviesdb");
            MongoCollection<Document> collection = database.getCollection("movies");

            for (Map.Entry<String, Movie> entry : movieMap.entrySet()) {
                Movie movie = entry.getValue();
                Document movieDocument = new Document()
                        .append("title", movie.getTitle())
                        .append("genre", movie.getGenre())
                        .append("description", movie.getDescription())
                        .append("actors", movie.getActors())
                        .append("link", movie.getLink())
                        .append("embedding", Arrays.toString(ai.getEmbeddings(
                                movie.getTitle() +
                                        movie.getGenre() +
                                        movie.getDescription() +
                                        movie.getActors())));
                collection.insertOne(movieDocument);
                System.out.println("Movie embedding saved: " + entry.getKey());
            }
        } catch (MongoException me) {
            System.err.println(" " + me);
        }
    }

    public void loadCSV() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("Top_10000_Movies_IMDb.csv")) {
            assert in != null;
            try (InputStreamReader reader = new InputStreamReader(in);
                 CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT)) {

                Map<String, Integer> headerMap = new HashMap<>();
                CSVRecord headerRecord = parser.iterator().next(); // Get the first record, which is the header
                for (int i = 0; i < headerRecord.size(); i++) {
                    headerMap.put(headerRecord.get(i), i);
                }

                // Iterate over records starting from the second one
                for (CSVRecord record : parser) {
                    String title = record.get(headerMap.get("Title"));
                    String genre = record.get(headerMap.get("Genre"));
                    String description = record.get(headerMap.get("Description"));
                    String actors = record.get(headerMap.get("Actors"));
                    String link = record.get(headerMap.get("Link"));

                    movieMap.put(title, new Movie(title, genre, description, actors, link));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

