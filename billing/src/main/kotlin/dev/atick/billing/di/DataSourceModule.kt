package dev.atick.billing.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.atick.billing.data.BillingDataSource
import dev.atick.billing.data.BillingDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindBillingDataSource(
        billingDataSourceImpl: BillingDataSourceImpl
    ): BillingDataSource
}