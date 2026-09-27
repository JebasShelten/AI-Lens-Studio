package com.shelten.ailensstudio

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.shelten.ailensstudio.presentation.gallery.GalleryScreen
import com.shelten.ailensstudio.ui.theme.AILensStudioTheme
import android.graphics.Color
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {

    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            recreate()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(
            window,
            true
        )

        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE

        WindowCompat.getInsetsController(
            window,
            window.decorView
        ).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }

        setContent {

            AILensStudioTheme {

                AppContent(
                    onRequestPermission = {
                        requestGalleryPermission()
                    }
                )
            }
        }
    }

    private fun requestGalleryPermission() {

        val permissions = when {
            Build.VERSION.SDK_INT >= 34 -> {

                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )
            }

            Build.VERSION.SDK_INT >= 33 -> {

                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            }

            else -> {

                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }

        permissionLauncher.launch(permissions)
    }

    private fun hasGalleryAccess(): Boolean {

        return when {

            Build.VERSION.SDK_INT >= 34 -> {

                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                        ) == PackageManager.PERMISSION_GRANTED
            }

            Build.VERSION.SDK_INT >= 33 -> {

                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            }

            else -> {

                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    @Composable
    private fun AppContent(
        onRequestPermission: () -> Unit
    ) {

        if (hasGalleryAccess()) {

            GalleryScreen()

        } else {

            PermissionScreen(
                onRequestPermission = onRequestPermission
            )
        }
    }
}

@Composable
private fun PermissionScreen(
    onRequestPermission: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "AI Lens Studio",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Allow photo access to build your gallery.",
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = onRequestPermission,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text("Allow Photos")
        }
    }
}