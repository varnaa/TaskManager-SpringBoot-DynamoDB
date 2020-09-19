package com.varnaa.taskManager.repository;

import com.varnaa.taskManager.model.Tasks;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface TasksRepository extends CrudRepository<Tasks, String> {

}
