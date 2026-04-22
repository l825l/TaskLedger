# 项目说明

## Git 工作流规则

**重要：本地仓库优先原则**

- 本地仓库的代码是最新的
- 远程仓库不是最新的
- **除非主动要求，否则拒绝远程仓库覆盖本地仓库**
- 合并远程分支时，必须保留本地分支的所有修改

### 合并策略

当需要合并远程分支时：
1. 优先使用 `git merge` 并保留本地修改
2. 如果存在冲突，手动解决并保留本地版本
3. 避免 `git reset --hard origin/master` 等覆盖本地仓库的操作

### 历史教训

**2026-04-22 事件**：`40e6266` 合并提交错误地用远程分支覆盖了本地分支的前置依赖功能 UI 代码，导致功能退化。

丢失的功能：
- 阻塞提示 UI
- 状态变更验证调用
- 依赖验证错误对话框
- 阻塞提示对话框

修复提交：通过从历史提交 `6feb5fe` 恢复代码。

---

## 版本号更迭规则

### 版本号格式

遵循语义化版本规范：`MAJOR.MINOR.PATCH`

- **MAJOR（主版本号）**：不兼容的 API 修改
- **MINOR（次版本号）**：向下兼容的功能性新增
- **PATCH（修订号）**：向下兼容的问题修复

### 更迭规则

| 变更类型 | 版本号变更 | 示例 |
|---------|-----------|------|
| 重大架构调整、不兼容修改 | MAJOR + 1 | 1.2.0 → 2.0.0 |
| 新增功能、功能改进 | MINOR + 1 | 1.2.0 → 1.3.0 |
| Bug 修复、小优化 | PATCH + 1 | 1.2.0 → 1.2.1 |

### 版本号修改位置

1. `app/build.gradle.kts` - `versionCode` 和 `versionName`
2. `app/src/main/res/values/strings.xml` - `app_version`（如有）

### 发布流程

1. 更新版本号
2. 提交代码到本地仓库
3. 推送到 Gitee 和 GitHub
4. 创建 Release 并上传 APK

### APK 命名规范

APK 文件命名格式：`TaskLedger-v{版本号}.apk`

示例：
- `TaskLedger-v1.2.1.apk`
- `TaskLedger-v1.2.2.apk`

### APK 发布平台

- **GitHub**: https://github.com/l825l/TaskLedger/releases
- **Gitee**: https://gitee.com/l825l/task-ledger/releases

---

## 项目结构

```
app/src/main/java/com/ledger/task/
├── data/           # 数据层（数据库、仓库实现）
├── domain/         # 领域层（模型、仓库接口、验证器）
├── ui/             # UI 层（屏幕、组件、主题、导航）
├── viewmodel/      # ViewModel 层
├── notification/   # 通知管理
├── backup/         # 备份恢复
└── update/         # 版本更新
```

## 技术栈

- Kotlin + Jetpack Compose
- Room 数据库
- Hilt 依赖注入
- Material 3 设计
