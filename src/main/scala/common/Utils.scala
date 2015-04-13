package common

import scala.io._
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar
import java.nio.file.Files
import java.nio.file.Paths
import scala.util.matching.Regex

object Utils {
  def aggregateWithPrevious[TSource, TDest](default: TSource, values: List[TSource], f: (TSource, TSource) => TDest): List[TDest] = {
    
    def compute(prev: TSource, v: List[TSource]) : List[TDest] = {
      if (!v.isEmpty)
        f(prev, v.head) :: compute(v.head, v.tail)
      else List[TDest]()
    }
    
    compute(default, values).tail
  }
  
  def aggregateGroupBy[TKey, TValue](values: List[TValue], k: (TValue) => TKey, f: (List[TValue]) => TValue): List[TValue] = {
      
    val m = values groupBy(x => k(x))
    for {
      k <- m.keys.toList
      l = f(m(k))
    }
     yield l
  }
  
  def getDateValues(file: String) = {
    val regex = """^([A-Z]\:)(\\[\w\-\d\.]+)+\.([\w]+)$""".r
    val f = file match { case regex(_*) => Source.fromFile(new File(file)) case _ => Source.fromString(file) } 
    for {
      line <- f.getLines
      items = line.split(";")
      date = new SimpleDateFormat("yyyyMMdd").parse(items(0))
    } yield new DateValue(date, items(1).toDouble)
  }
  
  def weekComparer(d1: DateValue, d2: DateValue) = {
    d1.week < d2.week
  }
  
  def getWeek(date: Date) = {
      val cal = Calendar.getInstance
      cal.set(Calendar.YEAR, 2000)
      cal.set(Calendar.MONTH, Calendar.JANUARY)
      cal.set(Calendar.DAY_OF_MONTH, 1)
      val refDate = cal.getTime
      val diff = (date.getTime - refDate.getTime) / (1000 * 60 * 60 * 24)
      scala.math.floor(diff/7).toInt
  }
  
  def getMonth(date: Date) = {
       val cal = Calendar.getInstance
       cal.setTime(date)
       val year = cal.get(Calendar.YEAR)
       val month = cal.get(Calendar.MONTH)
       (year - 2000) * 12 + month - 1
  }
}