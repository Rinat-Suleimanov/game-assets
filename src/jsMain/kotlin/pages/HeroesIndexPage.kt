package pages

import HeroesApi
import common.model.HeroModel
import csstype.*
import frontend.components.Header
import frontend.components.Hero
import frontend.components.NewHeroButton
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.Container
import mui.system.sx
import react.FC
import react.Props
import react.router.useNavigate
import react.useEffectOnce
import react.useState

private val scope = MainScope()
val HeroesIndexPage = FC<Props> {
    val navigate = useNavigate()
    var heroes: List<HeroModel> by useState(emptyList())
    useEffectOnce {
        scope.launch {
            heroes = HeroesApi.fetchAllHeroes().heroes
        }
    }

    Header {}
    Container {
        sx {
            width = 100.pct
            margin = 40.px
            display = Display.flex
            flexWrap = FlexWrap.wrap
            justifyContent = JustifyContent.flexStart
            alignItems = AlignItems.center
        }
        heroes.forEach { hero ->
            Hero {
                id = hero.id
                headIcon = hero.headIcon
                bigImage = hero.bigImage
                clickable = true
            }
        }
        NewHeroButton {}
    }
}
