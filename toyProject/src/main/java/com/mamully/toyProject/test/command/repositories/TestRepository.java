package com.mamully.toyProject.test.command.repositories;

import com.mamully.toyProject.test.command.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
