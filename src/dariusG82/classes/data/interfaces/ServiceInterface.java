package dariusG82.classes.data.interfaces;

import dariusG82.classes.services.file_services.AccountingFileService;
import dariusG82.classes.services.file_services.BusinessFileService;
import dariusG82.classes.services.file_services.WarehouseFileService;

public interface ServiceInterface {
    DataManagement getDataService();

    WarehouseFileService getWarehouseService();

    BusinessFileService getBusinessService();

    AccountingFileService getAccountingService();
}
