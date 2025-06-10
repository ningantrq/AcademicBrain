package com.yanhuo.platform.unit.service;

import com.yanhuo.platform.service.impl.CategoryServiceImpl;
import com.yanhuo.xo.dao.CategoryDao;
import com.yanhuo.xo.entity.Category;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * 📂 分类服务单元测试实战例子
 * 
 * 这是一个完整的单元测试示例，展示了：
 * ✅ 完整的测试生命周期（@BeforeEach, @AfterEach）
 * ✅ 多种测试场景（正常、异常、边界条件）
 * ✅ Mock对象的使用和验证
 * ✅ 详细的断言和验证
 * ✅ 清晰的测试文档和命名
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("📂 分类服务单元测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryServiceUnitTest {

    // 📌 模拟依赖
    @Mock
    private CategoryDao categoryDao;

    // 📌 被测试的服务
    @InjectMocks
    private CategoryServiceImpl categoryService;

    // 📌 测试数据
    private Category testCategory;
    private List<Category> testCategories;
    private static long testStartTime;

    /**
     * 🏗️ 所有测试开始前的准备
     */
    @BeforeAll
    static void setUpClass() {
        testStartTime = System.currentTimeMillis();
        System.out.println("🚀 开始执行分类服务单元测试套件...");
    }

    /**
     * 🔧 每个测试方法执行前的准备
     */
    @BeforeEach
    void setUp() {
        // 准备基础测试分类
        testCategory = new Category();
        testCategory.setId("cat-001");
        testCategory.setName("技术分享");
        testCategory.setDescription("关于技术的分享和讨论");
        testCategory.setSort(1);
        testCategory.setCreateTime(LocalDateTime.now());
        testCategory.setUpdateTime(LocalDateTime.now());

        // 准备测试分类列表
        testCategories = Arrays.asList(
            createCategory("cat-001", "技术分享", "技术相关内容", 1),
            createCategory("cat-002", "生活随笔", "生活感悟和随笔", 2),
            createCategory("cat-003", "学习笔记", "学习过程中的笔记", 3),
            createCategory("cat-004", "项目经验", "项目开发经验分享", 4)
        );

        System.out.println("📋 测试数据准备完成");
    }

    /**
     * 🧪 测试1：根据ID获取分类 - 成功场景
     */
    @Test
    @Order(1)
    @DisplayName("✅ 应该能根据ID成功获取分类信息")
    void shouldGetCategoryByIdSuccessfully() {
        System.out.println("🧪 执行测试：根据ID获取分类");
        
        // 🔹 Arrange（准备阶段）
        String categoryId = "cat-001";
        when(categoryDao.selectById(categoryId)).thenReturn(testCategory);
        
        // 🔹 Act（执行阶段）
        Category result = categoryService.getById(categoryId);
        
        // 🔹 Assert（断言阶段）
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(categoryId);
        assertThat(result.getName()).isEqualTo("技术分享");
        assertThat(result.getDescription()).isEqualTo("关于技术的分享和讨论");
        assertThat(result.getSort()).isEqualTo(1);
        
        // 验证方法调用
        verify(categoryDao, times(1)).selectById(categoryId);
        verifyNoMoreInteractions(categoryDao);
        
        System.out.println("✅ 测试通过：成功获取分类信息");
    }

    /**
     * 🧪 测试2：获取不存在的分类
     */
    @Test
    @Order(2)
    @DisplayName("❌ 获取不存在的分类应该返回null")
    void shouldReturnNullWhenCategoryNotFound() {
        System.out.println("🧪 执行测试：获取不存在的分类");
        
        // Arrange
        String nonExistentId = "non-existent-category";
        when(categoryDao.selectById(nonExistentId)).thenReturn(null);
        
        // Act
        Category result = categoryService.getById(nonExistentId);
        
        // Assert
        assertThat(result).isNull();
        verify(categoryDao, times(1)).selectById(nonExistentId);
        
        System.out.println("✅ 测试通过：正确处理不存在的分类");
    }

    /**
     * 🧪 测试3：保存新分类 - 成功场景
     */
    @Test
    @Order(3)
    @DisplayName("💾 应该能成功保存新分类")
    void shouldSaveNewCategorySuccessfully() {
        System.out.println("🧪 执行测试：保存新分类");
        
        // Arrange
        Category newCategory = new Category();
        newCategory.setName("新分类");
        newCategory.setDescription("这是一个新的分类");
        newCategory.setSort(5);
        
        when(categoryDao.insert(any(Category.class))).thenReturn(1);
        when(categoryDao.selectCount(any())).thenReturn(0L); // 假设名称不重复
        
        // Act
        boolean result = categoryService.save(newCategory);
        
        // Assert
        assertThat(result).isTrue();
        
        // 验证保存的数据
        verify(categoryDao, times(1)).insert(argThat(category -> 
            category.getName().equals("新分类") &&
            category.getDescription().equals("这是一个新的分类") &&
            category.getSort().equals(5) &&
            category.getCreateTime() != null
        ));
        
        System.out.println("✅ 测试通过：成功保存新分类");
    }

    /**
     * 🧪 测试4：保存重复名称的分类 - 失败场景
     */
    @Test
    @Order(4)
    @DisplayName("❌ 保存重复名称的分类应该失败")
    void shouldFailToSaveDuplicateCategoryName() {
        System.out.println("🧪 执行测试：保存重复名称分类");
        
        // Arrange
        Category duplicateCategory = new Category();
        duplicateCategory.setName("技术分享"); // 重复的名称
        duplicateCategory.setDescription("重复的分类");
        
        when(categoryDao.selectCount(any())).thenReturn(1L); // 模拟已存在
        
        // Act & Assert
        assertThatThrownBy(() -> categoryService.save(duplicateCategory))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("分类名称已存在");
        
        // 验证没有调用insert方法
        verify(categoryDao, never()).insert(any(Category.class));
        
        System.out.println("✅ 测试通过：正确拒绝重复名称");
    }

    /**
     * 🧪 测试5：获取所有分类列表
     */
    @Test
    @Order(5)
    @DisplayName("📋 应该能获取按排序号排列的分类列表")
    void shouldGetAllCategoriesOrderedBySort() {
        System.out.println("🧪 执行测试：获取分类列表");
        
        // Arrange
        when(categoryDao.selectList(any())).thenReturn(testCategories);
        
        // Act
        List<Category> result = categoryService.getAllCategories();
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(4);
        
        // 验证排序
        assertThat(result).extracting(Category::getName)
            .containsExactly("技术分享", "生活随笔", "学习笔记", "项目经验");
        
        // 验证排序号是递增的
        for (int i = 0; i < result.size() - 1; i++) {
            assertThat(result.get(i).getSort())
                .isLessThanOrEqualTo(result.get(i + 1).getSort());
        }
        
        verify(categoryDao, times(1)).selectList(any());
        
        System.out.println("✅ 测试通过：成功获取排序的分类列表");
    }

    /**
     * 🧪 测试6：更新分类信息
     */
    @Test
    @Order(6)
    @DisplayName("🔄 应该能成功更新分类信息")
    void shouldUpdateCategorySuccessfully() {
        System.out.println("🧪 执行测试：更新分类信息");
        
        // Arrange
        Category updateCategory = new Category();
        updateCategory.setId("cat-001");
        updateCategory.setName("技术分享更新版");
        updateCategory.setDescription("更新后的技术分享描述");
        updateCategory.setSort(10);
        
        when(categoryDao.updateById(any(Category.class))).thenReturn(1);
        when(categoryDao.selectById("cat-001")).thenReturn(updateCategory);
        when(categoryDao.selectCount(any())).thenReturn(0L); // 新名称不重复
        
        // Act
        Category result = categoryService.updateCategory(updateCategory);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("技术分享更新版");
        assertThat(result.getDescription()).isEqualTo("更新后的技术分享描述");
        assertThat(result.getSort()).isEqualTo(10);
        
        verify(categoryDao, times(1)).updateById(argThat(category ->
            category.getUpdateTime() != null // 验证更新时间被设置
        ));
        
        System.out.println("✅ 测试通过：成功更新分类信息");
    }

    /**
     * 🧪 测试7：删除分类
     */
    @Test
    @Order(7)
    @DisplayName("🗑️ 应该能成功删除分类")
    void shouldDeleteCategorySuccessfully() {
        System.out.println("🧪 执行测试：删除分类");
        
        // Arrange
        String categoryId = "cat-001";
        when(categoryDao.deleteById(categoryId)).thenReturn(1);
        
        // Act
        boolean result = categoryService.deleteById(categoryId);
        
        // Assert
        assertThat(result).isTrue();
        verify(categoryDao, times(1)).deleteById(categoryId);
        
        System.out.println("✅ 测试通过：成功删除分类");
    }

    /**
     * 🧪 测试8：参数验证测试
     */
    @Test
    @Order(8)
    @DisplayName("❌ 应该正确验证输入参数")
    void shouldValidateInputParameters() {
        System.out.println("🧪 执行测试：参数验证");
        
        // 测试null ID
        assertThatThrownBy(() -> categoryService.getById(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("分类ID不能为空");
        
        // 测试空字符串ID
        assertThatThrownBy(() -> categoryService.getById(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("分类ID不能为空");
        
        // 测试null分类对象
        assertThatThrownBy(() -> categoryService.save(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("分类对象不能为空");
        
        // 测试空名称
        Category invalidCategory = new Category();
        invalidCategory.setName("");
        invalidCategory.setDescription("描述");
        
        assertThatThrownBy(() -> categoryService.save(invalidCategory))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("分类名称不能为空");
        
        System.out.println("✅ 测试通过：参数验证正确");
    }

    /**
     * 🧪 测试9：边界条件测试
     */
    @Test
    @Order(9)
    @DisplayName("📐 应该正确处理边界条件")
    void shouldHandleBoundaryConditions() {
        System.out.println("🧪 执行测试：边界条件");
        
        // 测试空列表
        when(categoryDao.selectList(any())).thenReturn(Collections.emptyList());
        List<Category> emptyResult = categoryService.getAllCategories();
        
        assertThat(emptyResult).isNotNull();
        assertThat(emptyResult).isEmpty();
        
        // 测试最大长度名称
        Category longNameCategory = new Category();
        longNameCategory.setName("A".repeat(100)); // 假设最大长度100
        longNameCategory.setDescription("描述");
        longNameCategory.setSort(1);
        
        when(categoryDao.insert(any(Category.class))).thenReturn(1);
        when(categoryDao.selectCount(any())).thenReturn(0L);
        
        boolean result = categoryService.save(longNameCategory);
        assertThat(result).isTrue();
        
        System.out.println("✅ 测试通过：边界条件处理正确");
    }

    /**
     * 🧪 测试10：并发安全测试
     */
    @Test
    @Order(10)
    @DisplayName("🔒 应该在并发环境下保持数据一致性")
    void shouldMaintainConsistencyUnderConcurrency() throws InterruptedException {
        System.out.println("🧪 执行测试：并发安全");
        
        // Arrange
        when(categoryDao.selectById(anyString())).thenReturn(testCategory);
        
        // 创建多个线程同时访问
        List<Thread> threads = new ArrayList<>();
        List<Category> results = Collections.synchronizedList(new ArrayList<>());
        
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                Category result = categoryService.getById("cat-001");
                results.add(result);
            }));
        }
        
        // Act - 启动所有线程
        threads.forEach(Thread::start);
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Assert
        assertThat(results).hasSize(10);
        assertThat(results).allMatch(Objects::nonNull);
        assertThat(results).allMatch(cat -> "技术分享".equals(cat.getName()));
        
        System.out.println("✅ 测试通过：并发安全性验证成功");
    }

    /**
     * 🔧 辅助方法：创建测试分类
     */
    private Category createCategory(String id, String name, String description, Integer sort) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        category.setSort(sort);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        return category;
    }

    /**
     * 🧹 每个测试方法执行后的清理
     */
    @AfterEach
    void tearDown() {
        // 重置Mock对象，避免测试间干扰
        reset(categoryDao);
        System.out.println("🧹 测试清理完成");
    }

    /**
     * 📊 所有测试完成后的总结
     */
    @AfterAll
    static void tearDownClass() {
        long duration = System.currentTimeMillis() - testStartTime;
        System.out.println("🎉 分类服务单元测试套件执行完成！");
        System.out.println("⏱️ 总耗时: " + duration + "ms");
        System.out.println("📋 测试覆盖了10个不同场景，包括正常、异常、边界和并发测试");
    }
} 