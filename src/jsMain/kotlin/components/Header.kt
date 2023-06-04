package frontend.components

import common.RootPaths
import csstype.Display
import csstype.NamedColor
import mui.material.*
import react.FC
import react.Props
import mui.system.sx
import react.router.useNavigate

val Header = FC<Props> {
    val navigate = useNavigate()
    AppBar {
        position = AppBarPosition.static
        Toolbar {
            sx {
                color = NamedColor.white
            }
            Button {
                onClick = { navigate(RootPaths().toString()) }
                +"Todo"
                sx {
                    display = Display.block
                    color = NamedColor.white
                }
            }
            Button {
                onClick = {
                    navigate(RootPaths().Heroes.toString())
                }
                +"Heroes"
                sx {
                    display = Display.block
                    color = NamedColor.white
                }
            }
//            Button {
//                onClick = { navigate(RootPaths().Weapons.toString()) }
//                +"Weapons "
//                sx {
//                    display = Display.block
//                    color = NamedColor.white
//                }
//            }
//            Button {
//                onClick = {
//                    navigate(RootPaths().Commit.toString())
//                }
//                +"Changes"
//                sx {
//                    display = Display.block
//                    color = NamedColor.white
//                }
//            }
        }

    }
}