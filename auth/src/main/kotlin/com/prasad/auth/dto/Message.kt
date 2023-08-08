package com.prasad.auth.dto

import com.prasad.auth.constant.MsgStatus

data class Message(val status:MsgStatus, val message:String)