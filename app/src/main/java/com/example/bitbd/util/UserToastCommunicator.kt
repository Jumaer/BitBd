package com.example.bitbd.util

interface UserToastCommunicator {
    fun displayErrorMessage(message : String )
    fun displayInfoMessage(message : String )
    fun displaySuccessMessage(message : String )
    fun displayWarningMessage(message : String )
}