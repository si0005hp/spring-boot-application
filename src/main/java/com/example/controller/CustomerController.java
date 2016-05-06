package com.example.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.entity.Customer;
import com.example.service.IService;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

	@Autowired
	private IService service;
	
	@RequestMapping("/")
	@ResponseBody
	public String index() {
		return "Hello world!!!";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Customer findOne(@PathVariable long id) {
		return service.findOne(id);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Customer> register(@RequestBody Customer customer, UriComponentsBuilder uriBuilder) {
		URI location = uriBuilder.path("/customer/{id}").buildAndExpand(customer.getId()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);
		return new ResponseEntity<>(customer, headers, HttpStatus.CREATED);
	}
	
}
