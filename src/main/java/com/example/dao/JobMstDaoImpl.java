package com.example.dao;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.struct.JobMstDto;
import com.example.struct.JobMstDto.JobStatus;

@Repository
final class JobMstDaoImpl implements JobMstDao {

	@Override
	public List<JobMstDto> getAllJobMst() {
		return Arrays.asList(
				new JobMstDto(UUID.fromString("058e5b60-6427-4c85-a0bd-c6e3156d6162"), JobStatus.STARTED,
						LocalDateTime.now(), LocalDateTime.now()),
				new JobMstDto(UUID.fromString("e24ddb57-9c7b-4f70-a69a-39254cc695e4"), JobStatus.IN_PROGRESS,
						LocalDateTime.now(), LocalDateTime.now())
				);
	}

}
