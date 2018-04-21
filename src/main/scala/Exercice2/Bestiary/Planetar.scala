package Exercice2.Bestiary

import Exercice2.{Attack, Monster}
import Exercice2.Utils.Position

class Planetar (position: Position, id: Int)
  extends Monster(
    name = "Planetar",
    229,
    32,
    position,
    10,
    List(("ft", 30), ("fly", 90)),
    new Attack("+3 holy greatsword", 18, 21, List(27, 22, 17), 5),
    null,
    id,
    "GoodGuys"
  ){
}
