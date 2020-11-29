package com.gabriel.myfavouritemoviesapp.ui.movielist

import com.gabriel.usecases.GetMoviesUseCase
import com.nhaarman.mockitokotlin2.any
import org.mockito.Mockito.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MoviesViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getMoviesUseCase: GetMoviesUseCase

    @Mock
    lateinit var observerUIModel: Observer<UiModel>

    @Mock
    lateinit var observerModelNavigation: Observer<NavigationModel>

    private lateinit var vm: MoviesViewModel

    @Before
    fun setUp() {
        vm = MoviesViewModel(
            getMoviesUseCase,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun `when requesting movies, then a request-movies event is sent to view`() {
        vm.model.observeForever(observerUIModel)

        verify(observerUIModel).onChanged(
            UiModel.RequestMovies
        )
    }

    @Test
    fun `when requesting movies, then a successful event is returned `() {
        runBlocking {
            vm.model.observeForever(observerUIModel)
            whenever(
                getMoviesUseCase.execute(
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnSuccess(mockedMovieUiList)
            }
            vm.onRequestPopularMovies()

            verify(observerUIModel).onChanged(
                UiModel.Content(mockedMovieUiList)
            )
        }
    }

    @Test
    fun `when requesting movies, then a show general error event is returned`() {
        runBlocking {
            vm.model.observeForever(observerUIModel)
            whenever(
                getMoviesUseCase.execute(
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnError(resourceError)
            }
            vm.onRequestPopularMovies()

            verify(observerUIModel).onChanged(
                UiModel.ShowGeneralError(resourceError.message)
            )
        }
    }

    @Test
    fun `when requesting movies, then a loading event is sent to view`() {

        vm.model.observeForever(observerUIModel)

        vm.onRequestPopularMovies()

        verify(observerUIModel).onChanged(
            UiModel.Loading
        )
    }

    @Test
    fun `when clicking on a movie, then a navigation event is sent to view`() {
        vm.modelNavigation.observeForever(observerModelNavigation)

        vm.onMovieClicked(mockedMovieUi)

        verify(observerModelNavigation).onChanged(
            NavigationModel(mockedMovieUi)
        )
    }

    @Test
    fun `when requesting movies from retry button, then a successful event is returned `() {
        runBlocking {
            vm.model.observeForever(observerUIModel)
            whenever(
                getMoviesUseCase.execute(
                    any(),
                    any()
                )
            ).then { invocation ->
                invocation.invocationOnSuccess(mockedMovieUiList)
            }
            vm.onRetryButtonClicked()

            verify(observerUIModel).onChanged(
                UiModel.Content(mockedMovieUiList)
            )
        }
    }
}