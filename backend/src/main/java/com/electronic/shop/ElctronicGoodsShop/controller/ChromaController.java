package com.electronic.shop.ElctronicGoodsShop.controller;

import com.electronic.shop.ElctronicGoodsShop.response.ApiResponse;
import com.electronic.shop.ElctronicGoodsShop.service.chroma.IChromaService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingResponse;
import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/collections")
public class ChromaController {

    private final IChromaService chromaService;
    @GetMapping
    public ResponseEntity<ApiResponse> getAllCollections(){
        List<Collection> collections = chromaService.getCollections();
        return ResponseEntity.ok(new ApiResponse("Collections found!", collections));
    }
    @DeleteMapping("/{collectionName}/delete")
    public ResponseEntity<ApiResponse> deleteCollection (@PathVariable String collectionName) {
        chromaService.deleteCollection (collectionName);
        return ResponseEntity.ok(new ApiResponse("Collection deleted!", collectionName));
}
    @GetMapping("/{collectionId}/embeddings")
    public ResponseEntity<ApiResponse> getCollectionEmbeddings (@PathVariable String collectionId) {
        GetEmbeddingResponse embedding = chromaService.getEmbedding(collectionId);

        return ResponseEntity.ok(new ApiResponse ("Embedding found!",embedding));
    }
}