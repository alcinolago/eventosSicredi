package br.com.sicredi.di

import br.com.sicredi.data.repository.EventRepository
import br.com.sicredi.domain.interactor.EventsInteractor
import br.com.sicredi.domain.interactor.EventsInteractorImpl
import br.com.sicredi.presentation.events.list.EventsListViewModel
import br.com.sicredi.presentation.events.details.EventDetailViewModel
import br.com.sicredi.provider.string.StringProvider
import br.com.sicredi.provider.string.StringProviderImpl
import br.com.sicredi.service.RetrofitServiceFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appInject = module {

    single { RetrofitServiceFactory.createService(EventRepository::class.java) }
    single<EventsInteractor> { EventsInteractorImpl(get()) }

    single<StringProvider> { StringProviderImpl(androidContext()) }

    viewModel { EventsListViewModel(get(), get()) }
    viewModel { EventDetailViewModel(get(), get()) }
}