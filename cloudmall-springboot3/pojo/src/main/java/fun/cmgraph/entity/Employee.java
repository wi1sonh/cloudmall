package fun.cmgraph.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {

    private Integer id;
    private String name;
    private String account;
    private String password;
    private String phone;
    private Integer age;
    private Integer gender;
    private String pic;
    private Integer status;
    private Integer createUser;
    private Integer updateUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
