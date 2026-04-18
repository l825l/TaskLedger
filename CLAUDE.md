# TaskLedger 项目

任务账本 Android 应用

## 技术栈

- **语言**: Kotlin
- **UI**: Jetpack Compose
- **数据库**: Room
- **架构**: MVVM + Repository Pattern

## Gitee 仓库

- **仓库地址**: https://gitee.com/l825l/task-ledger
- **Git 远程地址**: https://gitee.com/l825l/task-ledger.git

## Git 工作流

### 分支策略

使用 GitHub Flow：
- `master` 为主分支，始终保持可部署状态
- 功能开发从 `master` 创建 feature 分支
- 完成后提交 Pull Request 合并回 `master`

### 提交规范

```
<type>(<scope>): <subject>

类型：feat, fix, docs, style, refactor, test, chore, perf
示例：feat(backup): 添加自动备份功能
```

### 常用命令

```bash
# 推送到 Gitee
git push origin master

# 创建功能分支
git checkout -b feature/功能名称

# 查看状态
git status
git log --oneline -10
```

## 项目功能

- 任务管理（创建、编辑、删除、完成）
- 分类管理
- 优先级任务
- 今日任务视图
- 数据备份与恢复
- 生物识别认证
- 自动备份功能

## 构建与运行

```bash
# 构建
./gradlew assembleDebug

# 安装到设备
adb install app/build/outputs/apk/debug/app-debug.apk
```
