package fun.cmgraph.service;

import com.github.pagehelper.Page;
import fun.cmgraph.result.PageResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.DigestUtils;

import fun.cmgraph.dto.*;
import fun.cmgraph.entity.Employee;
import fun.cmgraph.mapper.EmployeeMapper;
import fun.cmgraph.service.serviceImpl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;


    @Test
    void getEmployeeById() {
        when(employeeMapper.getById(1)).thenReturn(new Employee(1, "test", "account", "password", "12345678910", 18, 1, "pic", 1, 1, 1, null, null));

        Employee employee = employeeService.getEmployeeById(1);
        assertEquals(employee, new Employee(1, "test", "account", "password", "12345678910", 18, 1, "pic", 1, 1, 1, null, null));
    }

    @Test
    void login() {
        String password = "password";
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        when(employeeMapper.getByAccount("account")).thenReturn(new Employee(1, "test", "account", password, "12345678910", 18, 1, "pic", 1, 1, 1, null, null));

        EmployeeLoginDTO EmployeeLoginDTO = new EmployeeLoginDTO();
        EmployeeLoginDTO.setAccount("account");
        EmployeeLoginDTO.setPassword("password");

        Employee employee = employeeService.login(EmployeeLoginDTO);
        assertEquals(employee, new Employee(1, "test", "account", password, "12345678910", 18, 1, "pic", 1, 1, 1, null, null));
    }

    @Test
    void register() {
        EmployeeLoginDTO EmployeeLoginDTO = new EmployeeLoginDTO();
        EmployeeLoginDTO.setAccount("account");
        EmployeeLoginDTO.setPassword("password");

        employeeService.register(EmployeeLoginDTO);
        verify(employeeMapper, times(1)).regEmployee(any());
    }

    @Test
    void employeePageList() {
        PageDTO pageDTO = new PageDTO(1, 10, "test");
        when(employeeMapper.pageQuery(pageDTO)).thenReturn(new Page<Employee>());

        PageResult pageResult = employeeService.employeePageList(pageDTO);
        assertEquals(pageResult, new PageResult(0L, (new Page<Employee>()).getResult()));
    }

    @Test
    void update() {
        EmployeeDTO employeeDTO = new EmployeeDTO(1, "test", "account", "password", "12345678910", 18, 1, "pic");

        employeeService.update(employeeDTO);
        verify(employeeMapper, times(1)).update(any());
    }

    @Test
    void delete() {
        employeeService.delete(1);
        verify(employeeMapper, times(1)).delete(1);
    }

    @Test
    void onOff() {
        employeeService.onOff(1);
        verify(employeeMapper, times(1)).onOff(1);
    }

    @Test
    void addEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO(1, "test", "account", "password", "12345678910", 18, 1, "pic");

        employeeService.addEmployee(employeeDTO);
        verify(employeeMapper, times(1)).addEmployee(any());
    }

    @Test
    void fixPwd() {
        String password = "password";
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        when(employeeMapper.getById(any())).thenReturn(new Employee(1, "test", "account", password, "12345678910", 18, 1, "pic", 1, 1, 1, null, null));
        EmployeeFixPwdDTO employeeDTO = new EmployeeFixPwdDTO("password", "newPassword");

        employeeService.fixPwd(employeeDTO);
        verify(employeeMapper, times(1)).updatePwd(any());
    }
}