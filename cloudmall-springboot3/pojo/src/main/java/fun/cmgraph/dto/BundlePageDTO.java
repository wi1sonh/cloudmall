package fun.cmgraph.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@AllArgsConstructor
@Data
public class BundlePageDTO implements Serializable {

    private int page;
    private int pageSize;
    private String name;
    private Integer categoryId;
    private Integer status;
}
