package Exercice2.Bestiary

import Exercice2.Utils.Position
import Exercice2.{Attack, Monster}

class Solar (position: Position, id: Int)
  extends Monster (
    "Solar",
    363,
    44,
    position,
    15,
    List(("ft", 50), ("fly", 150)),
    new Attack("+5 dancing greatsword", 21, 24, List(35, 30, 25, 20), 5),
    new Attack("+5 composite longbow", 16, 20, List(31, 26, 21, 16), 110),
    id,
    "GoodGuys"
  )
{

}
