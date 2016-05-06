package com.example.dao;

import com.example.entity.Customer;

public interface IDao {

	Customer findOne(long id);
	
}
