package controllers

import javax.inject._
import models.{Player, JsonConverter}
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc._
import play.api.libs.json.Json._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  /**
    *
    * @return
    */
  def game_initiate = Action {
    Ok("Game Initiated")
  }

  def addPlayer = Action(parse.json) { implicit request =>
    val np = Player((request.body \ "id").as[Int], (request.body \ "name").as[String], null)
    Ok(toJson(Map("id" -> np.getId())))
  }

  def randomizeTurns (playerCount: Int): List[Int] = {
    var turnList: List[Int] = Nil
    var num = 1
    //add 1 - player # to list
    for (a <- 1 to playerCount) {
      turnList = num :: turnList
      num += 1
    }
    //shuffle
    turnList = util.Random.shuffle(turnList)
    return turnList
  }

  class CC[T] { def unapply(a:Any):Option[T] = Some(a.asInstanceOf[T]) }

  object M extends CC[Map[String, Any]]
  object L extends CC[List[Any]]
  object S extends CC[String]
  object I extends CC[Int]
  object B extends CC[Boolean]
  object P extends CC[Player]
  object J extends CC[JsValue]

  def gameInitiator = Action(parse.json) { implicit request =>
    val players = for {
      J(player_data) <- ((request.body) \ "data").as[List[JsValue]]
      I(id) = (player_data \ "id").as[Int]
      S(name) = (player_data \ "name").as[String]
      S(email) = (player_data \ "email").as[String]
      P(player) = Player(id, name, email)
    } yield {
      Map[String, Any]("id" -> player.getId(), "name" -> player.getName(), "email" -> player.getEmail())
    }
    val final_data = JsonConverter.toJson(Map("data" -> players))
    Ok(final_data)
  }
}
