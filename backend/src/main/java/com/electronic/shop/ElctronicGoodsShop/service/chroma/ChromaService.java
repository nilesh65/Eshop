package com.electronic.shop.ElctronicGoodsShop.service.chroma;

import com.electronic.shop.ElctronicGoodsShop.service.llm.LLMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingsRequest;
import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingResponse;

import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.ai.chroma.vectorstore.ChromaApi.QueryRequest.Include.all;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChromaService implements IChromaService {

    private final ChromaApi chromaApi;
    private final ChromaVectorStore chromaVectorStore;
    private final LLMService llmService;
    @Override
    public void deleteCollection(String collectionName) {
        try {
            chromaApi.deleteCollection(collectionName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Chroma collection: " + collectionName, e);
        }
    }

    @Override
    public List<Collection> getCollections() {
        try {
            return chromaApi.listCollections();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get collections");
        }
    }

    @Override
    public GetEmbeddingResponse getEmbedding(String collectionId) {
        try {
            GetEmbeddingsRequest request = new GetEmbeddingsRequest(
                    null,null,0,0,all);
            return chromaApi.getEmbeddings(collectionId,request);
        } catch (Exception e){
            throw new RuntimeException("Failed to get embedding: "+ collectionId);
        }
    }
    public String saveEmbeddings(MultipartFile image, Long productId) throws IOException {
        String imageDescription = llmService.describeImage(image);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("productId",productId);

        var document = Document.builder().
                id(productId.toString())
                .text(imageDescription)
                .metadata(metadata)
                .build();
        try {
            chromaVectorStore.doAdd(List.of(document));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return  "Document added to chroma store successfully";
    }

    public List<Long> searchImageSimilarity(MultipartFile image) throws IOException{
        String imageDescription = llmService.describeImage(image);
        SearchRequest searchRequest = SearchRequest.builder()
                .query(imageDescription)
                .topK(10)
                .similarityThreshold(0.85f)
                .build();
        List<Document> searchResult = chromaVectorStore.doSimilaritySearch(searchRequest);
        searchResult.forEach(document -> {
            Double score = document.getScore();

            Double distance = null;
            Object distanceObj = document.getMetadata().get("distance");
            if(distanceObj!=null)
            {
                distance = Double.parseDouble(distanceObj.toString());
            }

            Object productId = document.getMetadata().get("productId");
            log.info("Search image similarity score: {}, Product Id: {}," +
                    "Distance: {}",score,productId,distance);
        });
        return searchResult.stream().map(doc ->
                doc.getMetadata().get("productId"))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
