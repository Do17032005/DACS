package com.example.Clothesshoponline.service;

import com.example.Clothesshoponline.model.Address;
import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> getUserAddresses(User user) {
        return addressRepository.findByUser(user);
    }

    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
    }

    @Transactional
    public Address saveAddress(Address address, User user) {
        address.setUser(user);

        // If this is set as default, unset other defaults
        if (address.isDefault()) {
            List<Address> userAddresses = addressRepository.findByUser(user);
            for (Address addr : userAddresses) {
                if (addr.isDefault()) {
                    addr.setDefault(false);
                    addressRepository.save(addr);
                }
            }
        }

        return addressRepository.save(address);
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

    @Transactional
    public void setDefaultAddress(Long id, User user) {
        // Unset all default addresses for this user
        List<Address> userAddresses = addressRepository.findByUser(user);
        for (Address addr : userAddresses) {
            if (addr.isDefault()) {
                addr.setDefault(false);
                addressRepository.save(addr);
            }
        }

        // Set the selected address as default
        Address address = getAddressById(id);
        address.setDefault(true);
        addressRepository.save(address);
    }

    public Address getDefaultAddress(User user) {
        return addressRepository.findByUserAndIsDefaultTrue(user)
                .orElse(null);
    }
}
