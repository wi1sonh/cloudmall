package fun.cmgraph.service;

import fun.cmgraph.dto.UserDTO;
import fun.cmgraph.dto.UserLoginDTO;
import fun.cmgraph.entity.User;

public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);

    User getUser(Integer id);

    void update(UserDTO userDTO);
}
