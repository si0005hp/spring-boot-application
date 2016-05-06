package com.example.dao;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.entity.Customer;

@Repository
public class DaoImpl implements IDao {

	public Customer findOne(long id) {
		return new Customer(id, UUID.randomUUID().toString());
	}

}
