package com.portfolio.controller;

import com.portfolio.model.PortfolioItem;
import com.portfolio.service.PortfolioItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PortfolioController {

	private final PortfolioItemService portfolioItemService;

	@Autowired
	public PortfolioController(PortfolioItemService portfolioItemService) {
		this.portfolioItemService = portfolioItemService;

	}

	// GET all portfolio items
	@GetMapping("/get")
	public ResponseEntity<List<PortfolioItem>> getAllPortfolioItems() {
		List<PortfolioItem> items = portfolioItemService.getAllPortfolioItems();
		return ResponseEntity.ok(items);

	}

	// GET a portfolio item by ID
	@GetMapping("/get/{ItemId}")
	public ResponseEntity<PortfolioItem> getPortfolioItemById(@PathVariable String ItemId) {
		PortfolioItem item = portfolioItemService.getPortfolioItemById(ItemId);
		if (item != null) {
			return ResponseEntity.ok(item);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// Create a new portfolio item
	@PostMapping("/post")
	public ResponseEntity<PortfolioItem> createPortfolioItem(
			@RequestParam("file") MultipartFile file,
			@RequestParam("preoject_name") String project_name,
			@RequestParam("description") String description,
			@RequestParam("skills") String skills,
			@RequestParam("repo_link") String repo_link) {
		PortfolioItem createdItem = portfolioItemService.createPortfolioItem(file,project_name,description,skills,repo_link);
		return ResponseEntity.ok(createdItem);

	}

	// Updating a portfolio Item
	// Updating a Portfolio item
	@PutMapping("/update/{ItemId}")
	public ResponseEntity<PortfolioItem> updatePortfolioItem(
			@PathVariable String ItemId,
			@RequestBody PortfolioItem updatedItem) {
		PortfolioItem updated = portfolioItemService.updatePortfolioItem(ItemId, updatedItem);
		if (updated != null) {
			return ResponseEntity.ok(updated);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Deleting an item by ID
	@DeleteMapping("/delete/{ItemId}")
	public ResponseEntity<Void> deletePortfolioItem(@PathVariable String ItemId) {
		portfolioItemService.deletePortfolioItem(ItemId);
		return ResponseEntity.noContent().build();
	}

}
