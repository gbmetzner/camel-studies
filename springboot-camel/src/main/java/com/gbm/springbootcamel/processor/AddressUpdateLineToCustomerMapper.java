package com.gbm.springbootcamel.processor;

import com.gbm.springbootcamel.common.InvalidCustomerAddressException;
import com.gbm.springbootcamel.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressUpdateLineToCustomerMapper {

    public void validate(List<String> addressLineRow) throws InvalidCustomerAddressException
    {
        // Make sure the row has the valid number of tokens
        if (addressLineRow == null || addressLineRow.size() != 6) {
            throw new InvalidCustomerAddressException(                    "Invalid row, must have 6 columns of data");
        }

        if (addressLineRow.get(0) == null || addressLineRow.get(0).isBlank()) {
            throw new InvalidCustomerAddressException(                    "Invalid ID, required field");
        }

        try {
            Integer.parseInt(addressLineRow.get(0));
        } catch (NumberFormatException e) {
            throw new InvalidCustomerAddressException(                    "Invalid ID, must be a number");
        }

        if (addressLineRow.get(1) == null || addressLineRow.get(1).isBlank()) {
            throw new InvalidCustomerAddressException(                    "Invalid Address Line 1, required field");
        }

        if (addressLineRow.get(3) == null || addressLineRow.get(3).isBlank()) {
            throw new InvalidCustomerAddressException(                    "Invalid City, required field");        }

        if (addressLineRow.get(4) == null || addressLineRow.get(4).isBlank()) {
            throw new InvalidCustomerAddressException(                    "Invalid State, required field");
        }

        if (addressLineRow.get(5) == null || addressLineRow.get(5).isBlank()) {
            throw new InvalidCustomerAddressException(                    "Invalid Postal Code, required field");
        }
    }

    public Customer process(List<String> addressRow) {
        return new Customer(Integer.parseInt(addressRow.get(0)), addressRow.get(1), addressRow.get(2), addressRow.get(3),
                addressRow.get(4), addressRow.get(5));
    }

}
