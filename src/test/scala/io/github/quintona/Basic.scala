package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

import io.gatling.core.action.Action
import io.gatling.core.result.writer.DataWriter
import io.gatling.core.result.writer.RequestMessage
import akka.actor.ActorRef
import io.gatling.core.action.builder.ActionBuilder
import akka.actor.Props
import io.gatling.core.result.message._


object MyActions {
  class MyAction(val next: ActorRef) extends Action {
    def execute(session: Session) {
      val start = System.currentTimeMillis
      val executionEndDate = start+160
      DataWriter.dispatch(
        RequestMessage(session.scenarioName, session.userId, List(), "Test Scenario",
          start, start, executionEndDate, executionEndDate,
          OK, None, List()))
      next ! session
    }
  }

}

class BasicSimulation extends Simulation {

  val mine = new ActionBuilder {
    def build(next: ActorRef, protocols: io.gatling.core.config.Protocols) = system.actorOf(Props(new MyActions.MyAction(next)))
  }

  val httpConf = http
    .baseURL("http://computer-database.herokuapp.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("BasicSimulation")
    .exec(mine)
    .pause(5)

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}