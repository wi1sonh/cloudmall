package fun.cmgraph.service;

import fun.cmgraph.dto.CategoryDTO;
import fun.cmgraph.dto.CategoryTypePageDTO;
import fun.cmgraph.entity.Category;
import fun.cmgraph.result.PageResult;

import java.util.List;

public interface CategoryService {
    void addCategory(CategoryDTO categoryDTO);

    PageResult getPageList(CategoryTypePageDTO categoryTypePageDTO);

    List<Category> getList(Integer type);

    Category getById(Integer id);
    void onOff(Integer id);

    void udpate(CategoryDTO categoryDTO);

    void delete(Integer id);

}
