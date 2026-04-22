# TaskLedger 项目

任务账本 Android 应用

## 技术栈

- **语言**: Kotlin
- **UI**: Jetpack Compose
- **数据库**: Room
- **架构**: MVVM + Repository Pattern

## 远程仓库

### Gitee

- **仓库地址**: https://gitee.com/l825l/task-ledger
- **Git 远程地址**: https://gitee.com/l825l/task-ledger.git
- **远程名称**: `origin`
- **令牌路径**: `~/.config/gitee/token`

#### 创建 Release（避免中文乱码）

Gitee API 使用 `-F` 参数传递中文会导致乱码，需使用 JSON 文件方式：

```bash
GITEE_TOKEN=$(cat ~/.config/gitee/token)

# 1. 创建 JSON 数据文件（UTF-8 编码）
cat > /tmp/release_data.json << 'JSONEOF'
{
  "tag_name": "v1.2.0",
  "name": "v1.2.0",
  "body": "## 版本 1.2.0 - 更新说明\n\n### 修复问题\n- 修复问题描述",
  "target_commitish": "master"
}
JSONEOF

# 2. 使用 JSON 文件创建 Release
curl -s -X POST "https://gitee.com/api/v5/repos/l825l/task-ledger/releases?access_token=$GITEE_TOKEN" \
  -H "Content-Type: application/json; charset=utf-8" \
  --data-binary @/tmp/release_data.json

# 3. 上传 APK 到 Release
curl -X POST "https://gitee.com/api/v5/repos/l825l/task-ledger/releases/<RELEASE_ID>/attach_files" \
  -H "Content-Type: multipart/form-data" \
  -F "access_token=$GITEE_TOKEN" \
  -F "file=@app/build/outputs/apk/release/TaskLedger-v1.2.0.apk"
```

**注意**：`-F` 参数仅用于上传文件，中文描述必须通过 JSON 文件传递。

### GitHub

- **仓库地址**: https://github.com/l825l/TaskLedger
- **Git 远程地址**: git@github.com:l825l/TaskLedger.git
- **远程名称**: `github`

## Git 工作流

### 分支策略

使用 GitHub Flow：
- `master` 为主分支，始终保持可部署状态
- 功能开发从 `master` 创建 feature 分支
- 完成并且询问同意后提交 Pull Request 合并回 `master`

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

# 推送到 GitHub
git push github master

# 推送到所有远程仓库
git push origin master && git push github master

# 创建功能分支
git checkout -b feature/功能名称

# 查看状态
git status
git log --oneline -10

# 添加文件到暂存区
git add <文件路径>

# 提交更改
git commit -m "type: 描述"

# 查看修改统计
git diff --stat
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

- **Android SDK 路径**: `D:\Andriod\SDK`

```bash
# 构建 debug 版本
./gradlew assembleDebug

# 构建 release 版本
./gradlew assembleRelease

# 安装到设备
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 发布规范

### 版本号规则（语义化版本）

遵循 [Semantic Versioning 2.0.0](https://semver.org/lang/zh-CN/) 规范：

**版本格式**: `MAJOR.MINOR.PATCH`（如 `1.2.1`）

| 版本类型 | 递增条件 | 示例 |
|----------|----------|------|
| **MAJOR**（主版本） | 不兼容的 API 变更 | 1.x.x → 2.0.0 |
| **MINOR**（次版本） | 向后兼容的功能新增 | 1.2.x → 1.3.0 |
| **PATCH**（修订版本） | 向后兼容的问题修复 | 1.2.0 → 1.2.1 |

**判断规则**：

1. **PATCH**: Bug 修复、安全修复、代码优化、文档更新
2. **MINOR**: 新功能、新特性、性能改进（不破坏现有功能）
3. **MAJOR**: 架构重构、API 变更、删除功能、重大改动

**版本号更新步骤**：

1. 修改 `app/build.gradle.kts` 中的 `versionCode` 和 `versionName`
2. `versionCode` 每次发布递增 1
3. `versionName` 根据上述规则更新

```kotlin
// build.gradle.kts
versionCode = 4      // 每次发布 +1
versionName = "1.2.1" // 根据语义化版本规则更新
```

### APK 命名规范

每个 release 版本必须生成对应的标签 APK 文件，命名格式：`TaskLedger-v<版本号>.apk`

```bash
# 示例
TaskLedger-v1.1.0.apk
TaskLedger-v1.2.0.apk

# 构建后复制并重命名
cp app/build/outputs/apk/release/app-release.apk app/build/outputs/apk/release/TaskLedger-v1.2.0.apk
```

### Release APK 上传规范

**重要**：上传到 Gitee 和 GitHub 的 APK 必须为重命名后的 release 版本（`TaskLedger-v<版本号>.apk`），禁止上传其他版本的 APK（如 `app-debug.apk`、`app-release.apk`）。

### 发布流程

1. 更新 `app/build.gradle.kts` 中的版本号
2. 构建 release APK：`./gradlew assembleRelease`
3. 重命名 APK：`cp app/build/outputs/apk/release/app-release.apk app/build/outputs/apk/release/TaskLedger-v<版本号>.apk`
4. 提交版本更新并推送代码
5. 创建 Gitee 和 GitHub Release
6. 上传 APK 到 Release

## 代码审查记录

### 2026-04-19 代码审查修复

**已修复问题：**

| 问题 | 级别 | 修复方案 |
|------|------|----------|
| 密码最小长度过短 | LOW | 从 6 位增加到 8 位 |
| TaskEditViewModel 缓存未清理 | MEDIUM | save() 成功后调用 clearAvailableTasksCache() |
| Repository update/delete 缺少返回值 | HIGH | 返回 Result<Unit> |
| PriorityTasksViewModel Flow 收集低效 | HIGH | 使用 stateIn 直接转换 |
| TodayTasksViewModel Flow 收集低效 | HIGH | 使用 stateIn 直接转换 |
| TodayTasksScreen O(n²) 复杂度 | MEDIUM | 使用 itemsIndexed + 预计算优先级范围 |
| TodayTasksScreen.kt 文件过大 | MEDIUM | 提取 SwipeableTaskRow 到独立文件 |
| LedgerCenterViewModel.kt 文件过大 | HIGH | 拆分为 5 个职责单一的类 |

**提交记录：**
```
140f95e refactor(viewmodel): 拆分 LedgerCenterViewModel
1d9611a refactor: 代码审查问题修复
```

### 2026-04-20 关联事项区域修复

**已修复问题：**

| 问题 | 级别 | 修复方案 |
|------|------|----------|
| 移除关联任务无确认 | MEDIUM | 添加删除确认对话框，显示任务名称并需用户确认 |
| 相关任务显示重复 | MEDIUM | 添加 `filteredRelatedTasks` 过滤逻辑，排除已在前置依赖中的任务 |
| 关联任务操作繁琐 | LOW | 实现左滑快速完成/撤销功能，滑动超过阈值触发操作 |
| 关联任务卡片样式单一 | LOW | 使用优先级背景色 (`priority.bgColor`) 区分任务优先级 |
| 已完成任务显示删除线 | LOW | 移除删除线样式，保持任务标题清晰可读 |
| 依赖验证缺失 | HIGH | 新增 `TaskDependencyValidator` 检测自引用和循环依赖 |
| 阻塞状态无提示 | HIGH | 新增 `DependencyState` 密封类，显示阻塞提示和阻塞任务列表 |

**新增领域模型：**

| 文件 | 职责 |
|------|------|
| `DependencyState.kt` | 依赖状态密封接口（NoDependencies/Unblocked/Blocked） |
| `TaskDependencyValidator.kt` | 使用 DFS 检测循环依赖，验证前置任务合法性 |

**提交记录：**
```
6feb5fe feat(dependency): 添加任务依赖验证和阻塞状态功能
```

## 架构说明

### ViewModel 拆分

LedgerCenterViewModel 按职责拆分为以下模块：

| 文件 | 行数 | 职责 |
|------|------|------|
| LedgerDataLoader.kt | 174 | 数据加载和筛选 |
| LedgerExportManager.kt | 511 | 导出、分享和导出历史管理 |
| LedgerBackupCoordinator.kt | 557 | 备份/恢复协调、生物识别、密码管理 |
| AutoBackupSettingsManager.kt | 157 | 自动备份设置管理 |
| LedgerCenterViewModel.kt | 392 | 主 ViewModel（组合协调各模块） |

设计要点：
- **单一状态源**: LedgerCenterUiState 保持在主 ViewModel 中
- **组合模式**: 各模块通过回调更新主状态
- **公共 API 不变**: UI 层无需修改
