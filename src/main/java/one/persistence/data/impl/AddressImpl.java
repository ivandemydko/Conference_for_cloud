package one.persistence.data.impl;

import one.persistence.data.IAddress;
import one.persistence.entity.Address;
import one.persistence.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AddressImpl implements IAddress {
    @Autowired
   private AddressRepository addressRepo;

    @Override
    public Address findAddress(String city, String street, String building, String room) {
        return addressRepo.findByCityAndStreetAndBuildingAndRoom(city, street, building, room);
    }

    @Override
    public Address saveAddress(Address address) {
        return addressRepo.save(address);
    }
}
