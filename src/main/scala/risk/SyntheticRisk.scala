package risk

import scala.io._
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar
import common._

class SyntheticRisk(csv: String) {
  private val m: Int = 52
  private val T: Int = 260
  val values = Utils.getDateValues(csv).toList
  val returnByDate = Utils.aggregateWithPrevious(new DateValue(new Date(), 0.0), values, (prev: DateValue, current: DateValue) => { new DateValue(current.date, if (prev.value != 0.0) ((current.value - prev.value) / prev.value) else 0) })
  val returnByPeriod = Utils.aggregateGroupBy(returnByDate, 
                                              (current: DateValue) => { current.week }, 
                                              (l: List[DateValue]) => (l.foldLeft(new DateValue(new Date(),1.0))((f, g) => new DateValue(g.date, f.value * (1 + g.value)))) - 1.0)
  
  override def toString = {
    val date = for {
      ret <- returnByDate
    } yield ret.toString
    val period = for {
      ret <- returnByPeriod.sortWith((d1, d2) => Utils.weekComparer(d1, d2))
    } yield ret.toString
    date.mkString("\n") + "\n\n" + period.mkString("\n")
  }
}