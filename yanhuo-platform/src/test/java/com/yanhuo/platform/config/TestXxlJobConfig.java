package com.yanhuo.platform.config;

import com.yanhuo.common.im.Message;
import com.yanhuo.common.result.Result;
import com.yanhuo.platform.client.ChatClient;
import com.yanhuo.platform.client.EsClient;
import com.yanhuo.platform.client.OssClient;
import com.yanhuo.xo.vo.NoteSearchVo;
import com.xxl.job.core.executor.XxlJobExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 测试环境XXL-Job配置 - 禁用XXL-Job
 */
@Configuration
public class TestXxlJobConfig {

    /**
     * 创建一个空的XXL-Job执行器Bean来替代真实的执行器
     * 这样可以防止XXL-Job的自动配置和初始化
     */
    @Bean
    @ConditionalOnProperty(name = "xxl.job.enabled", havingValue = "false", matchIfMissing = true)
    public XxlJobExecutor xxlJobExecutor() {
        return new XxlJobExecutor();
    }

    /**
     * 创建一个ChatClient的mock实现，用于测试环境
     */
    @Bean
    public ChatClient chatClient() {
        return new ChatClient() {
            @Override
            public Result<?> sendMsg(Message message) {
                // 测试环境下的空实现
                return Result.ok();
            }
        };
    }

    /**
     * 创建一个EsClient的mock实现，用于测试环境
     */
    @Bean
    public EsClient esClient() {
        return new EsClient() {
            @Override
            public void test() {
                // 测试环境下的空实现
            }

            @Override
            public void addNote(NoteSearchVo noteSearchVo) {
                // 测试环境下的空实现
            }

            @Override
            public void updateNote(NoteSearchVo noteSearchVo) {
                // 测试环境下的空实现
            }

            @Override
            public void addNoteBulkData(List<NoteSearchVo> noteSearchVoList) {
                // 测试环境下的空实现
            }

            @Override
            public void deleteNote(String noteId) {
                // 测试环境下的空实现
            }
        };
    }

    /**
     * 创建OssClient的模拟实现，用于测试环境
     */
    @Bean
    public OssClient ossClient() {
        return new OssClient() {
            @Override
            public Result<List<String>> saveBatch(MultipartFile[] files, Integer type) {
                // 测试环境返回空列表
                return Result.ok(List.of());
            }

            @Override
            public Result<?> deleteBatch(List<String> filePaths, Integer type) {
                return Result.ok();
            }
        };
    }
} 