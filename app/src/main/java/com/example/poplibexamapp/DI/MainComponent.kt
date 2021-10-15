package com.example.poplibexamapp.DI

import android.content.Context
import com.example.poplibexamapp.*
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Singleton

@Singleton
@Component (modules = [AndroidInjectionModule::class, MainModule::class, DataBaseModule::class])
interface MainComponent: AndroidInjector<MainApplication> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun withContext(context: Context): Builder

        @BindsInstance
        fun withRouter(router: Router): Builder

        @BindsInstance
        fun withNavigatorHolder(navigatorHolder: NavigatorHolder): Builder

        @BindsInstance
        fun withIoScheduler (ioScheduler: Scheduler): Builder

        @BindsInstance
        fun withImageLoader (imageLoader: GlideImageLoader): Builder

        @BindsInstance
        fun withNetworkStatus (networkStatus: NetworkStatus): Builder

        fun build(): MainComponent
    }
}