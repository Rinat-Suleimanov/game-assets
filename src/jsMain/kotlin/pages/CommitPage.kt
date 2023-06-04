package pages

import frontend.components.Header
import mui.material.Container
import react.FC
import react.Props
import react.dom.html.ReactHTML.h1

val CommitPage = FC<Props> {
    Header {}
    Container {
        h1 {
            +"Commit Page"
        }
    }

}