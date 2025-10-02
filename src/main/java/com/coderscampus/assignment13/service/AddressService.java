package com.coderscampus.assignment13.service;

import com.coderscampus.assignment13.domain.Address;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    public boolean isAddressEmpty(Address address) {
        if (address == null) return true;
        return (address.getAddressLine1() == null || address.getAddressLine1().isBlank()) &&
                (address.getAddressLine2() == null || address.getAddressLine2().isBlank()) &&
                (address.getCity()   == null || address.getCity().isBlank()) &&
                (address.getRegion()  == null || address.getRegion().isBlank()) &&
                (address.getCountry()  == null || address.getCountry().isBlank()) &&
                (address.getZipCode()    == null || address.getZipCode().isBlank());
    }

}
