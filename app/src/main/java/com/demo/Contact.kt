package com.demo

class Contact() {

      var ROLL_NO:Int=0
      var NAME:String?=null
      var AGE:Int=0
      var MOB:String?=null

     constructor(ROLL_NO: Int, NAME: String?, AGE: Int, MOB: String?) : this() {
         this.ROLL_NO = ROLL_NO
         this.NAME = NAME
         this.AGE = AGE
         this.MOB = MOB
     }
 }

