package com.example.lista3

data class Subject(
    val name: String
)

object Subjects
{
    val subjectList = mutableListOf(
        Subject("Soccer"),
        Subject("Programming"),
        Subject("PUM"),
        Subject("Eanglish"),
        Subject("Math"),
    )
}