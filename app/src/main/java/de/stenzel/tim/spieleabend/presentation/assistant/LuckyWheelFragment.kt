package de.stenzel.tim.spieleabend.presentation.assistant

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bluehomestudio.luckywheel.WheelItem
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.LuckyWheelFragmentBinding
import de.stenzel.tim.spieleabend.helpers.showToast

@AndroidEntryPoint
class LuckyWheelFragment : Fragment() {

    private var _binding: LuckyWheelFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LuckyWheelViewModel by viewModels()
    private val args: LuckyWheelFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LuckyWheelFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.amountOfPlayers == -1) {
            // TODO: 26.10.21 handle error
        } else {
            val wheelItems = arrayListOf<WheelItem>()
            for (i in 1 until args.amountOfPlayers + 1) {
                when {
                    i % 3 == 0 -> {
                        wheelItems.add(WheelItem(R.color.s_light_grey, BitmapFactory.decodeResource(resources, R.drawable.man), "Spieler $i"))
                    }
                    i % 3 == 1 -> {
                        wheelItems.add(WheelItem(R.color.p_light_red, BitmapFactory.decodeResource(resources, R.drawable.man_2), "Spieler $i"))
                    }
                    else -> {
                        wheelItems.add(WheelItem(R.color.p_light_red, BitmapFactory.decodeResource(resources, R.drawable.man_3), "Spieler $i"))
                    }
                }
            }
            binding.firstPlayerWheel.addWheelItems(wheelItems)
        }

        viewModel.luckyWinner.observe(viewLifecycleOwner, Observer { winner ->
            binding.firstPlayerWheel.setTarget(winner)
            binding.firstPlayerWheel.rotateWheelTo(winner)
        })

        binding.firstPlayerSpinBtn.setOnClickListener {
            viewModel.startSpinning(args.amountOfPlayers)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}