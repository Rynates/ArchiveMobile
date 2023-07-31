package com.example.helper.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.helper.R
import com.example.helper.databinding.ActivityMainBinding
import com.example.helper.utils.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    private lateinit var navController: NavController
    private val topLevelDestinations = setOf(getLoginDestination())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)



        setContentView(binding.root)
        navController = getRootNavController()
       // prepareRootNavController(false, navController)
         NavigationUI.setupWithNavController(binding.bottomNavigationView,navController = navController)


    }

//    private fun prepareRootNavController(isSignedIn: Boolean, navController: NavController) {
//        val graph = navController.navInflater.inflate(getMainNavigationGraphId())
//        graph.setStartDestination(
//            if (isSignedIn) {
//                getTabsDestination()
//            } else {
//                getLoginDestination()
//            }
//        )
//        navController.graph = graph
//    }
//
    private fun getRootNavController(): NavController {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.mainFragmentContainerView) as NavHostFragment
        return navHost.navController
    }
//
//    private fun isStartDestination(destination: NavDestination?): Boolean {
//        if (destination == null) return false
//        val graph = destination.parent ?: return false
//        val startDestinations = topLevelDestinations + graph.startDestinationId
//        return startDestinations.contains(destination.id)
//    }
//
//    override fun onBackPressed() {
//        if (isStartDestination(navController.currentDestination)) {
//            super.onBackPressed()
//        } else {
//            navController.popBackStack()
//        }
//    }

    override fun onSupportNavigateUp(): Boolean =
        (navController.navigateUp()) || super.onSupportNavigateUp()

    private fun getMainNavigationGraphId(): Int = R.navigation.nav_graph

    private fun getLoginDestination(): Int = R.id.loginFragment


}