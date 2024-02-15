package com.example.nycschoolsapplication.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class SchoolViewModelTest {

    @Mock
    private lateinit var schoolApi: SchoolApi

    private lateinit var schoolRepository: SchoolRepository
    private lateinit var viewModel: SchoolViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        schoolRepository = SchoolRepository(schoolApi)
        viewModel = SchoolViewModel(schoolRepository)
    }

    @Test
    fun validateFetchSchoolsSuccess() = runBlocking {
        whenever(schoolRepository.fetchSchools()).thenReturn(listOf())
        viewModel.fetchSchools()
        assertNotNull((viewModel.schools.value as SchoolViewModel.Result.Success).schools)
    }

    @Test
    fun validateFetchSchoolsFailed() = runBlocking {
        whenever(schoolRepository.fetchSchools()).thenReturn(null)
        viewModel.fetchSchools()
        assertNotNull((viewModel.schools.value as SchoolViewModel.Result.Error).message)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}