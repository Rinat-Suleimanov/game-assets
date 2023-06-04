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

private val imageSet = listOf(
    "Breakfast" to "https://images.unsplash.com/photo-1551963831-b3b1ca40c98e",
    "Burger" to "https://images.unsplash.com/photo-1551782450-a2132b4ba21d",
    "Camera" to "https://images.unsplash.com/photo-1522770179533-24471fcdba45",
    "Coffee" to "https://images.unsplash.com/photo-1444418776041-9c7e33cc5a9c",
    "Hats" to "https://images.unsplash.com/photo-1533827432537-70133748f5c8",
    "Honey" to "https://images.unsplash.com/photo-1558642452-9d2a7deb7f62",
    "Basketball" to "https://images.unsplash.com/photo-1516802273409-68526ee1bdd6",
    "Fern" to "https://images.unsplash.com/photo-1518756131217-31eb79b20e8f",
    "Mushrooms" to "https://images.unsplash.com/photo-1597645587822-e99fa5d45d25",
    "Tomato basil" to "https://images.unsplash.com/photo-1567306301408-9b74779a11af",
    "Sea star" to "https://images.unsplash.com/photo-1471357674240-e1a485acb3e1",
    "Bike" to "https://images.unsplash.com/photo-1589118949245-7d38baf380d6",
)

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
