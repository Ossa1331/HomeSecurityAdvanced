package com.example.homesecurity.factory;

import com.example.homesecurity.entity.Address;
import com.example.homesecurity.service.AddressService;
import com.example.homesecurity.thread.DeleteAddressThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteAddressThreadFactory {

    private static final Logger logger= LoggerFactory.getLogger(DeleteCameraThreadFactory.class);

    private final AddressService addressService;

    @Autowired
    public DeleteAddressThreadFactory(AddressService addressService) {
        this.addressService = addressService;
    }

    public DeleteAddressThread create(Address address) {
        DeleteAddressThread thread = new DeleteAddressThread(addressService);
        thread.setAddress(address);
        logger.info("DeleteCameraThread created");

        return thread;
    }
}
