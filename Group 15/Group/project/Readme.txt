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
- `domain`: entities, value records, enums, and pure domain policies.
- `application`: use-case services, authentication, and `ImportOrderingFacade`.
- `application.port`: persistence port.
- `infrastructure.persistence.supabase`: Supabase REST adapter.
- `infrastructure.persistence.memory`: in-memory adapter for fast tests.
- `app`: JavaFX presentation and wiring.

## Phân công nhiệm vụ & % Đóng góp (Tổng 100%)

| Thành viên          | Phân công (Use case)                           | % Đóng góp |
| ------------------- | ---------------------------------------------- | ---------- |
| Nguyen Quy Duc      | UC002 - ManageSiteInformation; Integration lead| 20%        |
| Vu Duc Hoang Anh    | UC001 - ManageImportRequests; UC007            | 16%        |
| Nguyen Huu Nhan     | UC006 - ReceiveAndReconcileGoods               | 16%        |
| Tran Dang Sinh      | UC003 - CollectInventoryData                   | 16%        |
| Phan Khanh Vu       | UC004 - PlanImportAllocation                   | 16%        |
| Luong Quoc Khanh    | UC005 - PlaceOverseasOrders                    | 16%        |

*Tổng cộng: 100%*
