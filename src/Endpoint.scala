/**
  * Created by drew on 8/10/16.
  */
import scala.collection.mutable

abstract class Endpoint (
  protected var inbox: mutable.Queue[Message] = new mutable.Queue[Message],
  protected var outbox: mutable.Queue[Message] = new mutable.Queue[Message]
) {
  def poll: List[Message]
  def handle(msg: Message)
  def listening_for(msg: Message): Boolean
}