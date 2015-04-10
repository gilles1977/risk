package risk
import scala.io._
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar

class SyntheticRisk(filePath: String) {
  private val m: Int = 52
  private val T: Int = 260
  val values = getValues(filePath).toList
  val returnByDate = aggregateWithPrevious(new DateValue(new Date(), 0.0), values, (prev: DateValue, current: DateValue) => { new DateValue(current.date, if (prev.value != 0.0) ((current.value - prev.value) / prev.value) else 0) })

  private def getValues(filePath: String) = {
    for {
      line <- Source.fromFile(new File(filePath)).getLines
      items = line.split(";")
      date = new SimpleDateFormat("yyyyMMdd").parse(items(0))
    } yield new DateValue(date, items(1).toDouble)
  }

  private def aggregateWithPrevious[TSource, TDest](default: TSource, values: List[TSource], f: (TSource, TSource) => TDest): List[TDest] = {
    
    def compute(prev: TSource, v: List[TSource]) : List[TDest] = {
      if (!v.isEmpty)
        f(prev, v.head) :: compute(v.head, v.tail)
      else List[TDest]()
    }
    
    compute(default, values)
  }
  
  private def returnByPeriod(returnByDate: List[DateValue]) = {
    val sorted = returnByDate.sortWith(weekComparer)
    for {
      ret <- returnByDate
      weekNumber = getWeek(ret.date)
    } yield new {}
  }
  
  private def getWeek(date: Date) = {
    val cal = Calendar.getInstance
    cal.set(Calendar.YEAR, 2000)
    cal.set(Calendar.MONTH, Calendar.JANUARY)
    cal.set(Calendar.DAY_OF_MONTH, 1)
    val refDate = cal.getTime
    val diff = (date.getTime - refDate.getTime) / 1000 * 60 * 60 * 24
    scala.math.floor(diff/7).toInt
  }
  
  private def getMonth(date: Date) = {
     val cal = Calendar.getInstance
     cal.setTime(date)
     val year = cal.get(Calendar.YEAR)
     val month = cal.get(Calendar.MONTH)
     (year - 2000) * 12 + month - 1
  }
  
  override def toString = {
    val seq = for {
      ret <- returnByDate
    } yield ret.toString
    seq.mkString("\n")
  }

  class DateValue(d: Date, v: Double) {
    val date: Date = d
    val value: Double = v
    val week: Int = getWeek(d)
    val month: Int = getMonth(d)
    
    override def toString = {
      date.toString + " " + value.toString
    }
  }
  
  private def weekComparer(d1: DateValue, d2: DateValue) = {
    d1.week < d2.week
  }
}