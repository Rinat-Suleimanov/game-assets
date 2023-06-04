import common.RootPaths
import pages.*
import react.FC
import react.Props
import react.createElement
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter

val App = FC<Props> {
    BrowserRouter {
        Routes {
            Route {
                path = "/"
                index = true
                element = createElement(TodoPage)
            }
            Route {
                path = RootPaths().Heroes.toString()
                element = createElement(HeroesIndexPage)
            }
            Route {
                path = RootPaths().Heroes.New.toString()
                element = createElement(HeroNewPage)
            }
            Route {
                path = RootPaths().Heroes.Id("/:id").toString()
                element = createElement(HeroPage)
            }
//            Route {
//                path = RootPaths().Commit.toString()
//                element = createElement(CommitPage)
//            }
        }
    }
}