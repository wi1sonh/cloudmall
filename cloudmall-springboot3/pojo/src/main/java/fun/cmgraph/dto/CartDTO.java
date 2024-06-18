package fun.cmgraph.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CartDTO implements Serializable {

    private Integer dishId;
    private Integer setmealId;
    private String dishFlavor;

}
