package fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import adapters.AlarmListAdapter
import com.example.alarmapp.R
import com.example.alarmapp.databinding.FragmentAlarmBinding
import viewModel.AlarmListViewModel

class AlarmFragment : Fragment() {
    private lateinit var alarmAdapter: AlarmListAdapter
    private lateinit var binding: FragmentAlarmBinding
    private lateinit var alarmListViewModel: AlarmListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlarmBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.alarmToolbar.inflateMenu(R.menu.toolbar_menu)
        binding.alarmToolbar.setOnMenuItemClickListener{
            if (it.itemId == R.id.addAlarm){
                Toast.makeText(requireActivity(),"Add",Toast.LENGTH_LONG).show()
                Navigation.findNavController(view).navigate(R.id.action_alarmFragment_to_addAlarmFragment)
            }
            true
            if (it.itemId == R.id.editAlarm){
                Toast.makeText(requireActivity(),"Edit",Toast.LENGTH_LONG).show()
            }
            true
        }
        //view model
        alarmListViewModel = ViewModelProvider(requireActivity()).get(AlarmListViewModel::class.java)
        //adapter
        alarmAdapter = AlarmListAdapter {
            //callback o day de co the lay dc context truyen vao ham cancel va ham schedule
            if (it.isOn) {
                context?.let { it1 -> it.cancelAlarm(it1) }
            }
            else{
                context?.let { it1 -> it.schedule(it1) }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set up recycler view
        val recyclerView:RecyclerView = binding.rcvAlarm
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = alarmAdapter

        alarmListViewModel.liveListAlarm.observe(viewLifecycleOwner, Observer {
            if (it != null) alarmAdapter.listAlarm = it
        })
    }

}

