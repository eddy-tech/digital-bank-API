package org.backend.banqueSI.mappers;

import org.backend.banqueSI.dtos.CustomerDTO;
import org.backend.banqueSI.entities.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

//MapStruct JMapper
@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer (Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        return customerDTO;

        /*
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());

         */
    }

    public Customer fromCustomerDTO (CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }
}
