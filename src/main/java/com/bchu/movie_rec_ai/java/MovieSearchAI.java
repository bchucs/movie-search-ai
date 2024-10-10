package com.bchu.movie_rec_ai.java;

import ai.djl.MalformedModelException;
import ai.djl.huggingface.translator.TextEmbeddingTranslatorFactory;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import java.io.IOException;

public class MovieSearchAI {
    String DJL_MODEL = "sentence-transformers/all-MiniLM-L6-v2";
    String DJL_PATH = "djl://ai.djl.huggingface.pytorch/" + DJL_MODEL;

    public float[] getEmbeddings(String text) throws ModelNotFoundException, MalformedModelException, IOException, TranslateException {
        Criteria<String, float[]> criteria =
                Criteria.builder()
                        .setTypes(String.class, float[].class)
                        .optModelUrls(DJL_PATH)
                        .optEngine("PyTorch")
                        .optTranslatorFactory(new TextEmbeddingTranslatorFactory())
                        .optProgress(new ProgressBar())
                        .build();

        ZooModel<String, float[]> model = criteria.loadModel();
        Predictor<String, float[]> predictor = model.newPredictor();
        return predictor.predict(text);
    }
}
