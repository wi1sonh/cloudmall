package fun.cmgraph.service;

import com.github.pagehelper.Page;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fun.cmgraph.entity.AddressBook;
import fun.cmgraph.mapper.AddressBookMapper;
import fun.cmgraph.service.serviceImpl.AddressBookServiceImpl;

@ExtendWith(MockitoExtension.class)
class AddressBookServiceTest {

    @Mock
    private AddressBookMapper addressBookMapper;

    @InjectMocks
    private AddressBookServiceImpl addressBookService;

    @Test
    void addAddress() {
        AddressBook addressBook = new AddressBook();
        addressBookService.addAddress(addressBook);
        verify(addressBookMapper, times(1)).insert(any());
    }

    @Test
    void list() {
        AddressBook addressBook = new AddressBook();
        when(addressBookMapper.getUserAddress(addressBook)).thenReturn(null);
        addressBookService.list(addressBook);
        verify(addressBookMapper, times(1)).getUserAddress(any());
    }

    @Test
    void updateAddress() {
        AddressBook addressBook = new AddressBook();
        addressBookService.updateAddress(addressBook);
        verify(addressBookMapper, times(1)).udpate(any());
    }

    @Test
    void getById() {
        when(addressBookMapper.getById(1)).thenReturn(new AddressBook());
        addressBookService.getById(1);
        verify(addressBookMapper, times(1)).getById(1);
    }

    @Test
    void setDefault() {
        AddressBook addressBook = new AddressBook();
        addressBookService.setDefault(addressBook);
        verify(addressBookMapper, times(1)).udpate(any());
    }

    @Test
    void deleteById() {
        addressBookService.deleteById(1);
        verify(addressBookMapper, times(1)).delete(any());
    }
}