package com.elastic.zhl.controller;

import com.elastic.zhl.entity.Employee;
import com.elastic.zhl.repository.EmployeeRepository;
import com.google.gson.Gson;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZHL on 2018/12/29.
 */
@RestController
@RequestMapping("/es")
public class ElasticSearchController {

    @Autowired
    private EmployeeRepository er;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    /**
     * @Author ZHL
     * @Description  调用 ES 获取 IK 分词后结果
     * @Date 14:41 2018/12/29
     * @Param [searchContent]
     * @return java.util.List<java.lang.String>
     **/
    @GetMapping("/ik")
   public List<String> getIkAnalyzeSearchTerms(String searchContent) {
        //调用 IK 分词分词

       AnalyzeRequestBuilder ikRequest = new AnalyzeRequestBuilder(elasticsearchTemplate.getClient(),
               AnalyzeAction.INSTANCE, "megacorp", searchContent);
       ikRequest.setTokenizer("ik_max_word");

       List<AnalyzeResponse.AnalyzeToken> ikTokenList = ikRequest.execute().actionGet().getTokens();

       //循环赋值
       List<String> searchTermList = new ArrayList<>();

       ikTokenList.forEach(ikToken -> {
           searchTermList.add(ikToken.getTerm());
       });

       return searchTermList;
   }

    //增加
    @RequestMapping("/add")
    public String add(Employee employee){
        er.save(employee);

        System.err.println("add a obj");

        return "success";
    }

    //删除
    @RequestMapping("/delete")
    public String delete(){
        Employee employee=new Employee();
        employee.setId("1");
        er.delete(employee);

        return "success";
    }

    //局部更新
    @RequestMapping("/update")
    public String update(){

        Employee employee=er.queryEmployeeById("1");
        employee.setFirstName("哈哈");
        er.save(employee);

        System.err.println("update a obj");

        return "success";
    }

    //查询
    @RequestMapping("/query/{id}")
    public Employee query(@PathVariable("id")String id){

        Employee accountInfo=er.queryEmployeeById(id);
        System.err.println(new Gson().toJson(accountInfo));

        return accountInfo;
    }

    @GetMapping("/query/firstName")
    public List<Employee> queryFirstName(@RequestParam("firstName") String firstName){
        Employee param = new Employee();
        param.setFirstName(firstName);
        List<Employee> e = er.findByFirstName(firstName);
        System.err.println(new Gson().toJson(e));

        return e;
    }
}
