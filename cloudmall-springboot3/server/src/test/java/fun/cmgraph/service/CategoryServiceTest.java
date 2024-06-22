package fun.cmgraph.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fun.cmgraph.dto.*;
import fun.cmgraph.entity.Category;
import fun.cmgraph.mapper.CategoryMapper;
import fun.cmgraph.service.serviceImpl.CategoryServiceImpl;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Test
    void addCategory() {
        CategoryDTO categoryDTO = new CategoryDTO(1, "test", 1, 1);
        categoryServiceImpl.addCategory(categoryDTO);
        verify(categoryMapper, times(1)).add(any(Category.class));
    }

    @Test
    void getList() {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "test", 1, 1, 1, 100, 100, null, null));
        when(categoryMapper.getList(1)).thenReturn(categoryList);
        List<Category> result = categoryServiceImpl.getList(1);
        assertEquals(1, result.size());
        assertEquals("test", result.get(0).getName());
    }

    @Test
    void getById() {
        Category category = new Category(1, "test", 1, 1, 1, 100, 100, null, null);
        when(categoryMapper.getById(1)).thenReturn(category);
        Category result = categoryServiceImpl.getById(1);
        assertEquals("test", result.getName());
    }

    @Test
    void onOff() {
        categoryServiceImpl.onOff(1);
        verify(categoryMapper, times(1)).onOff(1);
    }

    @Test
    void udpate() {
        CategoryDTO categoryDTO = new CategoryDTO(1, "test", 1, 1);
        categoryServiceImpl.udpate(categoryDTO);
        verify(categoryMapper, times(1)).update(any(Category.class));
    }

    @Test
    void delete() {
        categoryServiceImpl.delete(1);
        verify(categoryMapper, times(1)).delete(1);
    }
}