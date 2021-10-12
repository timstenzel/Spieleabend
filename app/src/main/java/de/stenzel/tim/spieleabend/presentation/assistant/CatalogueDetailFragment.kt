package de.stenzel.tim.spieleabend.presentation.assistant

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.CatalogueDetailFragmentBinding
import de.stenzel.tim.spieleabend.glide.GlideApp
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogueDetailFragment : Fragment() {

    private var _binding: CatalogueDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val args: CatalogueDetailFragmentArgs by navArgs()

    private val viewModel: CatalogueDetailViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = CatalogueDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.game.observe(viewLifecycleOwner, Observer { game ->
            //image
            if (game.image_url.isEmpty()) {
                GlideApp.with(requireContext()).load(R.drawable.news_default).into(binding.catalogueDetailImage)
            } else {
                GlideApp.with(requireContext()).load(game.image_url).error(R.drawable.error_default).into(binding.catalogueDetailImage)
            }
            //rating
            binding.catalogueDetailRating.rating = game.average_user_rating.toFloat()
            binding.catalogueDetailRatingNum.text = getString(R.string.catalogue_detail_rating_num, game.num_user_ratings)
            //title
            binding.catalogueDetailTitle.text = game.name
            //publisher + year
            binding.catalogueDetailPublisher.text = getString(R.string.catalogue_detail_publisher, game.primary_publisher.name, game.year_published)
            //players
            val players = if (game.min_players == game.max_players) {
                getString(R.string.catalogue_player, game.max_players)
            } else {
                getString(R.string.catalogue_players, game.min_players, game.max_players)
            }
            binding.catalogueDetailPlayers.text = players
            //age
            binding.catalogueDetailAge.text = getString(R.string.catalogue_detail_age, game.min_age)
            //time
            binding.catalogueDetailTime.text = getString(R.string.catalogue_time, game.min_playtime, game.max_playtime)
            //category
            val category = game.categories
            val readableCategories = category.joinToString(separator = ", ") {it.name}
            binding.catalogueDetailCategory.text = getString(R.string.catalogue_detail_categories, readableCategories)
            //mechanics
            val mechanics = game.mechanics
            val readableMechanics = mechanics.joinToString(separator = ", ") {it.name}
            binding.catalogueDetailMechanics.text = getString(R.string.catalogue_detail_mechanics, readableMechanics)
            //learning complexity
            binding.catalogueDetailLearningComplexity.text = getString(R.string.catalogue_detail_learning_complexity, game.average_learning_complexity)
            //strategy complexity
            binding.catalogueDetailStrategyComplexity.text = getString(R.string.catalogue_detail_strategy_complexity, game.average_strategy_complexity)
            //rules url
            binding.catalogueDetailRules.text = getString(R.string.catalogue_detail_rules, game.rules_url)
            //description
            binding.catalogueDetailDescription.text = Html.fromHtml(game.description, Html.FROM_HTML_MODE_COMPACT)

        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            when (loading) {
                true -> {binding.progressbar.root.show()}
                false -> {binding.progressbar.root.hide()}
            }
        })

        if (args.id.isEmpty()) {
            // TODO: 07.10.21 show error view
        } else {
            //start load of game details
            Log.d("CDFrag", "loadGameDetails")
            lifecycleScope.launch {
                viewModel.loadGameDetails(args.id)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}