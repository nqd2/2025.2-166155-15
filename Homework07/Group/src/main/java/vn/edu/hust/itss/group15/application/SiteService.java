package vn.edu.hust.itss.group15.application;

import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;
import vn.edu.hust.itss.group15.domain.ImportSite;
import vn.edu.hust.itss.group15.domain.OperationLog;
import vn.edu.hust.itss.group15.application.port.Store;

public class SiteService {
  private final Store store;

  public SiteService(Store store) {
    this.store = store;
  }

  public ImportSite updateSite(String siteCode, String siteName, int shipDays, int airDays, Set<String> merchandiseCatalog) {
    store.findSite(siteCode).orElseThrow(() -> new BusinessException("site not found: " + siteCode));
    if (shipDays <= 0 || airDays <= 0) {
      throw new BusinessException("delivery days must be positive");
    }
    if (!store.merchandiseCatalog().containsAll(merchandiseCatalog)) {
      throw new BusinessException("merchandiseCatalog contains unknown merchandise");
    }
    ImportSite updated = new ImportSite(siteCode, siteName, shipDays, airDays, merchandiseCatalog);
    store.saveSite(updated);
    store.appendLog(new OperationLog(store.nextId("operation_logs", "LOG"), "system", "UPDATE_SITE", LocalDateTime.now(), siteCode));
    return updated;
  }

  public ImportSite createSite(String siteCode, String siteName, int shipDays, int airDays, Set<String> merchandiseCatalog) {
    if (siteCode == null || siteCode.isBlank()) {
      throw new BusinessException("siteCode is required");
    }
    String code = siteCode.trim();
    if (store.findSite(code).isPresent()) {
      throw new BusinessException("Site already exists: " + code);
    }
    if (shipDays <= 0 || airDays <= 0) {
      throw new BusinessException("delivery days must be positive");
    }
    if (merchandiseCatalog == null || merchandiseCatalog.isEmpty()) {
      throw new BusinessException("merchandiseCatalog is required");
    }
    if (!store.merchandiseCatalog().containsAll(merchandiseCatalog)) {
      throw new BusinessException("merchandiseCatalog contains unknown merchandise");
    }
    ImportSite site = new ImportSite(code, siteName, shipDays, airDays, merchandiseCatalog);
    store.saveSite(site);
    store.appendLog(new OperationLog(store.nextId("operation_logs", "LOG"), "system", "CREATE_SITE", LocalDateTime.now(), code));
    return site;
  }

  public String createMerchandise(String merchandiseCode) {
    if (merchandiseCode == null || merchandiseCode.isBlank()) {
      throw new BusinessException("merchandise code is required");
    }
    String formatted = merchandiseCode.trim().toUpperCase();
    if (store.merchandiseCatalog().contains(formatted)) {
      throw new BusinessException("merchandise already exists: " + formatted);
    }
    store.saveMerchandise(formatted);
    store.appendLog(new OperationLog(store.nextId("operation_logs", "LOG"), "system", "CREATE_MERCHANDISE", LocalDateTime.now(), formatted));
    return formatted;
  }

  public List<ImportSite> listSites() {
    return store.sites();
  }
}
