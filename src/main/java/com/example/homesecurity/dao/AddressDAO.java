package com.example.homesecurity.dao;

import com.example.homesecurity.entity.Address;

import java.util.List;

public interface AddressDAO {

    void save(Address address);

    List<Address> findAll();

    void deleteAddress(Long id);

    List<Address> findByUsername();
}
