package pages

import common.RootPaths
import common.model.HeroModel
import csstype.Color
import csstype.NamedColor
import csstype.TextDecoration
import frontend.components.Header
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.router.useNavigate
import react.useEffectOnce
import react.useState

private val scope = MainScope()

val TodoPage = FC<Props> {
    val navigate = useNavigate()
    var heroes: List<HeroModel> by useState(emptyList())
    useEffectOnce {
        scope.launch {
            heroes = HeroesApi.fetchNotCompletedHeroes().heroes
        }
    }
    Header {}
    h1 {
        +"Heroes to complete:"
    }
    ul {
        heroes.forEach { hero ->
            li {
                Button {
                    variant = ButtonVariant.text
                    onClick = {
                        navigate(RootPaths().Heroes.Id(hero.id).toString())
                    }
                    val tips = listOfNotNull(
                        if (hero.headIcon == null) "design head icon" else null,
                        if (hero.bigImage == null) "design big image" else null,
                    )
                    +"${hero.id}${tips.takeIf { it.isNotEmpty() }?.let { " (${it.joinToString(", ")})" } ?: ""}"
                }
            }
        }
    }
}