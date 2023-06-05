package db

import common.model.HeroModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

const val USER_ID = "first_user_id"
//const val USER_ID = "second_user_id"

object DbService {


    private const val DB_URL = "jdbc:sqlite:game_assets.db"

    object Heroes : Table("heroes") {
        val id = text("id")
        val fullImage = text("full_image").nullable()
        val icon = text("icon").nullable()

        override val primaryKey = PrimaryKey(id)
    }

    object HeroesChanges : Table("heroes_changes") {
        val heroId = text("hero_id").references(Heroes.id, onDelete = ReferenceOption.CASCADE)
        val userId = text("user_id")
        val fullImage = text("full_image").nullable()
        val icon = text("icon").nullable()

        override val primaryKey = PrimaryKey(heroId, userId)
    }

    object Weapons : Table("weapons") {
        val id = text("id")
        val fullImage = text("full_image").nullable()
        val icon = text("icon").nullable()
        val iconBroken = text("icon_broken").nullable()

        override val primaryKey = PrimaryKey(id)
    }

    init {
        Database.connect(DB_URL)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Heroes)
            SchemaUtils.createMissingTablesAndColumns(HeroesChanges)
            SchemaUtils.createMissingTablesAndColumns(Weapons)
        }

//        createInitialHeroes()
    }

    fun getAllHeroes(): List<HeroModel> {
        return transaction {
            Heroes.selectAll()
                .map {
                    HeroModel(it[Heroes.id].toString()).apply {
                        bigImage = it[Heroes.fullImage]
                        headIcon = it[Heroes.icon]
                    }
                }
        }
    }

    fun getCompletedHeroes(): List<HeroModel> {
        return transaction {
            Heroes.select { Heroes.icon.isNotNull() and Heroes.fullImage.isNotNull() }
                .map {
                    HeroModel(it[Heroes.id].toString()).apply {
                        bigImage = it[Heroes.fullImage]
                        headIcon = it[Heroes.icon]
                    }
                }
        }
    }

    fun getUncompletedHeroes(): List<HeroModel> {
        return transaction {
            Heroes.select { Heroes.icon.isNull() or Heroes.fullImage.isNull() }
                .map {
                    HeroModel(it[Heroes.id].toString()).apply {
                        bigImage = it[Heroes.fullImage]
                        headIcon = it[Heroes.icon]
                    }
                }
        }
    }

    fun findHero(id: String): HeroModel? {
        return transaction {
            Heroes.select { Heroes.id eq id }.limit(1)
                .map {
                    HeroModel(it[Heroes.id].toString()).apply {
                        bigImage = it[Heroes.fullImage]
                        headIcon = it[Heroes.icon]
                    }
                }
                .firstOrNull()
        }
    }

    fun updateHero(hero: HeroModel) {
        transaction {
            Heroes.update({ Heroes.id eq hero.id }) {
                it[fullImage] = hero.bigImage
                it[icon] = hero.headIcon
            }
        }
    }

    fun findHeroChanges(id: String): HeroModel? {
        return transaction {
            HeroesChanges.select { HeroesChanges.heroId eq id and (HeroesChanges.userId eq USER_ID) }.firstOrNull()?.let {
                HeroModel(it[HeroesChanges.heroId].toString()).apply {
                    bigImage = it[HeroesChanges.fullImage]
                    headIcon = it[HeroesChanges.icon]
                }
            }
        }
    }

    fun insertHero(heroId: String) {
        transaction {
            Heroes.insert {
                it[id] = heroId
            }
        }
    }

    private fun createInitialHeroes() {
        transaction {
            repeat(10) { order ->
                Heroes.insert {
                    it[id] = "hero_$order"
                    it[fullImage] = "full_image_$order"
                    it[icon] = "icon_$order"
                }
            }
            Heroes.insert {
                it[id] = "hero_10"
            }
            Heroes.insert {
                it[id] = "hero_11"
                it[fullImage] = "full_image_11"
            }
            Heroes.insert {
                it[id] = "hero_12"
                it[icon] = "icon_12"
            }
        }
    }

    fun updateHeroIcon(heroId: String, icon: String) {
        transaction {
            HeroesChanges.select { HeroesChanges.heroId eq heroId and (HeroesChanges.userId eq USER_ID) }
                .firstOrNull()
                ?.let {
                    HeroesChanges.update({ HeroesChanges.heroId eq heroId and (HeroesChanges.userId eq USER_ID) }) {
                        it[HeroesChanges.icon] = icon
                    }
                }
                ?: HeroesChanges.insert {
                    it[HeroesChanges.heroId] = heroId
                    it[HeroesChanges.userId] = USER_ID
                    it[HeroesChanges.icon] = icon
                }
        }
    }

    fun updateHeroImage(heroId: String, image: String) {
        transaction {
            HeroesChanges.select { HeroesChanges.heroId eq heroId and (HeroesChanges.userId eq USER_ID) }
                .firstOrNull()
                ?.let {
                    HeroesChanges.update({ HeroesChanges.heroId eq heroId and (HeroesChanges.userId eq USER_ID) }) {
                        it[HeroesChanges.fullImage] = image
                    }
                }
                ?: HeroesChanges.insert {
                    it[HeroesChanges.heroId] = heroId
                    it[HeroesChanges.userId] = USER_ID
                    it[HeroesChanges.fullImage] = image
                }
        }
    }

    fun deleteHeroChanges(heroId: String) {
        transaction {
            HeroesChanges.deleteWhere { HeroesChanges.heroId eq heroId and (HeroesChanges.userId eq USER_ID) }
        }
    }
}