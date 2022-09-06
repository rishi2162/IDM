package com.example.demandmanagement.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.Skill
import com.example.demandmanagement.model.Recipient
import kotlinx.android.synthetic.main.fragment_recipients.*
import kotlinx.android.synthetic.main.skill_grid_item.view.*
import java.util.ArrayList

class RecipientsFragment : Fragment() {
    var recipientArrayList = ArrayList<Recipient>()
    var adapter: recipientAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipients, container, false)
        adapter = recipientAdapter(recipientArrayList,requireContext())

        var griDRecipient = view.findViewById<GridView>(R.id.gridRecipient)
        var autoCompleteRecipients = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteRecipients)
        var recipientList = resources.getStringArray(R.array.recipients)
        griDRecipient.visibility = View.GONE
        griDRecipient.adapter = adapter

        var recipientsArrayAdapter = ArrayAdapter(requireContext(),R.layout.drop_down_item_yoe,recipientList)
        autoCompleteRecipients.threshold=0
        autoCompleteRecipients.setAdapter(recipientsArrayAdapter)

        autoCompleteRecipients.setOnItemClickListener { adapterView, view, i, l ->
            var selectedRecipient = adapterView.getItemAtPosition(i)
            Log.i("recipient", selectedRecipient.toString())
            recipientArrayList.add(Recipient(selectedRecipient.toString()))
            autoCompleteRecipients.text.clear()
            if(recipientArrayList.isEmpty()){
            }else{
                griDRecipient.visibility = View.VISIBLE
            }
            adapter!!.notifyDataSetChanged()
        }

        griDRecipient.onItemLongClickListener = (AdapterView.OnItemLongClickListener { p0, p1, p2, p3 ->
//            Toast.makeText(applicationContext,p3.toString(),Toast.LENGTH_SHORT).show()
            recipientArrayList.removeAt(p3.toInt())
            adapter!!.notifyDataSetChanged()
            if(recipientArrayList.isEmpty()){
                griDRecipient.visibility = View.GONE
            }
            return@OnItemLongClickListener true
        })

        val txtBack = view.findViewById<TextView>(R.id.tvNewDemand)
        txtBack.setOnClickListener {
            val transition = fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameLayout, NewFragment())?.commit()
        }

        return view
    }

}

class recipientAdapter: BaseAdapter {

    var recipientList = ArrayList<Recipient>()
    var context: Context? = null

    constructor(recipientList: ArrayList<Recipient>, context: Context?) : super() {
        this.recipientList = recipientList
        this.context = context
    }

    override fun getCount(): Int {
        return recipientList.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(index: Int, p1: View?, p2: ViewGroup?): View {
        var recipient: Recipient = this.recipientList[index]
        var inflater: LayoutInflater =
            context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var recipientView = inflater.inflate(R.layout.skill_grid_item, null)
        recipientView.skillName.text = recipient.recipient!!
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
        return recipientView
    }
}