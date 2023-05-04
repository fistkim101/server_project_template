package com.library.samplemysqllibrary.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceType = DataSourceConfiguration.WRITE_SOURCE_KEY;
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            dataSourceType = TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? DataSourceConfiguration.READ_SOURCE_KEY : DataSourceConfiguration.WRITE_SOURCE_KEY;
        }
        return dataSourceType;
    }
}
