package com.example.lista3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lista3.databinding.ExerciseListItemBinding

class ExerciseListAdapter(private val exerciseLists: List<ExerciseList>) : RecyclerView.Adapter<ExerciseListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListViewHolder {
        val binding = ExerciseListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseListViewHolder, position: Int) {
        val exerciseList = exerciseLists[position]
        val exercise = exerciseList.exercises[position % exerciseList.exercises.size]

        holder.bind(
            subject = "Exercise ${position + 1}",
            grade = "pkt: ${exercise.points}",
            content = exercise.content
        )
    }

    override fun getItemCount(): Int {
        return exerciseLists.sumOf { it.exercises.size }
    }
}
