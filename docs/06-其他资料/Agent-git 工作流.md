## Agent Git 工作流

> 原文详细内容：`https://mp.weixin.qq.com/s/70hz6sYNwxErRkP7dkY8-Q?scene=1&click_id=1`

| 场景                                  | 推荐工具             | 理由                                                         |
| ------------------------------------- | -------------------- | ------------------------------------------------------------ |
| 已有 Git 工作流，希望改善历史整理体验 | Jujutsu              | Git 兼容，学习曲线低，`jj split` / `jj absorb` 显著降低提交整理成本 |
| 多个 agent 并发、需要管理并行工作流   | GitButler            | 虚拟分支原生支持多 agent 并发，无需 worktree 运维            |
| 团队规范严格，需要完整 CI/CD 集成     | Git + 本文最佳实践   | 生态最成熟，工具链支持最完整                                 |
| 个人实验性项目或 solo agentic 开发    | Jujutsu 或 GitButler | 两者都提供比 Git 更流畅的 agentic 工作体验                   |

---



### 一个项目同时开发多个任务

#### git

##### **使用worktree**

描述：可理解为复制当前项目（即该文件夹），但是该项目没有独立的.git文件夹，链接了原项目的.git文件夹。

操作：可以选择创建worktree的同时并创建分支，也可以选择仅创建worktree

```bash
# 第一步：为任务 A 创建 worktree,并自动创建分支
git worktree add -b feat/ISSUE-99-login ../project-feat-99
# 第二步：使用AI IDE打开该新项目(当成原项目新开了个分支工作)，修改完成后，正常合并代码。

# 语法
git worktree add [选项] <新分支名> <新工作目录路径>
```

```bash
# 第一步：任务 A 创建 worktree
git worktree add ../project-feat-99
# 第二步：使用AI IDE打开该新项目(当成原项目切换到了另一个分支工作)，修改完成后，正常合并代码。

# 语法
git worktree add <新工作目录路径> [<commit-ish>]
# [<commit-ish>]：可选参数，放在路径后面，指定基于哪个「提交 / 分支」创建（比如远程分支 origin/hotfix、本地分支、commit hash）
```

**常用的命令**

**1. 创建新的 worktree（新任务）**

```bash
git worktree add [选项] <新分支名> <新工作目录路径>
git worktree add <新工作目录路径> [<commit-ish>]
```

**2. 查看当前有哪些 worktree**

```bash
git worktree list
```

3. **删除 worktree（任务完成）**

```bash
# 语法
git worktree remove <文件夹路径>

git worktree remove ../project-feat-99
```

---



#### GitButler

还没开始使用，以下是部分文章介绍：

##### 简介

**GitButler** 是一个构建在 Git 之上的版本控制客户端，提供桌面 GUI 和命令行工具 but。

最核心的创新是**虚拟分支**：多个分支可以同时处于活跃状态，共享同一个工作目录，而不需要 worktree。



传统 Git 的工作方式是「先切分支再做事」：你必须决定要在哪个分支上工作，然后 checkout，然后修改。GitButler 的工作方式是「先做事再分类」：你直接修改文件，然后将每个 hunk（代码块）分配给对应的虚拟分支。

这对 agentic coding 的意义是：多个 agent 可以同时向同一个工作目录写入，GitButler 按 hunk 粒度将变更归类到不同分支，避免了为每个 agent 单独维护 worktree 的运维负担。

##### **与 Git 的主要差异**

| 维度       | Git                      | GitButler                             |
| ---------- | ------------------------ | ------------------------------------- |
| 并发分支   | 需要 worktree 或频繁切换 | 多个虚拟分支共享同一工作目录          |
| 变更归类   | 先切分支再修改           | 先修改再按 hunk 分配到分支            |
| 历史整理   | `git rebase -i`          | 拖拽或 `but commit`，自动 rebase 上层 |
| 操作撤销   | reflog（有限）           | 完整操作日志，`but undo`              |
| Agent 集成 | 无原生支持               | 内置 hooks 和 MCP server              |

##### **解决的 Git 局限性**

- **脏工作区难以管控：**虚拟分支让不同关注点的变更在同一工作目录中保持分离，git diff 中的噪声问题从根源上被消除
- **多 agent 并发冲突：**每个 agent 会话绑定一个虚拟分支，互斥的 hunk 自动归类，无需 worktree 隔离
- **大提交审查困难：**hunk 级别的分配机制天然产生小而聚焦的提交，stacked branches 支持按依赖关系拆分 PR

---



### 分支管理

**不是传统的按“功能/修复”来分类，而是定义了一套以 `agent/` 为前缀、完全面向任务追踪的命名体系**。

**定义了一套以 `agent/` 为根路径的、面向任务的完整工作流程和约定体系**。

这套设计的核心思路，是让每一次代码变更都能精确对应到一个具体的任务和 Agent 执行实例，从而解决传统工作流中“谁改的、为什么改”信息缺失的问题。

#### **命名约定**

**命名格式**：**`agent/<任务ID>-<简短描述>`**

```bash
agent/PROJ-234-refresh-token-rotation
agent/PROJ-301-migrate-postgres-schema
```

它包含了两个核心信息字段：

- **`agent/` 前缀**：一眼就能与人类开发者或传统分支区分开，方便统一配置分支保护规则。
- **`<任务ID>` (如 `PROJ-234`)**：这是最关键的部分，它将分支与具体的需求/缺陷单强绑定，打通了从“业务需求 → AI任务 → 代码变更”的完整追溯链路。

#### **分支生命周期**

**创建时**：必须从最新的主分支（如 `oauth-lzh`）切出。
**合并时**：必须通过拉取请求 (PR) 提交给人类审查。
**合并后**：分支应被删除，保持仓库整洁。

#### **环境隔离规范**

**一个分支一个工作区**：为每个 `agent/*` 分支创建一个独立的 `git worktree`。

---



### 提交规范

#### Commit Message 格式

遵循 **Conventional Commits** 规范，格式如下：

```
<type>(<scope>): <subject>

<body>

<trailers>  
```

格式说明

| 部分       | 必需 | 说明                                |
| :--------- | :--- | :---------------------------------- |
| `type`     | ✅    | 提交类型：和正常git流一样           |
| `scope`    | ⚪    | 影响范围：本次变更影响哪个服务/模块 |
| `subject`  | ✅    | 简短描述（50 字符以内）             |
| `body`     | ⚪    | 详细说明：背景、动机、实现细节      |
| `trailers` | ⚪    | Agent 专用元数据（见 5.2）          |

---

#### Agent 专用 Trailer

Trailer 是 Git 原生支持的结构化元数据机制，位于 Commit message 末尾，与正文之间有一个空行。格式为 `Key: Value`。

在 commit message 末尾添加 Git Trailer 格式的元数据：

| Trailer          | 必需 | 说明                  | 示例                                                         |
| :--------------- | :--- | :-------------------- | :----------------------------------------------------------- |
| `Agent-Task`     | ✅    | 原始任务描述或任务 ID | `Agent-Task: ISSUE-88 - 实现任务进度追踪和取消机制`          |
| `Agent-Model`    | ✅    | 使用的模型            | `Agent-Model: gpt-4o`                                        |
| `Agent-Decision` | ✅    | 关键设计决策及理由    | `Agent-Decision: 将安全扫描工具集成到 pre-commit 和 Makefile，实现自动化安全检查；优先使用工具链集成而非文档清单` |

##### 为什么使用 Trailer

- **Git 原生支持**：如果想要找出使用了特定模型完成的任务提交，或者是要找出完成了特定任务的提交，可通过 `git interpret-trailers`、`git log --format` 等命令解析，迅速找出目的提交

##### 常用 Trailer 查询命令

```bash
# 列出所有包含 Agent-Task trailer 的提交
git log --format='%(trailers:key=Agent-Task,valueonly)'

# 按 trailer 过滤提交历史
git log --grep="^Agent-Task:" --all

# 显示完整 trailers
git log --format="%H%n%s%n%(trailers:key=Agent-Task,key=Agent-Decision)%n"
```

##### 一次提交信息完整示例

```
chore(platform-common): 集成安全扫描工具链
在 .pre-commit-config.yaml 中添加 bandit 静态代码安全扫描
添加 pip-audit 依赖漏洞扫描 hook
在 Makefile 中新增 security-scan 目标
添加 safety>=3.0.0 到 dev 依赖
支持通过 make security-scan 运行完整安全扫描

Agent-Task: ISSUE-88 - 实现任务进度追踪和取消机制
Agent-Model: gpt-4o
Agent-Decision: 将安全扫描工具集成到 pre-commit 和 Makefile，实现自动化安全检查；优先使用工具链集成而非文档清单
```

---



### 提交策略

#### 小步提交

Checkpoint Commit 策略

![Checkpoint Commit 策略](https://mmbiz.qpic.cn/mmbiz_png/5lJ4HUd9eVOiahfRrRnumzz8HaBiaXyRQT7RZLwaprmmQfouicK0hJaZ79nTSrwXwcicCxot23ag7FQv4MEXuQUKGyicNDdsx0bbxZ9UoA1ic910g/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1#imgIndex=9)

##### 适用场景

预期执行时间超过 15 分钟的任务（即一次对话执行时间），应使用 Checkpoint Commit 保存进度。

##### 指令示例

在 Agent 的系统提示词中加入：

```
在完成以下关键节点时，执行一次 git commit：
1. 完成数据模型/接口定义
2. 完成核心逻辑实现
3. 完成测试编写
4. 完成文档更新

每个 checkpoint commit 的 message 以 [WIP] 开头
```

##### 好处

- **任务恢复**：防止长任务中断丢失进度，同时为后续审查和排错提供切分点。
- **分段 Review**：Checkpoint Commit 天然成为 Code Review 的切分点
- **问题定位**：便于 git bisect 定位问题引入的具体阶段

##### **提交特征**

- 以`[WIP]`开头标记为未完成
- 提交信息简单，只记录进度（如`[WIP] add refresh token model`）
- 不要求语义完整，甚至可能包含编译不通过的代码（仅用于保存现场）

##### 示例

```
[WIP] feat(auth): add refresh token model
[WIP] feat(auth): implement refresh token issuance
[WIP] test(auth): add refresh token tests
[WIP] docs(auth): update API docs
```

#### 整理提交

**使用 Interactive Rebase 整理WIP提交**

![使用 Interactive Rebase 整理 Agent 历史](https://mmbiz.qpic.cn/mmbiz_png/5lJ4HUd9eVPwgLsaNB8Bmr7RTPichvAliaa2eqBT2juWV2B2rIIX4SOjSFDq2xHePXnIjoV7SQI5TogxMpCs8T7OnyxYicPnHm9g2MoXTE05hs/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1#imgIndex=10)

Agent 工作完成后，对刚刚对话产生的WIP提交进行整理

##### **整理策略**

- 将 [WIP] checkpoint commits 合并（squash）为有意义的语义 commit
- 确保最终历史中每个 commit 都能独立理解和回滚

**让 Agent 辅助完成历史整理**

整理提交历史这件事本身也可以交给 agent 来做，以下是一个简洁的 prompt，可以在任务完成后直接发给 agent：

##### 示例

```
当前任务已经完成，请执行 WIP 提交整理，准备进入代码评审。具体操作如下：

1.  查看提交历史：首先运行 `git log --oneline main..HEAD` 命令，列出从主分支分叉后的所有提交。
    本次任务的提交通常带有 `[WIP]` 前缀或连续的临时提交信息。

2.  提出整理方案：分析当前的提交列表，为我规划一份整理方案。方案需要明确以下内容：
    需要合并（squash）的提交：将同一逻辑变更下的多个 `[WIP]` 提交合并成一个有意义的语义提交。
    需要保留（pick）的提交：已经是逻辑独立、信息清晰的提交。
    需要修改信息（reword）的提交：内容独立但描述不清晰的提交。
    提交信息（commit message）：为所有保留或合并后的提交，严格按照 Conventional Commits 格式编写清晰的信息，并在正文中补充 `Agent-Task` 等必要说明。

3.  方案确认：将这份整理方案先展示给我确认，不要直接执行。

4.  执行历史重写：在我确认方案后，使用 `git rebase -i main` 执行交互式变基，完成提交历史的整理。

5.  展示最终结果：整理成功后，再次运行 `git log --oneline main..HEAD` 命令，向我展示修改后的、最终要推送到远程的提交历史。

要求：最终的提交历史应实现“每个提交只做一件事”（Atomic Commit），清晰、独立且便于 reviewer 理解和回滚
```

#### 原子提交

![原子提交](https://mmbiz.qpic.cn/sz_mmbiz_png/5lJ4HUd9eVNAXldTeYTfYRVYsMF0MfbDd5gcEUy2icShW4yxnrTpoK7qYy1jib3PTQibjoGhRmCU4gsY9iaZgRK1cPJ0MibgibezyRDwleXyL8GuI/640?wx_fmt=png&from=appmsg&tp=webp&wxfrom=5&wx_lazy=1#imgIndex=11)



##### 定义

一个 commit 只表达一个可解释、可回滚、可验证的语义变化，且在该 commit 节点上代码可以编译、测试可以通过。

##### 解决问题

Agent 执行长任务时，往往将多个不相关的修改混入同一次提交，atomic commit 是对抗这种熵增的直接手段

##### 系统提示词引导

```
When implementing a feature, break your work into atomic commits:
- Each commit must represent exactly one logical change
- Each commit must leave the codebase in a buildable, testable state
- Do not mix refactoring with feature changes in the same commit
- Do not mix changes to multiple unrelated modules in the same commit
```

##### 使用场景

这是在整理WIP提交时需要遵循的规则

---

