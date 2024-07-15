package com.example.uitask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.uitask.databinding.ItemProgressCardBinding

data class MotorData(
    val yearlyDuration: String,
    val currentDate: String,
    val campaignName: String,
    val targetLabel: String,
    val targetValue: String,
    val imageInProgressBar: Int,
    val progressMax: Int,
    val progressCurrent: Int,
    val earnedLabel: String,
    val earnedValue: String,
    val eligibilityText: String,
    val goalDifference: String,
    val upcomingTarget: String,
    val viewProgress: String
)

class MotorAdapter(private val motorList: MutableList<MotorData>) :
    RecyclerView.Adapter<MotorAdapter.MotorViewHolder>() {

    class MotorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bind = ItemProgressCardBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progress_card, parent, false)
        return MotorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return motorList.size
    }

    override fun onBindViewHolder(holder: MotorViewHolder, position: Int) {
        val motorItem = motorList[position]

        holder.bind.yearlyDuration.text = motorItem.yearlyDuration
        holder.bind.currentDate.text = motorItem.currentDate
        holder.bind.campaignName.text = motorItem.campaignName
        holder.bind.targetLayout.targetLabel.text = motorItem.targetLabel
        holder.bind.targetLayout.targetValue.text = motorItem.targetValue
        holder.bind.imageInsideProgressBar.setImageResource(motorItem.imageInProgressBar)
        holder.bind.circularProgressIndicator.max = motorItem.progressMax
        holder.bind.circularProgressIndicator.progress = motorItem.progressCurrent
        holder.bind.earnedLayout.earnedLabel.text = motorItem.earnedLabel
        holder.bind.earnedLayout.earnedValue.text = motorItem.earnedValue
        holder.bind.clubGold.clubGoldCon.visibility = View.INVISIBLE
        holder.bind.eligibility.text = motorItem.eligibilityText
        val drawableEligibility =
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_not_eligible)
        holder.bind.eligibility.setCompoundDrawablesWithIntrinsicBounds(
            drawableEligibility, null, null, null
        )

        holder.bind.linearProgressBarItem.apply {
            linearProgressBar.visibility = View.GONE
            linearProgressTextOne.visibility = View.GONE
            linearProgressTextTwo.visibility = View.GONE
            linearProgressTextThree.visibility = View.GONE
            linearEarned.visibility = View.GONE
            linearTarget.visibility = View.GONE

            val drawableGoalDiff = ContextCompat.getDrawable(holder.itemView.context,R.drawable.ic_info)
            goalDifference.apply {
                visibility = View.VISIBLE
                text = motorItem.goalDifference
                setCompoundDrawablesWithIntrinsicBounds(null, null, drawableGoalDiff, null)
            }

            upcomingSlabTarget.text = motorItem.upcomingTarget
            viewProgressButton.text = motorItem.viewProgress
        }
    }
}