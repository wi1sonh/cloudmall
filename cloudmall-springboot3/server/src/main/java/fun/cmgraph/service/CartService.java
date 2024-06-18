package fun.cmgraph.service;

import fun.cmgraph.dto.CartDTO;
import fun.cmgraph.entity.Cart;

import java.util.List;

public interface CartService {
    void add(CartDTO cartDTO);

    List<Cart> getList();

    void clean();

    void sub(CartDTO cartDTO);
}
