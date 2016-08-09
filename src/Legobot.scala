import scala.actors.threadpool.Queue

/**
  * Created by drew on 8/9/16.
  */
object Legobot {
  def main(args: Array[String]): Unit = {
    val message = new Message("!weather blah", None)

    val weather = new WeatherListener()
    println(weather.get_responses(message)(0).text)
    val m2 = new Message("90210", None)
    println(weather.get_responses(m2)(0).text)
  }
}

class Legobot(val baseplate: Lego, var inbox: List[Message], var outbox: List[Message]) {
  def process_message(): Unit = {
    val message = inbox.take(1).head
    val responses = baseplate.get_responses(message)
    outbox = outbox ::: responses
  }
}
