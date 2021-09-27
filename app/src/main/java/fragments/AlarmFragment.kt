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
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.alarmapp.R
import com.example.alarmapp.databinding.AlarmItemBinding
import com.example.alarmapp.databinding.FragmentAlarmBinding
import model.Alarm
import model.AlarmDatabase
import model.AlarmRepository
import viewModel.AlarmListViewModel

class AlarmFragment : Fragment() {
    private lateinit var alarmAdapter: AlarmListAdapter
    private lateinit var binding: FragmentAlarmBinding
    private val alarmListViewModel:AlarmListViewModel by viewModels{
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T = activity?.application?.let { AlarmListViewModel(it) } as T
        }
    }
    //View model

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlarmBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.alarmToolbar.inflateMenu(R.menu.toolbar_menu)

        binding.alarmToolbar.setOnMenuItemClickListener{
            if (it.itemId == R.id.addAlarm){
                Toast.makeText(requireActivity(),"Add",Toast.LENGTH_LONG).show()
                Navigation.findNavController(view).navigate(R.id.action_alarmFragment_to_addAlarmFragment)
            }
            true
        }

        alarmAdapter = AlarmListAdapter ({
            //callback o day de co the lay dc context truyen vao ham cancel va ham schedule
            if (it.isOn) {
                activity?.let { it1 -> it.cancelAlarm(it1 as Context) }
                Log.e("On toggle", "On->Off")
                alarmListViewModel.update(alarm = it)
            } else {
                activity?.let { it1 -> it.schedule(it1 as Context) }
                Log.e("On toggle", "Off->On")
                alarmListViewModel.update(alarm = it)
            }},alarmListViewModel, activity!!)


        alarmListViewModel.listAlarmLiveData.observe(viewLifecycleOwner, {
            alarmAdapter.listAlarm = it
            alarmAdapter.notifyDataSetChanged()

            Log.d("check", "current list ${it.size}")
        })

        //set up recycler view
        var itemDecoration = DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL)
        //itemDecoration.setDrawable(context.getDrawable(R.drawable.line_divider))
        val recyclerView:RecyclerView = binding.rcvAlarm
        recyclerView.apply {
            adapter = alarmAdapter
            layoutManager = LinearLayoutManager(context)
            this.addItemDecoration(itemDecoration)
        }
    }

}

