package frontend.components

import common.RootPaths
import csstype.*
import mui.material.*
import mui.material.Size
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML.img
import react.key
import react.router.useNavigate

val NewHeroButton = FC<Props> {
    Card {
        val navigate = useNavigate()
        onClick = {
            navigate(RootPaths().Heroes.New.toString())
        }
        sx {
            width = 300.px
            height = 300.px
            margin = 20.px
            alignItems = AlignItems.center
        }
        CardContent {
            sx {
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.center
                alignItems = AlignItems.center
                minHeight = 100.pct
            }
            img {
                src = "plus.svg"
            }
        }
    }
}
