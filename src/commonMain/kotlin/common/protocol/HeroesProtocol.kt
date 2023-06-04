package common.protocol

import common.model.HeroModel
import kotlinx.serialization.Serializable

@Serializable
data class HeroCreateRequest(
    val heroId: String,
)

@Serializable
data class HeroesResponse(
    val heroes: List<HeroModel>
)

@Serializable
data class HeroResponse(
    val hero: HeroModel?
)