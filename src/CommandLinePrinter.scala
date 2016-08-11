class CommandLinePrinter extends Connector {
  def handle(message: Message) = {
    println(message.text)
    val message2 = Message("90210", Metadata(this, new NullConnector))
    outbox += message2
  }

  val message1 = Message("!weather", Metadata(this, new NullConnector))
  outbox += message1

}
