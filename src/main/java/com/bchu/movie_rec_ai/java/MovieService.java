package com.bchu.movie_rec_ai.java;

import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    private final Map<Movie, float[]> dbEmbeddings;

    public MovieService() {
       dbEmbeddings = loadDbEmbeddings();
    }

    public Map<Movie, float[]> loadDbEmbeddings() {

        Map<Movie, float[]> map = new HashMap<>();
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("moviesdb");
        MongoCollection<Document> collection = db.getCollection("movies");

        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            map.put(new Movie(document.getString("title"), document.getString("genre"), document.getString("description"), document.getString("actors"), document.getString("link")), toFloatArray(document.getString("embedding")));
        }
        cursor.close();
        return map;
    }

    public List<Map.Entry<Movie, Double>> returnRecMovies(float[] queryEmbedding) {
        Map<Movie, Double> sorted = new HashMap<>();
        for (Map.Entry<Movie, float[]> entry : dbEmbeddings.entrySet()) {
            double sim = cosineSimilarity(entry.getValue(), queryEmbedding);
            sorted.put(entry.getKey(), sim);
        }
        return sorted.entrySet()
                .stream()
                .sorted(Map.Entry.<Movie, Double>comparingByValue().reversed())
                .limit(50)
                .toList();
    }

    private float[] toFloatArray(String embedding) {
        String cleanedStr = embedding.replaceAll("[\\[\\]]", "");
        String[] stringArray = cleanedStr.split(",\\s*");

        float[] floatArray = new float[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            floatArray[i] = Float.parseFloat(stringArray[i]);
        }

        return floatArray;
    }

    private double cosineSimilarity(float[] vectorA, float[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public Map<Movie, float[]> getDbEmbeddings() {
        return dbEmbeddings;
    }
}
