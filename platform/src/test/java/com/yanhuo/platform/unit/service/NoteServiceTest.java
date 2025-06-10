package com.yanhuo.platform.unit.service;

import com.yanhuo.platform.service.impl.NoteServiceImpl;
import com.yanhuo.xo.dao.NoteDao;
import com.yanhuo.xo.entity.Note;
import com.yanhuo.xo.entity.User;
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
 * 📝 笔记服务单元测试示例
 * 
 * 这个测试类展示了完整的单元测试最佳实践：
 * ✅ 使用@ExtendWith(MockitoExtension.class) 而不是Spring上下文
 * ✅ 使用@Mock 模拟依赖
 * ✅ 使用@InjectMocks 注入被测试对象
 * ✅ 清晰的测试命名和文档
 * ✅ AAA模式（Arrange-Act-Assert）
 * ✅ 边界条件测试
 * ✅ 异常场景测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("📝 笔记服务单元测试")
class NoteServiceTest {

    // 📌 模拟依赖对象
    @Mock
    private NoteDao noteDao;

    // 📌 被测试的服务对象（会自动注入Mock依赖）
    @InjectMocks
    private NoteServiceImpl noteService;

    // 📌 测试数据
    private Note testNote;
    private User testUser;
    private List<Note> testNotes;

    /**
     * 🔧 每个测试方法执行前的准备工作
     */
    @BeforeEach
    void setUp() {
        // 准备测试用户
        testUser = new User();
        testUser.setId("user-123");
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        
        // 准备测试笔记
        testNote = new Note();
        testNote.setId("note-123");
        testNote.setTitle("测试笔记标题");
        testNote.setContent("这是一个测试笔记的内容");
        testNote.setUserId("user-123");
        testNote.setCreateTime(LocalDateTime.now());
        testNote.setUpdateTime(LocalDateTime.now());
        
        // 准备测试笔记列表
        testNotes = Arrays.asList(
            createNote("note-1", "笔记1", "内容1"),
            createNote("note-2", "笔记2", "内容2"),
            createNote("note-3", "笔记3", "内容3")
        );
    }

    /**
     * 🧪 测试：根据ID获取笔记 - 正常情况
     */
    @Test
    @DisplayName("✅ 应该能根据ID成功获取笔记")
    void shouldGetNoteByIdSuccessfully() {
        // 🔹 Arrange（准备阶段）
        String noteId = "note-123";
        when(noteDao.selectById(noteId)).thenReturn(testNote);
        
        // 🔹 Act（执行阶段）
        Note result = noteService.getById(noteId);
        
        // 🔹 Assert（断言阶段）
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(noteId);
        assertThat(result.getTitle()).isEqualTo("测试笔记标题");
        assertThat(result.getContent()).isEqualTo("这是一个测试笔记的内容");
        
        // 验证方法调用
        verify(noteDao, times(1)).selectById(noteId);
        verifyNoMoreInteractions(noteDao);
    }

    /**
     * 🧪 测试：根据ID获取笔记 - 笔记不存在
     */
    @Test
    @DisplayName("❌ 获取不存在的笔记应该返回null")
    void shouldReturnNullWhenNoteNotFound() {
        // Arrange
        String nonExistentId = "non-existent";
        when(noteDao.selectById(nonExistentId)).thenReturn(null);
        
        // Act
        Note result = noteService.getById(nonExistentId);
        
        // Assert
        assertThat(result).isNull();
        verify(noteDao, times(1)).selectById(nonExistentId);
    }

    /**
     * 🧪 测试：保存笔记 - 正常情况
     */
    @Test
    @DisplayName("✅ 应该能成功保存新笔记")
    void shouldSaveNoteSuccessfully() {
        // Arrange
        Note newNote = new Note();
        newNote.setTitle("新笔记");
        newNote.setContent("新笔记内容");
        newNote.setUserId("user-123");
        
        when(noteDao.insert(any(Note.class))).thenReturn(1);
        
        // Act
        boolean result = noteService.save(newNote);
        
        // Assert
        assertThat(result).isTrue();
        verify(noteDao, times(1)).insert(argThat(note -> 
            note.getTitle().equals("新笔记") &&
            note.getContent().equals("新笔记内容") &&
            note.getUserId().equals("user-123")
        ));
    }

    /**
     * 🧪 测试：保存笔记 - 数据验证失败
     */
    @Test
    @DisplayName("❌ 保存空标题笔记应该失败")
    void shouldFailToSaveNoteWithEmptyTitle() {
        // Arrange
        Note invalidNote = new Note();
        invalidNote.setTitle(""); // 空标题
        invalidNote.setContent("有内容");
        invalidNote.setUserId("user-123");
        
        // Act & Assert
        assertThatThrownBy(() -> noteService.save(invalidNote))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("标题不能为空");
        
        // 验证没有调用DAO
        verify(noteDao, never()).insert(any(Note.class));
    }

    /**
     * 🧪 测试：根据用户ID获取笔记列表
     */
    @Test
    @DisplayName("📋 应该能根据用户ID获取笔记列表")
    void shouldGetNotesByUserId() {
        // Arrange
        String userId = "user-123";
        when(noteDao.selectList(any())).thenReturn(testNotes);
        
        // Act
        List<Note> result = noteService.getNotesByUserId(userId);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).extracting(Note::getTitle)
            .containsExactly("笔记1", "笔记2", "笔记3");
        
        verify(noteDao, times(1)).selectList(any());
    }

    /**
     * 🧪 测试：更新笔记
     */
    @Test
    @DisplayName("🔄 应该能成功更新笔记")
    void shouldUpdateNoteSuccessfully() {
        // Arrange
        Note updateNote = new Note();
        updateNote.setId("note-123");
        updateNote.setTitle("更新后的标题");
        updateNote.setContent("更新后的内容");
        
        when(noteDao.updateById(any(Note.class))).thenReturn(1);
        when(noteDao.selectById("note-123")).thenReturn(updateNote);
        
        // Act
        Note result = noteService.updateNote(updateNote);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("更新后的标题");
        assertThat(result.getContent()).isEqualTo("更新后的内容");
        
        verify(noteDao, times(1)).updateById(updateNote);
        verify(noteDao, times(1)).selectById("note-123");
    }

    /**
     * 🧪 测试：删除笔记
     */
    @Test
    @DisplayName("🗑️ 应该能成功删除笔记")
    void shouldDeleteNoteSuccessfully() {
        // Arrange
        String noteId = "note-123";
        when(noteDao.deleteById(noteId)).thenReturn(1);
        
        // Act
        boolean result = noteService.deleteById(noteId);
        
        // Assert
        assertThat(result).isTrue();
        verify(noteDao, times(1)).deleteById(noteId);
    }

    /**
     * 🧪 测试：搜索笔记
     */
    @Test
    @DisplayName("🔍 应该能根据关键字搜索笔记")
    void shouldSearchNotesByKeyword() {
        // Arrange
        String keyword = "测试";
        List<Note> searchResults = Arrays.asList(testNote);
        when(noteDao.selectList(any())).thenReturn(searchResults);
        
        // Act
        List<Note> result = noteService.searchNotes(keyword);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains("测试");
        
        verify(noteDao, times(1)).selectList(any());
    }

    /**
     * 🧪 测试：批量删除笔记
     */
    @Test
    @DisplayName("🗑️ 应该能批量删除笔记")
    void shouldDeleteNotesBatch() {
        // Arrange
        List<String> noteIds = Arrays.asList("note-1", "note-2", "note-3");
        when(noteDao.deleteBatchIds(noteIds)).thenReturn(3);
        
        // Act
        boolean result = noteService.deleteBatch(noteIds);
        
        // Assert
        assertThat(result).isTrue();
        verify(noteDao, times(1)).deleteBatchIds(noteIds);
    }

    /**
     * 🧪 测试：参数验证 - 空参数
     */
    @Test
    @DisplayName("❌ 传入null参数应该抛出异常")
    void shouldThrowExceptionWhenNullParameter() {
        // Act & Assert
        assertThatThrownBy(() -> noteService.getById(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("笔记ID不能为空");
        
        assertThatThrownBy(() -> noteService.save(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("笔记对象不能为空");
    }

    /**
     * 🧪 测试：边界条件 - 空列表
     */
    @Test
    @DisplayName("📋 用户没有笔记时应该返回空列表")
    void shouldReturnEmptyListWhenUserHasNoNotes() {
        // Arrange
        String userId = "user-with-no-notes";
        when(noteDao.selectList(any())).thenReturn(Collections.emptyList());
        
        // Act
        List<Note> result = noteService.getNotesByUserId(userId);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        
        verify(noteDao, times(1)).selectList(any());
    }

    /**
     * 🔧 辅助方法：创建测试笔记
     */
    private Note createNote(String id, String title, String content) {
        Note note = new Note();
        note.setId(id);
        note.setTitle(title);
        note.setContent(content);
        note.setUserId("user-123");
        note.setCreateTime(LocalDateTime.now());
        note.setUpdateTime(LocalDateTime.now());
        return note;
    }

    /**
     * 🧹 每个测试方法执行后的清理工作
     */
    @AfterEach
    void tearDown() {
        // 重置Mock对象
        reset(noteDao);
    }
} 