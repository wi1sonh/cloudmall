package fun.cmgraph.service;

import fun.cmgraph.dto.EmployeeDTO;
import fun.cmgraph.dto.EmployeeFixPwdDTO;
import fun.cmgraph.dto.EmployeeLoginDTO;
import fun.cmgraph.dto.PageDTO;
import fun.cmgraph.entity.Employee;
import fun.cmgraph.result.PageResult;

public interface EmployeeService {
    Employee getEmployeeById(Integer id);

    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void register(EmployeeLoginDTO employeeLoginDTO);

    PageResult employeePageList(PageDTO pageDTO);

    void update(EmployeeDTO employeeDTO);

    void delete(Integer id);

    void onOff(Integer id);

    void addEmployee(EmployeeDTO employeeDTO);

    void fixPwd(EmployeeFixPwdDTO employeeFixPwdDTO);
}
