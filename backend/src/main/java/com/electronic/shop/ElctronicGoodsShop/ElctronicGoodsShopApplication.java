package com.electronic.shop.ElctronicGoodsShop;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ElctronicGoodsShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElctronicGoodsShopApplication.class, args);
	}

}
