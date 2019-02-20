package controllera

import javax.inject._
import models.{JsonConverter, Player, Territory}
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
    val np = Player((request.body \ "id").as[Int], (request.body \ "name").as[String], null, null, null, null)
    Ok(toJson(Map("id" -> np.getId())))
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
    val turnOrder: List[Int] = List(1, 2, 3)
    var players: List[Map[String, Any]] = Nil
    var num: Int = 0
    var input: List[JsValue] = ((request.body) \ "data").as[List[JsValue]]
    var num_armies: Int = initArmiesUnits(input.length)
    for (player_data <- input) {
      val id = (player_data \ "id").as[Int]
      val name = (player_data \ "name").as[String]
      val email = (player_data \ "email").as[String]
      val color = (player_data \ "color").as[Int]
      val turn = turnOrder(num)
      val player = Player(id, name, email, turn, color, num_armies)
      num = num + 1
      val player_map = Map[String, Any]("id" -> player.getId(), "name" -> player.getName(), "email" -> player.getEmail())
      players = player_map :: players
    }
    val final_data = JsonConverter.toJson(Map("data" -> players))
    Ok(final_data)
  }

  def initArmiesUnits(playerList: List[Player]): Int = {
    if (playerList.length == 3) {
      35
    } else if (playerList.length == 4) {
      30
    } else if (playerList.length == 5) {
      25
    } else 20
  }
}
