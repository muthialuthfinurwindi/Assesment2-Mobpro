package com.muthia0027.mobpro1.ui.screen

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.muthia0027.mobpro1.R
import com.muthia0027.mobpro1.ui.theme.Mobpro1Theme

@Composable
fun DisplayAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        text = { Text(text = stringResource(R.string.btn_hapus)) },
        confirmButton = {
            TextButton(onClick = {onConfirmation() }) {
                Text(text = stringResource(R.string.hapus_desc))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(R.string.btn_batal))
            }
        },
        onDismissRequest = { onDismissRequest() }
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DialogPreview() {
    Mobpro1Theme{
        DisplayAlertDialog(
            onDismissRequest = {},
            onConfirmation = {}
        )
    }
}