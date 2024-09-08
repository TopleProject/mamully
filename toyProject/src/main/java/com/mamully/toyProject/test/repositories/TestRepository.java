package com.mamully.toyProject.test.repositories;

import com.mamully.toyProject.test.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
