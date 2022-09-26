package com.example.demandmanagement.fragment

import android.animation.Animator
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.airbnb.lottie.LottieAnimationView
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_demand_approval.*
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DemandApprovalFragment : Fragment() {

    private lateinit var builder : AlertDialog.Builder
    private lateinit var btnApprove : Button
    private lateinit var btnReject : Button
    private lateinit var tvApprove : TextView
    private lateinit var tvReject : TextView

    lateinit var tvDemandId: TextView
    lateinit var tvDate: TextView
    lateinit var tvDueDate: TextView
    lateinit var tvRequester: TextView
    lateinit var tvPriority: TextView
    lateinit var tvShift: TextView
    lateinit var tvDmDesgn: TextView
    lateinit var tvYoe: TextView
    lateinit var tvSkills: TextView
    lateinit var tvDesc: TextView
    lateinit var tvRequiredQty: TextView
    lateinit var tvFulfilledQty: TextView

    private lateinit var lottieApproved : LottieAnimationView
    private lateinit var lottieRejected : LottieAnimationView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_demand_approval, container, false)

        val bundle = this.arguments
        if (bundle != null) {
            val demandJSONObject = JSONObject()
            demandJSONObject.put("demandId", bundle.getString("demandId"))
            demandJSONObject.put("date", bundle.getString("date"))
            demandJSONObject.put("dueDate", bundle.getString("dueDate"))
            demandJSONObject.put("requesterUserId", bundle.getString("userId"))
            demandJSONObject.put("priority", bundle.getString("priority"))
            demandJSONObject.put("shift", bundle.getString("shift"))
            demandJSONObject.put("dmDesgn", bundle.getString("dmDesgn"))
            demandJSONObject.put("yoe", bundle.getString("yoe"))
            demandJSONObject.put("skills", bundle.getString("skills"))
            demandJSONObject.put("desc", bundle.getString("desc"))
            demandJSONObject.put("requiredQty", bundle.getInt("requiredQty"))

            setView(demandJSONObject, view)
        }

        val btnApprovalMessages = view.findViewById<Button>(R.id.btnApprovalMessages)
        btnApprovalMessages.setOnClickListener {
            val transition = this.fragmentManager?.beginTransaction()
            val fragment = MessagesFragment()
            transition?.replace(R.id.frameLayout, fragment)
                ?.addToBackStack(fragment.javaClass.name)?.commit()
        }

        val txtBack = view.findViewById<TextView>(R.id.txtBack)
        txtBack.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        btnApprove = view.findViewById(R.id.btnApprove)
        btnReject = view.findViewById(R.id.btnReject)

        tvApprove = view.findViewById(R.id.tvApprove)
        tvReject = view.findViewById(R.id.tvReject)

        builder = AlertDialog.Builder(requireActivity())

        btnApprove.setOnClickListener {
            builder.setTitle("Alert")
                .setMessage("Are you sure want to approve?")
                .setPositiveButton("Yes"){ _, it ->
                    btnApprove.visibility = View.GONE
                    btnReject.visibility = View.GONE
                    tvApprove.visibility = View.VISIBLE

                    lottieApproved = view.findViewById(R.id.lottieApproved)
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        lottieApproved.visibility = View.VISIBLE
                        lottieApproved.speed = 0.40F
                    }, 100)

                    lottieApproved.addAnimatorListener(object : Animator.AnimatorListener{
                        override fun onAnimationStart(p0: Animator?) {
                            Log.i("myTag", "temp")
                        }

                        override fun onAnimationEnd(p0: Animator?) {
                            lottieApproved.visibility = View.GONE
                        }

                        override fun onAnimationCancel(p0: Animator?) {
                            TODO("Not yet implemented")
                        }

                        override fun onAnimationRepeat(p0: Animator?) {
                            TODO("Not yet implemented")
                        }

                    })

                }
                .setNegativeButton("No"){ _, it ->

                }.show()
        }

        btnReject.setOnClickListener {
            builder.setTitle("Alert")
                .setMessage("Are you sure want to Reject?")
                .setPositiveButton("Yes"){ _, it ->
                    btnApprove.visibility = View.GONE
                    btnReject.visibility = View.GONE
                    tvReject.visibility = View.VISIBLE

                    lottieRejected = view.findViewById(R.id.lottieRejected)
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        lottieRejected.visibility = View.VISIBLE
                        lottieRejected.speed = 0.60F
                    }, 100)

                    lottieRejected.addAnimatorListener(object : Animator.AnimatorListener{
                        override fun onAnimationStart(p0: Animator?) {
                            Log.i("myTag", "temp")
                        }

                        override fun onAnimationEnd(p0: Animator?) {
                            lottieRejected.visibility = View.GONE
                        }

                        override fun onAnimationCancel(p0: Animator?) {
                            TODO("Not yet implemented")
                        }

                        override fun onAnimationRepeat(p0: Animator?) {
                            TODO("Not yet implemented")
                        }

                    })
                }
                .setNegativeButton("No"){ _, it ->

                }.show()
        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setView(data: JSONObject, view: View) {
        tvDemandId = view.findViewById(R.id.tvDemandId)
        tvDemandId.text = data.getString("demandId")

        tvDate = view.findViewById(R.id.tvDate)
        tvDate.text = convertDate(data.getString("date"))

        tvDueDate = view.findViewById(R.id.tvDueDate)
        tvDueDate.text = convertDate(data.getString("dueDate"))

        tvRequester = view.findViewById(R.id.tvRequester)
        tvRequester.text = data.getString("requesterUserId")

        tvPriority = view.findViewById(R.id.tvPriority)
        tvPriority.text = data.getString("priority")

        tvShift = view.findViewById(R.id.tvShift)
        tvShift.text = data.getString("shift")

        tvDmDesgn = view.findViewById(R.id.tvDmDesgn)
        tvDmDesgn.text = data.getString("dmDesgn")

        tvYoe = view.findViewById(R.id.tvYoe)
        tvYoe.text = data.getString("yoe")

        tvSkills = view.findViewById(R.id.tvSkils)
        tvSkills.text = data.getString("skills")

        tvDesc = view.findViewById(R.id.tvDesc)
        tvDesc.text = data.getString("desc")

        tvRequiredQty = view.findViewById(R.id.tvRequiredQty)
        tvRequiredQty.text = data.getInt("requiredQty").toString()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDate(date: String): String? {
        val datetime: LocalDateTime =
            LocalDateTime.parse(
                date.subSequence(0, date.length - 8),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S")
            )
        val formattedDate: String = datetime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))

        return formattedDate
    }

}