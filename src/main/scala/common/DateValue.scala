package common

import java.util.Date
import java.text.SimpleDateFormat

class DateValue(d: Date, v: Double) {
  
    val date: Date = d
    val value: Double = v
    val week: Int = Utils.getWeek(d)
    val month: Int = Utils.getMonth(d)
    
    override def toString = {
      new SimpleDateFormat("dd/MM/yyyy").format(date) + " " + week.toString + " " + value.toString
    }
    
    def -(dv: Double): DateValue = {
      new DateValue(date, value - dv)
    }
  }