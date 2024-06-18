package fun.cmgraph.dto;

import fun.cmgraph.entity.Dish;
import fun.cmgraph.entity.DishFlavor;
import fun.cmgraph.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetmealDTO implements Serializable {

    private Integer id;
    private String name;
    private String pic;
    private String detail;
    private BigDecimal price;
    private String status;
    private Integer categoryId;
    // 当前套餐包含的多种菜品
    private List<SetmealDish> setmealDishes = new ArrayList<>();

}
