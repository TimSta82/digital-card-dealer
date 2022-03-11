package de.digitaldealer.cardsplease.core.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import de.digitaldealer.cardsplease.KEY_FILEPROVIDER
import java.io.File

class CameraUtils {

    fun checkCameraAvailable(context: Context): Boolean = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)

    fun takePhoto(fragment: Fragment, fileName: String, launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUri(fragment.requireContext(), fileName))
        }
        launcher.launch(intent)
    }

    private fun getFileUri(context: Context, fileName: String) = FileProvider.getUriForFile(context, context.packageName + KEY_FILEPROVIDER, File(context.filesDir, fileName))
}
