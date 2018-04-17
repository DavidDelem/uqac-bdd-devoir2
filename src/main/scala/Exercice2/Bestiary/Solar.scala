package Exercice2.Bestiary

import Exercice2.{Attack, Monster}

class Solar () extends Monster{
  this.name = "Solar"
  this.hp = 363
  this.armor = 44
  this.regeneration = 15
  this.speeds = ("ft", 50) :: this.speeds
  this.speeds = ("fly", 150) :: this.speeds

  this.melee = new Attack("+5 dancing greatsword", 21, 24, List(35, 30, 25, 20), 5)
  this.ranged = new Attack("+5 composite longbow", 16, 20, List(31, 26, 21, 16), 110)
}
