package com.honesty.post.model.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportRepo extends JpaRepository<Report, UUID> {

    boolean existsReportByPostIdAndUserUid(UUID postUid, UUID userUid);
}
