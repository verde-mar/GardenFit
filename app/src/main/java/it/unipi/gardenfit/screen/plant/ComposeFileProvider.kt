package it.unipi.gardenfit.screen.plant

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import it.unipi.gardenfit.R
import java.io.File

class ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(plantName: String, context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                plantName,
                ".png",
                directory,
            )
            val authority = context.packageName + ".provider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}