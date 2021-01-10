package com.example.demospringbean.Service;

import com.example.demospringbean.Model.Customer;
import com.example.demospringbean.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    // method lấy tất cả các customer, jpa findAll()
    @Override
    public Iterable<Customer> getAll() {
        return customerRepository.findAll();
    }

    // tạo mới 1 đối tượng và lưu vào db, sau đó trả về reponse (saveandflush)
    @Override
    public Customer createCustomer(Customer requestCustomer) {
        Customer reponseCustomer = new Customer();
        reponseCustomer.setAge(requestCustomer.getAge());
        reponseCustomer.setEmail(requestCustomer.getEmail());
        reponseCustomer.setName(requestCustomer.getName());
        return customerRepository.saveAndFlush(reponseCustomer);
    }

    // xóa 1 khách hàng bằng id, jpa deleteById()
    @Override
    public void deleteCustomder(Integer id) {
        customerRepository.deleteById(id);
    }

    // tìm kiếm 1 khách hàng, jpa findById
    @Override
    public Optional<Customer> getCustomer(Integer id) {
        Customer customer = new Customer(id);
    // multi language getCustomer ?

        return customerRepository.findById(id);
    }
}
