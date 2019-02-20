package models

class Game(players: List[Player], territories: List[Territory]) {

  def getPlayerList() = {
    val player_list: List[Player] = null
    players.foreach {_.getName() :: player_list}
    player_list
  }

  def getTerritoryList() = {
    val territory_list: List[Territory] = null
    territories.foreach {_.getName() :: territory_list}
    territory_list
  }
}

object Game {
  def apply(players: List[Player], territories: List[Territory]) = new Game(players, territories)
}
