package com.elastic.zhl.repository;

import com.elastic.zhl.entity.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by ZHL on 2018/12/29.
 */
public interface EmployeeRepository extends ElasticsearchRepository<Employee, String> {
    Employee queryEmployeeById(String id);

    Employee queryEmployeeByFirstName(String firstName);

    List<Employee> findByFirstName(String firstName);

}
