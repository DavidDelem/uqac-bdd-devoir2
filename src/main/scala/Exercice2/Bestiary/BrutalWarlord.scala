package Exercice2.Bestiary

import Exercice2.Utils.Position
import Exercice2.{Attack, Monster}

class BrutalWarlord (position: Position)
  extends Monster (
    "Brutal Warlord",
    141,
    27,
    position,
    0,
    List(("ft", 30)),
    new Attack("+1 vicious flail", 11, 18, List(20, 15, 10), 5),
    new Attack("mwk throwing axe", 6, 11, List(19), 110)
  )
{

}
