package fun.cmgraph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class ProductPageDTO implements Serializable {

    private int page;
    private int pageSize;
    private String name;
    private Integer categoryId;
    private Integer status;

}
