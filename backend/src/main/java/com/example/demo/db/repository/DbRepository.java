package com.example.demo.db.repository;

import com.example.demo.db.model.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DbRepository<T extends Persistable> extends JpaRepository<T, Long> {

}
