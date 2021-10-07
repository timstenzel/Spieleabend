package de.stenzel.tim.spieleabend.models

/**
 * Wrapper class for Boardgames (fields and types from boardgameatlas api, but not all needed)
 */
data class BoardgameWrapper(
    val games: List<Game>,
    //val count: Int
) {
    data class Game(
        val id: String,
        val handle: String,
        val url: String,
        val name: String,
        val price: String,
        val price_ca: String,
        val price_uk: String,
        val price_au: String,
        val msrp: Int,
        val msrps: List<Msrp>,
        val discount: String,
        val year_published: Int,
        val min_players: Int,
        val max_players: Int,
        val min_playtime: Int,
        val max_playtime: Int,
        val min_age: Int,
        val description: String,
        val commentary: String,
        val faq: String,
        val thumb_url: String,
        val image_url: String,
        val matches_specs: Any,
        val specs: List<Any>,
        val mechanics: List<Mechanic>,
        val categories: List<Category>,
        val publishers: List<Publisher>,
        val designers: List<Designer>,
        val primary_publisher: PrimaryPublisher,
        val primary_designer: PrimaryDesigner,
        val developers: List<Any>,
        val related_to: List<Any>,
        val artists: List<String>,
        val names: List<Any>,
        val rules_url: String,
        val amazon_rank: Int,
        val official_url: String,
        val sell_sheet_url: Any,
        val store_images_url: Any,
        val comment_count: Int,
        val num_user_ratings: Int,
        val average_user_rating: Double,
        val weight_amount: Int,
        val weight_units: String,
        val size_height: Double,
        val size_depth: Double,
        val size_units: String,
        val historical_low_prices: List<HistoricalLowPrice>,
        val active: Boolean,
        val num_user_complexity_votes: Int,
        val average_learning_complexity: Double,
        val average_strategy_complexity: Double,
        val visits: Int,
        val lists: Int,
        val mentions: Int,
        val links: Int,
        val plays: Int,
        val rank: Int,
        val type: String,
        val sku: String,
        val upc: String,
        val isbn: String,
        val video_links: List<VideoLink>,
        val availability_status: String,
        val num_distributors: Int,
        val trending_rank: Int,
        val listing_clicks: Int,
        val is_historical_low: Boolean,
        val msrp_text: String,
        val price_text: String,
        val tags: List<String>,
        val images: Images,
        val description_preview: String,
        val cs_rating: Double
    ) {
        data class Msrp(
            val country: String,
            val price: Double
        )

        data class Mechanic(
            val id: String,
            val url: String
        )

        data class Category(
            val id: String,
            val url: String
        )

        data class Publisher(
            val id: String,
            val num_games: Any,
            val score: Int,
            val game: Game,
            val url: String,
            val images: Images
        ) {
            class Game(
            )

            data class Images(
                val thumb: Any,
                val small: Any,
                val medium: Any,
                val large: Any,
                val original: Any
            )
        }

        data class Designer(
            val id: String,
            val num_games: Any,
            val score: Int,
            val game: Game,
            val url: String,
            val images: Images
        ) {
            class Game(
            )

            data class Images(
                val thumb: Any,
                val small: Any,
                val medium: Any,
                val large: Any,
                val original: Any
            )
        }

        data class PrimaryPublisher(
            val id: String,
            val name: String,
            val url: String
        )

        data class PrimaryDesigner(
            val id: String,
            val name: String,
            val url: String
        )

        data class HistoricalLowPrice(
            val country: String,
            val date: String,
            val price: Double,
            val isLow: Boolean
        )

        data class VideoLink(
            val title: String,
            val url: String
        )

        data class Images(
            val thumb: String,
            val small: String,
            val medium: String,
            val large: String,
            val original: String
        )
    }
}