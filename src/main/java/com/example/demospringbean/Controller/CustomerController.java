package com.example.demospringbean.Controller;

import com.example.demospringbean.Model.Customer;
import com.example.demospringbean.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    // lay tất cả danh sách khách hàng
    @GetMapping("/customers/getall")
    public Iterable<Customer> getAll(){
        return customerService.getAll();
    }

    // xóa 1 khách hàng thông qua id
    @GetMapping("/customers/deleteById/{id}")
    public void deleteCustomerById(@PathVariable("id") Integer id){
        customerService.deleteCustomder(id);
    }

    // tạo mới 1 khách hàng
    @PostMapping("/customers/create")
    public Customer createCustomer(@RequestBody Customer customer){
        return customerService.createCustomer(customer);
    }
    @GetMapping("/customers/findById/{id}")

    // lấy dữ liệu khách hàng thông qua id
    public Optional<Customer> getCumstomer(@PathVariable("id") Integer id){
        return customerService.getCustomer(id);
    }
}
