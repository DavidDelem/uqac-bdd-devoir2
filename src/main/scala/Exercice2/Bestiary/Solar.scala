package Exercice2.Bestiary

import Exercice2.{Attack, LivingEntity}
import Exercice2.Utils.Position

class Solar (position: Position, id: Int)
  extends LivingEntity (
    id,
    "Solar",
    363,
    363,
      29,
    position,
    "GoodGuys",
    15,
    List(("ft", 9), ("fly", 27)),
    new Attack("+5 dancing greatsword", 21, 24, List(35, 30, 25, 20), 5),
    new Attack("+5 composite longbow", 16, 20, List(31, 26, 21, 16), 110)
  ){

  override def move(){}
}
