package com.example.ingrediscan
import android.content.pm.PackageManager
import android.graphics.Camera
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ingrediscan.ui.theme.IngrediScanTheme
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.camera.view.PreviewView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ingrediscan.databinding.ActivityMainBinding

import com.example.ingrediscan.Camera.CameraXFeatures

//generate the highest level of the front page & other elements
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_previous_results,
                R.id.navigation_profile, R.id.navigation_scan,
                R.id.navigation_search
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        
        // Hide the BottomNavigationView when the scan destination is displayed
        // Lines inside the if statement are for removing the action bar (if needed)
        navController.addOnDestinationChangedListener { navController, destination, arguments ->
            if (destination.id == R.id.navigation_scan) {
                binding.navView.visibility = View.GONE
//                supportActionBar?.hide()
            } else {
                binding.navView.visibility = View.VISIBLE
//                supportActionBar?.show()
            }
        }

        //if permissions are granted it will ask for camera permissions
        if(!allPermissionsGranted()){
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }
    //helper function
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this,it) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Called when a permission request has been completed.
     *
     * This function is invoked after the user responds to a runtime permission request.
     * It checks if all requested permissions have been granted. If any permission is denied,
     * a toast message is displayed to inform the user, and the activity is closed.
     *
     * @param requestCode The integer request code originally supplied to [ActivityCompat.requestPermissions],
     *                    allowing you to identify which permission request this result corresponds to.
     * @param permissions An array of the requested permissions.
     * @param grantResults An array of results for the corresponding permissions, where each entry is either
     *                     [PackageManager.PERMISSION_GRANTED] or [PackageManager.PERMISSION_DENIED].
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}
