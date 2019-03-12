package models

// Like continents in the original Risk game.
class District(id: Int, name: String, territories: List[Territory]) {
  def getId() = id

  def getName() = name

  def getTerritories() = territories
}

object District {
  def apply(id: Int, name: String, territories: List[Territory]): District = new District(id, name, territories)
}
