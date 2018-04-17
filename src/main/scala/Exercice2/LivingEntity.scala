package Exercice2

class LivingEntity extends Serializable {

  var armor: Int = 0
  var name: String = ""
  var hp: Int = 0

  def takeDamage(amount: Int) = {
    this.hp -= amount
    if(this.hp < 0) this.hp = 0
  }
}
