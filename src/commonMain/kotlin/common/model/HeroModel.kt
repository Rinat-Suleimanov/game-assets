package common.model

import kotlinx.serialization.Serializable

@Serializable
open class HeroModel(val id: String){
    var bigImage: String? = null
    var headIcon: String? = null
}