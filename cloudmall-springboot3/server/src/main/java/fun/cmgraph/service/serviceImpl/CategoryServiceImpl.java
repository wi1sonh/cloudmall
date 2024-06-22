package fun.cmgraph.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import fun.cmgraph.dto.CategoryDTO;
import fun.cmgraph.dto.CategoryTypePageDTO;
import fun.cmgraph.entity.Category;
import fun.cmgraph.mapper.CategoryMapper;
import fun.cmgraph.result.PageResult;
import fun.cmgraph.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增分类
     * @param categoryDTO
     */
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(1);  // 默认启用
        categoryMapper.add(category);
    }

    /**
     * 根据type条件分页查询
     * @param categoryTypePageDTO
     * @return
     */
    public PageResult getPageList(CategoryTypePageDTO categoryTypePageDTO) {
        PageHelper.startPage(categoryTypePageDTO.getPage(), categoryTypePageDTO.getPageSize());
        Page<Category> pagelist = categoryMapper.getPageList(categoryTypePageDTO);
        return new PageResult(pagelist.getTotal(), pagelist.getResult());
    }

    /**
     * 获取所有分类列表
     * @return
     */
    public List<Category> getList(Integer type) {
        List<Category> categoryList = categoryMapper.getList(type);
        return categoryList;
    }

    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    public Category getById(Integer id) {
        return categoryMapper.getById(id);
    }


    /**
     * 根据name查询分类
     * @param name
     * @return
     */
    public Category getByName(String name) {
        return categoryMapper.getByName(name);
    }

    /**
     * 更新分类信息
     * @param categoryDTO
     */
    public void udpate(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.update(category);
    }

    /**
     * 根据id删除分类
     * @param id
     */
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }

    /**
     * 分类起售/停售
     */
    public void onOff(Integer id) {
        categoryMapper.onOff(id);
    }
}
