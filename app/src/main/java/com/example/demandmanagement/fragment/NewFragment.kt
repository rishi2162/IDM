package com.example.demandmanagement.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.Skill
import kotlinx.android.synthetic.main.fragment_new.*
import kotlinx.android.synthetic.main.skill_grid_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class NewFragment : Fragment() {

    @SuppressLint("NewApi")
    var formatDate = SimpleDateFormat("dd MMMM YYYY",Locale.US)
    var adapter: skillAdapter? = null
    var skillList = ArrayList<Skill>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_new, container, false)
        adapter = skillAdapter(skillList,requireContext())
        var griDSkill = view.findViewById<GridView>(R.id.gridSkill)
        val btnCalendarView = view.findViewById<TextView>(R.id.btnCalendar)
        var inputYOE = view.findViewById<AutoCompleteTextView>(R.id.dropDownYoe)
        var inputLOC = view.findViewById<AutoCompleteTextView>(R.id.dropDownLocation)
        var inputShift = view.findViewById<AutoCompleteTextView>(R.id.dropDownShift)
        var inputPriority = view.findViewById<AutoCompleteTextView>(R.id.dropDownPriority)
        var inpuTSkill = view.findViewById<EditText>(R.id.inputSkill)

        griDSkill.visibility = View.GONE
        griDSkill.adapter = adapter

        var yoeList = resources.getStringArray(R.array.yearOfExp)
        var locList = resources.getStringArray(R.array.location)
        var shiftList = resources.getStringArray(R.array.shifts)
        var priorityList = resources.getStringArray(R.array.priority)

        var locArrayAdapter = ArrayAdapter(requireContext(),R.layout.drop_down_item_yoe,locList)
        var arrayAdapter = ArrayAdapter(requireContext(),R.layout.drop_down_item_yoe,yoeList)
        var shiftArrayAdapter = ArrayAdapter(requireContext(),R.layout.drop_down_item_yoe,shiftList)
        var priorityArrayAdapter = ArrayAdapter(requireContext(),R.layout.drop_down_item_yoe,priorityList)

        inputYOE.setAdapter(arrayAdapter)
        inputLOC.setAdapter(locArrayAdapter)
        inputShift.setAdapter(shiftArrayAdapter)
        inputPriority.setAdapter(priorityArrayAdapter)

        inpuTSkill.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(inputSkill.text.contains(',') && inputSkill.text.length > 1){
                    Log.i("Comma", inputSkill.text.toString().substring(0,inputSkill.text.toString().length-1))
                    var inputskill = inputSkill.text.toString().substring(0,inputSkill.text.toString().length-1)
                    skillList.add(Skill(inputskill))
                    inputSkill.text.clear()
                    if(skillList.isEmpty()){
                    }else{
                        griDSkill.visibility = View.VISIBLE
                    }
                    adapter!!.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        griDSkill.onItemLongClickListener = (AdapterView.OnItemLongClickListener { p0, p1, p2, p3 ->
//            Toast.makeText(applicationContext,p3.toString(),Toast.LENGTH_SHORT).show()
            skillList.removeAt(p3.toInt())
            adapter!!.notifyDataSetChanged()
            if(skillList.isEmpty()){
                griDSkill.visibility = View.GONE
            }
            return@OnItemLongClickListener true
        })

        val btnNextPage = view.findViewById<Button>(R.id.btnNextPage)
        btnNextPage.setOnClickListener {
            val transition = this.fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameLayout, RecipientsFragment())?.commit()
        }
        btnCalendarView.setOnClickListener(){
            val getDate: Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                val selectDate:Calendar = Calendar.getInstance()
                selectDate.set(Calendar.YEAR,i)
                selectDate.set(Calendar.MONTH,i2)
                selectDate.set(Calendar.DAY_OF_MONTH,i3)
                val date = formatDate.format(selectDate.time)
                btnCalendarView.text = date
            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            datePicker.show()
        }
        return view
    }
}

class skillAdapter: BaseAdapter {

    var skillList = ArrayList<Skill>()
    var context: Context? = null

    constructor(skillList: ArrayList<Skill>, context: Context?) : super() {
        this.skillList = skillList
        this.context = context
    }

    override fun getCount(): Int {
        return skillList.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(index: Int, p1: View?, p2: ViewGroup?): View {
        var skill: Skill = this.skillList[index]
        var inflater: LayoutInflater =
            context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var skillView = inflater.inflate(R.layout.skill_grid_item, null)
        skillView.skillName.text = skill.skill!!
//        skillView.btnCross.setOnClickListener(){
//            Log.i("name", index.toString())
//            skillView.visibility = View.INVISIBLE
//            var mainActivity = MainActivity()
//            mainActivity.removeSkill(index)
//            Log.i("skill",this.skillList[index].skill.toString())
//            this.skillList.removeAt(index)
//            mainActivity.removeSkill(index)
//            var cclass = skillAdapter(this.skillList, context)
//            cclass.notifyDataSetChanged()
//        }
        return skillView
    }
}


