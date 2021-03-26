// expose an endpoint as an finatra server using cats-effect project

import cats.effect.IO
import sttp.tapir._
import sttp.tapir.server.finatra._
import sttp.tapir.server.finatra.cats.FinatraCatsServerInterpreter
import com.twitter.util.Future
import com.twitter.finatra.http.Controller



def countCharacters(s: String): IO[Either[Unit, Int]] =
  IO.pure(Right[Unit, Int](s.length))

val countCharactersEndpoint: Endpoint[String, Unit, Int, Any] =
  endpoint.in(stringBody).out(plainBody[Int])

val countCharactersRoute: FinatraRoute = FinatraCatsServerInterpreter.toRoute(countCharactersEndpoint)(countCharacters)



def logic(s: String, i: Int): Future[Either[Unit, String]] = ???
val anEndpoint: Endpoint[(String, Int), Unit, String, Any] = ???
val aRoute: FinatraRoute = FinatraServerInterpreter.toRoute(anEndpoint)((logic _).tupled)



val atRoute: FinatraRoute = ???
class MyController extends Controller with TapirController {
  addTapirRoute(atRoute)
}