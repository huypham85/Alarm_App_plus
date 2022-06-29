package fragments

import adapters.AlarmListAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapp.R
import com.example.alarmapp.databinding.FragmentAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import viewModel.AlarmListViewModel

@AndroidEntryPoint
class AlarmFragment : Fragment() {
    private lateinit var alarmAdapter: AlarmListAdapter
    private lateinit var binding: FragmentAlarmBinding
    private val viewModel: AlarmListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.alarmToolbar.inflateMenu(R.menu.toolbar_menu)

        binding.alarmToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.addAlarm) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_alarmFragment_to_addAlarmFragment)
            }
            true
        }

        alarmAdapter = AlarmListAdapter({
            //callback o day de co the lay dc context truyen vao ham cancel va ham schedule
            if (it.isOn) {
                activity?.let { it1 -> it.cancelAlarm(it1 as Context) }
                Log.e("On toggle", "On->Off")
                viewModel.update(alarm = it)
            } else {
                activity?.let { it1 -> it.schedule(it1 as Context) }
                Log.e("On toggle", "Off->On")
                viewModel.update(alarm = it)
            }
        }, viewModel, requireActivity())


        viewModel.listAlarms.observe(viewLifecycleOwner) {
            alarmAdapter.listAlarm = it
            alarmAdapter.notifyDataSetChanged()
        }

        //set up recycler view
        val recyclerView: RecyclerView = binding.rcvAlarm
        recyclerView.apply {
            adapter = alarmAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

}

