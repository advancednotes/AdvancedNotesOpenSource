package com.advancednotes.domain.models

import androidx.compose.ui.text.input.TextFieldValue


sealed class AdvancedContentUIObject {
    data class MyText(val value: AdvancedContentTextItemUI) : AdvancedContentUIObject()
    data class MyList(val value: List<AdvancedContentListItemUI>) : AdvancedContentUIObject()
    data class MyOther(val value: String) : AdvancedContentUIObject()
}

data class AdvancedContentTextItemUI(
    var textFieldValue: TextFieldValue
)

data class AdvancedContentListItemUI(
    var textFieldValue: TextFieldValue
)