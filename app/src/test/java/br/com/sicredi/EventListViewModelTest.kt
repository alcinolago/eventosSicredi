package br.com.sicredi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import br.com.sicredi.data.model.events.EventsResponse
import br.com.sicredi.domain.interactor.EventsInteractor
import br.com.sicredi.presentation.events.list.EventsListViewModel
import br.com.sicredi.provider.scheduler.BaseSchedulerProvider
import br.com.sicredi.provider.scheduler.TrampolineSchedulerProvider
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EventListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    private lateinit var eventInteractor: EventsInteractor

    @Mock
    private lateinit var schedulerProvider: BaseSchedulerProvider

    @Mock
    private lateinit var observeEventList: Observer<List<EventsResponse>>

    @Mock
    private lateinit var observeBoolean: Observer<Boolean>

    private lateinit var viewModel: EventsListViewModel
    private lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)

        lifecycle = LifecycleRegistry(lifecycleOwner)
        BDDMockito.given(lifecycleOwner.lifecycle).willReturn(lifecycle)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        schedulerProvider = TrampolineSchedulerProvider()

        viewModel = EventsListViewModel(eventInteractor, schedulerProvider)
    }

    @Test
    fun `it should initialize fields properly`() {
        assertNull(viewModel.eventMutableLiveData.value)
        assertFalse(viewModel.isLoading.value!!)
    }

    @Test
    fun `should return events list`() {

        val movieResponse = listOf(
            EventsResponse(0, "date", "description", "image", 0.0, 0.0, 0.0, "title")
        )

        `when`(eventInteractor.getEvents()).thenReturn(Observable.just(movieResponse))

        viewModel.eventMutableLiveData.observe(lifecycleOwner, observeEventList)
        viewModel.isLoading.observe(lifecycleOwner, observeBoolean)

        viewModel.getEvents()

        verify(observeEventList, times(1)).onChanged(any())
        verify(observeBoolean, times(3)).onChanged(any())
    }

    @Test
    fun `should return error when try to get event list`() {

        `when`(eventInteractor.getEvents()).thenReturn(Observable.error(Exception()))

        viewModel.isLoading.observe(lifecycleOwner, observeBoolean)

        viewModel.getEvents()

        verify(observeBoolean, times(3)).onChanged(any())
    }
}