import scala.collection.mutable

/**
  * Created by drew on 8/9/16.
  */
object Legobot {
  def main(args: Array[String]): Unit = {
    val message = new Message("!weather blah", None)
    val weather = new WeatherListener()
    val m2 = new Message("90210", None)
    var q = new mutable.Queue[Message]
    q.enqueue(message)
    q.enqueue(m2)
    val legobot = new Legobot(weather, inbox=q)
  }
}

class Legobot(val baseplate: Lego,
              var inbox: mutable.Queue[Message] = new mutable.Queue[Message],
              var outbox: mutable.Queue[Message] = new mutable.Queue[Message],
              var connectors: List[Connector] = Nil) {

  def process_next_message(): Unit = {
    if (inbox.nonEmpty) {
      val message = inbox.dequeue()
      baseplate.handle(message)
    }
  }

  def poll_connectors(): Unit = {
    connectors.foreach(connector => inbox ++= connector.poll())
  }

  def poll_legos(): Unit = {
    outbox ++= baseplate.poll()
  }

  def send_next_message(): Unit = {
    if (outbox.nonEmpty) {
      val message = outbox.dequeue()
      // left off here
      // make connectors a tree
    }
  }
}
