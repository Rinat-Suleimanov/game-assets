package pages

import HeroesApi
import common.model.HeroModel
import common.protocol.HeroResponse
import common.protocol.HeroesResponse
import csstype.*
import frontend.components.Header
import frontend.components.Hero
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.*
import mui.system.sx
import org.w3c.files.File
import react.*
import react.dom.html.InputType
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.input
import react.router.useParams


private val scope = MainScope()
val HeroPage = FC<Props> { props ->
    val id = useParams()["id"]

    var heroState: HeroModel? by useState(null)
    useEffectOnce {
        scope.launch {
            heroState = id?.let { HeroesApi.fetchById(it)?.hero }
        }
    }
    var heroChanges: HeroModel? by useState(null)
    useEffectOnce {
        scope.launch {
            heroChanges = id?.let {
                HeroesApi.fetchChangesById(it)?.hero
            }
        }
    }

    var iconDialogOpen by useState(false)
    var imageDialogOpen by useState(false)

    var iconFile: File? by useState(null)
    var imageFile: File? by useState(null)

    Header {}
    Grid {
        container = true
        Grid {
            item = true
            Container {
                h1 {
                    +"Hero: $id"
                }
                heroState?.let {
                    Hero {
                        this.id = it.id
                        this.headIcon = it.headIcon
                        this.bigImage = it.bigImage
                        this.clickable = false
                        this.editActions = true
                        this.onIconClick = {
                            iconDialogOpen = true
                        }
                        this.onImageClick = {
                            imageDialogOpen = true
                        }
                    }
                }
                Dialog {
                    open = iconDialogOpen
                    onClose = { _, _ -> iconDialogOpen = false }

                    DialogTitle {
                        +"Upload avatar"
                    }
                    DialogContent {
                        DialogContentText {
                            +"Upload image file from your computer"
                        }
                        input {
                            type = InputType.file
                            accept = "image/*"
                            onChange = {
                                val file = it.target.asDynamic().files[0]
                                console.log(file)
                                if (file != null) {
                                    iconFile = file
                                }

                            }
                        }
                        DialogActions {
                            Button {
                                onClick = { iconDialogOpen = false }
                                +"Cancel"
                            }
                            Button {
                                onClick = {
                                    iconFile?.let {
                                        scope.launch {
                                            val response = HeroesApi.iconUpload(heroState!!.id, it)
                                            if (response.ok) {
                                                console.log("icon updated")
                                                heroChanges = HeroesApi.fetchChangesById(heroState!!.id)?.hero
                                            }
                                        }
                                    }

                                    iconDialogOpen = false
                                }
                                +"Upload"
                            }
                        }
                    }
                }
                Dialog {
                    open = imageDialogOpen
                    onClose = { _, _ -> imageDialogOpen = false }

                    DialogTitle {
                        +"Upload avatar"
                    }
                    DialogContent {
                        DialogContentText {
                            +"Upload image file from your computer"
                        }
                        input {
                            type = InputType.file
                            accept = "image/*"
                            onChange = {
                                val file = it.target.asDynamic().files[0]
                                console.log(file)
                                if (file != null) {
                                    imageFile = file
                                }

                            }
                        }
                        DialogActions {
                            Button {
                                onClick = { imageDialogOpen = false }
                                +"Cancel"
                            }
                            Button {
                                onClick = {
                                    imageFile?.let {
                                        scope.launch {
                                            val response = HeroesApi.imageUpload(heroState!!.id, it)
                                            if (response.ok) {
                                                console.log("image updated")
                                                heroChanges = HeroesApi.fetchChangesById(heroState!!.id)?.hero
                                            }
                                        }
                                    }

                                    imageDialogOpen = false
                                }
                                +"Upload"
                            }
                        }
                    }
                }
            }
        }
        heroChanges?.let { changes ->
            Grid {
                item = true
                Container {
                    h1 {
                        +"Uncommitted Changes: "
                    }
                    Hero {
                        this.id = changes.id
                        this.headIcon = changes.headIcon
                        this.bigImage = changes.bigImage
                        this.clickable = false
                        this.editActions = false
                    }
                }
            }
        }
    }
    Button {
        sx {
            margin = 10.px
            visibility = if (heroChanges == null) Visibility.hidden else Visibility.visible
        }
        variant = ButtonVariant.contained
        onClick = {
            scope.launch {
                heroState = HeroesApi.commitChanges(heroState!!.id).hero
                heroChanges = null
            }
        }
        +"Commit Changes"
    }
}
