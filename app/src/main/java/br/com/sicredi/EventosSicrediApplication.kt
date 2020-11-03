package br.com.sicredi

import android.app.Application
import br.com.sicredi.di.appInject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EventosSicrediApplication : Application() {

    override fun onCreate() {

        super.onCreate()

        startKoin {
            androidContext(this@EventosSicrediApplication)
            modules(appInject)
        }
    }
}