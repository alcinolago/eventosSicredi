package br.com.sicredi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import br.com.sicredi.core.extension.Event
import br.com.sicredi.data.model.events.EventsResponse
import br.com.sicredi.domain.interactor.EventsInteractor
import br.com.sicredi.presentation.events.details.EventDetailViewModel
import br.com.sicredi.provider.scheduler.BaseSchedulerProvider
import br.com.sicredi.provider.scheduler.TrampolineSchedulerProvider
import br.com.sicredi.provider.string.StringProvider
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class EventDetailViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    private lateinit var eventInteractor: EventsInteractor

    @Mock
    private lateinit var schedulerProvider: BaseSchedulerProvider

    @Mock
    private lateinit var stringProvider: StringProvider

    @Mock
    private lateinit var observeEventRespnse: Observer<EventsResponse>

    @Mock
    private lateinit var observeBoolean: Observer<Boolean>

    @Mock
    private lateinit var observeNameErrorMessage: Observer<Event<String>>

    @Mock
    private lateinit var observeEmailErrorMessage: Observer<Event<String>>

    @Mock
    private lateinit var observeEventSelected: Observer<String>

    private lateinit var viewModel: EventDetailViewModel
    private lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)

        lifecycle = LifecycleRegistry(lifecycleOwner)
        BDDMockito.given(lifecycleOwner.lifecycle).willReturn(lifecycle)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        schedulerProvider = TrampolineSchedulerProvider()

        viewModel = EventDetailViewModel(eventInteractor, stringProvider, schedulerProvider)
    }

    @Test
    fun `it should initialize fields properly`() {
        assertNull(viewModel.eventMutableLiveData.value)
        assertNull(viewModel.eventCheckIMessage.value)
        assertNull(viewModel.name.value)
        assertNull(viewModel.email.value)
        assertNull(viewModel.eventSelected.value)
        assertNull(viewModel.nameErrorMessage.value)
        assertNull(viewModel.emailErrorMessage.value)
        assertFalse(viewModel.isLoading.value!!)
    }

    @Test
    fun `should get event by id from service`() {

        val movieResponse = EventsResponse(
            0,
            "date",
            "description",
            "image",
            0.0,
            0.0,
            0.0,
            "title"
        )

        `when`(eventInteractor.getEventById(1)).thenReturn(Observable.just(movieResponse))

        viewModel.eventMutableLiveData.observe(lifecycleOwner, observeEventRespnse)
        viewModel.eventSelected.observe(lifecycleOwner, observeEventSelected)
        viewModel.isLoading.observe(lifecycleOwner, observeBoolean)

        viewModel.getEventById(1)

        verify(observeEventRespnse, Mockito.times(1)).onChanged(ArgumentMatchers.any())
        verify(observeEventSelected, Mockito.times(1)).onChanged(ArgumentMatchers.any())
        verify(observeBoolean, Mockito.times(3)).onChanged(ArgumentMatchers.any())
    }

    @Test
    fun `should return error when try to get event by id from service`() {

        `when`(eventInteractor.getEventById(1)).thenReturn(Observable.error(Exception()))

        viewModel.isLoading.observe(lifecycleOwner, observeBoolean)

        viewModel.getEventById(1)

        verify(observeBoolean, Mockito.times(3)).onChanged(ArgumentMatchers.any())
    }

    @Test
    fun `should show error message if name is null or empty in checkin form`() {

        viewModel.name.value = ""

        viewModel.isLoading.observe(lifecycleOwner, observeBoolean)
        viewModel.nameErrorMessage.observe(lifecycleOwner, observeNameErrorMessage)

        viewModel.validateDataEntry()

        verify(observeNameErrorMessage, times(1)).onChanged(any())
        verify(observeBoolean, times(4)).onChanged(any())
    }

    @Test
    fun `should show error message if email is null or empty in checkin form`() {

        viewModel.email.value = ""

        viewModel.isLoading.observe(lifecycleOwner, observeBoolean)
        viewModel.emailErrorMessage.observe(lifecycleOwner, observeEmailErrorMessage)

        viewModel.validateDataEntry()

        verify(observeEmailErrorMessage, times(1)).onChanged(any())
        verify(observeBoolean, times(4)).onChanged(any())
    }
}