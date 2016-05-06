package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.IDao;
import com.example.entity.Customer;

@Service
final class ServiceImpl implements IService {

	@Autowired
	private IDao dao;

	public Customer findOne(long id) {
		return dao.findOne(id);
	}
	
}
