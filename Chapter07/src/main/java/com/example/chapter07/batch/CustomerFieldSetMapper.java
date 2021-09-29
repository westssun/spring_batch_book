package com.example.chapter07.batch;

import com.example.chapter07.domain.Customer;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * @author Michael Minella
 */
public class CustomerFieldSetMapper implements FieldSetMapper<Customer> {

    /**
     * FieldSet : 다양한 타입의 필드 관리 가능
     * @param fieldSet
     * @return
     */
    public Customer mapFieldSet(FieldSet fieldSet) {
        Customer customer = new Customer();

//		customer.setAddress(fieldSet.readString("addressNumber") +
//				" " + fieldSet.readString("street"));
        customer.setCity(fieldSet.readString("address"));
        customer.setCity(fieldSet.readString("city"));
        customer.setFirstName(fieldSet.readString("firstName"));
        customer.setLastName(fieldSet.readString("lastName"));
        customer.setMiddleInitial(fieldSet.readString("middleInitial"));
        customer.setState(fieldSet.readString("state"));
        customer.setZipCode(fieldSet.readString("zipCode"));

        return customer;
    }
}
