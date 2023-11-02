@file:OptIn(ExperimentalMaterial3Api::class)

package com.myprojectname.app.feature.petupload

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.myprojectname.app.common.model.GeoLocation
import com.myprojectname.app.common.model.PetAge
import com.myprojectname.app.common.model.PetCategory
import com.myprojectname.app.common.model.PetGender
import com.myprojectname.app.common.model.PetSize
import com.myprojectname.app.common.model.PetStatus
import com.myprojectname.app.feature.petupload.viewmodel.PetUploadSideEffects
import com.myprojectname.app.feature.petupload.viewmodel.PetUploadState
import com.myprojectname.app.feature.petupload.viewmodel.PetUploadViewModel
import com.myprojectname.app.localization.Localization
import com.myprojectname.app.localization.getCurrentLocalization
import com.myprojectname.app.localization.getLocalizedModelName
import com.myprojectname.app.platform.RootSnackbarHostStateRepository
import com.myprojectname.app.ui.components.ColoredSnackBarHost
import com.myprojectname.app.ui.components.Picker
import com.myprojectname.app.ui.components.PickerModel
import com.myprojectname.app.ui.components.PickerOption
import com.myprojectname.app.ui.components.SnackbarType
import com.myprojectname.app.ui.components.showSnackbar
import com.myprojectname.app.ui.icon.MyProjectNameIcons
import com.myprojectname.app.ui.theme.MyProjectNameTheme
import com.myprojectname.app.ui.theme.Typography
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform
import kotlin.enums.EnumEntries

class PetUploadScreen : Screen {
    private lateinit var showingModal: MutableState<Boolean>
    private val rootSnackbarHostStateRepository = getRootSnackbarHostState()

    @Composable
    override fun Content() {
        MyProjectNameTheme {
            val currentNavigator = LocalNavigator.currentOrThrow
            val localization = getCurrentLocalization()
            val snackbarHostState = remember { SnackbarHostState() }
            showingModal = remember { mutableStateOf(false) }

            val viewModel = getScreenModel<PetUploadViewModel>()

            SetupSideEffects(viewModel, snackbarHostState, localization)
            PetUploadView(viewModel, snackbarHostState) {
                currentNavigator.pop()
            }
        }
    }

    private fun getRootSnackbarHostState(): RootSnackbarHostStateRepository {
        val koin = KoinPlatform.getKoin()
        return koin.get(RootSnackbarHostStateRepository::class, null, null)
    }

    @Composable
    private fun SetupSideEffects(
        viewModel: PetUploadViewModel,
        snackbarHostState: SnackbarHostState,
        localization: Localization
    ) {
        val coroutineScope = rememberCoroutineScope()
        val sideEffects = viewModel.sideEffects.collectAsState(PetUploadSideEffects.Initial, coroutineScope.coroutineContext)
        when (sideEffects.value) {
            PetUploadSideEffects.Initial -> {}
            PetUploadSideEffects.OnSubmitted -> {
                LaunchedEffect(coroutineScope) {
                    rootSnackbarHostStateRepository.snackbarHostState.showSnackbar(
                        type = SnackbarType.SUCCESS,
                        message = localization.loginSuccess,
                        duration = SnackbarDuration.Long
                    )
                }
                viewModel.onPetUploaded()
            }
            is PetUploadSideEffects.OnSubmittedError -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        type = SnackbarType.ERROR,
                        message = (sideEffects.value as PetUploadSideEffects.OnSubmittedError).errorMessage
                    )
                }
            }
        }
    }

    @Composable
    fun PetUploadView(viewModel: PetUploadViewModel, snackbarHostState: SnackbarHostState, onClose: () -> Unit) {
        val localization = getCurrentLocalization()
        val state by viewModel.state.collectAsState()

        val name = remember { mutableStateOf(TextFieldValue()) }
        val description = remember { mutableStateOf(TextFieldValue()) }
        val category = remember { mutableStateOf(PetCategory.DOGS) }
        val breed = remember { mutableStateOf(TextFieldValue()) }
        val age = remember { mutableStateOf(PetAge.ADULT) }
        val gender = remember { mutableStateOf(PetGender.MALE) }
        val size = remember { mutableStateOf(PetSize.MEDIUM) }
        val color = remember { mutableStateOf(TextFieldValue()) }
        val status = remember { mutableStateOf(PetStatus.ADOPTABLE) }

        val pickerModel = remember { mutableStateOf(PickerModel("", null, emptyList())) }

        MyProjectNameTheme {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        snackbar = {
                            ColoredSnackBarHost(snackbarHostState)
                        }
                    )
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = localization.petUploadTitle,
                                style = Typography.get().bodyLarge
                            )
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        navigationIcon = {
                            IconButton(onClick = { onClose.invoke() }) {
                                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go back")
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                name.value = TextFieldValue()
                                description.value = TextFieldValue()
                                category.value = PetCategory.DOGS
                                breed.value = TextFieldValue()
                                age.value = PetAge.ADULT
                                gender.value = PetGender.MALE
                                size.value = PetSize.MEDIUM
                                color.value = TextFieldValue()
                                status.value = PetStatus.ADOPTABLE
                                viewModel.resetErrorValues()
                            }) {
                                Icon(imageVector = MyProjectNameIcons.Delete, contentDescription = null)
                            }
                        }
                    )
                }
            ) {
                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .padding(it).padding(bottom = 46.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(scrollState)
                ) {
                    PetUploadForm(
                        localization,
                        state,
                        name,
                        description,
                        category,
                        breed,
                        age,
                        gender,
                        size,
                        color,
                        status,
                        pickerModel
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(70.dp)
                            .align(Alignment.BottomCenter)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Button(
                            onClick = {
                                viewModel.onSubmit(
                                    name.value.text,
                                    description.value.text,
                                    emptyList(),
                                    category.value,
                                    GeoLocation(0.0, 0.0),
                                    breed.value.text,
                                    color.value.text,
                                    age.value,
                                    gender.value,
                                    size.value,
                                    status.value
                                )
                            },
                            colors = ButtonDefaults.buttonColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 6.dp)
                                .size(50.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(10)
                        ) {
                            Text(
                                text = localization.petUploadFormSubmit,
                                style = Typography.get().titleMedium
                            )
                        }
                    }
                }
            }
            if (showingModal.value) {
                Picker(
                    modifier = Modifier.padding(bottom = 70.dp),
                    pickerModel = pickerModel.value
                ) { showingModal.value = false }
            }
        }
    }

    @Composable
    fun PetUploadForm(
        localization: Localization,
        state: PetUploadState,
        name: MutableState<TextFieldValue>,
        description: MutableState<TextFieldValue>,
        category: MutableState<PetCategory>,
        breed: MutableState<TextFieldValue>,
        age: MutableState<PetAge>,
        gender: MutableState<PetGender>,
        size: MutableState<PetSize>,
        color: MutableState<TextFieldValue>,
        status: MutableState<PetStatus>,
        pickerModel: MutableState<PickerModel>
    ) {
        UploadImageField(localization)
        Spacer(modifier = Modifier.size(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 100.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name.value,
                onValueChange = { name.value = it },
                placeholder = {
                    Text(
                        text = localization.petUploadFormName,
                        style = Typography.get().titleSmall
                    )
                },
                isError = state.isNameError,
                supportingText = { Text(text = state.nameSupportingText) }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description.value,
                onValueChange = { description.value = it },
                placeholder = {
                    Text(
                        text = localization.petUploadFormDescription,
                        style = Typography.get().titleSmall
                    )
                },
                isError = state.isDescriptionError,
                supportingText = { Text(text = state.descriptionSupportingText) }
            )

            val categoryInteractionSource = remember { MutableInteractionSource() }
            val isCategoryPressed by categoryInteractionSource.collectIsPressedAsState()
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = getLocalizedModelName(category.value.name),
                onValueChange = { },
                placeholder = {
                    Text(
                        text = localization.petUploadFormDescription,
                        style = Typography.get().titleSmall
                    )
                },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = MyProjectNameIcons.ArrowDropDown, contentDescription = null)
                },
                interactionSource = categoryInteractionSource
            )

            Spacer(modifier = Modifier.size(16.dp))

            val locationInteractionSource = remember { MutableInteractionSource() }
            val isLocationPressed by locationInteractionSource.collectIsPressedAsState()
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = localization.petUploadLocationField,
                onValueChange = { },
                placeholder = {
                    Text(
                        text = localization.petUploadLocationField,
                        style = Typography.get().titleSmall
                    )
                },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = MyProjectNameIcons.ArrowDropDown, contentDescription = null)
                },
                interactionSource = locationInteractionSource
            )

            Spacer(modifier = Modifier.size(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = breed.value,
                onValueChange = { breed.value = it },
                placeholder = {
                    Text(
                        text = localization.petUploadBreedField,
                        style = Typography.get().titleSmall
                    )
                }
            )

            Spacer(modifier = Modifier.size(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = color.value,
                onValueChange = { color.value = it },
                placeholder = {
                    Text(
                        text = localization.petUploadColorField,
                        style = Typography.get().titleSmall
                    )
                }
            )

            Spacer(modifier = Modifier.size(16.dp))

            val ageInteractionSource = remember { MutableInteractionSource() }
            val isAgePressed by ageInteractionSource.collectIsPressedAsState()
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = getLocalizedModelName(age.value.name),
                onValueChange = { },
                placeholder = {
                    Text(
                        text = localization.petUploadAgeField,
                        style = Typography.get().titleSmall
                    )
                },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = MyProjectNameIcons.ArrowDropDown, contentDescription = null)
                },
                interactionSource = ageInteractionSource
            )

            Spacer(modifier = Modifier.size(16.dp))

            val genderInteractionSource = remember { MutableInteractionSource() }
            val isGenderPressed by genderInteractionSource.collectIsPressedAsState()
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = getLocalizedModelName(gender.value.name),
                onValueChange = { },
                placeholder = {
                    Text(
                        text = localization.petUploadGenderField,
                        style = Typography.get().titleSmall
                    )
                },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = MyProjectNameIcons.ArrowDropDown, contentDescription = null)
                },
                interactionSource = genderInteractionSource
            )

            Spacer(modifier = Modifier.size(16.dp))

            val sizeInteractionSource = remember { MutableInteractionSource() }
            val isSizePressed by sizeInteractionSource.collectIsPressedAsState()
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = getLocalizedModelName(size.value.name),
                onValueChange = { },
                placeholder = {
                    Text(
                        text = localization.petUploadSizeField,
                        style = Typography.get().titleSmall
                    )
                },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = MyProjectNameIcons.ArrowDropDown, contentDescription = null)
                },
                interactionSource = sizeInteractionSource
            )

            Spacer(modifier = Modifier.size(16.dp))

            val statusInteractionSource = remember { MutableInteractionSource() }
            val isStatusPressed by statusInteractionSource.collectIsPressedAsState()
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = getLocalizedModelName(status.value.name),
                onValueChange = { },
                placeholder = {
                    Text(
                        text = localization.petUploadStatusField,
                        style = Typography.get().titleSmall
                    )
                },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = MyProjectNameIcons.ArrowDropDown, contentDescription = null)
                },
                interactionSource = statusInteractionSource
            )

            if (isCategoryPressed || isGenderPressed || isAgePressed || isSizePressed || isStatusPressed) {
                showingModal.value = true
            }
            if (isCategoryPressed) {
                pickerModel.value = PickerModel(
                    title = localization.homeScreenCategoryTitle,
                    description = null,
                    options = PetCategory.entries.toPickerList {
                        category.value = PetCategory.entries[it]
                        showingModal.value = false
                    }
                )
            }
            if (isGenderPressed) {
                pickerModel.value = PickerModel(
                    title = localization.petUploadGenderField,
                    description = null,
                    options = PetGender.entries.toPickerList {
                        gender.value = PetGender.entries[it]
                        showingModal.value = false
                    }
                )
            }
            if (isAgePressed) {
                pickerModel.value = PickerModel(
                    title = localization.petUploadAgeField,
                    description = null,
                    options = PetAge.entries.toPickerList {
                        age.value = PetAge.entries[it]
                        showingModal.value = false
                    }
                )
            }
            if (isSizePressed) {
                pickerModel.value = PickerModel(
                    title = localization.petUploadSizeField,
                    description = null,
                    options = PetSize.entries.toPickerList {
                        size.value = PetSize.entries[it]
                        showingModal.value = false
                    }
                )
            }
            if (isStatusPressed) {
                pickerModel.value = PickerModel(
                    title = localization.petUploadStatusField,
                    description = null,
                    options = PetStatus.entries.toPickerList {
                        status.value = PetStatus.entries[it]
                        showingModal.value = false
                    }
                )
            }
            if (isLocationPressed) {
                // TODO: Show Map component
            }
        }
    }

    @Composable
    fun UploadImageField(localization: Localization) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.inversePrimary)
                .clickable {}
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            Icon(
                imageVector = MyProjectNameIcons.Camera,
                contentDescription = null,
                modifier = Modifier.size(60.dp).align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = localization.petUploadTitle,
                style = Typography.get().bodyMedium,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = localization.petUploadAddImageDescription,
                style = Typography.get().bodySmall,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

private fun <E : Enum<E>> EnumEntries<E>.toPickerList(action: (ordinal: Int) -> Unit): List<PickerOption> {
    val pickerList = mutableListOf<PickerOption>()
    this.map {
        pickerList.add(
            PickerOption(getLocalizedModelName(it.name), null) {
                action.invoke(it.ordinal)
            }
        )
    }
    return pickerList
}
