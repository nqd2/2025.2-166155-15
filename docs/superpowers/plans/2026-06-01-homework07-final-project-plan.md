# Homework07 Final Project Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement Homework07 final project as one group Maven application using JDK 22, with all six members coding from `develop` through personal feature/fix branches.

**Architecture:** `develop` is the integration branch for Homework07. All member branches start from `develop` and merge back through pull requests. Design and analysis are treated as already finished; this plan is for implementation, integration, testing, packaging, and final submission.

**Tech Stack:** Java 22, Maven, JUnit 5 if tests are added, Git branch flow with `develop`, pull requests, and member branches under `hw07/<MSSV>-<HoTenKhongDau>/<type>/<short-topic>`.

---

## File Structure

- Create or modify: `Homework07/Group/pom.xml`
  - Maven project configuration.
  - Java release set to `22`.
- Create or modify: `Homework07/Group/src/main/java/`
  - Production code for final project.
- Create or modify: `Homework07/Group/src/test/java/`
  - Unit tests and integration tests.
- Create or modify: `Homework07/Group/README.md`
  - Build/run instructions, member assignment, feature list.
- Create or modify: `Homework07/Group/docs/`
  - Final project docs, diagrams, screenshots, exported report material.
- Modify: `AGENTS.md`
  - Homework07 branch convention and Maven/JDK 22 rules.

## Phase Summary

- Phase 1: bootstrap Maven/JDK 22 project and code architecture baseline.
- Phase 2: each member implements assigned use case on personal branch from `develop`.
- Phase 3: integrate modules into end-to-end business workflow on `develop`.
- Phase 4: stabilize, package, document, and merge `develop` to `main`.

## Branch Convention

- Integration branch: `develop`.
- Final merge target: `main`.
- Member branch pattern: `hw07/<MSSV>-<HoTenKhongDau>/<type>/<short-topic>`.
- Allowed `type`: `feature`, `fix`, `hotfix`, `refactor`, `test`, `docs`, `chore`.
- Slug rules:
  - ASCII only.
  - No Vietnamese accents.
  - Lowercase for `<type>` and `<short-topic>`.
  - Use `-` inside `<short-topic>`.
  - Keep branch scoped to one feature, fix, or task.

Concrete member branches:
```bash
git switch develop
git pull --ff-only origin develop
git switch -c hw07/20235798-NguyenHuuNhan/feature/receive-reconcile-goods
git switch -c hw07/20235658-VuDucHoangAnh/feature/manage-import-requests
git switch -c hw07/20235658-VuDucHoangAnh/feature/manage-system
git switch -c hw07/20235821-TranDangSinh/feature/collect-inventory-data
git switch -c hw07/20235880-PhanKhanhVu/feature/plan-import-allocation
git switch -c hw07/20235754-LuongQuocKhanh/feature/place-overseas-orders
git switch -c hw07/20235682-NguyenQuyDuc/feature/manage-site-information
```

Use only one `git switch -c ...` command per member branch.

## Phase 1 Plan: Codebase Bootstrap and Architecture Baseline

### Task 1: Create `develop`, Maven Skeleton, and Shared Code Layout

**Files:**
- Create: `Homework07/Group/pom.xml`
- Create: `Homework07/Group/src/main/java/`
- Create: `Homework07/Group/src/test/java/`
- Create: `Homework07/Group/README.md`
- Create: base packages under `Homework07/Group/src/main/java/vn/edu/hust/itss/group15/`

- [ ] **Step 1: Create or update `develop`**

Run:
```bash
git switch main
git pull --ff-only origin main
git switch -c develop
git push -u origin develop
```
Expected: `develop` exists locally and on remote. If remote `develop` already exists, use:
```bash
git fetch origin
git switch develop
git pull --ff-only origin develop
```

- [ ] **Step 2: Create Maven directories**

Run:
```bash
mkdir -p Homework07/Group/src/main/java
mkdir -p Homework07/Group/src/test/java
mkdir -p Homework07/Group/docs
```
Expected: standard Maven folders exist under `Homework07/Group/`.

- [ ] **Step 3: Create base Java packages**

Run:
```bash
mkdir -p Homework07/Group/src/main/java/vn/edu/hust/itss/group15/domain
mkdir -p Homework07/Group/src/main/java/vn/edu/hust/itss/group15/service
mkdir -p Homework07/Group/src/main/java/vn/edu/hust/itss/group15/repository
mkdir -p Homework07/Group/src/main/java/vn/edu/hust/itss/group15/app
mkdir -p Homework07/Group/src/test/java/vn/edu/hust/itss/group15
```
Expected: shared package structure exists before members add use-case code.

- [ ] **Step 4: Create Maven `pom.xml`**

Create `Homework07/Group/pom.xml` with:
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>vn.edu.hust.itss.group15</groupId>
  <artifactId>import-ordering-system</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <name>Import Ordering System</name>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.release>22</maven.compiler.release>
    <junit.jupiter.version>5.10.3</junit.jupiter.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>${junit.jupiter.version}</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.13.0</version>
        <configuration>
          <release>${maven.compiler.release}</release>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.2.5</version>
      </plugin>
    </plugins>
  </build>
</project>
```

- [ ] **Step 5: Create README build instructions**

Create `Homework07/Group/README.md` with:
~~~markdown
# Homework07 - Import Ordering System

## Requirements

- JDK 22
- Maven 3.9+

## Build

```bash
mvn -version
mvn clean test
mvn clean package
```

## Branch Flow

- Base branch: `develop`
- Member branch: `hw07/<MSSV>-<HoTenKhongDau>/<type>/<short-topic>`
- Pull request target: `develop`
- Final merge: `develop` -> `main`
~~~

- [ ] **Step 6: Verify Java and Maven**

Run:
```bash
cd Homework07/Group
mvn -version
mvn clean test
```
Expected: Maven reports Java version 22 and build succeeds.

- [ ] **Step 7: Commit bootstrap**

Run:
```bash
git add Homework07/Group/pom.xml Homework07/Group/README.md Homework07/Group/src Homework07/Group/docs AGENTS.md
git commit -m "chore: bootstrap Homework07 Maven project"
git push origin develop
```
Expected: bootstrap commit on `develop`.

## Phase 2 Plan: Implement Assigned Use-Case Modules

### Task 2: Code Feature Branches From `develop`

**Files:**
- Modify under `Homework07/Group/src/main/java/`
- Modify under `Homework07/Group/src/test/java/`
- Modify under `Homework07/Group/docs/`

- [ ] **Step 1: Create branch from `develop`**

Run one command set for the assigned work:
```bash
git switch develop
git pull --ff-only origin develop
git switch -c hw07/20235798-NguyenHuuNhan/feature/receive-reconcile-goods

git switch develop
git pull --ff-only origin develop
git switch -c hw07/20235658-VuDucHoangAnh/feature/manage-import-requests

git switch develop
git pull --ff-only origin develop
git switch -c hw07/20235658-VuDucHoangAnh/feature/manage-system

git switch develop
git pull --ff-only origin develop
git switch -c hw07/20235821-TranDangSinh/feature/collect-inventory-data

git switch develop
git pull --ff-only origin develop
git switch -c hw07/20235880-PhanKhanhVu/feature/plan-import-allocation

git switch develop
git pull --ff-only origin develop
git switch -c hw07/20235754-LuongQuocKhanh/feature/place-overseas-orders

git switch develop
git pull --ff-only origin develop
git switch -c hw07/20235682-NguyenQuyDuc/feature/manage-site-information
```
Expected: run only the three commands for current member branch.

- [ ] **Step 2: Implement assigned use case only**

Assignment:
- Nguyễn Hữu Nhân: UC006 `ReceiveAndReconcileGoods`
- Vũ Đức Hoàng Anh: UC001 `ManageImportRequests`
- Vũ Đức Hoàng Anh: UC007 `ManageSystem`
- Trần Đăng Sinh: UC003 `CollectInventoryData`
- Phan Khánh Vũ: UC004 `PlanImportAllocation`
- Lương Quốc Khánh: UC005 `PlaceOverseasOrders`
- Nguyễn Quý Đức: UC002 `ManageSiteInformation`

Expected: code, tests, and docs stay scoped to assigned use case.

- [ ] **Step 3: Add tests for assigned use case**

Create tests under `Homework07/Group/src/test/java/vn/edu/hust/itss/group15/`. Minimum test coverage per use case:
- UC001: valid request created; invalid quantity/date rejected.
- UC002: delivery days updated; invalid days rejected.
- UC003: Site filtering works; out-of-stock response stores zero.
- UC004: allocation respects stock/date/ship priority; insufficient stock rejected.
- UC005: order lines grouped by Site; invalid delivery means rejected.
- UC006: received vs ordered discrepancy detected; note required when mismatch exists.
- UC007: role assignment checked; unauthorized admin action rejected.

- [ ] **Step 4: Run local build before push**

Run:
```bash
cd Homework07/Group
mvn clean test
```
Expected: all tests pass.

- [ ] **Step 5: Push branch**

Run:
```bash
git push -u origin HEAD
```
Expected: branch pushed for pull request into `develop`.

## Phase 3 Plan: Integrate End-to-End Workflow on `develop`

### Task 3: Merge Feature Branches and Wire Full Business Flow

**Files:**
- Review all changed files under `Homework07/Group/`

- [ ] **Step 1: Check pull request branch naming**

Expected branch examples:
```text
hw07/20235798-NguyenHuuNhan/feature/receive-reconcile-goods
hw07/20235658-VuDucHoangAnh/feature/manage-import-requests
hw07/20235821-TranDangSinh/feature/collect-inventory-data
hw07/20235880-PhanKhanhVu/feature/plan-import-allocation
hw07/20235754-LuongQuocKhanh/feature/place-overseas-orders
hw07/20235682-NguyenQuyDuc/feature/manage-site-information
```

- [ ] **Step 2: Run integration build on `develop`**

Run:
```bash
git switch develop
git pull --ff-only origin develop
cd Homework07/Group
mvn clean test
mvn clean package
```
Expected: test and package succeed with JDK 22.

- [ ] **Step 3: Verify end-to-end workflow**

Run or document one demo scenario covering:
```text
UC001 Create import request
UC003 Collect inventory data
UC004 Plan import allocation
UC005 Place overseas orders
UC006 Receive and reconcile goods
UC002 Update Site delivery information when needed
UC007 Manage users/master data/logs as support feature
```
Expected: demo flow completes without manual data patching between use cases.

- [ ] **Step 4: Fix integration bugs on proper branch type**

Use:
```bash
git switch develop
git pull --ff-only origin develop
git switch -c hw07/20235682-NguyenQuyDuc/fix/integration-build
```
Expected: integration fixes do not happen directly on `develop`.

## Phase 4 Plan: Stabilize, Package, and Submit

### Task 4: Final Homework07 Submission

**Files:**
- Create or update: `Homework07/Group/docs/`
- Create or update: `Homework07/Group/README.md`
- Create or update: final `.docx`, `.pdf`, images if assignment requires these files.

- [ ] **Step 1: Final quality gate**

Run:
```bash
git switch develop
git pull --ff-only origin develop
cd Homework07/Group
mvn -version
mvn clean test
mvn clean package
```
Expected: Maven uses Java 22, tests pass, package succeeds.

- [ ] **Step 2: Freeze features and check no unfinished markers**

Run:
```bash
rg -n "TODO|TBD|xxx|\\.\\.\\." Homework07/Group AGENTS.md || true
```
Expected: no unfinished markers in submitted project files.

- [ ] **Step 3: Complete submission docs**

Update `Homework07/Group/README.md` and `Homework07/Group/docs/` with:
- member assignment,
- build/run guide,
- implemented use cases,
- test/demo instructions,
- screenshots or exported images if required by lecturer.

Expected: reviewer can build, run, and understand final project from `Homework07/Group/`.

- [ ] **Step 4: Merge `develop` to `main`**

Run:
```bash
git switch main
git pull --ff-only origin main
git merge --no-ff develop
git push origin main
```
Expected: final project merged after `develop` passes quality gate.

## Pull Request Checklist

- Source branch starts with `hw07/<MSSV>-<HoTenKhongDau>/`.
- Source branch was created from latest `develop`.
- Target branch is `develop`.
- PR states use case, member, branch type, and test result.
- `mvn -version` shows Java 22.
- `mvn clean test` passes.
- `mvn clean package` passes before final merge.
- No direct commits to `main` for Homework07 work.

## Self-Review

Spec coverage:
- Phase 1 bootstraps Maven/JDK 22 project and shared code layout.
- Phase 2 implements assigned use-case modules with tests.
- Phase 3 integrates modules into end-to-end workflow on `develop`.
- Phase 4 stabilizes, packages, documents, and merges final project.
- Every member branches from `develop`; branch pattern supports `feature`, `fix`, `hotfix`, `refactor`, `test`, `docs`, `chore`.

Placeholder scan:
- No unfinished placeholder text in this plan.
- Branch examples use concrete MSSV/name values from the repo.

Type consistency:
- Branch base is always `develop`.
- Final PR target for member branches is always `develop`.
- Final integration path is `develop` to `main`.
