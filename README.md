**This project is a Movie Recommendation System that leverages AI and MongoDB for storing and retrieving movie embeddings. The application processes a dataset of movies, generates embeddings using AI sentence transformer (all-minilm-l6-v2), and stores them in a MongoDB database for querying and comparison.**

**Features**
CSV Parsing: Reads movie data from a CSV file (e.g., IMDB movie data).
AI Embedding Generation: Uses an AI model to generate vector embeddings based on movie details such as title, genre, description, and actors.
MongoDB Integration: Stores movie details and embeddings in a MongoDB database.
Efficient Querying: Retrieves stored movies from MongoDB for comparison or recommendation.

**Libraries Used**
MongoDB Java Driver: For interacting with MongoDB.
Apache Commons CSV: For parsing CSV files.
Deep Java Library (DJL): For AI-based embedding generation.
PyTorch Backend: Integrated with DJL for AI model inference.
