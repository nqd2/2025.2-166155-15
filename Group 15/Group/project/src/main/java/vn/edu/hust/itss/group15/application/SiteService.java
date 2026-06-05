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

  public List<ImportSite> listSites() {
    return store.sites();
  }
}
