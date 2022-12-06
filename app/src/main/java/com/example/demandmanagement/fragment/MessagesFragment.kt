package com.example.demandmanagement.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity
import com.example.demandmanagement.adapter.MessageAdapter
import com.example.demandmanagement.model.CommentEntity
import com.example.demandmanagement.model.FulfilEntity
import com.example.demandmanagement.model.Message
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime

class MessagesFragment : BottomSheetDialogFragment() {
    private var userData = JSONObject()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    lateinit var btnAddEmp: ImageView
    var commentList = arrayListOf<CommentEntity>()
    var dummyFulfilList = arrayListOf<FulfilEntity>()
    private lateinit var messageBox: EditText
    lateinit var autoCompleteEmployees: AutoCompleteTextView
    lateinit var btnFulfillDemand: Button
    private lateinit var chipGroupEmployees: ChipGroup
    lateinit var empChipGroup: ChipGroup
    var employeesString: String = ""
    var fulfillEmployees: String = ""
    var employeeList: List<String> = listOf(
        "Ayush Das(INC02234)",
        "Ayushi Das(INC02161)",
        "Ayushmaan Das(INC02162)",
        "Ayushs Das(INC02163)",
    )

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_messages, container, false)
        (activity as MainActivity).disableSwipe()
        userData = (activity as MainActivity).getUserData()
        empChipGroup = view.findViewById(R.id.empChipGroup)
        val lottieCheckNoComment: LottieAnimationView =
            view.findViewById(R.id.lottieCheckNoComment)
        val tvNoComment: TextView = view.findViewById(R.id.tvNoComment)
        val MessRecyclerView: RecyclerView = view.findViewById(R.id.MessRecyclerView)
        var commentArray = ArrayList<String>()
        var demandId = ""
        var state = ""
        val bundle = this.arguments
        if (bundle != null) {
            demandId = bundle.getString("demandId").toString()
            state = bundle.getString("state").toString()
            commentArray = bundle.getStringArrayList("commentStringArray") as ArrayList<String>
            for (i in commentArray.indices) {
                val myComment = Gson().fromJson(commentArray[i], CommentEntity::class.java)
                commentList.add(myComment)
            }
//            if (commentList.isEmpty()) {
//                val MessRecyclerView: RecyclerView = view.findViewById(R.id.MessRecyclerView)
//                MessRecyclerView.visibility = View.GONE
//            } else {
//                val lottieCheckNoComment: LottieAnimationView =
//                    view.findViewById(R.id.lottieCheckNoComment)
//                val tvNoComment: TextView = view.findViewById(R.id.tvNoComment)
//
//                lottieCheckNoComment.visibility = View.GONE
//                tvNoComment.visibility = View.GONE
//
//                setView(commentList, view)
//            }
//            setView(commentList, view)
        }
        if (commentList.isEmpty()) {
            MessRecyclerView.visibility = View.GONE
        } else {
            lottieCheckNoComment.visibility = View.GONE
            tvNoComment.visibility = View.GONE
            setView(commentList, view, state)
        }
        val txtDemandBack = view.findViewById<TextView>(R.id.txtDemandBack)
        txtDemandBack.text = "Demand ID - $demandId"
//        txtDemandBack.setOnClickListener {
//            (activity as MainActivity).onBackKeyPressed()
//        }
        // implement the message box
        messageBox = view.findViewById(R.id.messageBox)
        val btnMessage = view.findViewById<ImageView>(R.id.btnMessage)
        btnMessage.setOnClickListener {
            Log.d("fulf", fulfillEmployees)
            empChipGroup.removeAllViews()
            val mess = messageBox.text.toString()

            if (fulfillEmployees.isNotEmpty()) {
                var fulfillEmployeesArray = fulfillEmployees.split(",").toTypedArray()
                for (i in fulfillEmployeesArray.indices) {
                    if (fulfillEmployeesArray[i] != "null" && fulfillEmployeesArray[i].isNotEmpty())
                    //Log.d("emp", fulfillEmployeesArray[i])
                        dummyFulfilList.add(
                            FulfilEntity(
                                "S000",
                                fulfillEmployeesArray[i].substring(
                                    0,
                                    fulfillEmployeesArray[i].indexOf('(')
                                ),
                                fulfillEmployeesArray[i].substring(
                                    fulfillEmployeesArray[i].indexOf(
                                        '('
                                    ) + 1, fulfillEmployeesArray[i].length - 1
                                ),
                                "PENDING"
                            )
                        )
                }
                //Log.d("dummy", dummyFulfilList.toString())
            }

            if (mess.isNotEmpty()) {
                lottieCheckNoComment.visibility = View.GONE
                tvNoComment.visibility = View.GONE
                MessRecyclerView.visibility = View.VISIBLE

                val fulfillArr = JSONArray()
                for (i in dummyFulfilList) {
                    val jsonObj = JSONObject()
                    jsonObj.put("empName", i.empName)
                    jsonObj.put("empId", i.empId)
                    jsonObj.put("statusOfFulfilledQty", i.statusOfFulfilledQty)

                    fulfillArr.put(jsonObj)
                }


                val messPayload = JSONObject()
                messPayload.put("demandId", demandId)
                messPayload.put("requserId", userData.getString("loggedInUserId"))
                messPayload.put("comment", mess)
                messPayload.put("fulfilledQtyCmt", fulfillArr)
                messPayload.put("date", LocalDateTime.now())
                apiCall(messPayload)
                //Log.i("mess", messPayload.toString())
                messageBox.setText("")
                commentList.add(
                    CommentEntity(
                        "COOO",
                        mess,
                        "INC0000",
                        userData.getString("loggedInUser"),
                        dummyFulfilList,
                        LocalDateTime.now().toString() + "+00:00",
                        true
                    )
                )
                setView(commentList, view, state)

            } else {
                Toast.makeText(requireContext(), "Please enter message", Toast.LENGTH_SHORT).show()
            }
            messageBox.onEditorAction(EditorInfo.IME_ACTION_DONE)
            fulfillEmployees = ""

        }
        // Add Employee
        btnAddEmp = view.findViewById(R.id.btnAddEmp)
        if (state == "received") {
            btnAddEmp.visibility = View.VISIBLE
        }
        btnAddEmp.setOnClickListener {
            //empChipGroup.visibility = View.VISIBLE
            val dialogBinding = layoutInflater.inflate(R.layout.add_employees, null)
            val mDialog = Dialog(requireActivity())
            mDialog.setContentView(dialogBinding)
            mDialog.setCancelable(true)
            mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mDialog.show()
            autoCompleteEmployees = dialogBinding.findViewById(R.id.autoCompleteEmployees)
            chipGroupEmployees = dialogBinding.findViewById(R.id.chipGroupEmployees)
            var adapter =
                ArrayAdapter(
                    requireActivity(),
                    android.R.layout.simple_list_item_1,
                    employeeList
                )
            autoCompleteEmployees.setAdapter(adapter)
            autoCompleteEmployees.setOnKeyListener { _, keyCode, event ->
                if (autoCompleteEmployees.text.toString().isNotEmpty()
                ) {
                    val name = autoCompleteEmployees.text.toString()
                    if (name.contains('(')) {
                        addChip(name)
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Employee not found!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    autoCompleteEmployees.text.clear()
                    return@setOnKeyListener true
                }
                false
            }
            btnFulfillDemand = dialogBinding.findViewById(R.id.btnFulfillDemand)
            btnFulfillDemand.setOnClickListener {
                if (chipGroupEmployees.childCount >= 1) {
                    val n = chipGroupEmployees.childCount - 1
                    fulfillEmployees = ""
                    for (i in 0..n) {
                        val chip = chipGroupEmployees.getChildAt(i) as Chip
                        if (fulfillEmployees.isEmpty()) {
                            fulfillEmployees += chip.text.toString()
                        } else {
                            fulfillEmployees += ",${chip.text.toString()}"
                        }
                    }

                    if (fulfillEmployees.isNotEmpty()) {
                        var fulfillEmployeesArray = fulfillEmployees.split(",").toTypedArray()
                        for (i in fulfillEmployeesArray.indices) {
                            if (fulfillEmployeesArray[i] != "null" && fulfillEmployeesArray[i].isNotEmpty())
                            //Log.d("emp", fulfillEmployeesArray[i])
//                                dummyFulfilList.add(
//                                    FulfilEntity(
//                                        fulfillEmployeesArray[i].substring(
//                                            0,
//                                            fulfillEmployeesArray[i].indexOf('(')
//                                        ),
//                                        fulfillEmployeesArray[i].substring(
//                                            fulfillEmployeesArray[i].indexOf(
//                                                '('
//                                            ) + 1, fulfillEmployeesArray[i].length - 1
//                                        ),
//                                        "PENDING"
//                                    )
//                                )
                                addEmpChip(fulfillEmployeesArray[i])
                        }
                        //Log.d("dummy", dummyFulfilList.toString())
                    }
                    mDialog.dismiss()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "You fulfilled 0 demands",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    mDialog.dismiss()
                }
            }


        }

        return view
    }

    private fun setView(commentList: ArrayList<CommentEntity>, view: View, state: String) {
        val messRecyclerView = view.findViewById<RecyclerView>(R.id.MessRecyclerView)
        val myLinearLayoutManager = LinearLayoutManager(activity)
        messRecyclerView.layoutManager = myLinearLayoutManager
        messageAdapter =
            MessageAdapter(
                requireActivity(),
                commentList,
                userData.getString("loggedInUser"),
                state
            )
        messRecyclerView.adapter = messageAdapter
    }

    private fun apiCall(jsonObject: JSONObject) {
        val queue = Volley.newRequestQueue(requireContext())
        val url = "http://20.204.235.62:8080/createComment"
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
            { response ->
                Log.i("successRequest", response.toString())
            },
            {
                Log.d("error", it.localizedMessage as String)
            }) {
        }
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    private fun addChip(text: String) {
        var flag: Boolean = false
        val n = chipGroupEmployees.childCount - 1
        for (i in 0..n) {
            val chip = chipGroupEmployees.getChildAt(i) as Chip
            if (chip.text.toString() == text) {
                flag = true
                break
            }
        }
        if (!flag) {
            val chip = Chip(requireActivity())
            chip.text = text
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener {
                chipGroupEmployees.removeView(chip)
            }
            chipGroupEmployees.addView(chip)
        }
    }

    private fun addEmpChip(text: String) {
        var flag: Boolean = false
        val n = empChipGroup.childCount - 1
        for (i in 0..n) {
            val chip = empChipGroup.getChildAt(i) as Chip
            if (chip.text.toString() == text) {
                flag = true
                break
            }
        }
        if (!flag) {
            val chip = Chip(requireActivity())
            chip.text = text
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener {
                empChipGroup.removeView(chip)
            }
            empChipGroup.addView(chip)
        }
    }
}