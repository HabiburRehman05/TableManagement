package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.exception.TableAlreadyExistsException;
import com.example.services.TableService;


@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/table")
public class TableController {

	@Autowired
	private TableService tableService;

	@GetMapping("/getSample")
	public String getSample() {
		return "Sample is working";
	}

//    @GetMapping("/describe/{tableName}")
//    public void describeTable(@PathVariable String tableName) {
//        tableService.describeTable(tableName);
//    }

	@GetMapping("/getTableData")
	public void getTableData() {
		tableService.getTableData();
	}

	@PostMapping("/savedVariable")
	public ResponseEntity<String> receiveVariable(@RequestBody String receivedData) {

		try {
			String decodedData = URLDecoder.decode(receivedData, StandardCharsets.UTF_8.toString());

			// Handle the decoded data
			System.out.println("Received decoded data from Quasar: " + decodedData);

			// You can now store the data or perform any other actions
			int length = decodedData.length();
			String query = decodedData.substring(0, decodedData.length() - 1);
			System.out.println(query);
			
			// Send a response back to Quasar
			String acknowledgmentMessage = "Data received successfully";
			
			
			return ResponseEntity.ok(acknowledgmentMessage + query);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Error decoding data");
		}

//        System.out.println("Received data from Quasar: " + receivedData);
//
//        // You can now store the data or perform any other actions
//
//        // Send a response back to Quasar
//        String acknowledgmentMessage = "Data received successfully";
//        return ResponseEntity.ok(acknowledgmentMessage);
	}
	
	@PostMapping("/execute")
	public ResponseEntity<String> executeQuery(@RequestBody String receivedData) {
		
		try {
			String decodedData = URLDecoder.decode(receivedData, StandardCharsets.UTF_8.toString());

			System.out.println("Received decoded data from Quasar: " + decodedData);

			int length = decodedData.length();
			String query = decodedData.substring(0, decodedData.length() - 1);
			System.out.println(query);
					
			tableService.executeSqlQuery(query);
	
			return ResponseEntity.ok("Query Executed successfully " + query);
		}catch(TableAlreadyExistsException e) {
			 return ResponseEntity.status(HttpStatus.CONFLICT).body("Table already exists: " + e.getMessage());
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Error decoding data");
		}
		
		
	}
	
}
