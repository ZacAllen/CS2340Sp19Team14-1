package controllers

import javax.inject._
import models._
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.libs.json.Json._
import play.api.data._
import play.api.data.Forms._

import scala.util.Random

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  val gForm = Form(
    mapping(
      "name1" -> nonEmptyText,
      "email1" -> nonEmptyText,
      "name2" -> nonEmptyText,
      "email2" -> nonEmptyText,
      "name3" -> nonEmptyText,
      "email3" -> nonEmptyText,
      "name4" -> text,
      "email4" -> text,
      "name5" -> text,
      "email5" -> text,
      "name6" -> text,
      "email6" -> text
    ) (GameData.apply)(GameData.unapply)
  )

  def index: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.start_game(gForm))
  }

  /**
    *
    * @return
    */
  def gameInitiate: Action[AnyContent] = Action { implicit request =>
    gForm.bindFromRequest.fold(
      formWithErrors => {
        print("Not Processed")
        Ok(formWithErrors.toString)
      },

      gameData => {
        var list: List[Map[String, Any]] = Nil
        var playerData: Map[String, Any] = Map("id" -> 1, "name" -> gameData.name1, "email" -> gameData.email1)
        list = playerData :: list
        playerData = Map("id" -> 2, "name" -> gameData.name2, "email" -> gameData.email2)
        list = playerData :: list
        playerData = Map("id" -> 3, "name" -> gameData.name3, "email" -> gameData.email3)
        list = playerData :: list
        if (gameData.name4 != "") {
          playerData = Map("id" -> 4, "name" -> gameData.name4, "email" -> gameData.email4)
          list = playerData :: list
          if (gameData.name5 != "") {
            playerData = Map("id" -> 5, "name" -> gameData.name5, "email" -> gameData.email5)
            list = playerData :: list
            if (gameData.name6 != "") {
              playerData = Map("id" -> 6, "name" -> gameData.name6, "email" -> gameData.email6)
              list = playerData :: list
            }
          }
        }

        gameInitiator(list)
        val playerInitData = gameInitiator(list)
        val territoryInitData = randomizeTerritories(createTerritories(), playerInitData)
        Ok(views.html.game(territoryInitData, playerInitData)).withSession(
          "player_data" -> JsonConverter.toJson(playerInitData),
          "territory_data" -> JsonConverter.toJson(territoryInitData)
        )
      }
    )
  }

  def addPlayer: Action[JsValue] = Action(parse.json) { implicit request =>
    val np = Player((request.body \ "id").as[Int], (request.body \ "name").as[String], "", 0, 0, 0,List())
    Ok(toJson(Map("id" -> np.getId)))
  }

  def gameInitiator(input: List[Map[String, Any]]): List[Map[String, Any]] = {
    val turnOrder: List[Int] = randomizeTurns(input.length)
    var players: List[Map[String, Any]] = Nil
    var num: Int = 0
    val num_armies: Int = initArmiesUnits(input.length)
    for (player_data <- input) {
      val id = player_data("id").asInstanceOf[Int]
      val name = player_data("name").asInstanceOf[String]
      val email = player_data("email").asInstanceOf[String]
      val color = turnOrder(num)
      val turn = turnOrder(num)
      val territories = List()
      val player = Player(id, name, email, turn, color, num_armies, territories)
      num = num + 1
      val player_map = Map[String, Any]("id" -> player.getId, "name" -> player.getName, "email" -> player.getEmail,
        "color" -> player.getColor, "turn" -> player.getTurn, "numArmies" -> player.getNumArmies)
      players = player_map :: players
    }
    players
  }

  def initArmiesUnits(numPlayers: Int): Int = {
    if (numPlayers == 3) {
      val numArmies = 35
      numArmies
    } else if (numPlayers == 4) {
      val numArmies = 30
      numArmies
    } else if (numPlayers == 5) {
      val numArmies = 25
      numArmies
    } else {
      val numArmies = 20
      numArmies
    }
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

  /*
  Creates a list of Territory objects, with ID's 1 to 42, and Names "1" to "42"
  We may or may not use the Territory class in the future. We could perhaps modify
  the randomizeTerritories method to map these territories' int ids to each player's
   */

  def createTerritories(): List[Territory] = {
    var territoryList: List[Territory] = Nil
    var name = "1"
    for (inc <- 1 to 42 ) {
      var territory: Territory = new Territory(inc, name, null,0, 0, null, true)
      territoryList = territory :: territoryList
      var nameTemp = name.toInt
      nameTemp = 1 + nameTemp
      name = nameTemp.toString
    }
    territoryList
  }

  //Modified to return a map of id keys mapped to actual Territory objects.
  //Creates a map that contains all the territory ID's (i.e. numbers) as keys, and the Territory objects as values.
  //Each territory object has an owner id that matches to the id of the player that owns it.
  def randomizeTerritories(territoryID: List[Territory], playerID: List[Map[String, Any]]): Map[Int, Territory] = {
    val territories = util.Random.shuffle(territoryID)
    val players = util.Random.shuffle(playerID)

    var map:Map[Int, Territory] = Map()
    var i = 0
    for (a <- 1 to territories.length) {
      //map += territories(a) -> players(i % players.length)
      map += a -> territories(a - 1)
      val p = players(i % players.length)
      territories(a - 1).setOwnerID(p.get("id"))
      println(p.get("id").toString + "OOOOOO")
      territories(a - 1).setOwnerName(p.get("name"))
      territories(a - 1).addUnits(1)
      i += 1
    }
    map
  }

  //Returns the # of armies the player is allowed to place at the start of their turn
  def newArmies(playerID: Int, territories: Map[Int, Territory]): Int = {
    //Based on the Rules on the Google doc, the # of new armies is the # of territories / 3. If < 3 territories they
    //Get 1 army
    var numTerritories = 0
    for ((_, value) <- territories) {
      if (value.getOwnerID == playerID) numTerritories += 1
    }
    if (numTerritories < 3) {
      1
    } else {
      numTerritories / 3
    }
  }

  def placeSingleArmy(territory: Territory): Unit = {
    territory.addUnits(1)
  }

  //Randomizes a player's territories based on the # of armies to add
  def randomizeArmies(territories: List[Territory], numArmies: Int): Unit = {
    val random = new Random
    for (_ <- 1 to numArmies) {
      territories(random.nextInt(territories.length)).addUnits(1)
    }
  }

  //Set the turns for players using the random list generated and the player list
  def setPlayerTurn(players: List[Player], turnList: List[Int]) {
    if (players != Nil && turnList != Nil) {
      players.head.setTurn(turnList.head)
      setPlayerTurn(players.tail, turnList.tail)
    }
  }

  //Bonus for occupying the whole district
  val REDBONUS = 5
  val BROWNBONUS = 5
  val BLUEBONUS = 5
  val GREENBONUS = 5
  val ORANGEBONUS = 5
  val YELLOWBONUS = 5

  //Give players additional army units at the beginning of their turns
  def addArmies(player: Player) {
    if (player.getTerritories.length <= 2) {
      player.addArmyUnits(1)
    }  else {
      player.addArmyUnits(player.getTerritories.length/3)
      if (player.getTerritories.count(_.getId <= 7) == 7) {
        player.addArmyUnits(REDBONUS)
      } else if (player.getTerritories.count(x => x.getId >= 8 && x.getId <= 13) == 6) {
        player.addArmyUnits(BROWNBONUS)
      } else if (player.getTerritories.count(x => x.getId >= 14 && x.getId <= 21) == 8) {
        player.addArmyUnits(BLUEBONUS)
      } else if (player.getTerritories.count(x => x.getId >= 22 && x.getId <= 29) == 8) {
        player.addArmyUnits(GREENBONUS)
      } else if (player.getTerritories.count(x => x.getId >= 30 && x.getId <= 36) == 7) {
        player.addArmyUnits(ORANGEBONUS)
      } else if (player.getTerritories.count(x => x.getId >= 37 && x.getId <= 42) == 6) {
        player.addArmyUnits(YELLOWBONUS)
      }
    }
  }

  //Add a new Soldier to a territory.
  def placeNewArmies(player: Player, soldier: Soldier, territory: Territory, numArmies: Int): Unit = {
      for (i <- 1 to numArmies) {
        territory.addSoldier(soldier)
        territory.setSoldiers(territory.getSoldiers.sorted)
        soldier.setTerritory(territory)
        player.addArmyUnits(-soldier.getPrice)
      }
  }
}
