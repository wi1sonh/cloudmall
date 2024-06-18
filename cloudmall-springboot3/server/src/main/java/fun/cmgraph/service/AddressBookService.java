package fun.cmgraph.service;

import fun.cmgraph.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    void addAddress(AddressBook addressBook);

    List<AddressBook> list(AddressBook addressBook);

    void updateAddress(AddressBook addressBook);

    AddressBook getById(Integer id);

    void setDefault(AddressBook addressBook);

    void deleteById(Integer id);
}
