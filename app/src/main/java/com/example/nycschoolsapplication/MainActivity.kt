package com.example.nycschoolsapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nycschoolsapplication.data.MyViewModelFactory
import com.example.nycschoolsapplication.data.SchoolInfo
import com.example.nycschoolsapplication.data.SchoolViewModel

//Create a native app in Kotlin with Jetpack Compose components to provide information on NYC High schools.
//Display a list of NYC High Schools (school_name, dbn)
//Access the JSON API directly from below link: https://data.cityofnewyork.us/resource/s3k6-pzi2.json
//Selecting a school should show additional information about the school. At the very least, display the overview_paragraph
//Please implement using MVVM architecture to solve #1 and #2
//Implement the unit test cases using JUnit/Mockito
//Write automation will be plus point

class MainActivity : ComponentActivity() {
    private val viewModel: SchoolViewModel by viewModels { MyViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NYCApp(viewModel = viewModel)
        }
    }

}

@Composable
private fun NYCApp(
    viewModel: SchoolViewModel,
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = SchoolRoute.HOME.route
    ) {
        composable(SchoolRoute.HOME.route) {
            HomeScreen(viewModel = viewModel,
                onItemClicked = { school ->
                    viewModel.selectSchool(school)
                    navController.navigate(SchoolRoute.DETAIL.route)
                })
        }
        composable(SchoolRoute.DETAIL.route) {
            DetailScreen(viewModel = viewModel)
        }
    }

}

@Composable
fun DetailScreen(viewModel: SchoolViewModel) {
    val school = viewModel.selectedSchool.collectAsState()
    Column( modifier = Modifier.padding(10.dp)) {

        val dbn = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("DBN :  ")
            }
            append(school.value.dbn.orEmpty())
        }
        Text(text = dbn)

        val schoolName = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("SchoolName :  ")
            }
            append(school.value.schoolName.orEmpty())
        }
        Text(text = schoolName)

        val text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("OverviewParagraph :  ")
            }
            append(school.value.overviewParagraph.orEmpty())
        }
        Text(text = text, textAlign = TextAlign.Justify)
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier, viewModel: SchoolViewModel,
    onItemClicked: (SchoolInfo) -> Unit
) {
    val result = viewModel.schools.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchSchools()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (result.value) {
            is SchoolViewModel.Result.Loading -> {
                //Show loading
                CircularProgressIndicator(modifier = Modifier.width(36.dp))
            }


            is SchoolViewModel.Result.Success -> {
                //Show list of schools
                if ((result.value as SchoolViewModel.Result.Success).schools.isEmpty()) {
                    Text(text = "No schools found")
                } else {
                    SchoolList(
                        schools = (result.value as SchoolViewModel.Result.Success).schools,
                        onItemClicked = onItemClicked
                    )
                }
            }

            is SchoolViewModel.Result.Error -> {
                //Show error
                Text(text = (result.value as SchoolViewModel.Result.Error).message)
            }
        }
    }
}
@Composable
private fun SchoolList(schools: List<SchoolInfo>, onItemClicked: (SchoolInfo) -> Unit) {
    LazyColumn(  modifier = Modifier.padding(10.dp),verticalArrangement = Arrangement.spacedBy(8.dp)) {

        items(schools.size) { school ->
            Text(text = "School Info: ", fontWeight = FontWeight.Bold)

            //Show school
            Column(modifier = Modifier.clickable { onItemClicked(schools[school]) }) {
                Text(text = "SchoolName :  "+ schools[school].schoolName.orEmpty(),fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(text = "Neighborhood Name :  " + schools[school].neighborhood.orEmpty())
                Text(text = "TotalStudents : " + schools[school].totalStudents.orEmpty())
                Text(text = "AttendanceRate : " + schools[school].attendanceRate.orEmpty())
                Text(text = "ExtracurricularActivities:  " + schools[school].extracurricularActivities.orEmpty() )
                Text(text = "SchoolSports :  " + schools[school].schoolSports.orEmpty())
                Text(text = "SchoolAccessibility Description :  " + schools[school].schoolAccessibilityDescription.orEmpty())
            }
            // Add a divider
            if (school != schools.lastIndex) {
                Divider(color = Color.Gray, thickness = 1.dp)
            }
        }
    }
}
