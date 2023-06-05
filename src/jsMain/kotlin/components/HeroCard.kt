package frontend.components

import common.RootPaths
import csstype.*
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import org.w3c.files.File
import react.FC
import react.Props
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML.img
import react.key
import react.router.useNavigate

data class HeroProps(
    var id: String,
    var bigImage: String? = null,
    var headIcon: String? = null,
    var clickable: Boolean = true,
    var editActions: Boolean = false,
    var onIconClick: MouseEventHandler<*>,
    var onImageClick: MouseEventHandler<*>,
) : Props

val Hero = FC<HeroProps> { heroProps ->
    Card {
        if (heroProps.clickable) {
            val navigate = useNavigate()
            onClick = {
                navigate(RootPaths().Heroes.Id(heroProps.id).toString())
            }
        }
        sx {
            width = 300.px
            margin = 20.px
        }
        CardContent {
            Box {
                sx {
                    display = Display.flex
                    justifyContent = JustifyContent.spaceBetween
                    margin = 10.px
                }
                Avatar {
                    alt = "${heroProps.id} icon"
                    heroProps.headIcon?.let {
                        src = it
                    }
                    +(heroProps.id.first().uppercase())
                }
                Typography {
                    variant = TypographyVariant.h5
                    +heroProps.id
                }
            }

            heroProps.bigImage?.let {
                ImageListItem {
                    sx {
                        margin = 10.px
                        width = 250.px
                    }
                    img {
                        src = it
                        alt = "${heroProps.id} image"
                    }
                }
            }
        }
        if (heroProps.editActions) {
            CardActions {
                Button {
                    onClick = heroProps.onIconClick
                    +"Upload Icon"
                }
                Button {
                    onClick = heroProps.onImageClick
                    +"Upload Image"
                }
            }
        }
    }
}
