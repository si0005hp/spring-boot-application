package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.JobMstDao;
import com.example.struct.JobMstDto;

@Service
final class JobMstManageServiceImpl implements JobMstManageService {

	@Autowired
	private JobMstDao dao;
	
	@Override
	public List<JobMstDto> getAllJobMst() {
		return dao.getAllJobMst();
	}

}
