package controllers

import javax.inject._
import models.{JsonConverter, Player, RegistrationForm}
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.libs.json.Json._
import scalaj.http.{Http, HttpOptions}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport{

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index: Action[AnyContent] = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  /**
    *
    * @return
    */
  def gameInitiate: Action[AnyContent] = Action {
    Ok("Game Initiated")
  }

  def addPlayer: Action[JsValue] = Action(parse.json) { implicit request =>
    val np = Player((request.body \ "id").as[Int], (request.body \ "name").as[String], "", 0, 0, 0)
    Ok(toJson(Map("id" -> np.getId())))
  }

  def gameInitiator: Action[JsValue] = Action(parse.json) { implicit request =>
    val input: List[JsValue] = (request.body \ "data").as[List[JsValue]]
    val turnOrder: List[Int] = randomizeTurns(input.length)
    var players: List[Map[String, Any]] = Nil
    var num: Int = 0
    val num_armies: Int = initArmiesUnits(input.length)
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

  def initArmiesUnits(numPlayers: Int): Int = {
    if (numPlayers == 3) {
      val armies: Int = 35
      armies
    } else if (numPlayers == 4) {
      val armies: Int = 30
      armies
    } else if (numPlayers == 5) {
      val armies: Int = 25
      armies
    } else {
      val armies: Int = 20
      armies
    }
  }

  def initDataForm(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.RiskHome(RegistrationForm.form))
  }

  def initFormPost(): Action[AnyContent] = Action { implicit request =>
    val formData: RegistrationForm = RegistrationForm.form.bindFromRequest.get
    var finalMap: Map[String, List[Map[String, Any]]] = Map({"data" -> Nil})
    finalMap("data") = {Map("id" -> 1, "name" -> formData.name1, "email" -> formData.email1, "color" -> 1)} :: finalMap("data")
    finalMap("data") = {Map("id" -> 2, "name" -> formData.name2, "email" -> formData.email2, "color" -> 2)} :: finalMap("data")
    finalMap("data") = {Map("id" -> 3, "name" -> formData.name3, "email" -> formData.email3, "color" -> 3)} :: finalMap("data")
    if (formData.name4 != "") {
      finalMap("data") = {Map("id" -> 4, "name" -> formData.name4, "email" -> formData.email4, "color" -> 4)} :: finalMap("data")
      if (formData.name5 != "") {
        finalMap("data") = {Map("id" -> 5, "name" -> formData.name5, "email" -> formData.email5, "color" -> 5)} :: finalMap("data")
        if (formData.name6 != "") {
          finalMap("data") = {Map("id" -> 6, "name" -> formData.name6, "email" -> formData.email6, "color" -> 6)} :: finalMap("data")
        }
      }
    }
    val finalJson = JsonConverter.toJson(finalMap)
    Ok(Http("http://localhost:9000/player/multi-add").postData(finalJson)
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asInstanceOf[JsValue])
  }

  def randomizeTurns (playerCount: Int): List[Int] = {
    var turnList: List[Int] = Nil
    var num = 1
    //add 1 - player # to list
    for (_ <- 1 to playerCount) {
      turnList = num :: turnList
      num += 1
    }
    //shuffle
    turnList = util.Random.shuffle(turnList)
    turnList
  }
}
