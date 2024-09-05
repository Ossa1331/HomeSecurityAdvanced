package com.example.homesecurity.factory;

import com.example.homesecurity.entity.Address;
import com.example.homesecurity.service.AddressService;
import com.example.homesecurity.thread.SaveAddressThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveAddressThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(SaveAddressThreadFactory.class);

    private final AddressService addressService;

    @Autowired
    public SaveAddressThreadFactory(AddressService addressService) {
        this.addressService = addressService;
    }

    public SaveAddressThread create(Address address) {
        SaveAddressThread thread = new SaveAddressThread(addressService);
        thread.setAddress(address);
        logger.info("SaveAddressThread created");

        return thread;
    }
}