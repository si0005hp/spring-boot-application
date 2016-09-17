package com.example.struct;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class JobMstDto {

	private UUID jobId;
	private JobStatus status;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public enum JobStatus {
		STARTED(0, "started"),
		IN_PROGRESS(1, "in progress"),
		FINISHED(2, "finished");
		private final int value;
		private final String name;
		
		@Override
		public String toString() {
			return name;
		}
	}
	
}
