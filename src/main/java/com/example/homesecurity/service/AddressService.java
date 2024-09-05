package com.example.homesecurity.service;

import com.example.homesecurity.dao.AddressDAO;
import com.example.homesecurity.entity.Address;
import com.example.homesecurity.util.IsDatabaseOperationInProgress;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {


    private final AddressDAO addressDAO;

    public AddressService(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public synchronized void saveAddress(Address address) {
        while (IsDatabaseOperationInProgress.isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", ex);
            }
        }
        IsDatabaseOperationInProgress.isDatabaseOperationInProgress = true;
        try {
            addressDAO.save(address);
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }

    public synchronized List<Address> findAddressesByUsername(){
        List<Address> userAddresses=new ArrayList<>();
        while(IsDatabaseOperationInProgress.isDatabaseOperationInProgress){
            try {
                wait();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", ex);
            }
        }
        IsDatabaseOperationInProgress.isDatabaseOperationInProgress = true;
        try {
            userAddresses.addAll(addressDAO.findByUsername());
        } finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
        return userAddresses;
    }

    public synchronized void deleteAddress(Address address){
        while(IsDatabaseOperationInProgress.isDatabaseOperationInProgress){
            try {
                wait();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", ex);
            }
        }
        IsDatabaseOperationInProgress.isDatabaseOperationInProgress = true;
        try{
            addressDAO.deleteAddress(address.getId());
        }
        finally {
            IsDatabaseOperationInProgress.isDatabaseOperationInProgress = false;
            notifyAll();
        }
    }
}