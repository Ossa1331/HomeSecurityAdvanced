package com.example.homesecurity.thread;

import com.example.homesecurity.entity.Address;
import com.example.homesecurity.service.AddressService;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteAddressThread implements Runnable{

    private static final Logger logger= LoggerFactory.getLogger(DeleteAddressThread.class);

    private Address address;
    private final AddressService addressService;

    public DeleteAddressThread(AddressService addressService) {
        this.addressService = addressService;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public void run(){
        Platform.runLater(()->{
            try{
                addressService.deleteAddress(address);
                logger.info("DeleteCameraThread running...");
            }catch(Exception e){
                logger.error("there has been an error in thread runtime. ", e);
            }
        });
    }
}
