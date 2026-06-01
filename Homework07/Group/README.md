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

## Backend Architecture

The backend follows a DDD-style layered design:

- `domain`: entities, value records, enums, and pure domain policies such as allocation rules.
- `application`: use-case services, authentication, and `ImportOrderingFacade`.
- `application.port`: persistence port required by the application layer.
- `infrastructure.persistence.supabase`: Supabase REST adapter using raw Java `HttpClient`.
- `infrastructure.persistence.memory`: in-memory adapter for fast tests.
- `app`: JavaFX presentation and wiring.

Dependency direction: `app -> application -> domain`; infrastructure implements application ports and is injected at startup.

## Run JavaFX App

```bash
export IMPORT_SUPABASE_URL='https://<project-ref>.supabase.co'
export IMPORT_SUPABASE_KEY='<supabase-anon-or-service-role-key>'
mvn javafx:run
```

Before the first run, execute `src/main/resources/db/schema.sql` in Supabase SQL Editor. The app then asks for the first `SystemAdministrator` account.

## Branch Flow

- Base branch: `develop`
- Member branch: `hw07/<MSSV>-<HoTenKhongDau>/<type>/<short-topic>`
- Pull request target: `develop`
- Final merge: `develop` -> `main`

## Module Ownership

| Member | Use case |
| --- | --- |
| Nguyen Huu Nhan | UC006 - ReceiveAndReconcileGoods |
| Vu Duc Hoang Anh | UC001 - ManageImportRequests; UC007 - ManageSystem |
| Tran Dang Sinh | UC003 - CollectInventoryData |
| Phan Khanh Vu | UC004 - PlanImportAllocation |
| Luong Quoc Khanh | UC005 - PlaceOverseasOrders |
| Nguyen Quy Duc | UC002 - ManageSiteInformation; integration lead |
