package Exercice2.Bestiary

import Exercice2.Utils.Position
import Exercice2.{Attack, Monster}

class DoubleAxeFury (position: Position, id: Int)
  extends Monster (
    "Double Axe Fury",
    142,
    17,
    position,
    0,
    List(("ft", 40)),
    new Attack("+1 orc double axe", 11, 18, List(19, 14, 9), 5),
    new Attack("mwk composite longbow", 7, 14, List(16,11,6), 110),
    id,
    "BadGuys"
  )
{

}
