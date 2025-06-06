#!/bin/bash

# 测试运行脚本
# 提供统一的测试执行入口

set -e

echo "🚀 开始执行测试套件..."

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 函数：打印带颜色的消息
print_message() {
    local color=$1
    local message=$2
    echo -e "${color}${message}${NC}"
}

# 函数：运行后端测试
run_backend_tests() {
    print_message $BLUE "📋 运行后端测试..."
    
    # 单元测试
    print_message $YELLOW "🔧 运行单元测试..."
    mvn test -Dtest="**/*Test.java" || {
        print_message $RED "❌ 单元测试失败"
        exit 1
    }
    
    # 集成测试
    print_message $YELLOW "🔗 运行集成测试..."
    mvn verify -Dtest="**/*IntegrationTest.java" || {
        print_message $RED "❌ 集成测试失败"
        exit 1
    }
    
    # 验收测试
    print_message $YELLOW "✅ 运行验收测试..."
    mvn verify -Dtest="**/*AcceptanceTest.java" || {
        print_message $RED "❌ 验收测试失败"
        exit 1
    }
    
    print_message $GREEN "✅ 后端测试完成"
}

# 函数：运行性能测试
run_performance_tests() {
    print_message $BLUE "⚡ 运行性能测试..."
    
    # 启动应用（如果未运行）
    if ! curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
        print_message $YELLOW "🚀 启动应用服务器..."
        mvn spring-boot:run > /dev/null 2>&1 &
        APP_PID=$!
        
        # 等待应用启动
        for i in {1..30}; do
            if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
                break
            fi
            sleep 2
        done
    fi
    
    # 运行性能测试
    mvn test -Dtest="**/*PerformanceTest.java" || {
        print_message $RED "❌ 性能测试失败"
        if [ ! -z "$APP_PID" ]; then
            kill $APP_PID
        fi
        exit 1
    }
    
    # 清理
    if [ ! -z "$APP_PID" ]; then
        kill $APP_PID
    fi
    
    print_message $GREEN "✅ 性能测试完成"
}

# 函数：运行前端测试
run_frontend_tests() {
    print_message $BLUE "🎨 运行前端测试..."
    
    cd yanhuo-web
    
    # 安装依赖（如果需要）
    if [ ! -d "node_modules" ]; then
        print_message $YELLOW "📦 安装前端依赖..."
        npm install
    fi
    
    # 单元测试
    print_message $YELLOW "🔧 运行前端单元测试..."
    npm run test:unit || {
        print_message $RED "❌ 前端单元测试失败"
        cd ..
        exit 1
    }
    
    # 端到端测试
    print_message $YELLOW "🌐 运行端到端测试..."
    npm run test:e2e || {
        print_message $RED "❌ 端到端测试失败"
        cd ..
        exit 1
    }
    
    cd ..
    print_message $GREEN "✅ 前端测试完成"
}

# 函数：生成测试报告
generate_reports() {
    print_message $BLUE "📊 生成测试报告..."
    
    # 后端测试覆盖率报告
    mvn jacoco:report
    
    # 前端测试覆盖率报告
    cd yanhuo-web
    npm run test:coverage
    cd ..
    
    print_message $GREEN "✅ 测试报告生成完成"
    print_message $YELLOW "📁 后端覆盖率报告: target/site/jacoco/index.html"
    print_message $YELLOW "📁 前端覆盖率报告: yanhuo-web/coverage/index.html"
}

# 主函数
main() {
    case "${1:-all}" in
        "unit")
            print_message $BLUE "🔧 仅运行单元测试..."
            mvn test -Dtest="**/*Test.java"
            cd yanhuo-web && npm run test:unit && cd ..
            ;;
        "integration")
            print_message $BLUE "🔗 仅运行集成测试..."
            mvn verify -Dtest="**/*IntegrationTest.java"
            ;;
        "performance")
            print_message $BLUE "⚡ 仅运行性能测试..."
            run_performance_tests
            ;;
        "acceptance")
            print_message $BLUE "✅ 仅运行验收测试..."
            mvn verify -Dtest="**/*AcceptanceTest.java"
            ;;
        "frontend")
            print_message $BLUE "🎨 仅运行前端测试..."
            run_frontend_tests
            ;;
        "backend")
            print_message $BLUE "🖥️ 仅运行后端测试..."
            run_backend_tests
            ;;
        "all")
            print_message $BLUE "🚀 运行所有测试..."
            run_backend_tests
            run_frontend_tests
            run_performance_tests
            generate_reports
            ;;
        "help"|"-h"|"--help")
            echo "使用方法: $0 [选项]"
            echo ""
            echo "选项:"
            echo "  all          运行所有测试 (默认)"
            echo "  unit         仅运行单元测试"
            echo "  integration  仅运行集成测试"
            echo "  performance  仅运行性能测试"
            echo "  acceptance   仅运行验收测试"
            echo "  frontend     仅运行前端测试"
            echo "  backend      仅运行后端测试"
            echo "  help         显示此帮助信息"
            exit 0
            ;;
        *)
            print_message $RED "❌ 未知选项: $1"
            print_message $YELLOW "使用 '$0 help' 查看可用选项"
            exit 1
            ;;
    esac
    
    print_message $GREEN "🎉 测试执行完成!"
}

# 执行主函数
main "$@" 