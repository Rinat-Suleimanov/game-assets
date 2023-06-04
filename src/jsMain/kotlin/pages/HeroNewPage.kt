package pages

import HeroesApi
import common.RootPaths
import common.model.HeroModel
import common.protocol.HeroCreateRequest
import csstype.*
import frontend.components.Header
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.*
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ButtonType
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import react.dom.onChange
import react.router.useNavigate
import react.useState

private val scope = MainScope()
val HeroNewPage = FC<Props> {
    val navigate = useNavigate()
    var heroId: String? by useState(null)
    Header {}
    Container {
        sx {
            width = 50.pct
            margin = Auto.auto
        }
        h1 {
            +"New Hero Page"
        }

        Box {
            sx {
                display = Display.flex
                flexDirection = FlexDirection.column
            }
            component = form
            onSubmit = { event ->
                event.preventDefault()
                console.log("Hero ID: $heroId")
                scope.launch {
                    heroId?.let { HeroesApi.createNewHero(HeroCreateRequest(it)) }
                    navigate(RootPaths().Heroes.toString())
                }
            }

            TextField {
                id = "hero-id"
                label = ReactNode("Hero id")
                variant = FormControlVariant.outlined
                onChange = { event ->
                    heroId = event.target.asDynamic().value as String
                }
            }

            Button {
                sx {
                    margin = 10.px
                }
                variant = ButtonVariant.contained
                color = ButtonColor.primary
                type = ButtonType.submit
                disabled = heroId == null
                +"Create Hero"
            }

        }
    }

}