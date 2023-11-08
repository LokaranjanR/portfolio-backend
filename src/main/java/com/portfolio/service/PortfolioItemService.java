package com.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.model.PortfolioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.io.IOException;
import java.io.InputStream;

//@Service makes this class a Spring managed component
@Service
public class PortfolioItemService {
	private final S3Client s3Client;
	private final String bucketName; // has to be changed with actual bucket name

	@Autowired
	public PortfolioItemService(S3Client s3Client, String bucketName) {
		this.s3Client = s3Client;
		this.bucketName = bucketName;
	}

	// to save a new Portfolio item

	public PortfolioItem createPortfolioItem(MultipartFile file, String project_name, String description, String skills,
			String repo_link) {
		try {
			String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
			String key = "portfolio/" + filename;
//		        Upload to S3
			s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key) // Specifying a key for your item
					.build(), RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

//		        Create a new Portfolio Item
			PortfolioItem newItem = new PortfolioItem(project_name, description, skills, repo_link, key);

			return newItem;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	// Logic to retrieve all portfolio
	public List<PortfolioItem> getAllPortfolioItems() {
		List<PortfolioItem> portfolioItems = new ArrayList<>();

		ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
				.bucket(bucketName)
				.prefix("portfolio/")
				.build();
		
		ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjectsRequest);

		for (S3Object s3Object : listObjectsResponse.contents()) {
			String key = s3Object.key();

//			 Retrieve all objects from S3
			GetObjectRequest getObjectRequest = GetObjectRequest.builder()
					.bucket(bucketName)
					.key(key)
					.build();

//			 Get the GetObjectResponse object from the ResponseInputStream

			try (ResponseInputStream<GetObjectResponse> getObjectResponse = s3Client.getObject(getObjectRequest)) {
//				 Parse the object data into a portfolio item
				InputStream objectData = getObjectResponse;
				PortfolioItem item = parseItemFromJson(objectData);
				portfolioItems.add(item);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return portfolioItems;
	}

	// Logic to retrieve a portfolio item by ID

	public PortfolioItem getPortfolioItemById(String ItemId) {
//		 Contructing the S3 request key based on the ID
		String key = "portfolio/" + ItemId + ".json";

//		 Retrieve the objects from S3
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build();

		try (ResponseInputStream<GetObjectResponse> getObjectResponse = s3Client.getObject(getObjectRequest)) {
			InputStream objectData = getObjectResponse;
			return parseItemFromJson(objectData);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// logic to update a portfolio item(PUT)

	public PortfolioItem updatePortfolioItem(String ItemId, PortfolioItem updatedItem) {
//		 Construct the S3 key based on the Item ID

		String key = "portfolio/" + ItemId + ".json";

//		 Convert the updated item to json
		String updatedItemJson = convertItemToJson(updatedItem);

//		 Upload the item to S3
		s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).build(),
				RequestBody.fromString(updatedItemJson));

		return updatedItem;
	}

	public void deletePortfolioItem(String ItemId) {
//		 Construct an S3 key based on the ItemId
		String key = "portfolio/" + ItemId + ".json";

//		 Deleting the object from S3
		s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());
	}

//	 method to convert objects to JSON
	private String convertItemToJson(PortfolioItem item) {
		try {
//			 Creating an Object mapper from jackson
			ObjectMapper objectMapper = new ObjectMapper();

//			 Serialize the PortfolioItem object to JSON
			String jsonString = objectMapper.writeValueAsString(item);

			return jsonString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

// parsing the item from JSON
	private PortfolioItem parseItemFromJson(InputStream jsonStream) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(jsonStream, PortfolioItem.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

//	 logic to list and retrieve items from S3
	private List<PortfolioItem> retrieveAllPortfolioItemsFromS3() {
		List<PortfolioItem> portfolioItems = new ArrayList<>();

		ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder().bucket(bucketName).prefix("portfolio/")
				.build();

		ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjectsRequest);

		for (S3Object s3Object : listObjectsResponse.contents()) {
			String key = s3Object.key();

//			 S3 request to get the object Data
			GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();

//			 Retrieve the object data as an input Stream
			try (InputStream objectData = s3Client.getObjectAsBytes(getObjectRequest).asInputStream()) {
//				Parse and desiarlize the data
				PortfolioItem item = parseItemFromJson(objectData);
				portfolioItems.add(item);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return portfolioItems;

	}

}
