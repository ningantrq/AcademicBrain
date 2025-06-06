// 用户功能端到端测试
import { test, expect } from '@playwright/test'

test.describe('用户功能端到端测试', () => {
  test.beforeEach(async ({ page }) => {
    // 访问应用首页
    await page.goto('/')
  })

  test('用户登录流程', async ({ page }) => {
    // 点击登录按钮
    await page.click('[data-testid="login-button"]')
    
    // 填写登录表单
    await page.fill('[data-testid="username-input"]', 'testuser')
    await page.fill('[data-testid="password-input"]', 'password123')
    
    // 提交登录
    await page.click('[data-testid="submit-login"]')
    
    // 验证登录成功
    await expect(page.locator('[data-testid="user-avatar"]')).toBeVisible()
  })

  test('用户搜索功能', async ({ page }) => {
    // 在搜索框中输入关键词
    await page.fill('[data-testid="search-input"]', '测试用户')
    
    // 点击搜索按钮
    await page.click('[data-testid="search-button"]')
    
    // 等待搜索结果加载
    await page.waitForSelector('[data-testid="search-results"]')
    
    // 验证搜索结果显示
    await expect(page.locator('[data-testid="search-results"]')).toBeVisible()
    await expect(page.locator('[data-testid="user-item"]')).toHaveCount(1, { timeout: 5000 })
  })

  test('用户信息编辑', async ({ page }) => {
    // 假设用户已登录，进入个人资料页面
    await page.goto('/profile')
    
    // 点击编辑按钮
    await page.click('[data-testid="edit-profile-button"]')
    
    // 修改昵称
    await page.fill('[data-testid="nickname-input"]', '新昵称')
    
    // 保存修改
    await page.click('[data-testid="save-profile-button"]')
    
    // 验证保存成功提示
    await expect(page.locator('[data-testid="success-message"]')).toBeVisible()
  })

  test('响应式设计测试', async ({ page }) => {
    // 测试移动端视图
    await page.setViewportSize({ width: 375, height: 667 })
    
    // 验证移动端导航菜单
    await expect(page.locator('[data-testid="mobile-menu-button"]')).toBeVisible()
    
    // 测试平板视图
    await page.setViewportSize({ width: 768, height: 1024 })
    
    // 验证平板端布局
    await expect(page.locator('[data-testid="tablet-layout"]')).toBeVisible()
    
    // 测试桌面端视图
    await page.setViewportSize({ width: 1920, height: 1080 })
    
    // 验证桌面端布局
    await expect(page.locator('[data-testid="desktop-layout"]')).toBeVisible()
  })

  test('页面性能测试', async ({ page }) => {
    // 开始性能监控
    const startTime = Date.now()
    
    // 访问页面
    await page.goto('/')
    
    // 等待页面完全加载
    await page.waitForLoadState('networkidle')
    
    const loadTime = Date.now() - startTime
    
    // 验证页面加载时间在合理范围内（3秒内）
    expect(loadTime).toBeLessThan(3000)
    
    // 验证关键元素已加载
    await expect(page.locator('[data-testid="main-content"]')).toBeVisible()
  })

  test('错误处理测试', async ({ page }) => {
    // 测试网络错误处理
    await page.route('**/api/user/**', route => route.abort())
    
    // 尝试加载用户数据
    await page.goto('/user/123')
    
    // 验证错误提示显示
    await expect(page.locator('[data-testid="error-message"]')).toBeVisible()
    
    // 验证重试按钮存在
    await expect(page.locator('[data-testid="retry-button"]')).toBeVisible()
  })
}) 