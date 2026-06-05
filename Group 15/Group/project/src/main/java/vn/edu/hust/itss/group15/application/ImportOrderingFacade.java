package vn.edu.hust.itss.group15.application;

import vn.edu.hust.itss.group15.application.port.Store;

public class ImportOrderingFacade {
  private final Store store;
  private final ImportRequestService importRequests;
  private final SiteService sites;
  private final InventoryService inventory;
  private final AllocationService allocation;
  private final OrderService orders;
  private final ReceiptService receipts;
  private final AdminService admin;
  private final AuthService auth;

  public ImportOrderingFacade(Store store) {
    this.store = store;
    importRequests = new ImportRequestService(store);
    sites = new SiteService(store);
    inventory = new InventoryService(store);
    allocation = new AllocationService(store);
    orders = new OrderService(store);
    receipts = new ReceiptService(store);
    admin = new AdminService(store);
    auth = new AuthService(store, admin);
  }

  public Store store() {
    return store;
  }

  public ImportRequestService importRequests() {
    return importRequests;
  }

  public SiteService sites() {
    return sites;
  }

  public InventoryService inventory() {
    return inventory;
  }

  public AllocationService allocation() {
    return allocation;
  }

  public OrderService orders() {
    return orders;
  }

  public ReceiptService receipts() {
    return receipts;
  }

  public AdminService admin() {
    return admin;
  }

  public AuthService auth() {
    return auth;
  }
}
